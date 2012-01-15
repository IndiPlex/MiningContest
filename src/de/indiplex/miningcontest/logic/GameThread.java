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
import de.indiplex.miningcontest.util.Door;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class GameThread implements Runnable {

    private MiCo mico;
    private final long duration = 15 * 60 * 1000; // 15 Minutes
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

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setDrop(Player player) {
        drops.put(player, currTime);
        dropMessage.put(player, true);
    }

    public void setDoor(Location b) {
        doors.put(b, currTime);
    }

    @Override
    public void run() {
        mico.startingTime = (lastCheck = (lastMessage = System.currentTimeMillis()));
        while (running) {
            currTime = System.currentTimeMillis();
            mico.elapsedTime = mico.startingTime - currTime;
            if (currTime - lastMessage >= 60000) {
                mico.printPoints();
                lastMessage = System.currentTimeMillis();
            }
            if (currTime - lastCheck >= 300) {

                /// Message players who dropped items in the last 10 secs with their points 
                for (Player p : mico.getPlayers()) {
                    if (dropMessage.get(p) != null && dropMessage.get(p)) {
                        if (currTime - drops.get(p) > 3000) {
                            p.sendMessage("You have " + mico.getTeam(p).getPoints(p) + " points now!");
                            dropMessage.put(p, false);
                        }
                    }
                }
                for (WithDoors t : mico.getCheckedChunks()) {
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
                }
                // TODO: Write the code to update signs

                lastCheck = currTime;
            }
            if (currTime - lastCheck >= 1000) {
                for (Team t : mico.getTeams()) {
                    for (Player p : t.getMembers()) {
                        for (Outpost o : mico.getOutposts()) {
                            if (o.isInside(p.getLocation())) {
                                o.increaseConState(t);
                            }
                        }
                    }
                }
            }
            if (mico.elapsedTime >= duration) {
            }
        }
        running = false;
    }
}