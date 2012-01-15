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
import de.indiplex.miningcontest.logic.WithDoors;
import de.indiplex.miningcontest.map.MapChunk;
import java.awt.Point;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class Base extends MapChunk implements WithDoors {
    
    private ArrayList<Location> doors = new ArrayList<Location>();
    private Team team;
    
    public Base(Point pos, int[][] data, Room room, Type type) {
        super();
        this.pos = pos;
        this.data = data;
        this.room = room;
        this.type = type;
        
        for (int x=0;x<16;x++) {
            for (int y=0;y<16;y++) {
                if (data[x][y]==Material.IRON_DOOR_BLOCK.getId()) {
                    doors.add(new Location(null, pos.x*16+x, room.getStart()+1, pos.y*16+y));
                    doors.add(new Location(null, pos.x*16+x, room.getStart()+2, pos.y*16+y));
                }
            }
        }
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public Team getTeam() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<Location> getDoors() {
        return doors;
    }
    
}
