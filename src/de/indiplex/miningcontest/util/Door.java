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
package de.indiplex.miningcontest.util;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class Door {
    private static void close(Block b) {
        byte data = b.getData();
        data = (byte) Bit.clearBit(data, 2);        
        b.setData(data);
    }
    
    private static void toogle(Block b) {
        byte data = b.getData();
        data = (byte) Bit.flipBit(data, 2);
        b.setData(data);
    }
    
    public static void closeDoor(Block b) {
        close(b);
        if (Bit.testBit(b.getData(), 3)) {
            close(b.getRelative(BlockFace.DOWN));
        } else {
            close(b.getRelative(BlockFace.UP));
        }
    }
    
    public static void toogleDoor(Block b) {
        toogle(b);
        if (Bit.testBit(b.getData(), 3)) {
            toogle(b.getRelative(BlockFace.DOWN));
        } else {
            toogle(b.getRelative(BlockFace.UP));
        }
    }
}
