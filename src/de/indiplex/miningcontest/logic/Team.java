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
import de.indiplex.miningcontest.logic.classes.MiCoClass;
import de.indiplex.miningcontest.logic.classes.Miner;
import de.indiplex.miningcontest.map.ColorMap;
import de.indiplex.miningcontest.map.Map;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Bukkit;
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
    private HashMap<Player, MiCoClass> classes;
    private Base base;
    private MiCo mico;

    /**
     * Initializes a new Team
     * @param number Teamnumber
     * @param mico The MiningContest
     */
    public Team(int number, MiCo mico) {
        this.number = number;
        members = new ArrayList<Player>();
        points = new HashMap<Player, Long>();
        classes = new HashMap<Player, MiCoClass>();
        this.mico = mico;
    }

    /**
     * 
     * @return The teamnumber 
     */
    public int getNumber() {
        return number;
    }
    
    /**
     * Resets the team (clear points and members)
     */
    public void reset() {
        teamPoints = 0;
        points.clear();
        members.clear();
    }
        
    private void sendRawData(Player player, short mapId, byte[] data) {
        player.sendMap(Bukkit.createMap(Bukkit.getWorld("ContestWorld")));
        return;
    }
    
    /**
     * Sends a map to a player
     * @param player The player to send to
     * @param mapId The id of the map
     * @param data The data to send to the player
     */
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
    
    /**
     * Adds a player to the team
     * @param player The player
     * @return boolean true if successful, false if not
     */
    public boolean addMember(Player player) {
        if(hasMember(player)) return false;
        members.add(player);
        Map map = mico.getMap();
        sendMap(player, (short) 0, ColorMap.imageToBytes(map.getImage()));
        points.put(player, 0L);
        classes.put(player, new Miner(player));
        return true;
    }
    
    /**
     * Removes a player from the team
     * @param player The player
     * @return boolean true if successful, false if not
     */
    public boolean removeMember(Player player) {
        if(members.remove(player)) {
            points.remove(player);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @return The team's points
     */
    public long getTeamPoints() {
        return teamPoints;
    }
    
    /**
     * Returns whether the specified player belongs to the team
     * @param player The player
     * @return boolean true if player belongs to the team, false if not
     */
    public boolean hasMember(Player player) {
        return members.contains(player);
    }

    /**
     * 
     * @return ArrayList<Player> The members of the team 
     */
    public ArrayList<Player> getMembers() {
        return (ArrayList<Player>) members.clone();
    }
    
    /**
     * 
     * @param player The player how has increased te points
     * @param points The points
     * @return boolean true if successful, false if not
     */
    public boolean increasePoints(Player player, long points) {
        Long p = this.points.get(player);
        if (p==null) return false;
        teamPoints+=points;
        this.points.put(player, p+points);
        return true;
    }
    
    /**
     * Returns the points of a player
     * @param player The player
     * @return long The player's points
     */
    public long getPoints(Player player) {
        Long p = this.points.get(player);
        if (p==null) {
            return -1;
        }
        return p;
    }

    /**
     * Sets the teams base
     * @param base The base
     */
    public void setBase(Base base) {
        this.base = base;
    }

    /**
     * 
     * @return Base The base
     */
    public Base getBase() {
        return base;
    }
    
    /**
     * Returns the class of the specified player
     * @param p The player
     * @return MiCoClass The player's class
     */
    public MiCoClass getClass(Player p) {
        return classes.get(p);
    }
    
    /**
     * Sets the class of a player
     * @param p The player
     * @param t The class
     */
    public void setClass(Player p, MiCoClass.Type t) {
        classes.put(p, MiCoClass.createByType(p, t));
    }
    
    /**
     * Sends a message to the team
     * @param msg The message
     */
    public void sendMessage(String msg) {
        for (Player p:members) {
            p.sendMessage(msg);
        }
    }
    
    /**
     * Buys an item
     * @param toBuy The material
     * @param amount The amount of the material
     * @param player The player who is buying
     * @return int The price of the itemstack
     */
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