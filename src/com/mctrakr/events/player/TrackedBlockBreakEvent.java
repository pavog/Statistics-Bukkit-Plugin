/*
 * TrackedBlockBreakEvent.java
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

package com.mctrakr.events.player;

import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.event.HandlerList;

import com.mctrakr.events.StatisticsPlayerEvent;
import com.mctrakr.modules.stats.blocks.BlocksDetailedStats.BlockBreakEntry;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.util.VariableManager.PlayerVariable;

@Getter(AccessLevel.PUBLIC)
public class TrackedBlockBreakEvent extends StatisticsPlayerEvent {
    
    private static final HandlerList handlers = new HandlerList();
    private BlockBreakEntry data;
    
    public TrackedBlockBreakEvent(OnlineSession session, BlockBreakEntry data) {
        super(session, PlayerVariable.BLOCKS_BROKEN);
        this.data = data;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public String getParameterString() {
        return data.getBlock().getTypeId() + ":" + data.getBlock().getRawData();
    }
    
}
