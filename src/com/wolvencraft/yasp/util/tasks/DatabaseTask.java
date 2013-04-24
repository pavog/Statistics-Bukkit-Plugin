/* 
 * DatabaseTask.java
 * 
 * Statistics
 * Copyright (C) 2013 bitWolfy <http://www.wolvencraft.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.wolvencraft.yasp.util.tasks;

import org.bukkit.Bukkit;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.api.events.SynchronizationEvent;
import com.wolvencraft.yasp.api.events.SynchronizationPreProcessEvent;
import com.wolvencraft.yasp.session.*;
import com.wolvencraft.yasp.settings.Module;
import com.wolvencraft.yasp.settings.RemoteConfiguration;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.cache.OnlineSessionCache;

/**
 * Stores collected statistical data until it can be processed and sent to the database.<br />
 * This class is intended to be run in an asynchronous thread; all components are thread-safe.
 * @author bitWolfy
 *
 */
public class DatabaseTask implements Runnable {
    
    private static int iteration;

    /**
     * <b>Default constructor.</b><br />
     * Initializes an empty list of OnlineSessions
     */
    public DatabaseTask() {
        iteration = 0;
        
    }
    
    /**
     * Database synchronization method.<br />
     * Wraps around <code>public static void commit();</code>
     */
    @Override
    public void run() { commit(); }
    
    /**
     * Commits collected data to the database.<br />
     * Performs actions in the following order:<br />
     * <ul>
     * <li>Confirm that the synchronization is not paused.</li>
     * <li>Push all player data to the database</li>
     * <li>Push generic server statistics to the database</li>
     * <li>Confirms that the players marked as online are actually online</li>
     * <li>Fetch server totals for signs and statistics books</li>
     * <li>Clear settings cache</li>
     * </ul>
     * This method is likely to freeze the main server thread.
     * Asynchronous threading is strongly recommended.
     */
    public static void commit() {
        Bukkit.getServer().getPluginManager().callEvent(new SynchronizationPreProcessEvent(iteration));
        if(Statistics.getPaused()) return;
        Message.debug("Database synchronization in progress");
        
        for(OnlineSession session : OnlineSessionCache.getSessions()) {
            session.pushData();
            session.getTotals().fetchData();
        }
        
        Statistics.getServerStatistics().pushData();
        Statistics.getServerTotals().fetchData();
        
        Module.clearCache();
        RemoteConfiguration.clearCache();
        Bukkit.getServer().getPluginManager().callEvent(new SynchronizationEvent(iteration));
        iteration++;
    }
}
