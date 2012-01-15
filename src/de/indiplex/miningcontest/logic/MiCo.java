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

import de.indiplex.manager.IPMAPI;
import de.indiplex.miningcontest.generator.Base;
import de.indiplex.miningcontest.generator.Outpost;
import de.indiplex.miningcontest.map.Map;
import de.indiplex.miningcontest.map.MapChunk;
import de.indiplex.miningcontest.map.MapParser;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class MiCo {

    private ArrayList<Team> teams = new ArrayList<Team>();
    public boolean initializing = false;
    public boolean started = false;
    private int nextTeam = 0;
    private Random r = new Random(System.currentTimeMillis());
    private Map map;
    private IPMAPI api;
    private MapChunk lobby;
    private ArrayList<WithDoors> checkedChunks = new ArrayList<WithDoors>();
    private ArrayList<Outpost> outposts = new ArrayList<Outpost>();
    public long elapsedTime;
    public long startingTime;
    public GameThread gameThread;
    public StartThread startThread;
    private World micoWorld;
    private ArrayList<Player> players = new ArrayList<Player>(); // ArrayList to check players faster

    public MiCo() {
        this.elapsedTime = 0;
        setMap("Map4.png");
    }

    private void setMap(String mapName) {
        map = MapParser.parseMap("plugins/IndiPlex Manager/config/Mining Contest/res/" + mapName);        
        MapChunk[] mapChunks = map.getMapChunks();
        int t = 0;
        for (MapChunk mc : mapChunks) {
            if (mc.getType().equals(MapChunk.Type.BASE)) {
                Team team = new Team(t, this);
                team.setBase((Base) mc);
                ((Base) mc).setTeam(team);
                teams.add(team);
                checkedChunks.add((WithDoors) mc);
            } else if (mc.getType().equals(MapChunk.Type.LOBBY)) {
                lobby = mc;
            } else if (mc.getType().equals(MapChunk.Type.OUTPOST)) {
                checkedChunks.add((WithDoors) mc);
                outposts.add((Outpost) mc);
            }
        }
    }

    public Map getMap() {
        return map;
    }

    public void setApi(IPMAPI api) {
        this.api = api;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void end() {
        for (Team t : teams) {
            for (Player p : t.getMembers()) {
                p.sendMessage("You got " + t.getPoints(p));
            }
        }
        reset();
    }

    public void reset() {
        for (Team t : teams) {
            t.reset();
        }
        elapsedTime = 0;
        started = false;
        initializing = false;
        if (gameThread != null) {
            gameThread.setRunning(false);
        }
    }

    public void init(int startTime, Integer[] intervals) {
        startThread = new StartThread(startTime, intervals, this);
        new Thread(startThread).start();
    }

    public void start() {
        Bukkit.getServer().broadcastMessage("The mining-contest starts now!");
        for (Team t : teams) {
            for (Player p : t.getMembers()) {
                Location loc = new Location(Bukkit.getWorld("ContestWorld"), t.getBase().getPos().x*16+3, 53, t.getBase().getPos().y*16+3);
                p.teleport(loc);
            }
        }
        for (Team t : teams) {
            players.addAll(t.getMembers());
        }
        gameThread = new GameThread(this);
        new Thread(gameThread).start();
        started = true;
    }

    public Point getBase(int team) {
        return teams.get(team).getBase().getPos();
    }

    public int getTeamCount() {
        return teams.size();
    }

    public boolean isInBase(int x, int z) {
        for (Team t : teams) {
            Point p = t.getBase().getPos();
            if (p.x == x && p.y == z) {
                return true;
            }
        }
        return false;
    }

    public boolean isInBase(Location loc) {
        return isInBase(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()) && loc.getWorld().getName().equalsIgnoreCase("ContestWorld");
    }

    public MapChunk getLobby() {
        return lobby;
    }

    public ArrayList<Outpost> getOutposts() {
        return outposts;
    }

    private boolean isInBase(int x, int y, int z) {
        for (Team t : teams) {
            MapChunk mc = t.getBase();
            if (mc.isInside(x, y, z)) {
                return true;
            }
        }
        return false;
    }

    public boolean canDestroy(int x, int y, int z, World world) {
        boolean b = false;
        for (MapChunk mc : outposts) {
            if (mc.isInside(x, y, z)) {
                b = true;
                break;
            }
        }
        if (!b) {
            for (Team t : teams) {
                if (t.getBase().isInside(x, y, z)) {
                    b = true;
                    break;
                }
            }
        }
        if (!b) {
            if (lobby.isInside(x, y, z)) {
                b = true;
            }
        }
        return !(world.getName().equalsIgnoreCase("ContestWorld") && b);
    }

    public boolean canDestroy(Location loc) {
        return canDestroy(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getWorld());
    }

    public static boolean isInMap(int x, int z) {
        return (x <= 10 && x >= -10) && (z <= 10 && z >= -10);
    }

    public static boolean isBorderChunk(int x, int z) {
        return (x == 10 || x == -10) || (z == 10 || z == -10);
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public World getContestWorld() {
        return micoWorld;
    }

    public Team getNextTeam() {
        Team t = teams.get(nextTeam);
        return t;
    }

    public void nextTeam() {
        nextTeam++;
        if (nextTeam == teams.size()) {
            nextTeam = 0;
        }
    }

    public ArrayList<WithDoors> getCheckedChunks() {
        return checkedChunks;
    }

    public void setNextTeam(int nextTeam) {
        this.nextTeam = nextTeam;
    }

    public boolean isMiCoPlayer(Player player) {
        return started && players.contains(player);
    }

    public Team getTeam(Player player) {
        for (Team t : teams) {
            if (t.hasMember(player)) {
                return t;
            }
        }
        return null;
    }

    public void printPoints() {
        Team winningTeam = null;
        for (Team t : teams) {
            if (winningTeam == null || t.getTeamPoints() > winningTeam.getTeamPoints()) {
                winningTeam = t;
            }
        }
        for (Team t : teams) {
            for (Player p : t.getMembers()) {
                p.sendMessage("You have " + t.getPoints(p) + " points and your teams has " + t.getTeamPoints() + " points!");
                if (t.equals(winningTeam)) {
                    p.sendMessage("Your team is winning :)");
                } else {
                    p.sendMessage("Your team is winning :(");
                }
            }
        }
    }
}
