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

import de.indiplex.miningcontest.logic.MiCo;
import de.indiplex.miningcontest.logic.classes.MiCoClass;
import de.indiplex.miningcontest.map.Map;
import de.indiplex.miningcontest.map.MapChunk;
import de.indiplex.miningcontest.map.MapChunk.Type;
import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Sign;
import org.bukkit.entity.CreatureType;
import org.bukkit.generator.BlockPopulator;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class SpecialRoomGenerator extends BlockPopulator {

    private MiCo mico;
    private MiCoClass.Type[] types;
    private int ct;

    /**
     * All rooms that are not full or paths
     * @param mico The MiningContest
     */
    public SpecialRoomGenerator(MiCo mico) {
        this.mico = mico;
        types = MiCoClass.Type.values();
        ct = 0;
    }

    @Override
    public void populate(World world, Random random, Chunk source) {
        Map map = mico.getMap();
        if (map==null) {
            return;
        }
        int cx = source.getX() + 10;
        int cz = source.getZ() + 10;
        if (cx < 0 || cx > 20 || cz < 0 || cz > 20) {
            return;
        }
        MapChunk mapChunk = map.getMapChunk(cx, cz);
        MapChunk.Type t = mapChunk.getType();

        if (t.equals(Type.REST)) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    int val = mapChunk.getData(x, z);
                    if (val == 0xFFFFFFFF) {
                        continue;
                    }
                    Material mat = Material.AIR;
                    int s = 51;
                    int e = 55;
                    if (val == 0xFFFF0000) {
                        mat = Material.BEDROCK;
                        s = 0;
                        e = 127;
                    }
                    for (int y = s; y < e; y++) {
                        source.getBlock(x, y, z).setType(mat);
                    }
                }
            }
        } else {
            Room room = mapChunk.getRoom();
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = room.getStart(); y < room.getHeight() + room.getStart(); y++) {
                        int data = room.getData(x, y - room.getStart(), z);
                        byte sdata = room.getSpecialData(x, y - room.getStart(), z);
                        Block b = source.getBlock(x, y, z);
                        if (data != -1) {
                            b.setTypeId(data);
                        }
                        if (data != -1) {
                            if (data == Material.MOB_SPAWNER.getId()) {
                                if (b.getState() instanceof CreatureSpawner) {
                                    CreatureSpawner cs = (CreatureSpawner) b.getState();
                                    CreatureType ct = CreatureType.PIG;
                                    switch (sdata) {
                                        case 0:
                                            ct = CreatureType.ZOMBIE;
                                            break;
                                        case 1:
                                            ct = CreatureType.SKELETON;
                                            break;
                                        case 2:
                                            ct = CreatureType.SPIDER;
                                            break;
                                    }
                                    cs.setCreatureType(ct);
                                }
                            } else {
                                b.setData(sdata);
                            }
                        }
                        if (mapChunk.getType()==MapChunk.Type.LOBBY) {
                            ArrayList<Location> signs = ((Lobby) mapChunk).getSigns();
                            for (Location loc : signs) {
                                if (loc.getWorld()==null) {
                                    loc.setWorld(world);
                                }
                            }
                            if (signs.contains(b.getLocation())) {
                                Sign s = (Sign) b.getState();
                                if (ct<types.length) {
                                    s.setLine(1, types[ct].toString());
                                    ct++;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
