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

import java.awt.image.BufferedImage;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class Map {

    private byte[][] wool;
    private MapChunk[][] chunks;
    private BufferedImage image;

    public Map(byte[][] wool, MapChunk[][] chunks, BufferedImage image) {
        this.wool = wool;
        this.chunks = chunks;
        this.image = image;
    }

    public byte getWool(int x, int y) {
        return wool[x][y];
    }

    public byte[][] getWool() {
        return wool;
    }

    public MapChunk[] getMapChunks() {
        MapChunk[] mcs = new MapChunk[21*21];
        for (int i=0;i<21;i++) {
            for (int j=0;j<21;j++) {
                mcs[i*21+j] = chunks[i][j];
            }
        }
        return mcs;
    }

    public BufferedImage getImage() {
        return image;
    }

    public MapChunk getMapChunk(int x, int y) {
        return chunks[x][y];
    }
}
