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
package de.indiplex.miningcontest.map;

import de.indiplex.miningcontest.generator.Room;
import de.indiplex.miningcontest.generator.RoomParser;
import java.awt.Point;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class MapChunk {

    protected Type type;
    protected Room room;
    protected Point pos;
    protected int[][] data;

    /**
     * Initializes a new MapChunk
     * @param pos
     * @param data 
     */
    public MapChunk(Point pos, int[][] data) {
        this.pos = pos;
        this.data = data;

        switch (data[0][0]) {
            case 0xFF00FF00:
                type = Type.LOBBY;
                room = RoomParser.parseRoom(type);
                break;
            case 0xFF0000FF:
                type = Type.BASE;
                room = RoomParser.parseRoom(type);
                break;
            case 0xFF00FFFF:
                if (data[8][8] == 0xFFFF0000) {
                    type = Type.DUNGEON;
                    room = RoomParser.parseRoom(type);
                } else {
                    type = Type.LOOTROOM;
                    room = RoomParser.parseRoom(type);
                }
                break;
            case 0xFFFFFF00:
                type = Type.OUTPOST;
                room = RoomParser.parseRoom(type);                
                break;
            default:
                type = Type.REST;
                break;
        }        
    }
    
    protected MapChunk() {
        
    }

    /**
     * Checks whether a location is in the room
     * @param x The x-position
     * @param y The y-position
     * @param z The z-position
     * @return boolean true if location is inside the room, false if not
     */
    public boolean isInside(int x, int y, int z) {        
        int ax = x - pos.x * 16;
        int az = z - pos.y * 16;
        if (type.equals(Type.REST)) {
            if ((y>=51 && y<=55) && (ax >= 0 && ax <= 15) && (az >= 0 && az <= 15)) {
                return data[x][z]!=0xFFFFFFFF;
            } else {
                return false;
            }
        }
        
        return (ax >= 0 && ax <= 15) && (az >= 0 && az <= 15) && (y >= room.getStart() && y < room.getStart() + room.getHeight());
    }
    
    /**
     * Checks whether a location is in the room
     * @param loc The location
     * @return boolean true if location is inside the room, false if not
     */
    public boolean isInside(Location loc) {
        return loc.getWorld()!=null && loc.getWorld().getName().equalsIgnoreCase("ContestWorld") && isInside(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    /**
     * Get the BukkitChunk at the position of the MapChunk
     * @return Chunk The BukkitChunk
     */
    public Chunk getChunk() {
        return Bukkit.getWorld("ContestWorld").getChunkAt(pos.x, pos.y);
    }

    /**
     * Get the room inside of the MapChunk
     * @return Room The room
     */
    public Room getRoom() {
        return room;
    }
    
    /**
     * Get the spawn location
     * @return Location The spawn locaton
     */
    public Location getSpawnLoc() {
        return new Location(Bukkit.getWorld("ContestWorld"), pos.x*16+3, room.getStart()+2, pos.y*16+3);
    }
    
    /**
     * Get the color map data at the specified point
     * @param x The x-position
     * @param y The y-position
     * @return int The color data
     */
    public int getData(int x, int y) {
        return data[x][y];
    }
    
    /**
     * Get the position of the MapChunk
     * @return Point The position
     */
    public Point getPos() {
        return pos;
    }

    /**
     * Get the type of the MapChunk
     * @return 
     */
    public Type getType() {
        return type;
    }

    public enum Type {
        LOOTROOM,
        DUNGEON,
        LOBBY,
        BASE,
        OUTPOST,
        REST;
    }
}
