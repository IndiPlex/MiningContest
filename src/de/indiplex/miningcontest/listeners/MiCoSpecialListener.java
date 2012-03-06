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
package de.indiplex.miningcontest.listeners;

import de.indiplex.miningcontest.MiningContest;
import de.indiplex.miningcontest.logic.MiCo;
import de.indiplex.miningcontest.logic.Team;
import de.indiplex.virtualchests.VCAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class MiCoSpecialListener implements Listener {
    
    private MiCo mico;
    private VCAPI vc;

    public MiCoSpecialListener(MiCo mico) {
        this.mico = mico;
        vc = (VCAPI) MiningContest.getAPI().getAPI("vc");
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Team t = mico.getTeam((Player) e.getWhoClicked());
        if (t==null) {
            return;
        }
        String id = "T" + t.getNumber() + " " + t.getClass((Player) e.getWhoClicked()).getType().toString()+" shop";
        if (e.getRawSlot()<54 && e.getInventory().getName().equalsIgnoreCase(id)) {
            int res = t.buy(e.getCurrentItem().getType(), e.getCurrentItem().getAmount(), (Player) e.getWhoClicked());
            if (res==-1) {
                ((Player) e.getWhoClicked()).sendMessage("You don't have enough points!");
                e.setCancelled(true);
            } else if (res>0) {
                ItemStack is = e.getCurrentItem();
                ((Player) e.getWhoClicked()).sendRawMessage("You bougth "+is.getAmount()+" "+is.getType().toString().toLowerCase()+" and paid "+res+" points!");
            }
        }
    }
    
}
