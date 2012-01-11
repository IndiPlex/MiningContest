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

import de.indiplex.miningcontest.MiningContest;
import de.indiplex.miningcontest.logic.MiCo;
import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

/**
 * 
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class BasePopulator extends BlockPopulator {
    
    private MiCo mico;

    public BasePopulator(MiCo mico) {
        this.mico = mico;
    } 

    @Override
    public void populate(World world, Random random, Chunk source) {
        if (source.getX() == 0 && source.getZ() == 0) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 99; y < 128; y++) {
                        Block b = source.getBlock(x, y, z);
                        if ((x == 0 || x == 15) || (z == 0 || z == 15) || (y == 99 || y == 127)) {
                            b.setType(Material.BEDROCK);
                        } else {
                            b.setType(Material.AIR);
                        }
                    }
                }
            }
        } else if (mico.isInBase(source.getX(), source.getZ())) {
            for (int y = 50; y <= 60; y++) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        Block block = source.getBlock(x, y, z);
                        if ((x == 0 || x == 15) || (z == 0 || z == 15) || (y == 50 || y == 60)) {
                            if (isBaseExit(x, y, z)) {
                                block.setType(Material.AIR);
                            } else {
                                block.setType(Material.BEDROCK);
                            }
                        } else {
                            if (y == 59 && (x != 0 && x != 15 && z != 0 && z != 15)) {
                                block.setType(Material.GLOWSTONE);
                            } else {
                                block.setType(Material.AIR);
                            }
                        }
                    }
                }
            }

            for (int y = 59; y >= 52; y--) {
                for (int x = 59 - y; x < 16 - (59 - y); x++) {
                    for (int z = 59 - y; z < 16 - (59 - y); z++) {
                        Block b = source.getBlock(x, y, z);
                        b.setType(Material.GLOWSTONE);
                    }
                }
            }
            source.getBlock(7, 51, 7).setType(Material.GLOWSTONE);
            source.getBlock(7, 51, 8).setType(Material.GLOWSTONE);
            source.getBlock(8, 51, 7).setType(Material.GLOWSTONE);
            source.getBlock(8, 51, 8).setType(Material.GLOWSTONE);

            /*source.getBlock(7, 52, 7).setType(Material.GLOWSTONE);
            source.getBlock(7, 52, 8).setType(Material.GLOWSTONE);
            source.getBlock(8, 52, 7).setType(Material.GLOWSTONE);
            source.getBlock(8, 52, 8).setType(Material.GLOWSTONE);*/

            source.getBlock(7, 51, 9).setType(Material.CHEST);
            source.getBlock(8, 51, 9).setType(Material.CHEST);
            source.getBlock(7, 51, 6).setType(Material.CHEST);
            source.getBlock(8, 51, 6).setType(Material.CHEST);

            source.getBlock(9, 51, 7).setType(Material.CHEST);
            source.getBlock(9, 51, 8).setType(Material.CHEST);
            source.getBlock(6, 51, 7).setType(Material.CHEST);
            source.getBlock(6, 51, 8).setType(Material.CHEST);
            
            MiningContest.log.info(MiningContest.pre+"Placed base at X:"+source.getX()+" Z:"+source.getZ());
        }
    }
    
    private boolean isBaseExit(int x, int y, int z) {
        return (y >= 51 && y <= 54) && (((x >= 6 && x <= 9) && (z == 0 || z == 15)) || ((z >= 6 && z <= 9) && (x == 0 || x == 15)));
    }
}
