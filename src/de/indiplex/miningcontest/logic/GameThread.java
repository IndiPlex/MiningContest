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
package de.indiplex.miningcontest.logic;

import de.indiplex.miningcontest.generator.Outpost;
import de.indiplex.miningcontest.map.MapChunk;
import de.indiplex.miningcontest.util.Door;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class GameThread implements Runnable {

    private MiCo mico;
    private long lastMessage;
    private long lastCheck;
    private long currTime;
    private boolean running;
    private HashMap<Player, Long> drops = new HashMap<Player, Long>();
    private HashMap<Player, Boolean> dropMessage = new HashMap<Player, Boolean>();
    private HashMap<Location, Long> doors = new HashMap<Location, Long>();

    public GameThread(MiCo mico) {
        this.mico = mico;
        running = true;
    }

    /**
     * Changes the status of the MiningContest to running/not running
     * @param running true for running/false for not running
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Sets if the player should receive the dropMessage
     * @param player The player
     */
    public void setDrop(Player player) {
        drops.put(player, currTime);
        dropMessage.put(player, true);
    }

    /**
     * Sets the door to close
     * @param b The location of the door
     */
    public void setDoor(Location b) {
        doors.put(b, currTime);
    }

    @Override
    public void run() {
        mico.startingTime = (lastCheck = (lastMessage = System.currentTimeMillis()));
        long baseChecked = lastCheck;
        long signCheck = 0;
        while (running) {
            currTime = System.currentTimeMillis();
            mico.elapsedTime = currTime - mico.startingTime;
            if (currTime - lastMessage >= 60000) {
                mico.printPoints();
                mico.getShop().refill();
                lastMessage = System.currentTimeMillis();
            }
            if (currTime - lastCheck >= mico.getConfig().getMicoTickRate()) {
                /// Message players who dropped items in the last 10 secs with their points 
                for (Player p : mico.getPlayers()) {
                    if (dropMessage.get(p) != null && dropMessage.get(p)) {
                        if (currTime - drops.get(p) > 3000) {
                            p.sendMessage("You have " + mico.getTeam(p).getPoints(p) + " points now!");
                            dropMessage.put(p, false);
                        }
                    }
                }
                boolean checkSigns = currTime - signCheck >= mico.getConfig().getSignRefreshRate();
                if (checkSigns) {
                    signCheck = System.currentTimeMillis();
                }
                for (WithDoorsAndSigns t : mico.getCheckedChunks()) {
                    for (Location loc : t.getDoors()) {
                        if (loc.getWorld() == null) {
                            loc.setWorld(Bukkit.getWorld("ContestWorld"));
                        }
                        Block b = Bukkit.getWorld("ContestWorld").getBlockAt(loc);
                        Long l = doors.get(loc);
                        if (l != null) {
                            if (currTime - l > 2000) {
                                Door.closeDoor(b);
                                doors.remove(loc);
                            }
                        }
                    }
                    if (checkSigns) {
                        Location loc = t.getSign();
                        if (loc == null) {
                            continue;
                        }
                        if (loc.getWorld() == null) {
                            loc.setWorld(Bukkit.getWorld("ContestWorld"));
                        }
                        Block b = Bukkit.getWorld("ContestWorld").getBlockAt(loc);
                        if (b.getState() instanceof Sign) {
                            Sign s = (Sign) b.getState();
                            if (t.getTeam() != null) {
                                s.setLine(0, "Team " + t.getTeam().getNumber());
                                s.setLine(1, "Points:");
                                s.setLine(2, "" + t.getTeam().getTeamPoints());
                                s.setLine(3, "" + Math.round((mico.getConfig().getDuration() - mico.elapsedTime) / 1000));
                            } else if (t.getType().equals(MapChunk.Type.OUTPOST)) {
                                int i = 0;
                                Outpost outp = (Outpost) t;
                                for (Team te : outp.getConStateKeys()) {
                                    if (i == 4) {
                                        break;
                                    }
                                    String pre = "§"+ChatColor.values()[i+1].getChar();
                                    StringBuilder str = new StringBuilder(pre+"T" + te.getNumber() + ":");
                                    for (int j = 0; j < Math.round(outp.getConState(te)*13/100); j++) {
                                        str = str.append("-");
                                    }
                                    s.setLine(i, str.toString());
                                    i++;
                                }
                            }
                            s.update();
                        } else {
                            System.out.println("Error!");
                        }
                    }
                }

                lastCheck = currTime;
            }
            if (currTime - baseChecked >= 1000) {
                for (Team t : mico.getTeams()) {
                    for (Player p : t.getMembers()) {
                        MapChunk mc = mico.getMap().getMapChunk(p.getLocation().getChunk().getX() + 10, p.getLocation().getChunk().getZ() + 10);
                        if (mc.getType().equals(MapChunk.Type.OUTPOST)) {
                            ((Outpost) mc).increaseConState(t);
                        }
                    }
                }
                baseChecked = currTime;
            }
            if (mico.elapsedTime >= mico.getConfig().getDuration()) {
                mico.end();
            }
        }
        running = false;
    }
}