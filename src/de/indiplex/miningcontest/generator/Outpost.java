/*
 * MiningContest
 * Copyright (C) 2012 IndiPlex
 * 
 * MiningContest is free software: you can redistribute it and/or modify
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
package de.indiplex.miningcontest.generator;

import de.indiplex.miningcontest.logic.Team;
import de.indiplex.miningcontest.logic.WithDoorsAndSigns;
import de.indiplex.miningcontest.map.MapChunk;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class Outpost extends MapChunk implements WithDoorsAndSigns {

    private Team team = null;
    private ArrayList<Location> doors = new ArrayList<Location>();
    private Location sign;
    private HashMap<Team, Integer> conState = new HashMap<Team, Integer>();

    /**
     * Initializes a new Outpost
     * @param pos Position of the Outpost
     * @param data Color data of the MapParser
     * @param room The room data
     * @param type Type of the room (should be Outpost)
     */
    public Outpost(Point pos, int[][] data, Room room, Type type) {
        super();
        this.pos = pos;
        this.data = data;
        this.room = room;
        this.type = type;

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < room.getHeight(); y++) {
                for (int z = 0; z < 16; z++) {
                    if (room.getData(x, y, z) == Material.IRON_DOOR_BLOCK.getId()) {
                        Location loc = new Location(null, pos.x * 16 + x, room.getStart() + y, pos.y * 16 + z);
                        doors.add(loc);
                    } else if (room.getData(x, y, z)==Material.SIGN.getId() || room.getData(x, y, z)==Material.SIGN_POST.getId()) {
                        Location loc = new Location(null, pos.x*16+x, room.getStart()+y, pos.y*16+z);
                        sign = loc;
                    }
                }
            }
        }
    }

    /**
     * Sets the team which the Outpost belongs to
     * @param team 
     */
    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public Location getSign() {
        return sign;
    }

    public boolean isConquered() {
        return team != null;
    }

    @Override
    public Team getTeam() {
        return team;
    }

    @Override
    public ArrayList<Location> getDoors() {
        return doors;
    }

    /**
     * Increases the conquering status of the Outpost
     * @param t Team which is currently conquering
     */
    public void increaseConState(Team t) {
        if (conState.get(t)==null) {
            conState.put(t, 0);
        }
        if (conState.get(t) >= 100) {
            return;
        }
        for (Team te : conState.keySet()) {
            if (te != t) {
                int n = conState.get(te) - 5;
                if (n < 0) {
                    continue;
                }
                conState.put(te, n);
            }
        }
        conState.put(t, conState.get(t) + 5);
        if (conState.get(t) >= 100) {
            team = t;
            t.sendMessage("You conquered a base!");
        }
    }
    
    /**
     * 
     * @return int The size of the conState HashMap
     */
    public int getConStateSize() {
        return conState.size();
    }
    
    /**
     * 
     * @return Set<Team> The keys of the conState HashMap
     */
    public Set<Team> getConStateKeys() {
        return conState.keySet();
    }
    
    /**
     * 
     * @param t The team of which you want to know the state
     * @return int The current conquering status
     */
    public int getConState(Team t) {        
        return conState.get(t);
    }
    
}
