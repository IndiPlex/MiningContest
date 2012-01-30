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
package de.indiplex.miningcontest.generator;

import de.indiplex.miningcontest.generator.utils.Layer;
import de.indiplex.miningcontest.map.MapChunk;
import java.util.ArrayList;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class Room {

    private int[][][] data = new int[11][16][16];
    private byte[][][] sdata = new byte[11][16][16];
    private int height;
    private int start;
    private MapChunk.Type type;

    /**
     * Initializes a new Room
     * @param layers The layers of the room
     * @param type The type of the room
     * @param height The height of the room
     * @param start 
     */
    public Room(ArrayList<Layer> layers, MapChunk.Type type, int height, int start) {
        this.height = height;
        this.start = start;
        data = new int[height][16][16];
        sdata = new byte[height][16][16];
        for (int i = 0; i < 11; i++) {
            Layer l = layers.get(i);
            int[][] temp = new int[16][16];
            byte[][] stemp = new byte[16][16];
            for (int j = 0; j < 16; j++) {
                temp[j] = l.data[j].clone();
                stemp[j] = l.sdata[j].clone();
            }
            sdata[l.getValue()] = stemp;
            data[l.getValue()] = temp;
        }
        this.type = type;
    }

    public int getHeight() {
        return height;
    }

    public int getStart() {
        return start;
    }

    public int[][][] getData() {
        return data;
    }

    public int getData(int x, int y, int z) {
        return data[y][x][z];
    }

    public byte getSpecialData(int x, int y, int z) {
        return sdata[y][x][z];
    }
}
