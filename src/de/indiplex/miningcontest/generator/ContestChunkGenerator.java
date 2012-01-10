package de.indiplex.miningcontest.generator;

import de.indiplex.miningcontest.logic.MiCo;
import de.indiplex.multiworlds.MultiWorldsAPI;
import de.indiplex.multiworlds.generators.MWGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

public class ContestChunkGenerator extends MWGenerator {

    private final int COAL_CHANCE = 800; // Out of 10000
    private final int IRON_CHANCE = 400; // Out of 10000
    private final int REDSTONE_CHANCE = 160; // Out of 10000
    private final int DIAMOND_CHANCE = 80; // Out of 10000
    private MiCo mico;

    public ContestChunkGenerator() {
        mico = new MiCo();
    }

    @Override
    public void initAPI(MultiWorldsAPI API) {
        super.initAPI(API);
    }        

    @Override
    public byte[] generate(World world, Random random, int i, int i1) {
        byte[] blocks = new byte[32768];
        if (!MiCo.isInMap(i, i1)) {
            return blocks;
        }
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 127; y++) {
                    int val = (x * 16 + z) * 128 + y;
                    if (y == 0 || y == 125) {
                        blocks[val] = (byte) Material.BEDROCK.getId();
                        continue;
                    }
                    if (y == 126) {
                        blocks[val] = (byte) Material.WOOL.getId();
                        continue;
                    }
                    int r = random.nextInt(10000);
                    if (r < DIAMOND_CHANCE) {
                        blocks[val] = (byte) Material.DIAMOND_ORE.getId();
                    } else if (r < REDSTONE_CHANCE) {
                        blocks[val] = (byte) Material.REDSTONE_ORE.getId();
                    } else if (r < IRON_CHANCE) {
                        blocks[val] = (byte) Material.IRON_ORE.getId();
                    } else if (r < COAL_CHANCE) {
                        blocks[val] = (byte) Material.COAL_ORE.getId();
                    } else {
                        blocks[val] = (byte) Material.STONE.getId();
                    }
                }
            }
        }
        return blocks;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        List<BlockPopulator> pops = new ArrayList<BlockPopulator>();
        //pops.add(new CavePopulator(mico));
        //pops.add(new BasePopulator(mico));
        pops.add(new SpecialRoomGenerator(mico));
        pops.add(new WoolPopulator(mico.getMap().getWool()));
        return pops;
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        Block b = world.getChunkAt(0, 0).getBlock(7, 100, 8);

        return new Location(world, b.getX(), b.getY(), b.getZ());
    }

    public MiCo getMico() {
        return mico;
    }
        
}
