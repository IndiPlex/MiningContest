package de.indiplex.miningcontest.generator;

import de.indiplex.miningcontest.logic.MiCo;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

public class CavePopulator extends BlockPopulator {
    
    private MiCo mico;

    public CavePopulator(MiCo mico) {
        this.mico = mico;
    }        

    @Override
    public void populate(World world, Random random, Chunk source) {
        int x1 = source.getX();
        int z1 = source.getZ();
        ArrayList<Point> bases = new ArrayList<Point>();
        
        for (int i=0;i<mico.getTeamCount();i++) {
            Point p = mico.getBase(i);
            if (p==null) {
                return;
            }
            bases.add(p);
        }

        Direction dir = calcDirection(x1, z1, bases);
        if (dir.equals(Direction.UNKNOWN)) {
            return;
        }

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 51; y < 55; y++) {
                    if (isInCave(x, y, z, dir)) {
                        Block block = source.getBlock(x, y, z);
                        block.setType(Material.AIR);
                        if (x == 3 || x == 12 || z==3 || z==12) {
                            if (y==54 || z==6 || z==9 || x==6 || x==9) {
                                block.setType(Material.WOOD);
                            }
                        } 
                    }
                }
            }
        }
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int y = 52;
                if (isInCave(x, y, z, dir)) {
                    Block block = source.getBlock(x, y, z);
                    if (x == 3 || x == 12 || z==3 || z==12) {
                        if (z==7 || z==8 || x==7 || x==8) {
                            block.setType(Material.TORCH);
                            block.setData(getTorchDirection(block));
                        }
                    }
                }
            }
        }
    }

    private Direction calcDirection(int x, int z, ArrayList<Point> ps) {
        Direction d = Direction.UNKNOWN;
        
        for (Point p:ps) {
            if (p.x==x) {
                if (d.equals(Direction.Y)) {
                    return Direction.BOTH;
                } else {
                    d = Direction.X;
                }
            }
            if (p.y==z) {
                if (d.equals(Direction.X)) {
                    return Direction.BOTH;
                } else {
                    d = Direction.Y;
                }
            }
        }
        
        return d;
    }
    
    private byte getTorchDirection(Block block) {
        byte b = 0x5;        
        if (checkTorchPlace(block.getRelative(BlockFace.NORTH))) {
            b = 0x1;
        } else if (checkTorchPlace(block.getRelative(BlockFace.SOUTH))) {
            b = 0x2;
        } else if (checkTorchPlace(block.getRelative(BlockFace.EAST))) {
            b = 0x3;
        } else if (checkTorchPlace(block.getRelative(BlockFace.WEST))) {
            b = 0x4;
        }
        return b;
    }
    
    private boolean checkTorchPlace(Block block) {
        return !(block.getType().equals(Material.AIR) || block.getType().equals(Material.TORCH));
    }
    
    private boolean isInCave(int x, int y, int z, Direction dir) {
        boolean b = false;
        switch (dir) {            
            case X:
                b = (y >= 51 && y <= 54) && (x >= 6 && x <= 9);
                break;
            case Y:
                b = (y >= 51 && y <= 54) && (z >= 6 && z <= 9);
                break;
            case BOTH:
                b = (y >= 51 && y <= 54) && ((z >= 6 && z <= 9) || (x >= 6 && x <= 9));
                break;
        }
        return b;
    }
    
    private enum Direction {
        UNKNOWN,
        X,
        Y,
        BOTH;
    }
}
