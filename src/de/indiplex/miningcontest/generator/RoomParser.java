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
package de.indiplex.miningcontest.generator;

import de.indiplex.miningcontest.MiningContest;
import de.indiplex.miningcontest.map.MapChunk;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.EnumMap;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class RoomParser {

    private static RoomParser instance;
    private EnumMap<MapChunk.Type, Room> rooms = new EnumMap<MapChunk.Type, Room>(MapChunk.Type.class);

    /**
     * Initializes a new RoomParser
     */
    private RoomParser() {
    }

    private static RoomParser getInstance() {
        if (instance == null) {
            instance = new RoomParser();
        }
        return instance;
    }

    /**
     * Parses the room file xml
     * @param type MapChunk.Type of the element to parse
     * @return Room The finished room
     */
    public static Room parseRoom(MapChunk.Type type) {
        try {
            RoomParser rp = getInstance();
            if (rp.rooms.get(type) != null) {
                return rp.rooms.get(type);
            }

            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            RoomContentHandler roomContentHandler = new RoomContentHandler();
            xmlReader.setContentHandler(roomContentHandler);
            String fName = "res/" + type.toString().toLowerCase() + ".xml";
            xmlReader.parse(new InputSource(new FileInputStream(new File(MiningContest.getAPI().getDataFolder(), fName))));
            Room room = roomContentHandler.getRoom();
            rp.rooms.put(type, room);
            return room;
        } catch (SAXException ex) {
            ex.printStackTrace();
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}