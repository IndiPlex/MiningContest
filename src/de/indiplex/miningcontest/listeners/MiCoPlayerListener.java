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
import de.indiplex.miningcontest.logic.PointTable;
import de.indiplex.miningcontest.logic.Team;
import de.indiplex.miningcontest.map.MapChunk;
import de.indiplex.miningcontest.util.Door;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 *
 * @author IndiPlex <kahabrakh@indiplex.de>
 */
public class MiCoPlayerListener extends PlayerListener {

    private MiCo mico;

    public MiCoPlayerListener(MiCo mico) {
        this.mico = mico;
    }

    @Override
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (mico.isMiCoPlayer(player) && MiningContest.getCurrentContest().isInBase(player.getLocation())) {            
            Material mat = event.getItemDrop().getItemStack().getType();
            int amount = event.getItemDrop().getItemStack().getAmount();
            Integer points = PointTable.getPoints(mat);
            if (points != null) {
                MapChunk l = mico.getLobby();
                Location loc = new Location(event.getItemDrop().getWorld(), l.getPos().x*16+3, l.getRoom().getStart()+2, l.getPos().y*16+3);
                event.getItemDrop().teleport(loc);
                //event.getItemDrop().setTicksLived(10000);
                Team t = mico.getTeam(player);
                t.increasePoints(player, points * amount);
                mico.gameThread.setDrop(player);
            }
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block b = event.getClickedBlock();        
        if (b==null || !b.getWorld().getName().equalsIgnoreCase("ContestWorld") || !mico.started) {
            return;
        }
        if (b.getType().equals(Material.IRON_DOOR_BLOCK)) {
            for (Team t:mico.getTeams()) {
                if (t.getBase().isInside(b.getX(), b.getY(), b.getZ())) {
                    event.setCancelled(true);
                    if (!t.hasMember(event.getPlayer())) {
                        event.getPlayer().sendMessage(ChatColor.RED+"This is not your base!");
                        return;
                    }
                    mico.gameThread.setDoor(b.getLocation());
                    Door.toogleDoor(b);
                }
            }
        }
    }

    @Override
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (mico.isMiCoPlayer(event.getPlayer())) {
            event.setRespawnLocation(mico.getTeam(event.getPlayer()).getBase().getSpawnLoc());
        }
    }
    
}
