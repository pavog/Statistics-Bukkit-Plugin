/* 
 *    Copyright 2009-2011 The MyBatis Team
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *    
 *    Changelog:
 *    
 *    Cut down on unused code and generally optimized for desired tasks.
 *    - bitWolfy
 *    
 *    Added the ability to change the delimiter so you can run scripts that 
 *    contain stored procedures.
 *    - ChaseHQ
 */

package com.wolvencraft.yasp.cmd;

import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.util.Message;

public class ReconnectCommand implements BaseCommand {

	@Override
	public boolean run(String[] args) {
		try {
			Database.getInstance().reconnect();
			Message.sendFormattedSuccess(CommandManager.getSender(), "Re-established the database connection");
			return true;
		} catch (Exception ex) {
			Message.sendFormattedError(CommandManager.getSender(), "An error occurred while reconnecting to the database");
			return false;
		}
	}

	@Override
	public void getHelp() { Message.formatHelp("reconnect", "", "Attempts to reconnect to the database"); }

}
