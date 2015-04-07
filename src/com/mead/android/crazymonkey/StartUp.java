package com.mead.android.crazymonkey;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.mead.android.crazymonkey.model.Task;
import com.mead.android.crazymonkey.persistence.MongoTask;
import com.mead.android.crazymonkey.persistence.TaskDAO;
import com.mead.android.crazymonkey.sdk.AndroidSdk;
import com.mead.android.crazymonkey.util.Utils;

public class StartUp {

	public static void main(String[] args) {
		System.out.println("------------------- CRAZY MONKEY -------------------");
		System.out.println("[" + new Date() + "] - RUNNING...");
		ExecutorService threadPool = null;

		try {
			final CrazyMonkeyBuild build = new CrazyMonkeyBuild();
			String macAddr = Utils.getMACAddr();

			threadPool = Executors.newFixedThreadPool(build.getNumberOfEmulators());
			final AndroidSdk sdk = new AndroidSdk(build.getAndroidSdkHome(), build.getAndroidRootHome());
			CompletionService<Task> cs = new ExecutorCompletionService<Task>(threadPool);

			TaskDAO taskDAO = new MongoTask(build);			
			System.out.println(String.format(("[" + new Date() + "] - Reset the tasks %s on %s."), taskDAO.resetTask(macAddr), macAddr));
			
			int numberOfNoTasks = 0;

			while (true) {
				int activeCount = build.getActiveEmulatorCount();
				if (activeCount >= 0 && activeCount < build.getNumberOfEmulators()) {
					if (numberOfNoTasks != 0) {
						long waitSeconds = (long) Math.pow(2, numberOfNoTasks);
						String waitingMsg = String.format("[" + new Date() + "] - There are no tasks now, waiting for the tasks for %d seconds...", waitSeconds * 3);
						System.out.println(waitingMsg);
						Thread.sleep(waitSeconds * 3000);
					}
					int limit = build.getNumberOfEmulators() - activeCount;
					List<Task> tasks = taskDAO.getTasks(limit, macAddr, new Date());
					if (tasks != null && !tasks.isEmpty()) {
						numberOfNoTasks = 0;
						for (int i = 0; i < Math.min(tasks.size(), limit); i++) {
							Thread.sleep(5000);
							Task task = tasks.get(i);
							if (Integer.parseInt(task.getEmulator().getAvdName().substring(CrazyMonkeyBuild.EMULATOR_NAME_PREFIX.length())) != -1) {
								assignTask(build, sdk, cs, task);
							} else {
								System.out.println(String.format("No available emulator for the test case %s", task.getId()));
							}
						}
					} else {
						if (numberOfNoTasks < 5) {
							numberOfNoTasks++;
						}
					}
				} 
				Thread.sleep(10000);
			}
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (threadPool != null) {
				threadPool.shutdown();
			}
		}
		System.out.println("------------------- MONKEY RESTS -------------------");
		System.exit(0);
	}

	public static void assignTask(final CrazyMonkeyBuild build, final AndroidSdk sdk, CompletionService<Task> cs, final Task task)
			throws IOException, FileNotFoundException {

		System.out.print(String.format("[" + new Date() + "] - The %s task '%s' has started. \r\n", task.getEmulator().getAvdName(), task.getId()));
		task.setAssignTime(new Date());

		Callable<Task> runCallable = new RunScripts(build, task, sdk, new StreamTaskListener(getLoggerForTask(build, task)));
		final Future<Task> future = cs.submit(runCallable);

		ScheduledExecutorService canceller = Executors.newSingleThreadScheduledExecutor();
		canceller.schedule(new Callable<Void>() {
			public Void call() {
				if (future.cancel(true)) {
					System.out.println(String.format("[" + new Date() + "] - Cancelled the task '%s' since it's timeout in %d miniutes.", task.getId(), build.getEmulatorTimeout()));
				}
				return null;
			}
		}, build.getEmulatorTimeout(), TimeUnit.MINUTES);
		return;
	}

	public static FileOutputStream getLoggerForTask(final CrazyMonkeyBuild build, Task task) throws IOException, FileNotFoundException {
		File f = new File(build.getLogPath() + "//" + task.getId() + ".log");
		if (!f.exists()) {
			f.getParentFile().mkdirs();
			f.createNewFile();
		}
		FileOutputStream file = new FileOutputStream(f, true);
		return file;
	}
}
