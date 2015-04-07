package com.mead.android.crazymonkey;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.mead.android.crazymonkey.build.Builder;
import com.mead.android.crazymonkey.model.Task;
import com.mead.android.crazymonkey.model.Task.STATUS;
import com.mead.android.crazymonkey.sdk.AndroidSdk;
import com.mead.android.crazymonkey.util.Utils;

public class GenymotionRunner extends AbstractRunner {

	public GenymotionRunner(CrazyMonkeyBuild build, Task task, AndroidSdk androidSdk, StreamTaskListener taskListener) {
		super(build, task, androidSdk, taskListener);
	}

	@Override
	public boolean startUp() throws IOException, InterruptedException {
		long start = System.currentTimeMillis();
		if (logger == null) {
			logger = taskListener.getLogger();
		}
		
		String script = build.getTestScriptPath() + "//genymotion_start.bat";
		if (Utils.isUnix()) {
			script = build.getTestScriptPath() + "//genymotion_start.sh";
		}

		List<String> args = new ArrayList<String>();
		args.add(task.getEmulator().getAvdName());

		Builder builder = this.getBuilder(script, args);

		boolean result = builder.perform(build, androidSdk, task.getEmulator(), context, taskListener, "Success");
		
		if (!result) {
			log(logger, String.format("Start the geny motion %s via '%s' failed.", task.getEmulator().getAvdName(), script));
			task.setStatus(STATUS.NOT_BUILT);
		} else {
			log(logger, String.format("Start the genymotion %s via '%s' scussfully.", task.getEmulator().getAvdName(), script));
			log(logger, String.format("Genymotion is ready for use (took %d seconds).", (System.currentTimeMillis() - start) / 1000));
			this.getContext().setSerial(getSerialForGenyMotion("genymotion_" + task.getEmulator().getAvdName()) + ".ini");
		}
		return result;
	}

	@Override
	public boolean tearDown() throws IOException, InterruptedException {
		boolean result = false;
		try {
			if (logger == null) {
				logger = taskListener.getLogger();
			}
			
			String script = build.getTestScriptPath() + "//genymotion_stop.bat";
			if (Utils.isUnix()) {
				script = build.getTestScriptPath() + "//genymotion_stop.sh";
			}
	
			List<String> args = new ArrayList<String>();
			args.add(task.getEmulator().getAvdName());
			
			Builder builder = this.getBuilder(script, args);
			result = builder.perform(build, androidSdk, task.getEmulator(), context, taskListener, "Success");
			
			if (!result) {
				log(logger, String.format("Kill the geny motion via '%s' failed.", script));
			} else {
				log(logger, String.format("Kill the geny motion via '%s' scussfully.", script));
			}
		} finally {
			build.freeEmulator(task.getEmulator().getAvdName());
		}
		return result;
	}
	
	/**
	 * Get the serial 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public String getSerialForGenyMotion(String fileName) throws IOException {
		BufferedReader reader = null;
		String line = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.build.getUserDataPath() + "//" + fileName)));
			line = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return line;
	}
}
