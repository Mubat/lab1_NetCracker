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

    public HashMap<Date, LinkedList<Task>> shadowMap;// ����� ��� ��������

    // (���� ��������� ������, ������� ���� �������� �� 5 ���.)
    // � �����, ����� ����� �������� ��� ��� ������

    protected ShadowTasks() {
        shadowMap = new HashMap<Date, LinkedList<Task>>();
    }

    /**
     * ���������� ������� ������
     * 
     * @param date
     *            ����, ����� ����� �������� ������ ��� ���
     * @param task
     *            ������, ������� ����� ��������
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
     * ������� ������� ������
     * 
     * @param date
     *            ����, ����� ������, ������� ����� �������, ������ ����
     *            ��������
     * @param task
     *            ������, ������� ����� �������
     * @throws ModelException
     *             �� ������� ��������� ����, ���� �� ������� ������ ��
     *             ��������� ����.
     */
    protected boolean remove(Date date, Task task) throws ModelException {
        if (!shadowMap.containsKey(toDateFormat(date)))
            throw new ModelException("����� ��� ������� ���� �� ������� ("
                    + toDateFormat(new Date()) + ").");
        if (!containsTask(task))
            throw new ModelException("�� ������� ������ �� ��������� ���� ("
                    + toDateFormat(new Date()) + "). [" + task + "]");
        return shadowMap.get(toDateFormat(date)).remove(task);
    }

    protected boolean isEmpty() {
        return shadowMap.isEmpty();
    }

    /**
     * ��������, ���������� �� ������� ������ � ������ ��� ��� ���� ����� ������
     * ����, �� � ��������.
     * 
     * @param date
     *            ����, � ������� ��������� ������ (����, ����� ������� ������
     *            ������ ���� ��������)
     * @return ������� ������, ������� ������ ��������; ��� null ���� �����
     *         ������� ����� ���
     * @throws ModelException
     *             ���� onsetTask = null.
     */
    protected boolean containsTask(Task onsetTask) throws ModelException {
        if (onsetTask == null)
            throw new ModelException("ShadowTask. "
                    + "������������ �������� ������ �������� ������.");
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
     *            ����, �� ������� �������� ������ ������� �����
     * @return ������ ������� ����� ��� null, ���� ����� �� ������ ���� ��
     *         �������
     */
    protected LinkedList<Task> getShadowList(Date dueDate) {
        return shadowMap.get(toDateFormat(dueDate));
    }

    public boolean containsTasksByDate(Date dueDate) {
        return shadowMap.containsKey(toDateFormat(dueDate));
    }

    /**
     * ������ ����, � ������� ��������� ������.
     * 
     * @param oncetTask
     * @return ����, � ������� ��������� ������ (����, ����� ������ ������ ����
     *         ��������).
     * @throws ModelException
     *             ���� ������ �� ���� �������.
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
