package de.indiplex.miningcontest.generator;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

/**
 *
 * @author temp
 */
public class WoolPopulator extends BlockPopulator {

    private byte[][] data;

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
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error: " + absX + " " + absZ);
                }
            }
        }

    }
}
