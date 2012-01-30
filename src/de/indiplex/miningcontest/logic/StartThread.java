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

import org.bukkit.Bukkit;

/**
 *
 * @author IndiPlex <Cartan12@indiplex.de>
 */
public class StartThread implements Runnable {

    private int startTime;
    private Integer[] intervals;
    private MiCo mico;
    public boolean startNOW = false;
    private boolean running = true;

    public StartThread(MiCo mico) {
        this(10, new Integer[]{0, 5, 8, 9}, mico);
    }

    public StartThread(int startTime, Integer[] intervals, MiCo mico) {
        this.startTime = startTime;
        this.intervals = intervals;
        this.mico = mico;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        long sTime = System.currentTimeMillis();
        long currentTime;
        int cInterv = 0;
        int interv = intervals[cInterv];
        boolean printIt = true;
        while (running) {
            currentTime = System.currentTimeMillis();
            if (currentTime - sTime > interv * 60 * 1000 && printIt) {
                cInterv++;
                if (cInterv >= intervals.length) {
                    printIt = false;
                    continue;
                }                
                Bukkit.getServer().broadcastMessage("The mining-contest starts in " + (startTime - interv) + " minutes!");
                interv = intervals[cInterv];
            }
            if (currentTime - sTime > startTime * 60 * 1000 || startNOW) {
                break;
            }
        }
        
        mico.start();
    }
}
