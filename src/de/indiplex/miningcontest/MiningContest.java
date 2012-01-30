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
package de.indiplex.miningcontest;

import de.indiplex.manager.IPMAPI;
import de.indiplex.manager.IPMPlugin;
import de.indiplex.miningcontest.commands.MiCoCommands;
import de.indiplex.miningcontest.generator.ContestChunkGenerator;
import de.indiplex.miningcontest.listeners.MiCoBlockListener;
import de.indiplex.miningcontest.listeners.MiCoPlayerListener;
import de.indiplex.miningcontest.listeners.MiCoSpecialListener;
import de.indiplex.miningcontest.logic.MiCo;
import de.indiplex.multiworlds.MultiWorldsAPI;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

/**
 * 
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class MiningContest extends IPMPlugin {

    public World contestWorld;
    private MultiWorldsAPI MAPI;
    public static final String pre = "[MICO] ";
    private HashMap<String, Location> oldPosition = new HashMap<String, Location>();
    private static MiCo mc;
    private static IPMAPI API;

    @Override
    protected void init(IPMAPI API) {
        MiningContest.API = API;
    }

    @Override
    public void onIPMLoad() {
        try {
            String s = getClass().getResource("").getPath().split("\\!")[0];
            s = "plugins" + s.split("plugins")[1] + "plugins" + s.split("plugins")[2];
            s = s.replace("%20", " ");
            JarFile file = new JarFile(s);
            Enumeration<JarEntry> entries = file.entries();
            File dataFolder = getAPI().getDataFolder();
            while (entries.hasMoreElements()) {
                JarEntry e = entries.nextElement();
                if (e.toString().contains("res") && !e.isDirectory()) {
                    File f = new File(dataFolder, e.getName());
                    if (f.exists()) {
                        continue;
                    }
                    f.getParentFile().mkdirs();
                    copy(file.getInputStream(e), f);
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        if (mc.started) {
            mc.stop();
        }
        printDisabled(pre);
    }

    @Override
    public void onEnable() {
        MultiWorldsAPI api = (MultiWorldsAPI) getAPI().getAPI("MultiWorlds");
        MAPI = api;
        log.info(pre + "Hooked into MultiWorlds...");
        MAPI.setGenerator("contest", ContestChunkGenerator.class);
        contestWorld = MAPI.registerWorld("ContestWorld", "contest", World.Environment.NORMAL, true);

        mc = ((ContestChunkGenerator) MAPI.getGenByWorld("ContestWorld")).getMico();
        
        mc.setApi(API);

        log.info(pre + "Registered World!");
        log.info(pre + "Configuring world...");
        //contestWorld.setSpawnFlags(false, true);

        getCommand("MiCo").setExecutor(new MiCoCommands(this));
        Listener bListener = new MiCoBlockListener(mc);
        Listener pListener = new MiCoPlayerListener(mc);
        Listener sListener = new MiCoSpecialListener(mc);

        Listener eListener = new Listener() {

            @EventHandler
            public void onCreatureSpawn(CreatureSpawnEvent event) {
                if (event.getLocation().getWorld().getName().equalsIgnoreCase("ContestWorld") && !event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.SPAWNER)) {
                    event.setCancelled(true);
                }
            }
        };

        getServer().getPluginManager().registerEvents(bListener, this);
        getServer().getPluginManager().registerEvents(pListener, this);
        getServer().getPluginManager().registerEvents(eListener, this);
        getServer().getPluginManager().registerEvents(sListener, this);

        printEnabled(pre);
    }

    /**
     * Returns the current MiningContest
     *  @return MiCo The current MiningContest
     */
    public static MiCo getCurrentContest() {
        return mc;
    }

    /**
     * 
     * @param dir directory or file to delete
     * @return boolean true if successfull, false if not
     */
    private boolean del(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File aktFile : files) {
                del(aktFile);
            }
        }
        return dir.delete();
    }

    /**
     * Returns the world, where the MiningContest is working in.
     * @return World ContestWorld
     */
    public World getContestWorld() {
        return contestWorld;
    }

    /**
     * 
     * @param playerName Name of the player to look for the old coords
     * @return Location The location of the player before he joins the MiningContest
     */
    public Location getOldLoc(String playerName) {
        return oldPosition.get(playerName);
    }

    /**
     * 
     * @param playerName Name of the player to set the old coords
     * @param location The location of the player before he joins the MiningContest
     */
    public void setOldLoc(String playerName, Location location) {
        oldPosition.put(playerName, location);
    }

    /**
     * 
     * @return MultiWorldsAPI The MultiWorlds API
     */
    public MultiWorldsAPI getMWAPI() {
        return MAPI;
    }

    /**
     * 
     * @param in InputStream of the content to copy
     * @param out File to write to
     */
    private void copy(InputStream in, File out) throws IOException {
        if (!out.exists()) {
            out.createNewFile();
        }
        FileOutputStream fout = new FileOutputStream(out);
        int r = in.read();
        while (r != -1) {
            fout.write(r);
            r = in.read();
        }
        fout.close();
        in.close();
    }

    /**
     * 
     * @return IPMAPI The IndiPlexManager API
     */
    public static IPMAPI getAPI() {
        return API;
    }
}