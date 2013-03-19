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
import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.LocalSession;
import com.wolvencraft.yasp.util.Message;

public class DumpCommand implements BaseCommand {

	@Override
	public boolean run(String[] args) {
		for(LocalSession session : DataCollector.get())
			session.dump();
		Message.sendFormattedSuccess(CommandManager.getSender(), "The local data has been dumped");
		return true;
	}

	@Override
	public void getHelp() { Message.formatHelp("dump", "", "Dumps the locally stored data"); }

}
