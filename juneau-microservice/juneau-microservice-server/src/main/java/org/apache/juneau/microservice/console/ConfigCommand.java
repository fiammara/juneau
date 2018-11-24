// ***************************************************************************************************************************
// * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements.  See the NOTICE file *
// * distributed with this work for additional information regarding copyright ownership.  The ASF licenses this file        *
// * to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance            *
// * with the License.  You may obtain a copy of the License at                                                              *
// *                                                                                                                         *
// *  http://www.apache.org/licenses/LICENSE-2.0                                                                             *
// *                                                                                                                         *
// * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an  *
// * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the        *
// * specific language governing permissions and limitations under the License.                                              *
// ***************************************************************************************************************************
package org.apache.juneau.microservice.console;

import java.io.*;
import java.util.*;

import org.apache.juneau.config.*;
import org.apache.juneau.microservice.*;
import org.apache.juneau.utils.*;

/**
 * Implements the 'config' console command to get or set configuration.
 */
public class ConfigCommand extends ConsoleCommand {

	private final MessageBundle mb = MessageBundle.create(ConfigCommand.class, "Messages");

	@Override /* ConsoleCommand */
	public String getName() {
		return "config";
	}
	
	@Override /* ConsoleCommand */
	public String getSynopsis() {
		return "config [get|set]";
	}

	@Override /* ConsoleCommand */
	public String getInfo() {
		return mb.getString("info");
	}

	@Override /* ConsoleCommand */
	public String getDescription() {
		return mb.getString("description");
	}

	@Override /* ConsoleCommand */
	public boolean execute(Scanner in, PrintWriter out, Args args) {
		Config conf = Microservice.getInstance().getConfig();
		if (args.size() > 2) {
			String key = args.getArg(2);
			if (args.getArg(1).equals("get")) {
				String val = conf.getString(key);
				if (val == null)
					out.println(mb.getString("KeyNotFound", key));
				else
					out.println(val);
			}
			else if (args.getArg(1).equals("set")) {
				if(args.size() < 3) {
					out.println(mb.getString("InvalidArguments"));
				}
				else {
					conf.set(key, args.getArg(3));
					out.println(mb.getString("ConfigSet"));
				}
			}
		}
		else {
			out.println(mb.getString("InvalidArguments"));
		}
		return false;
	}
}