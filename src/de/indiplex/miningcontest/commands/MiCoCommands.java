/*
 * MiningContest
 * Copyright (C) 2011 IndiPlex
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.indiplex.miningcontest.commands;

import de.indiplex.miningcontest.MiningContest;
import de.indiplex.miningcontest.logic.MiCo;
import de.indiplex.miningcontest.logic.Team;
import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author IndiPlex <kahabrakh@indiplex.de>
 */
public class MiCoCommands implements CommandExecutor {

    private final MiningContest plugin;

    public MiCoCommands(MiningContest plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("You have to be a Player ;)");
            return true;
        }

        Player player = (Player) sender;
        boolean op = player.isOp();

        if (args.length == 0) {
            printUsage(op, player);
            return true;
        }
        
        MiCo mico = MiningContest.getCurrentContest();
        Team team = null;
        if (args[0].equals("join")) {
            if (mico.initializing) {
                team = mico.getNextTeam();
                boolean isOnList = false;
                for (Team t:mico.getTeams()) {
                    if (t.hasMember(player)) {
                        isOnList = true;
                    }
                }
                if (isOnList) {
                    player.sendMessage("You already are on the list!");
                } else {
                    team.addMember(player);
                    plugin.setOldLoc(player.getName(), player.getLocation());
                    player.teleport(plugin.contestWorld.getSpawnLocation());
                    player.sendMessage("You joined team " + (team.getNumber() + 1));  
                    mico.nextTeam();
                }
            } else {
                player.sendMessage("There is no active game at the moment");
            }
        }
        if (args[0].equals("leave")) {
            if (mico.started || mico.initializing) {
                boolean left = false;                
                for (Team t:mico.getTeams()) {
                    if (t.removeMember(player)) {
                        left = true;
                        team = t;
                    }
                }
                if (!left) {
                    player.sendMessage("You are not on the list!");
                } else {
                    player.teleport(plugin.getOldLoc(player.getName()));
                    mico.setNextTeam(team.getNumber());
                }
            } else {
                player.sendMessage("There is no game active at the moment");
            }
        }
        if (args[0].equals("shop")) {
            if (mico.started && mico.isMiCoPlayer(player)) {
                if (mico.isInsideTeamArea(player)) {
                    System.out.println("debug!!!");
                    mico.getShop().show(player);
                }
            }
        }
        if (args[0].equals("status")) {
            if (mico.started && mico.isMiCoPlayer(player)) {
                mico.printPoints(player);
            }
        }
        if (args[0].equals("info")) {
            if (mico.started && mico.isMiCoPlayer(player)) {
                for (Team t:mico.getTeams()) {
                    player.sendMessage("Team"+t.getNumber()+":");
                    for (Player p:t.getMembers()) {
                        player.sendMessage(p.getName());
                    }
                }
            }
        }

        if (op) {
            if (args[0].equalsIgnoreCase("reset")) {
                plugin.getMWAPI().resetWorld(plugin.contestWorld, 10);
                player.sendMessage("World reset!");
            }
            if (args[0].equalsIgnoreCase("start")) {
                if (!mico.initializing && !mico.started) {
                    int startTime = 5;
                    ArrayList<Integer> intervals = new ArrayList<Integer>(Arrays.asList(0,1,3,4));
                    if (args.length!=1 && args.length!=2) {
                        try {
                            startTime = Integer.parseInt(args[1]);
                            for (int i=2;i<args.length;i++) {
                                intervals.add(Integer.parseInt(args[i]));
                            }
                        } catch (NumberFormatException e) {
                            startTime = 10;                        
                        }
                    }
                    MiningContest.getCurrentContest().init(startTime, intervals.toArray(new Integer[0]));
                } else if (mico.initializing) {
                    mico.startThread.startNOW = true;
                }
            }
            if (args[0].equalsIgnoreCase("qs")) {
                mico.init(0, null);
                mico.getNextTeam().addMember(player);
                mico.start();
            }
            if (args[0].equalsIgnoreCase("stop")) {
                mico.stop();
            }
        }

        return true;
    }

    private void printUsage(boolean op, Player p) {
        if (op) {
            p.sendMessage("/mico <start|stop|reset|join|leave>");
        } else {
            p.sendMessage("/mico <join|leave>");
        }
    }
}
