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

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class Layer implements Cloneable {
    int val;
    public int[][] data = new int[16][16];
    public byte[][] sdata = new byte[16][16];

    public Layer(Area a, int val) {
        int[][] temp = new int[16][16];
        byte[][] stemp = new byte[16][16];
        for (int j = 0; j < 16; j++) {
            temp[j] = a.data[j].clone();
            stemp[j] = sdata[j].clone();
        }
        data = temp;
        this.sdata = stemp;
        this.val = val;
    }

    private Layer(int[][] data, int val) {
        int[][] temp = new int[16][16];
        byte[][] stemp = new byte[16][16];
        for (int j = 0; j < 16; j++) {
            temp[j] = data[j].clone();
            stemp[j] = sdata[j].clone();
        }
        this.data = temp;
        this.sdata = stemp;
        this.val = val;
    }

    @Override
    public Layer clone() throws CloneNotSupportedException {
        return new Layer(data, val);
    }

    public void include(Area a) {
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                if (a.data[x][y] != -1) {
                    data[x][y] = a.data[x][y];
                }
                if (a.sdata[x][y] != -2) {
                    sdata[x][y] = a.sdata[x][y];
                }
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Layer) && obj.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.val;
        return hash;
    }

    public int getValue() {
        return this.val;
    }
    
}
