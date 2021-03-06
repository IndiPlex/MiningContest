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
public class Row {
    public int val;
    public int dir;
    public int[] data = new int[16];
    public byte[] sdata = new byte[16];

    public Row(int val, int dir, int id, byte sd) {
        this.val = val;
        this.dir = dir;
        for (int i = 0; i < 16; i++) {
            data[i] = id;
            sdata[i] = sd;
        }
    }

    public Row() {
    }
    
}
