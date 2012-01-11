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

import de.indiplex.manager.IPMPlugin;
import de.indiplex.miningcontest.commands.MiCoCommands;
import de.indiplex.miningcontest.generator.ContestChunkGenerator;
import de.indiplex.miningcontest.listeners.MiCoBlockListener;
import de.indiplex.miningcontest.listeners.MiCoPlayerListener;
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
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.player.PlayerListener;

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

    @Override
    public void onLoad() {                
        try {
            String s = getClass().getResource("").getPath().split("\\!")[0];            
            s = "plugins"+s.split("plugins")[1]+"plugins"+s.split("plugins")[2];
            s = s.replace("%20", " ");
            JarFile file = new JarFile(s);
            Enumeration<JarEntry> entries = file.entries();
            File dataFolder = getAPI().getDataFolder();
            while(entries.hasMoreElements()) {
                JarEntry e = entries.nextElement();
                if (e.toString().contains("res") && !e.isDirectory()) {
                    File f = new File(dataFolder, e.getName());
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
        printDisabled(pre);
    }

    @Override
    public void onEnable() {
        MultiWorldsAPI API = (MultiWorldsAPI) getAPI().getAPI("MultiWorlds");
        MAPI = API;
        log.info(pre + "Hooked into MultiWorlds...");
        MAPI.setGenerator("contest", ContestChunkGenerator.class);
        contestWorld = MAPI.registerWorld("ContestWorld", "contest", World.Environment.NORMAL, true);
        
        mc = ((ContestChunkGenerator) MAPI.getGenByWorld("ContestWorld")).getMico();                
        
        log.info(pre + "Registered World!");
        log.info(pre + "Configuring world...");
        //contestWorld.setSpawnFlags(false, true);

        getCommand("MiCo").setExecutor(new MiCoCommands(this));
        BlockListener bListener = new MiCoBlockListener(mc);
        PlayerListener pListener = new MiCoPlayerListener(mc);
        
        EntityListener el = new EntityListener() {

            @Override
            public void onCreatureSpawn(CreatureSpawnEvent event) {
                if (event.getLocation().getWorld().getName().equalsIgnoreCase("ContestWorld") && !event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.SPAWNER)) {
                    event.setCancelled(true);
                }
            }
            
        };
        
        getServer().getPluginManager().registerEvent(Type.CREATURE_SPAWN, el, Priority.High, this);
        getServer().getPluginManager().registerEvent(Type.BLOCK_BREAK, bListener, Priority.High, this);
        getServer().getPluginManager().registerEvent(Type.SIGN_CHANGE, bListener, Priority.High, this);
        getServer().getPluginManager().registerEvent(Type.BLOCK_PLACE, bListener, Priority.High, this);
        getServer().getPluginManager().registerEvent(Type.BLOCK_CANBUILD, bListener, Priority.High, this);
        getServer().getPluginManager().registerEvent(Type.BLOCK_DAMAGE, bListener, Priority.High, this);
        getServer().getPluginManager().registerEvent(Type.REDSTONE_CHANGE, bListener, Priority.High, this);
        
        getServer().getPluginManager().registerEvent(Type.PLAYER_DROP_ITEM, pListener, Priority.High, this);
        getServer().getPluginManager().registerEvent(Type.PLAYER_INTERACT, pListener, Priority.High, this);
        getServer().getPluginManager().registerEvent(Type.PLAYER_RESPAWN, pListener, Priority.High, this);
                
        printEnabled(pre);
    }
    
    public static MiCo getCurrentContest() {
        return mc;
    }

    public boolean del(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File aktFile : files) {
                del(aktFile);
            }
        }
        return dir.delete();
    }

    public World getContestWorld() {
        return contestWorld;
    }
    
    public Location getOldLoc(String playerName) {
        return oldPosition.get(playerName);
    }
    
    public void setOldLoc(String playerName, Location location) {
        oldPosition.put(playerName, location);
    }

    public MultiWorldsAPI getMWAPI() {
        return MAPI;
    }
    
    private void copy(InputStream in, File out) throws IOException {
        if (!out.exists()) {
            out.createNewFile();
        }        
        FileOutputStream fout = new FileOutputStream(out);
        int r = in.read();
        while (r!=-1) {
            fout.write(r);
            r = in.read();
        }
        fout.close();
        in.close();
    }
    
}