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
package de.indiplex.miningcontest.logic.classes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class Crafter extends MiCoClass {

    public Crafter(Player player) {
        super(player, Type.CRAFTER);
    }

    @Override
    public void fitOutPlayer(Player player) {
        ItemStack[] result = new ItemStack[6];
        result[0] = new ItemStack(Material.STICK, 32);
        result[1] = new ItemStack(Material.WOOD, 32);
        result[2] = new ItemStack(Material.STONE, 16);
        result[3] = new ItemStack(Material.SAPLING, 3);
        result[4] = new ItemStack(Material.DIRT, 3);
        result[5] = new ItemStack(Material.TORCH, 48);
        player.getInventory().addItem(result);
        player.setItemInHand(new ItemStack(Material.STONE_SWORD, 1));
        player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS, 1));
        player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS, 1));
        player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE, 1));
        player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET, 1));
    }
    
}
