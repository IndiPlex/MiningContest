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

import de.indiplex.miningcontest.logic.MiCo;
import de.indiplex.miningcontest.logic.PointTable;
import de.indiplex.miningcontest.logic.Team;
import de.indiplex.miningcontest.logic.WithDoorsAndSigns;
import de.indiplex.miningcontest.logic.classes.MiCoClass.Type;
import de.indiplex.miningcontest.map.MapChunk;
import de.indiplex.miningcontest.util.Door;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 *
 * @author IndiPlex <kahabrakh@indiplex.de>
 */
public class MiCoPlayerListener implements Listener {

    private MiCo mico;

    public MiCoPlayerListener(MiCo mico) {
        this.mico = mico;
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (mico.isMiCoPlayer(player) && mico.isInsideTeamArea(player)) {
            Material mat = event.getItemDrop().getItemStack().getType();
            int amount = event.getItemDrop().getItemStack().getAmount();
            Integer points = PointTable.getPoints(mat);
            if (points != null) {
                MapChunk l = mico.getLobby();
                Location loc = new Location(event.getItemDrop().getWorld(), l.getPos().x * 16 + 3, l.getRoom().getStart() + 2, l.getPos().y * 16 + 3);
                event.getItemDrop().teleport(loc);
                Team t = mico.getTeam(player);
                t.increasePoints(player, points * amount);
                mico.gameThread.setDrop(player);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block b = event.getClickedBlock();
        if (b != null && event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && mico.initializing) {
            if (mico.getLobby().isInside(event.getPlayer().getLocation())) {
                if (mico.getLobby().getSigns().contains(b.getLocation())) {
                    Type ct = mico.getClass(event.getPlayer()).getType();
                    Team t = mico.getTeam(event.getPlayer());
                    Sign s = (Sign) b.getState();
                    String str = s.getLine(1);
                    Type nt = null;
                    try {
                        nt = Type.valueOf(str);
                    } catch(IllegalArgumentException e) {
                        return;
                    }
                    if (nt != null && nt != ct) {
                        t.setClass(event.getPlayer(), nt);
                        event.getPlayer().sendMessage("You are now a " + nt);
                    }
                }
            }
        }
        if (b == null || !b.getWorld().getName().equalsIgnoreCase("ContestWorld") || !mico.started || !mico.isMiCoPlayer(event.getPlayer())) {
            return;
        }
        Type ct = mico.getClass(event.getPlayer()).getType();
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            switch (b.getType()) {
                case WORKBENCH:
                    if (ct != Type.CRAFTER) {
                        event.setCancelled(true);
                        return;
                    }
                    break;
                case FURNACE:
                    if (ct != Type.MINER) {
                        event.setCancelled(true);
                        return;
                    }
                    break;
                case BREWING_STAND:
                    if (ct != Type.WIZZARD) {
                        event.setCancelled(true);
                        return;
                    }
                    break;
                case ENCHANTMENT_TABLE:
                    if (ct != Type.WIZZARD) {
                        event.setCancelled(true);
                        return;
                    }
                    break;
                case SIGN:
                case SIGN_POST:
                    Team t = mico.getTeam(event.getPlayer());
                    if (mico.getBase(t.getNumber()).getSign().equals(b.getLocation())) {
                        mico.getShop().show(event.getPlayer());
                    }
                    break;
            }
        }
        if (b.getType().equals(Material.IRON_DOOR_BLOCK)) {
            MapChunk mc = mico.getMap().getMapChunk(b.getChunk().getX() + 10, b.getChunk().getZ() + 10);
            if (mc == null) {
                return;
            }
            if (mc instanceof WithDoorsAndSigns) {
                WithDoorsAndSigns t = (WithDoorsAndSigns) mc;
                event.setCancelled(true);
                if (t.getTeam() != null && !t.getTeam().hasMember(event.getPlayer())) {
                    event.getPlayer().sendMessage(ChatColor.RED + "This is not your " + (t.getType() == MapChunk.Type.BASE ? "Base" : "Outpost") + "!");
                    return;
                }
                mico.gameThread.setDoor(b.getLocation());
                Door.toogleDoor(b);
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (mico.isMiCoPlayer(event.getPlayer())) {
            event.setRespawnLocation(mico.getTeam(event.getPlayer()).getBase().getSpawnLoc());
        }
    }
}
