package Sergi.MVC.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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

import Sergi.MVC.Controller.Controller;
import Sergi.MVC.Controller.MainFrameObserverInterface;
import Sergi.MVC.Controller.ModelException;
import Sergi.MVC.Model.parse.XMLreadWrite;

/**
 * @author Sergienko Oleg
 * 
 */
public class Model {

	private ArrayList<MainFrameObserverInterface> addEditObservers = new ArrayList<MainFrameObserverInterface>();
	private ArrayList<Task> arrList;
	private String fileName = "Tasks.xml";
	private XMLreadWrite analyzer;
	private TaskChecking taskCheking;
	private Thread thread;

	public Model() throws ModelException {
		arrList = new ArrayList<Task>();
		try {
			arrList = readTasksFromFile();
		} catch (DOMException e) {
			throw new ModelException(e);
		} catch (ParserConfigurationException e) {
			throw new ModelException(e);
		} catch (SAXException e) {
			throw new ModelException(e);
		} catch (FileNotFoundException e) {
			Controller
					.showErrorMessage("XML file with saved taskList not found. "
							+ "Please, check file \"Task.xml\" in folder with program.");
		} catch (IOException e) {
			throw new ModelException(e);
		} catch (ParseException e) {
			throw new ModelException(e);
		}

		taskCheking = new TaskChecking(this);
		
	}

	public void addNewTask(Task task) {
		arrList.add(task);
		notifyObservers(arrList);
	}

	/*
	 * public void addNewTask(ArrayList<Task> tasks) { for (Task task : tasks) {
	 * arrList.add(task); } }
	 */

	public void addTaskArray(Task[] tasks) {
		for (int i = 0; i < tasks.length; i++) {
			arrList.add(tasks[i]);
		}
		notifyObservers(arrList);
	}

	public void removeTask(Task task) {
		arrList.remove(task);
		notifyObservers(arrList);
	}

	public Task[] getArrayTaskList() {
		Task[] taskArray = new Task[arrList.size()];
		for (int i = 0; i < taskArray.length; i++) {
			taskArray[i] = arrList.get(i);
		}
		return taskArray;
	}

	public ArrayList<Task> getTaskList() {
		return arrList;
	}

	public Task getTask(String name) {
		for (Task task : arrList)
			if (task.getTitle().equals(name))
				return task;
		return null;
	}
	
	public Task getTask(Date date) {
		for (Task task : arrList)
			if (task.getTime().equals(date))
				return task;
		return null;
	}

	public void registerObserver(MainFrameObserverInterface observer) {
		addEditObservers.add(observer);
	}

	public void removeObserver(MainFrameObserverInterface observer) {
		addEditObservers.remove(observer);
	}

	public void notifyObservers(Object value) {
		for (MainFrameObserverInterface iterable_element : addEditObservers) {
			iterable_element.update(value);
		}
	}

	public ArrayList<Task> readTasksFromFile()
			throws ParserConfigurationException, SAXException, IOException,
			DOMException, ParseException, FileNotFoundException {
		analyzer = new XMLreadWrite();
		return analyzer.parseXMLFile(fileName);
	}

	public Document writeTasksToFile(Task[] taskArray) throws ModelException {
		if (taskArray.length == 0)
			return null;
		try {
			analyzer = new XMLreadWrite();
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(analyzer.createDocument(taskArray));
			StreamResult result = new StreamResult(new File(fileName));

			transformer.transform(source, result);

		} catch (TransformerConfigurationException e) {
			throw new ModelException(e);
		} catch (TransformerException e) {
			throw new ModelException(e);
		} catch (ParserConfigurationException e) {
			throw new ModelException(e);
		}
		return null;
	}

	public int getTaskIndex(String taskTitle) {
		for (int i = 0; i < arrList.size(); i++) {
			if (taskTitle.equals(arrList.get(i).getTitle()))
				return i;
		}
		return -1;
	}

	public void itsTimeToTask(LinkedList<Task> onsetTaskList) {
	    if(!onsetTaskList.isEmpty()) {
	        notifyObservers(onsetTaskList);
	        notifyObservers(arrList);
	    }
	}
	
	public void startTaskCheking() {
		thread = new Thread(taskCheking);
		thread.start();
	}
	
	public void checkTasks() {
	    itsTimeToTask(taskCheking.checkTasks());
	}

	public Task getTaskIndex(int selectedIndex) {
		return arrList.get(selectedIndex);
	}

	public boolean contains(Task chekingTask) {
		for (Task task : arrList) {
			if(task.equals(chekingTask))
				return true;
		}
		return false;
	}

    public void replaceTask(Task oldTaskData, Task newTaskData) {
        oldTaskData.setTitle(newTaskData.getTitle());
        oldTaskData.setActive(newTaskData.isActive());
        oldTaskData.setRepeatCount(newTaskData.getRepeatCount());
        oldTaskData.setStartTime(newTaskData.getStartTime());
        if(newTaskData.isRepeated())
            oldTaskData.setEndTime(newTaskData.getEndTime());
    }
}
