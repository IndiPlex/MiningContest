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
import de.indiplex.miningcontest.logic.classes.MCClass;
import de.indiplex.virtualchests.VCAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class Shop {

    private MiCo mico;
    private VCAPI vc;

    public Shop(MiCo mico) {
        this.mico = mico;
        vc = (VCAPI) MiningContest.getAPI().getAPI("vc");
    }

    public void init() {
        fill(false);
    }
    
    public void refill() {
        fill(true);
    }

    private void fill(boolean refill) {
        for (MCClass.Type type : MCClass.Type.values()) {
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

    public void show(Player player) {
        Team t = mico.getTeam(player);
        String id = "T" + t.getNumber() + " " + t.getClass(player).getType().toString()+" shop";
        vc.showChest(player, id);
    }
}
