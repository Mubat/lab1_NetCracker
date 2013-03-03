package Sergi.MVC.Model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import Sergi.MVC.Controller.Controller;

public class TaskObserverOld implements Runnable {

	private int sleepSeconds = 1;
	private Controller control;
	Thread thread;

	public TaskObserverOld(Controller control) {
		this.control = control;
		thread = new Thread(this);
		thread.run();

	}

	@Override
	public void run() {
		for (;;) {
			Task[] tasks = control.getTasks();
			for (Task task : tasks) {
				if(!task.isActive()) continue;
				new TaskTimer(control, task);
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
