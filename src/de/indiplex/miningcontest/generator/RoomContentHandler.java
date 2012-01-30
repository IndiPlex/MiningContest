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

import de.indiplex.miningcontest.generator.utils.Row;
import de.indiplex.miningcontest.generator.utils.Point;
import de.indiplex.miningcontest.generator.utils.Layer;
import de.indiplex.miningcontest.generator.utils.Area;
import de.indiplex.miningcontest.map.MapChunk.Type;
import java.util.ArrayList;
import org.bukkit.Material;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class RoomContentHandler implements ContentHandler {

    private String buffer = null;
    private ArrayList<Layer> layers = new ArrayList<Layer>();
    private ArrayList<Row> rows = new ArrayList<Row>();
    private ArrayList<Point> points = new ArrayList<Point>();
    private ArrayList<Integer> vals = new ArrayList<Integer>();
    private ArrayList<Integer> avals = new ArrayList<Integer>();
    private Area cArea;
    private Row cRow;
    private Room r;
    private int from = -1;
    private int to = -1;
    private int afrom = -1;
    private int ato = -1;
    private int x = -1;
    private int y = -1;
    private int id = 0;
    private byte sd = -1;
    private int dir = -1;
    private int height = 11;
    private int start = 50;

    @Override
    public void setDocumentLocator(Locator locator) {
    }

    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if (localName.equals("row")) {
            cRow = new Row();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("x")) {
            x = Integer.parseInt(buffer) - 1;
        } else if (localName.equals("y")) {
            y = Integer.parseInt(buffer) - 1;
        } else if (localName.equals("data")) {
            sd = (byte) Integer.parseInt(buffer);
        } else if (localName.equals("start")) {
            start = Integer.parseInt(buffer);
        } else if (localName.equals("dir")) {
            dir = Integer.parseInt(buffer);
        } else if (localName.equals("from")) {
            if (check()) {
                afrom = Integer.parseInt(buffer) - 1;
            } else {
                from = Integer.parseInt(buffer) - 1;
            }
        } else if (localName.equals("height")) {
            height = Integer.parseInt(buffer);
        } else if (localName.equals("to")) {
            if (check()) {
                ato = Integer.parseInt(buffer);
            } else {
                to = Integer.parseInt(buffer);
            }
        } else if (localName.equals("id")) {
            id = getMatByName(buffer);
        } else if (localName.equals("val")) {
            if (check()) {
                avals.add(Integer.parseInt(buffer) - 1);
            } else {
                vals.add(Integer.parseInt(buffer) - 1);
            }
        } else if (localName.equals("point")) {
            points.add(new Point(x, y, id, sd));
            x = -1;
            y = -1;
            id = 0;
            sd = -1;
        } else if (localName.equals("area")) {
            cArea = new Area(id, sd);
            id = 0;
            sd = -1;
        } else if (localName.equals("row")) {
            if (afrom != 0 && ato != 0) {
                for (int i = afrom; i < ato; i++) {
                    if (!avals.contains(i)) {
                        avals.add(i);
                    }
                }
            }
            for (int i : avals) {
                rows.add(new Row(i, dir, id, sd));
            }
            avals.clear();
            afrom = -1;
            ato = -1;
            cRow = null;
            dir = -1;
            id = -1;
            sd = -1;
        } else if (localName.equals("layer")) {
            if (from != 0 && to != 0) {
                for (int i = from; i < to; i++) {
                    if (!vals.contains(i)) {
                        vals.add(i);
                    }
                }
            }
            if (cArea == null) {
                cArea = new Area(0, (byte) -1);
            }
            cArea.addRows(rows);
            cArea.addPoints(points);
            for (int i : vals) {
                Layer l = new Layer(cArea, i);
                if (!layers.contains(l)) {
                    try {
                        layers.add(l.clone());
                    } catch (CloneNotSupportedException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    Area a = new Area(-1, (byte) -2);
                    a.addRows(rows);
                    a.addPoints(points);
                    Layer tl = layers.get(layers.indexOf(l));
                    tl.include(a);
                }
            }
            vals.clear();
            rows.clear();
            points.clear();
            from = -1;
            to = -1;
            cArea = null;
        } else if (localName.equals("room")) {
            r = new Room(layers, Type.DUNGEON, height, start);
        }
    }

    /**
     * 
     * @return Room The room
     */
    public Room getRoom() {
        return r;
    }

    private boolean check() {
        return (cRow != null);
    }

    private int getMatByName(String name) {
        return Material.getMaterial(name).getId();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        buffer = new String(ch, start, length);
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
    }
}