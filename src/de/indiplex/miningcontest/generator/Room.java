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

import de.indiplex.miningcontest.map.MapChunk;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class Room {

    private int[][][] data = new int[11][16][16];
    private byte[][][] sdata = new byte[11][16][16];
    private int heigth;
    private int start;
    private MapChunk.Type type;

    public Room(ArrayList<Layer> layers, MapChunk.Type type, int heigth, int start) {
        this.heigth = heigth;
        this.start = start;
        data = new int[heigth][16][16];
        sdata = new byte[heigth][16][16];
        for (int i = 0; i < 11; i++) {
            Layer l = layers.get(i);
            int[][] temp = new int[16][16];
            byte[][] stemp = new byte[16][16];
            for (int j=0;j<16;j++) {
                temp[j] = l.data[j].clone();
                stemp[j] = l.sdata[j].clone();
            }
            sdata[l.val] = stemp;
            data[l.val] = temp;
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("trololol"));
            for (int y = 0; y < 11; y++) {
                bw.write("y:" + y + "\n");
                for (int x = 0; x < 16; x++) {
                    bw.write("x:" + x + "\n");
                    for (int z = 0; z < 16; z++) {
                        bw.write("z:" + z);
                        bw.write(" "+data[y][x][z]+"\n");
                    }
                    bw.write("\n");
                }
                bw.write("\n");
                bw.write("\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.type = type;
    }

    public int getHeigth() {
        return heigth;
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

    public static class Layer implements Cloneable {

        int val;
        public int[][] data = new int[16][16];
        public byte[][] sdata = new byte[16][16];

        public Layer(Area a, int val) {
            int[][] temp = new int[16][16];
            byte[][] stemp = new byte[16][16];
            for (int j=0;j<16;j++) {
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
            for (int j=0;j<16;j++) {
                temp[j] = data[j].clone();
                stemp[j] = sdata[j].clone();
            }
            this.data = temp;
            this.sdata = stemp;
            this.val = val;
        }

        @Override
        protected Layer clone() throws CloneNotSupportedException {
            return new Layer(data, val);
        }
        
        public void include(Area a) {
            for (int x=0;x<16;x++) {
                for (int y=0;y<16;y++) {
                    if (a.data[x][y]!=-1) {
                        data[x][y] = a.data[x][y];
                    }
                    if (a.sdata[x][y]!=-2) {
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
        
    }

    public static class Area {

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

    public static class Row {

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

    public static class Point {

        public int x;
        public int y;
        public int data;
        public byte sdata;

        public Point(int x, int y, int id, byte sd) {
            this.x = x;
            this.y = y;
            this.data = id;
            this.sdata = sd;
        }       
        
    }
}
