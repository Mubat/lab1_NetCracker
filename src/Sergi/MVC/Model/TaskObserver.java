package Sergi.MVC.Model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import Sergi.MVC.Controller.Controller;

public class TaskObserver implements Runnable {

	private int sleepSeconds = 60;
	private Controller control;
	private Model model;
	Thread thread;

	public TaskObserver(Model model, Controller control) {
		this.model = model;
		this.control = control;
		thread = new Thread(this);
		thread.run();

	}

	@Override
	public void run() {
		for (;;) {
			Task[] tasks = control.getTasks();
			for (Task task : tasks) {
				if(task.nextTimeAfter(Calendar.getInstance().getTime()) == null) {
					if(task.isActive())
						control.itsTimeToTask(task);
					task.setActive(false);
				}
				if (!task.isActive())
					continue;
				Date presentTime = new Date();
				Date taskTime = task.lastTimeBefore(presentTime);
				
//				println("\t" + taskTime + " : " +  presentTime);
				if(taskTime != null && presentTime.after(taskTime) )
					control.itsTimeToTask(task);
			}
			try {
				Thread.sleep(1000 * sleepSeconds);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sravneniye dvuh dat
	 * 
	 * @param from
	 * @param with
	 * @return true  is both data are comparable.
	 */
	public static boolean dateCompare(Date date, Date with) {
		Calendar calF = new GregorianCalendar();
		calF.setTime(date);
		Calendar calW = new GregorianCalendar();
		calW.setTime(with);
		if (calF.get(Calendar.DAY_OF_YEAR) == calW.get(Calendar.DAY_OF_YEAR)
				&& calF.get(Calendar.HOUR_OF_DAY) == calW
						.get(Calendar.HOUR_OF_DAY)
				&& calF.get(Calendar.MINUTE) == calW.get(Calendar.MINUTE))
			return true;
		return false;
	}

}
