package com.mead.android.crazymonkey.build;

import java.util.ArrayList;
import java.util.List;

public class RunBatBuilder extends CommandLineBuilder {

	public RunBatBuilder(String script, List<String> args) {
		super(script, args);
	}

	public String[] buildCommandLine() {
		List<String> cmds = new ArrayList<String>();

		cmds.add("cmd");
		cmds.add("/c");
		cmds.add("call");
		cmds.add(script);
		cmds.addAll(args);

		return cmds.toArray(new String[] {});
	}
}
