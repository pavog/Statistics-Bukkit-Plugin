/*
 * HookManager.java
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

package com.mctrakr.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import com.mctrakr.Statistics;
import com.mctrakr.db.hooks.PluginHook;
import com.mctrakr.db.hooks.admincmd.AdminCmdHook;
import com.mctrakr.db.hooks.banhammer.BanHammerHook;
import com.mctrakr.db.hooks.commandbook.CommandBookHook;
import com.mctrakr.db.hooks.factions.FactionsHook;
import com.mctrakr.db.hooks.mcmmo.McMMOHook;
import com.mctrakr.db.hooks.mobarena.MobArenaHook;
import com.mctrakr.db.hooks.pvparena.PvpArenaHook;
import com.mctrakr.db.hooks.vanish.VanishHook;
import com.mctrakr.db.hooks.vault.VaultHook;
import com.mctrakr.db.hooks.votifier.VotifierHook;
import com.mctrakr.db.hooks.worldguard.WorldGuardHook;
import com.mctrakr.events.plugin.HookInitEvent;
import com.mctrakr.settings.ConfigLock.ModuleType;
import com.mctrakr.util.ExceptionHandler;
import com.mctrakr.util.Message;

public class HookManager {
    
    private static List<PluginHook> activeHooks = new ArrayList<PluginHook>();
    
    public HookManager() { }
    
    /**
     * Starts up the necessary hooks
     */
    public static void onEnable() {
        PluginManager plManager = Statistics.getInstance().getServer().getPluginManager();
        int hooksEnabled = 0;
        Message.log(
                "+-------- [ Hook Manager ] --------+",
                "|" + Message.centerString("Hook Manager starting up", 34) + "|",
                "|" + Message.centerString("", 34) + "|"
                );
        
        for(ApplicableHook hook : ApplicableHook.values()) {
            PluginHook hookObj;
            try { hookObj = hook.getHook().newInstance(); }
            catch (Throwable t) {
                ExceptionHandler.handle(t);
                continue;
            }
            
            if (plManager.getPlugin(hookObj.getPluginName()) == null) {
                Message.debug(Level.FINER, "|" + Message.centerString(hookObj.getPluginName() + " is not found", 34) + "|");
            } else {
                HookInitEvent event = new HookInitEvent(hookObj.getLock().getType().getAlias());
                Bukkit.getServer().getPluginManager().callEvent(event);
                if(event.isCancelled()) {
                    Message.log("|" + Message.centerString(hookObj.getPluginName() + " is cancelled", 34) + "|");
                    continue;
                }
                
                if(hookObj.enable()) {
                    Message.log("|" + Message.centerString(hookObj.getPluginName() + " has been enabled", 34) + "|");
                    activeHooks.add(hookObj);
                    hooksEnabled++;
                } else
                    Message.log("|" + Message.centerString("Could not enable " + hookObj.getPluginName(), 34) + "|"); 
            }
        }
        Message.log(
                "|                                  |",
                "|" + Message.centerString(hooksEnabled + " hooks enabled", 34) + "|",
                "+----------------------------------+"
                );
    }
    
    /**
     * Executes hook shutdown
     */
    public static void onDisable() {
        Message.log(
                "+-------- [ Hook Manager ] --------+",
                "|" + Message.centerString("Hook Manager shutting down", 34) + "|",
                "|" + Message.centerString("", 34) + "|"
                );
        
        for(PluginHook hook : activeHooks) {
            hook.disable();
            Message.log("|" + Message.centerString(hook.getPluginName() + " is shutting down", 34) + "|");
        }
        
        Message.log("+----------------------------------+");
    }
    
    /**
     * Returns a hook with the specified type
     * @param type Hook type
     * @return Hook, or <b>null</b> if it isn't active
     */
    public static PluginHook getHook(ModuleType type) {
        for(PluginHook hook : activeHooks) {
            if(hook.getLock().getType() == type) return hook;
        }
        return null;
    }
    
    @Getter(AccessLevel.PUBLIC)
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    private enum ApplicableHook {
        
        ADMIN_CMD       (AdminCmdHook.class),
        BAN_HAMMER      (BanHammerHook.class),
        COMMAND_BOOK    (CommandBookHook.class),
        FACTIONS        (FactionsHook.class),
        MCMMO           (McMMOHook.class),
        MOB_ARENA       (MobArenaHook.class),
        PVP_ARENA       (PvpArenaHook.class),
        VAULT           (VaultHook.class),
        VANISH          (VanishHook.class),
        VOTIFIER        (VotifierHook.class),
        WORLD_GUARD     (WorldGuardHook.class)
        ;
        
        @Getter(AccessLevel.PRIVATE)
        private Class<? extends PluginHook> hook;
        
    }
    
}