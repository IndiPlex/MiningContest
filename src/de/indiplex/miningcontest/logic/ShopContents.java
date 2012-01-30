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
package de.indiplex.miningcontest.logic;

import de.indiplex.miningcontest.logic.classes.MiCoClass;
import java.util.EnumMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class ShopContents {
    
    private static final ItemStack[] standardInv;
    private static final Map<MiCoClass.Type, ItemStack[]> standardInvs = new EnumMap<MiCoClass.Type, ItemStack[]>(MiCoClass.Type.class);
    private static final Map<Material, Integer> prices = new EnumMap<Material, Integer>(Material.class);
    
    static {
        //STANDARD
        ItemStack[] inv = new ItemStack[9];
        inv[0] = new ItemStack(Material.PORK, 4);
        inv[1] = new ItemStack(Material.PORK, 10);
        inv[2] = new ItemStack(Material.STONE_PICKAXE, 2);
        inv[3] = new ItemStack(Material.STONE_PICKAXE, 2);
        inv[4] = new ItemStack(Material.STONE_SWORD, 2);
        inv[5] = new ItemStack(Material.TORCH, 16);
        inv[6] = new ItemStack(Material.TORCH, 32);
        inv[7] = new ItemStack(Material.TORCH, 32);
        inv[8] = new ItemStack(Material.TORCH, 64);
        standardInv = inv;
        
        //MINER
        inv = new ItemStack[4];
        inv[0] = new ItemStack(Material.DIAMOND_PICKAXE, 2);
        inv[1] = new ItemStack(Material.IRON_PICKAXE, 2);
        inv[2] = new ItemStack(Material.IRON_PICKAXE, 2);
        inv[3] = new ItemStack(Material.TNT, 10);
        standardInvs.put(MiCoClass.Type.MINER, inv);
        
        //CRAFTER
        inv = new ItemStack[5];
        inv[0] = new ItemStack(Material.WOOD, 32);
        inv[1] = new ItemStack(Material.WOOD, 32);
        inv[2] = new ItemStack(Material.WOOD, 64);
        inv[3] = new ItemStack(Material.WOOD, 64);
        inv[4] = new ItemStack(Material.WORKBENCH, 2);
        standardInvs.put(MiCoClass.Type.CRAFTER, inv);
        
        //HUNTER
        inv = new ItemStack[18];
        inv[0] = new ItemStack(Material.ARROW, 32);
        inv[1] = new ItemStack(Material.ARROW, 32);
        inv[2] = new ItemStack(Material.ARROW, 64);
        inv[3] = new ItemStack(Material.ARROW, 64);
        inv[4] = new ItemStack(Material.ARROW, 64);
        inv[5] = new ItemStack(Material.BOW, 2);
        inv[6] = new ItemStack(Material.BOW, 2);
        inv[7] = new ItemStack(Material.IRON_SWORD, 2);
        inv[8] = new ItemStack(Material.IRON_BOOTS, 2);
        inv[9] = new ItemStack(Material.IRON_BOOTS, 2);
        inv[10] = new ItemStack(Material.IRON_LEGGINGS, 2);
        inv[11] = new ItemStack(Material.IRON_LEGGINGS, 2);
        inv[12] = new ItemStack(Material.IRON_CHESTPLATE, 2);
        inv[13] = new ItemStack(Material.IRON_CHESTPLATE, 2);
        inv[14] = new ItemStack(Material.IRON_HELMET, 2);
        inv[15] = new ItemStack(Material.IRON_HELMET, 2);
        inv[16] = new ItemStack(Material.COOKED_CHICKEN, 20);
        inv[17] = new ItemStack(Material.COOKED_CHICKEN, 10);
        standardInvs.put(MiCoClass.Type.HUNTER, inv);
        
        //WIZZARD
        inv = new ItemStack[8];
        inv[0] = new ItemStack(Material.BREWING_STAND_ITEM, 2);
        inv[1] = new ItemStack(Material.BREWING_STAND_ITEM, 2);
        inv[2] = new ItemStack(Material.WATER_BUCKET, 4);
        inv[3] = new ItemStack(Material.WATER_BUCKET, 2);
        inv[4] = new ItemStack(Material.GLASS_BOTTLE, 20);
        inv[5] = new ItemStack(Material.GLASS_BOTTLE, 10);
        inv[6] = new ItemStack(Material.NETHER_WARTS, 10);
        inv[7] = new ItemStack(Material.NETHER_WARTS, 10);
        standardInvs.put(MiCoClass.Type.WIZZARD, inv);
        
        //WARRIOR
        inv = new ItemStack[22];
        inv[0] = new ItemStack(Material.COOKED_BEEF, 20);
        inv[1] = new ItemStack(Material.DIAMOND_SWORD, 2);
        inv[2] = new ItemStack(Material.DIAMOND_BOOTS, 2);
        inv[3] = new ItemStack(Material.DIAMOND_BOOTS, 2);
        inv[4] = new ItemStack(Material.DIAMOND_LEGGINGS, 2);
        inv[5] = new ItemStack(Material.DIAMOND_LEGGINGS, 2);
        inv[6] = new ItemStack(Material.DIAMOND_CHESTPLATE, 2);
        inv[7] = new ItemStack(Material.DIAMOND_CHESTPLATE, 2);
        inv[8] = new ItemStack(Material.DIAMOND_HELMET, 2);
        inv[9] = new ItemStack(Material.DIAMOND_HELMET, 2);
        inv[10] = new ItemStack(Material.IRON_SWORD, 2);
        inv[11] = new ItemStack(Material.IRON_BOOTS, 2);
        inv[12] = new ItemStack(Material.IRON_BOOTS, 2);
        inv[13] = new ItemStack(Material.IRON_LEGGINGS, 2);
        inv[14] = new ItemStack(Material.IRON_LEGGINGS, 2);
        inv[15] = new ItemStack(Material.IRON_CHESTPLATE, 2);
        inv[16] = new ItemStack(Material.IRON_CHESTPLATE, 2);
        inv[17] = new ItemStack(Material.IRON_HELMET, 2);
        inv[18] = new ItemStack(Material.IRON_HELMET, 2);
        inv[19] = new ItemStack(Material.COOKED_BEEF, 10);
        inv[20] = new ItemStack(Material.COOKED_FISH, 20);
        inv[21] = new ItemStack(Material.COOKED_FISH, 10);
        standardInvs.put(MiCoClass.Type.WARRIOR, inv);
        
        //PRICES
        prices.put(Material.PORK, 20);
        prices.put(Material.STONE_PICKAXE, 30);
        prices.put(Material.STONE_SWORD, 35);
        // TODO: Add other prices
    }
    
    /**
     * Fills the specified inventory with the class items
     * @param inv The inventory to fill
     * @param t Class type of the player
     */
    public static void fillInventory(Inventory inv, MiCoClass.Type t) {
        ItemStack[] clStack = standardInvs.get(t);
        for (ItemStack is:standardInv) {
            inv.setItem(inv.firstEmpty(), is);
        }
        for (ItemStack is:clStack) {
            inv.setItem(inv.firstEmpty(), is);
        }
    }
    
    /**
     * Refills the specified inventory with the class items
     * @param inv The inventory to fill
     * @param t Class type of the player
     */
    public static void refillInventory(Inventory inv, MiCoClass.Type t) {
        if (inv==null) {
            return;
        }
        inv.clear();
        fillInventory(inv, t);
    }
    
    /**
     * Gets the price of the specified material
     * @param mat The material to get the price of
     * @return int The price
     */
    public static int getPrice(Material mat) {
        Integer i = prices.get(mat);
        if (i==null) {
            return 0;
        }
        return i;
    }
    
}
