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
package de.indiplex.miningcontest.logic;

import de.indiplex.miningcontest.generator.Base;
import de.indiplex.miningcontest.logic.classes.MCClass;
import de.indiplex.miningcontest.logic.classes.Miner;
import de.indiplex.miningcontest.map.ColorMap;
import de.indiplex.miningcontest.map.Map;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class Team {
    
    private int number;
    private ArrayList<Player> members;
    private long teamPoints;
    private HashMap<Player, Long> points;
    private HashMap<Player, MCClass> classes;
    private Base base;
    private MiCo mico;

    public Team(int number, MiCo mico) {
        this.number = number;
        members = new ArrayList<Player>();
        points = new HashMap<Player, Long>();
        classes = new HashMap<Player, MCClass>();
        this.mico = mico;
    }

    public int getNumber() {
        return number;
    }
    
    public void reset() {
        teamPoints = 0;
        points.clear();
        members.clear();
    }
        
    private void sendRawData(Player player, short mapId, byte[] data) {
        net.minecraft.server.Packet packet = new net.minecraft.server.Packet131ItemData((short) Material.MAP.getId(), mapId, data);
        net.minecraft.server.EntityPlayer entity = ((org.bukkit.craftbukkit.entity.CraftPlayer) player).getHandle();
        entity.netServerHandler.sendPacket(packet);
        return;
    }
    
    public void sendMap(Player player, short mapId, byte[] data) {
        for (int col = 0; col < 128; ++col) {
            byte[] raw = new byte[131];
            raw[0] = 0;
            raw[1] = (byte) col;
            raw[2] = 0;
            
            for (int row = 0; row < 128; ++row) {
                raw[3 + row] = data[row * 128 + col];
            }
            
            sendRawData(player, mapId, raw);
        }
    }
    
    public boolean addMember(Player player) {
        if(hasMember(player)) return false;
        members.add(player);
        Map map = mico.getMap();
        sendMap(player, (short) 0, ColorMap.imageToBytes(map.getImage()));
        points.put(player, 0L);
        classes.put(player, new Miner(player));
        return true;
    }
    
    public boolean removeMember(Player player) {
        if(members.remove(player)) {
            points.remove(player);
            return true;
        } else {
            return false;
        }
    }

    public long getTeamPoints() {
        return teamPoints;
    }
    
    public boolean hasMember(Player player) {
        return members.contains(player);
    }

    public ArrayList<Player> getMembers() {
        return (ArrayList<Player>) members.clone();
    }
    
    public boolean increasePoints(Player player, long points) {
        Long p = this.points.get(player);
        if (p==null) return false;
        teamPoints+=points;
        this.points.put(player, p+points);
        return true;
    }
    
    public long getPoints(Player player) {
        Long p = this.points.get(player);
        if (p==null) {
            return -1;
        }
        return p;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public Base getBase() {
        return base;
    }
    
    public MCClass getClass(Player p) {
        return classes.get(p);
    }
    
    public void setClass(Player p, MCClass.Type t) {
        classes.put(p, MCClass.createByType(p, t));
    }
    
    public void sendMessage(String msg) {
        for (Player p:members) {
            p.sendMessage(msg);
        }
    }
    
    public int buy(Material toBuy, int amount, Player player) {
        int price = ShopContents.getPrice(toBuy) * amount;
        if (price==0) {
            return 0;
        }
        long ps = points.get(player);
        if (ps-price<0) {
            return -1;
        }
        points.put(player, ps-price);
        return price;
    }
    
}