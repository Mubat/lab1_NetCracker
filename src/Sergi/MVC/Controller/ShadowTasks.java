package Sergi.MVC.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

import Sergi.MVC.Tools;
import Sergi.MVC.Model.Model;
import Sergi.MVC.Model.ModelException;
import Sergi.MVC.Model.Task;

/**
 * class that implements the mechanism of <b>shadow tasks</b>.
 * <b>Shadow task</b> is the task link that can be given in 5 minutes
 * @author Mubat
 *
 */
public class ShadowTasks extends Tools {

    private HashMap<Date, LinkedList<Task>> shadowMap;// нужно для отсрочки
    // (сюда заносятся задачи, которые были продлены на 5 мин.)
    // и время, когда нужно показать еще раз задачу

    protected ShadowTasks() {
        shadowMap = new HashMap<Date, LinkedList<Task>>();
    }

    /**
     * Adding a shadow  task
     * 
     * @param date
     *            Date when to show the task again
     * @param task
     *            a task that must be shown
     */
    protected void add(Date date, Task task) {
        LinkedList<Task> list = new LinkedList<Task>();

        list.add(task);
        if (shadowMap.containsValue(list)) {
            return;
        } else if (!shadowMap.containsKey(toDateFormat(date))) {
            shadowMap.put(toDateFormat(date), list);
            return;
        } else if (shadowMap.containsKey(toDateFormat(date))
                && !shadowMap.get(toDateFormat(date)).contains(task)) {
            list.addAll(shadowMap.get(toDateFormat(date)));
            shadowMap.put(toDateFormat(date), list);
        }
    }

    /**
     * Remove a task from the shadow register
     * 
     * @param date
     *            the date when the task to be removed should be shows
     * @param task
     *            the task to be deleted
     * @throws ModelException
     *             Unable to find the specified date, or is not found on task
      *            Specified date.
     */
    protected boolean remove(Date date, Task task) throws ModelException {
        if (!shadowMap.containsKey(toDateFormat(date)))
            throw new ModelException("Задач при текущей дате не найдено ("
                    + toDateFormat(new Date()) + ").");
        if (!containsTask(task))
            throw new ModelException("Не найдена задача по указанной дате ("
                    + toDateFormat(new Date()) + "). [" + task + "]");
        return shadowMap.get(toDateFormat(date)).remove(task);
    }

    protected boolean isEmpty() {
        return shadowMap.isEmpty();
    }

    /**
     * Check whether there is a shadow task list or not if there is such a problem,
     * then show it.
     * 
     * @param date The date by which the task is assigned (the date when the shadow task must be shown)
     * @return shadow task you want to display, or null if there are no problems of shadow
     * @throws ModelException if onsetTask = null.
     */
    protected boolean containsTask(Task onsetTask) throws ModelException {
        if (onsetTask == null)
            throw new ModelException("ShadowTask. "
                    + "Передаваемое значение задачи является пустым.");
        if (shadowMap.isEmpty())
            return false;

        Set<Entry<Date, LinkedList<Task>>> allShadowScope = shadowMap
                .entrySet();
        for (Entry<Date, LinkedList<Task>> entry : allShadowScope) {
            for (Task task : entry.getValue()) {
                if (onsetTask.equals(task))
                    return true;
            }
        }
        return false;
    }

    /**
     * Returns the value to which the specified date is mapped, or null if this
     * map contains no mapping for the key.
     * 
     * @param dueDate the date on which returns a list of shadow tasks
     * @return List of shadow tasks or null, if the problems on this date not found
     */
    protected LinkedList<Task> getShadowList(Date dueDate) {
        return shadowMap.get(toDateFormat(dueDate));
    }

    /**
     * Check whether there is a shadow task list or not if there is such a problem,
     * then show it.
     * @param dueDate date on which tasks are shady
     * @return List of shadow tasks that correspond to the date
     */
    public boolean containsTasksByDate(Date dueDate) {
        return shadowMap.containsKey(toDateFormat(dueDate));
    }

    /**
     * Check dates to which is assigned the task.
     * 
     * @param oncetTask
     * @return the date by which the task is assigned (the date when the task must be shown).
     * @throws ModelException If the problem has not been found.
     */
    public Date getDateByTask(Task oncetTask) throws ModelException {
        if (!containsTask(oncetTask))
            return null;
        Set<Entry<Date, LinkedList<Task>>> allShadowScope = shadowMap
                .entrySet();
        for (Entry<Date, LinkedList<Task>> entry : allShadowScope) {
            for (Task task : entry.getValue()) {
                if (oncetTask.equals(task))
                    return entry.getKey();
            }
        }
        return null;
    }

}
