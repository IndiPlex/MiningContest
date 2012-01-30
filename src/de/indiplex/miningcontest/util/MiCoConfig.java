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
package de.indiplex.miningcontest.util;

import de.indiplex.miningcontest.MiningContest;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class MiCoConfig {
    
    private static final Map<String, Object> defaults = new HashMap<String, Object>();
    static {
        defaults.put("options.mapName", "Map3.png");
        defaults.put("options.signRefreshRate", 2500);
        defaults.put("options.micoTickRate", 300);
        defaults.put("options.duration", new Long(15*60*1000)); // 15 minutes
    }
    
    private String mapName;
    private int signRefreshRate;
    private int micoTickRate;
    private long duration;

    public MiCoConfig() {
    }
    
    public void load() {
        YamlConfiguration config = MiningContest.getAPI().getConfig();
        config.addDefaults(defaults);
        config.options().copyDefaults(true);
        MiningContest.getAPI().saveConfig(config);
        mapName = config.getString("options.mapName");
        signRefreshRate = config.getInt("options.signRefreshRate");
        micoTickRate = config.getInt("options.micoTickRate");
        duration = config.getLong("options.duration");
    }

    public String getMapName() {
        return mapName;
    }

    public long getDuration() {
        return duration;
    }

    public int getMicoTickRate() {
        return micoTickRate;
    }

    public int getSignRefreshRate() {
        return signRefreshRate;
    }
    
}
