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

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public abstract class MiCoClass {
    
    private Player player;
    private Type type;

    /**
     * Initializes a new MiCoCass for a player
     * @param player The player
     * @param type The type of the class
     */
    public MiCoClass(Player player, Type type) {
        this.player = player;
        this.type = type;
    }

    /**
     * Get the player of this MiCoClass instance
     * @return Player the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the class type
     * @return Type The type of the class
     */
    public Type getType() {
        return type;
    }
    
    public abstract void fitOutPlayer(Player player);
    
    /**
     * Creates the new instance of the MiCoClass by the class type
     * @param p The player
     * @param t The type of the class
     * @return MiCoClass The instance of the MiCoClass
     */
    public static MiCoClass createByType(Player p, Type t) {
        switch(t) {
            case HUNTER:
                return new Hunter(p);
            case CRAFTER:
                return new Crafter(p);
            case WARRIOR:
                return new Warrior(p);
            case WIZZARD:
                return new Wizzard(p);
            default:
                return new Miner(p);
        }
    }
    
    /**
     * The types
     */
    public enum Type {
        HUNTER,
        WIZZARD,
        CRAFTER,
        MINER,
        WARRIOR;
    }
}
