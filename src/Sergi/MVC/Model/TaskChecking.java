package Sergi.MVC.Model;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

public class TaskChecking implements Runnable {

	private int sleepSeconds = 60;
	private Model model;

	public TaskChecking(Model model) {
		this.model = model;
	}

	@Override
	public void run() {
		for (;;) {
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
			try {
				Thread.sleep(1000 * sleepSeconds);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
