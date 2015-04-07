package com.mead.android.crazymonkey.persistence;

import java.util.Date;
import java.util.List;

import com.mead.android.crazymonkey.model.Task;

public interface TaskDAO {

	public List<Task> getTasks(int times, String slaverMac, Date date);
	
	public boolean updateTask(Task task);
	
	public boolean resetTask(String slaverMac);

}
