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

import java.util.EnumMap;
import java.util.Map;
import org.bukkit.Material;
import static org.bukkit.Material.*;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class PointTable {
    
    private static final Map<Material,Integer> points = new EnumMap<Material, Integer>(Material.class);
    
    static {
        points.put(DIAMOND, 200);
        points.put(IRON_ORE, 50);
        points.put(OBSIDIAN, 2000);
        points.put(COAL, 10);
        points.put(REDSTONE, 25);
        points.put(COBBLESTONE, 1);
    }
    
    public static Integer getPoints(Material mat) {
        return points.get(mat);
    }
}
