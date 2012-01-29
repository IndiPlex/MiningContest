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

import de.indiplex.miningcontest.map.MapChunk;
import java.awt.Point;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class Lobby extends MapChunk {
    
    private ArrayList<Location> signs = new ArrayList<Location>();
    
    public Lobby(Point pos, int[][] data, Room room, Type type) {
        super();
        this.pos = pos;
        this.data = data;
        this.room = room;
        this.type = type;
        
        for (int x=0;x<16;x++) {
            for (int y=0;y<room.getHeigth();y++) {
                for (int z=0;z<16;z++) {
                    if (room.getData(x, y, z)==Material.SIGN.getId() || room.getData(x, y, z)==Material.SIGN_POST.getId()) {
                        Location loc = new Location(null, pos.x*16+x, room.getStart()+y, pos.y*16+z);
                        signs.add(loc);
                    }
                }
            }
        }
    }

    public ArrayList<Location> getSigns() {
        return signs;
    }
    
}
