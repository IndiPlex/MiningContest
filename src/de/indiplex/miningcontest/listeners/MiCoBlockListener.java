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
package de.indiplex.miningcontest.listeners;

import de.indiplex.miningcontest.MiningContest;
import de.indiplex.miningcontest.logic.MiCo;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

/**
 *
 * @author IndiPlex <kahabrakh@indiplex.de>
 */
public class MiCoBlockListener implements Listener {
    
    private MiCo mico;

    public MiCoBlockListener(MiCo mico) {
        this.mico = mico;
    }

    @EventHandler
    public void onBlockCanBuild(BlockCanBuildEvent event) {
       event.setBuildable(true);
       
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {        
        if (!MiningContest.getCurrentContest().canDestroy(event.getBlock().getLocation()) && !event.getPlayer().isOp()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        if (!MiningContest.getCurrentContest().canDestroy(event.getBlock().getLocation()) && !event.getPlayer().isOp()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onBlockRedstoneChange(BlockRedstoneEvent event) {
        Block b = event.getBlock();
        if (!b.getWorld().getName().equalsIgnoreCase("ContestWorld")) {
            return;
        }
        if (b.getType().equals(Material.IRON_DOOR_BLOCK)) {
            event.setNewCurrent(0);
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (!MiningContest.getCurrentContest().canDestroy(event.getBlock().getLocation()) && !event.getPlayer().isOp()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!MiningContest.getCurrentContest().canDestroy(event.getBlock().getLocation()) && !event.getPlayer().isOp()) {
            event.setCancelled(true);
            return;
        } 
    }
}
