package Sergi.MVC.Model;

import java.util.Calendar;
import java.util.Date;

import Sergi.MVC.Controller.Controller;

public class TaskTimer implements Runnable {
	private Task task;
	private Controller controller;
	
	public TaskTimer(Controller controller, Task task) {
		this.task = task;
		this.controller = controller;
		Thread timerThread = new Thread(this);
		timerThread.start();
	}
	
	@Override
	public void run() {
		Date timer = task.nextTimeAfter(Calendar.getInstance().getTime());
		
		if(timer != null && timer.compareTo(task.getEndTime()) == 0)
			task.setActive(false);
		
	}

}
