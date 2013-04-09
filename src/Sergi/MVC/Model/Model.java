package Sergi.MVC.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import Sergi.MVC.Controller.MainFrameObserverInterface;
import Sergi.MVC.Model.parse.XMLreadWrite;

/**
 * @author Sergienko Oleg
 * 
 */
public class Model extends Sergi.MVC.Tools {

    private ArrayList<MainFrameObserverInterface> addEditObservers = new ArrayList<MainFrameObserverInterface>();
    private ArrayList<Task> arrList; // /база данных задач
    private String fileName = "Tasks.xml";
    private XMLreadWrite analyzer;
    private TaskChecking taskCheking;
    private Thread thread;

    public Model() throws ModelException {
        arrList = new ArrayList<Task>();
        arrList = readTasksFromFile();
        taskCheking = new TaskChecking(this);
    }

    /**
     * adding new tasks to the database
     * 
     * @param task
     *            a task that must be added
     * @throws ModelException
     *             error when adding the task is already listed
     */
    public void addNewTask(Task task) throws ModelException {
        if (arrList.contains(task))
            throw new ModelException("Данная задача уже существет.");
        arrList.add(task);
        notifyObservers(arrList);
    }

    /**
     * Add multiple tasks in a database
     * 
     * @param tasks
     *            array of tasks that must be added
     * @throws ModelException
     *             error when adding the task is already listed
     */
    public void addTaskArray(Task[] tasks) throws ModelException {
        for (int i = 0; i < tasks.length; i++) {
            addNewTask(tasks[i]);
        }
        notifyObservers(arrList);
    }

    /**
     * Deleting a task from the database
     * 
     * @param task
     *            the task to be removed
     * @throws ModelException
     *             issue where there is no such problem in the database
     */
    public void removeTask(Task task) throws ModelException {
        if (!arrList.contains(task))
            throw new ModelException("Ошибка удаления. Задача \""
                    + task.getTitle() + "\" не найдена в базе данных");
        arrList.remove(task);
        notifyObservers(arrList);
    }

    /**
     * Check the list of tasks that are present in the database
     * 
     * @return список задач, которые присутствуют в базе данных
     */
    public ArrayList<Task> getTaskList() {
        return arrList;
    }

    /**
     * Find the task in the database
     * 
     * @param name
     *            The name of the task you want to find (it finds the first
     *            available task)
     * @return task on behalf of, or null - if the problem has not been found
     */
    public Task getTask(String name) {
        for (Task task : arrList)
            if (task.getTitle().equals(name))
                return task;
        return null;
    }

    /**
     * Register an observer from the register
     * 
     * @param observer
     *            observer, to be recorded in the register
     */
    public void registerObserver(MainFrameObserverInterface observer) {
        addEditObservers.add(observer);
    }

    /**
     * Removal from the register of the observer
     * 
     * @param observer
     *            observer to be removed from the register
     */
    public void removeObserver(MainFrameObserverInterface observer) {
        addEditObservers.remove(observer);
    }

    /**
     * Notification observers of changes
     * 
     * @param value
     *            changes which have been made
     */
    public void notifyObservers(Object value) {
        for (MainFrameObserverInterface iterable_element : addEditObservers) {
            iterable_element.update(value);
        }
    }

    /**
     * Notification observers of changes in task dataBase
     * 
     * @throws ModelException
     */
    public void notifyObservers() throws ModelException {
        for (MainFrameObserverInterface iterable_element : addEditObservers) {
            iterable_element.update(getTaskList());
        }
    }

    /**
     * Reading the list of tasks from the XML file Tasks.xml
     * 
     * @return list of tasks that have been read from the file
     * @throws ModelException
     *             some exception
     */
    private ArrayList<Task> readTasksFromFile() throws ModelException {
        analyzer = new XMLreadWrite();
        try {
            return analyzer.parseXMLFile(fileName);
        } catch (DOMException e) {
            throw new ModelException(e);
        } catch (FileNotFoundException e) {
            throw new ModelException(
                    e,
                    "XML file with saved taskList not found. "
                            + "Please, check file \"Task.xml\" in folder with program.");
        } catch (ParseException e) {
            throw new ModelException(e);
        } catch (SAXException e) {
            throw new ModelException(e);
        } catch (IOException e) {
            throw new ModelException(e);
        } catch (ParserConfigurationException e) {
            throw new ModelException(e);
        }
    }

    /**
     * Write a list of tasks in the XML file Tasks.xml (old data fray)
     * 
     * @throws ModelException
     *             some exception
     */
    public void writeTasksToFile() throws ModelException {
        try {
            analyzer = new XMLreadWrite();
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(analyzer.createDocument(arrList));
            StreamResult result = new StreamResult(new File(fileName));

            transformer.transform(source, result);

        } catch (TransformerConfigurationException e) {
            throw new ModelException(e);
        } catch (TransformerException e) {
            throw new ModelException(e);
        } catch (ParserConfigurationException e) {
            throw new ModelException(e);
        }
    }

    /**
     * Returns the position of the task in the database by its name. (Carefully!
     * possible conflicts with names.)
     * 
     * @param taskTitle
     *            Title of the task
     * @return task by index
     * @throws ModelException
     *             when task cannot be find in database
     */
    public int getTaskIndex(String taskTitle) throws ModelException {
        for (int i = 0; i < arrList.size(); i++) {
            if (taskTitle.equals(arrList.get(i).getTitle()))
                return i;
        }
        throw new ModelException("Невозможно найти задачу");
    }

    /**
     * Returns the number of items in a database
     * 
     * @param task
     *            the task for which you want to know her number in the list
     * @return position of the task in database
     * @throws ModelException
     *             when task cannot be find in database
     */
    public int getTaskIndex(Task task) throws ModelException {
        for (int i = 0; i < arrList.size(); i++) {
            if (task.equals(arrList.get(i)))
                return i;
        }
        throw new ModelException("Невозможно найти задачу в базе данных");
    }

    /**
     * The method, which displays a message box with the tasks that have come
     * (They are passed in as a parameter)
     * 
     * @param onsetTaskList
     *            list of tasks that need to make the notification of their
     *            occurrence
     * @throws ModelException 
     */
    public void itsTimeToTask(LinkedList<Task> onsetTaskList)
            throws ModelException {
        if (!onsetTaskList.isEmpty()) {
            notifyObservers(onsetTaskList);
            notifyObservers(arrList);
        }
    }

    /**
     * Starting observer tasks to display them when their time comes
     */
    public void startTaskCheking() {
        thread = new Thread(taskCheking);
        thread.start();
    }

    /**
     * The method returns a task from the database by its number
     * 
     * @param selectedIndex index of the task in database
     * @return task by index
     */
    public Task getTaskByIndex(int selectedIndex) {
        return arrList.get(selectedIndex);
    }

    /**
     * Check whether there is a problem in the database
     * 
     * @param chekingTask task to be found in the database
     * @return Is there a task in the database
     */
    public boolean contains(Task chekingTask) {
        for (Task task : arrList) {
            if (task.equals(chekingTask))
                return true;
        }
        return false;
    }

    /**
     * Replacing the task information
     * 
     * @param oldTaskData old data of the task
     * @param newTaskData new data of the task
     * @throws ModelException When the task is already in the list,
     *           or the old look of the problem can not be found
     */
    public void replaceTask(Task oldTaskData, Task newTaskData)
            throws ModelException {
        if (newTaskData == null)
            throw new ModelException(
                    "Ошибка изменения параметров задачи. Новая задача не найдена.");
        if (getTaskList().contains(newTaskData))
            throw new ModelException("Задача уже присутствует в списке");

        oldTaskData.setTitle(newTaskData.getTitle());
        oldTaskData.setActive(newTaskData.isActive());
        oldTaskData.setRepeatCount(newTaskData.getRepeatCount());
        oldTaskData.setStartTime(newTaskData.getStartTime());
        if (newTaskData.isRepeated())
            oldTaskData.setEndTime(newTaskData.getEndTime());
        notifyObservers(arrList);
    }

    /**
     * method notifies all observers of an exception that is passed as a parameter
     * @param e exception to be passed
     */
    public void updateException(ModelException e) {
        notifyObservers(e);
    }

}
