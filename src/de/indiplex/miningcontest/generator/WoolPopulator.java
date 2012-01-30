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

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

/**
 * 
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class WoolPopulator extends BlockPopulator {

    private byte[][] data;

    /**
     * Places the wool at the top layer of the MiningContestWorld
     * @param data WoolColor data-array
     */
    public WoolPopulator(byte[][] data) {
        this.data = data;
    }

    @Override
    public void populate(World world, Random random, Chunk source) {
        int y = 126;
        if (data == null) {
            return;
        }

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int absX = (source.getX() + 10) * 16 + x;
                int absZ = (source.getZ() + 10) * 16 + z;


                if (absX > 336 || absZ > 336 || absX < 0 || absZ < 0) {
                    continue;
                }
                Block block = source.getBlock(x, y, z);
                try {
                    block.setData(data[absX][absZ]);
                } catch (ArrayIndexOutOfBoundsException e) {}
            }
        }

    }
}
