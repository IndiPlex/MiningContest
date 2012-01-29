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

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public abstract class MCClass {
    
    private Player player;
    private Type type;

    public MCClass(Player player, Type type) {
        this.player = player;
        this.type = type;
    }

    public Player getPlayer() {
        return player;
    }

    public Type getType() {
        return type;
    }
    
    public static MCClass createByType(Player p, Type t) {
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
    
    public enum Type {
        HUNTER,
        WIZZARD,
        CRAFTER,
        MINER,
        WARRIOR;
    }
}
