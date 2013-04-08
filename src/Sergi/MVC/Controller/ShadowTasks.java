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

public class ShadowTasks extends Tools {

    public HashMap<Date, LinkedList<Task>> shadowMap;// нужно для отсрочки

    // (сюда заносятся задачи, которые были продлены на 5 мин.)
    // и время, когда нужно показать еще раз задачу

    protected ShadowTasks() {
        shadowMap = new HashMap<Date, LinkedList<Task>>();
    }

    /**
     * Добавление теневой задачи
     * 
     * @param date
     *            дата, когда нужно показать задачу еще раз
     * @param task
     *            задача, которую нужно показать
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
     * Удалить теневую задачу
     * 
     * @param date
     *            дата, когда задача, которую нужно удалить, должна быть
     *            показана
     * @param task
     *            задача, которую нужно удалить
     * @throws ModelException
     *             Не найдена указанная дата, либо не найдена задача по
     *             указанной дате.
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
     * Проверка, существует ли теневая задача в списке или нет Если такая задача
     * есть, то её показать.
     * 
     * @param date
     *            дата, к которой приписана задача (дата, когда теневая задача
     *            должна быть показана)
     * @return теневую задачу, которую нунжно показать; или null если таких
     *         теневых задач нет
     * @throws ModelException
     *             если onsetTask = null.
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
     * @param dueDate
     *            дата, по которой вернется список теневых задач
     * @return список теневых задач или null, если задач по данной дате не
     *         найдено
     */
    protected LinkedList<Task> getShadowList(Date dueDate) {
        return shadowMap.get(toDateFormat(dueDate));
    }

    public boolean containsTasksByDate(Date dueDate) {
        return shadowMap.containsKey(toDateFormat(dueDate));
    }

    /**
     * Узнать дату, к которой приписана задача.
     * 
     * @param oncetTask
     * @return дату, к которой приписана задача (дату, когда задача должна быть
     *         показана).
     * @throws ModelException
     *             Если задача не была найдена.
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
