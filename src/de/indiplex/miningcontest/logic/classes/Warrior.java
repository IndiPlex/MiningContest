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
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class Warrior extends MiCoClass {

    public Warrior(Player player) {
        super(player, Type.WARRIOR);
    }

    @Override
    public void fitOutPlayer(Player player) {
        ItemStack[] result = new ItemStack[6];
        result[0] = new ItemStack(Material.STONE_PICKAXE, 1);
        result[1] = new Potion(PotionType.INSTANT_HEAL).toItemStack(5);
        result[2] = new ItemStack(Material.IRON_SWORD, 1);
        result[3] = new ItemStack(Material.COOKED_BEEF, 10);
        result[4] = new ItemStack(Material.COOKED_FISH, 10);
        result[5] = new ItemStack(Material.TORCH, 32);
        player.getInventory().addItem(result);
        player.setItemInHand(new ItemStack(Material.DIAMOND_SWORD, 1));
        player.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS, 1));
        player.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1));
        player.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1));
        player.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET, 1));
    }
    
}
