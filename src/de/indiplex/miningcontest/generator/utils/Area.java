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
package de.indiplex.miningcontest.generator.utils;

import java.util.ArrayList;

/**
 * Represents an area of a layer
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class Area {

    /**
     * 
     * @param id The material id
     * @param sd The data value
     */
    public Area(int id, byte sd) {
        data = new int[16][16];
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                data[x][y] = id;
                sdata[x][y] = sd;
            }
        }
    }

    public void addRows(ArrayList<Row> rows) {
        for (Row r : rows) {
            if (r.dir == 1) {
                for (int x = 0; x < 16; x++) {
                    data[x][r.val] = r.data[x];
                    sdata[x][r.val] = r.sdata[x];
                }
            } else {
                for (int y = 0; y < 16; y++) {
                    data[r.val][y] = r.data[y];
                    sdata[r.val][y] = r.sdata[y];
                }
            }
        }
    }

    public void addPoints(ArrayList<Point> points) {
        for (Point p : points) {
            data[p.x][p.y] = p.data;
            sdata[p.x][p.y] = p.sdata;
        }
    }
    public int[][] data = new int[16][16];
    public byte[][] sdata = new byte[16][16];
    
}
