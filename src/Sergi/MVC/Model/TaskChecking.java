package Sergi.MVC.Model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskChecking implements Runnable {

	private int sleepSeconds = 60;
	private Model model;

	public TaskChecking(Model model) {
		this.model = model;

	}

	@Override
	public void run() {
		checkArrayTask();
		timeAdjustment();
		for (;;) {
			checkArrayTask();
			try {
				Thread.sleep(1000 * sleepSeconds);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void timeAdjustment() {
		long secondsToCorrect = 60 - Calendar.getInstance().get(Calendar.SECOND); 
		checkArrayTask();
		try {
			Thread.sleep((secondsToCorrect - 1) * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void checkArrayTask() {
		ArrayList<Task> tasks = model.getTaskList();
		for (Task task : tasks) {
			if (!task.isActive())
				continue;
			Date presentTime = new Date();

			if (task.nextTimeAfter(presentTime) == null) {
				if (task.isActive()) {
					model.itsTimeToTask(task);
					task.setActive(false);
				}
				continue;
			}

			Date taskTime = task.lastTimeBefore(presentTime);
			if (taskTime != null && presentTime.after(taskTime)) {
				model.itsTimeToTask(task);
			}
		}
	}
}
