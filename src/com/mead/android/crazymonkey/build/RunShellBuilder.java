package com.mead.android.crazymonkey.build;

import java.util.ArrayList;
import java.util.List;

public class RunShellBuilder extends CommandLineBuilder {

	public RunShellBuilder(String script, List<String> args) {
		super(script, args);
	}

	public String[] buildCommandLine() {

		List<String> cmds = new ArrayList<String>();

		cmds.add("/bin/sh");
		cmds.add("-xe");
		cmds.add(script);
		cmds.addAll(args);

		return cmds.toArray(new String[] {});
	}
}
