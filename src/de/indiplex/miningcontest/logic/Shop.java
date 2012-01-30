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

import de.indiplex.miningcontest.MiningContest;
import de.indiplex.miningcontest.logic.classes.MiCoClass;
import de.indiplex.virtualchests.VCAPI;
import org.bukkit.entity.Player;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class Shop {

    private MiCo mico;
    private VCAPI vc;

    /**
     * Initializes a new shop
     * @param mico The MiningContest
     */
    public Shop(MiCo mico) {
        this.mico = mico;
        vc = (VCAPI) MiningContest.getAPI().getAPI("vc");
    }

    /**
     * Initializes the shop with clearing the contents before
     */
    public void init() {
        fill(false);
    }
    
    /**
     * Refills the shop
     */
    public void refill() {
        fill(true);
    }

    private void fill(boolean refill) {
        for (MiCoClass.Type type : MiCoClass.Type.values()) {
            for (Team t : mico.getTeams()) {
                String id = "T" + t.getNumber() + " " + type.toString()+" shop";
                if (!refill) {
                    if (vc.hasChest(id)) {
                        vc.removeChest(id);
                    }
                    vc.addChest(id);
                    ShopContents.fillInventory(vc.getChest(id), type);
                } else {
                    ShopContents.refillInventory(vc.getChest(id), type);
                }
            }
        }
    }

    /**
     * Shows the shop to the player
     * @param player The player which is supposed to see the shop
     */
    public void show(Player player) {
        Team t = mico.getTeam(player);
        String id = "T" + t.getNumber() + " " + t.getClass(player).getType().toString()+" shop";
        vc.showChest(player, id);
    }
}
