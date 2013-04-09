package Sergi.MVC.Model;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import Sergi.MVC.Tools;

/**
 * Class that implements the process of checking for problems of their approach.
 * @author serhienko
 * 
 */
public class TaskChecking extends Tools implements Runnable {

    private int sleepSeconds = 60;
    private Model model;

    /**
     * 
     * @param model Main class of model.
     */
    protected TaskChecking(Model model) {
        this.model = model;

    }

    @Override
    public void run() {
        try {
            model.itsTimeToTask(checkTasks());
            timeAdjustment();
            for (;;) {
                model.itsTimeToTask(checkTasks());
                try {
                    Thread.sleep(1000 * sleepSeconds);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (ModelException e) {
            model.updateException(e);
        }
    }

    private void timeAdjustment() {
        long secondsToCorrect = 60 - Calendar.getInstance()
                .get(Calendar.SECOND);
        checkTasks();
        try {
            Thread.sleep((secondsToCorrect - 1) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Making a list of tasks that need to be displayed in connection with the
     * onset of their time.
     * 
     * @return list of tasks that need to be displayed in connection with the
     *         onset of their time.
     */
    protected LinkedList<Task> checkTasks() {
        LinkedList<Task> onsetTasks = new LinkedList<Task>();
        for (Task task : model.getTaskList()) {
            if (!task.isActive())
                continue;
            Date presentTime = new Date();

            if (task.nextTimeAfter(presentTime) == null) {
                if (task.isActive()) {
                    onsetTasks.add(task);
                }
                continue;
            }

            Date taskTime = task.lastTimeBefore(presentTime);
            if (taskTime != null && presentTime.after(taskTime)) {
                onsetTasks.add(task);
            }
        }
        return onsetTasks;
    }

}
