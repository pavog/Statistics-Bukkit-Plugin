/*
 * VotifierTotalStats.java
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

package com.mctrakr.modules.hooks.votifier;

import lombok.AccessLevel;
import lombok.Getter;

import com.mctrakr.database.Query;
import com.mctrakr.database.Query.QueryResult;
import com.mctrakr.modules.DataStore.NormalData;
import com.mctrakr.modules.hooks.votifier.Tables.VotifierTotalsTable;
import com.mctrakr.session.OnlineSession;
import com.vexsoftware.votifier.model.Vote;

@Getter(AccessLevel.PUBLIC) 
public class VotifierTotalStats extends NormalData {
    
    private String serviceName;
    private int votes;
    
    public VotifierTotalStats(OnlineSession session, Vote vote) {
        super(session);
        this.serviceName = vote.getServiceName();
        this.votes = 0;
        
        fetchData();
    }
    
    @Override
    public void fetchData() {
        QueryResult result = Query.table(VotifierTotalsTable.TableName)
                .column(VotifierTotalsTable.Votes)
                .condition(VotifierTotalsTable.PlayerId, session.getId())
                .condition(VotifierTotalsTable.ServiceName, serviceName)
                .select();
        if(result == null) {
            Query.table(VotifierTotalsTable.TableName)
                .value(VotifierTotalsTable.PlayerId, session.getId())
                .value(VotifierTotalsTable.ServiceName, serviceName)
                .value(VotifierTotalsTable.Votes, votes)
                .insert();
        } else {
            votes = result.asInt(VotifierTotalsTable.Votes);
        }
    }

    @Override
    public boolean pushData() {
        return Query.table(VotifierTotalsTable.TableName)
                .value(VotifierTotalsTable.PlayerId, session.getId())
                .value(VotifierTotalsTable.ServiceName, serviceName)
                .value(VotifierTotalsTable.Votes, votes)
                .insert();
    }
    
    public void addVote() {
        votes++;
    }

}