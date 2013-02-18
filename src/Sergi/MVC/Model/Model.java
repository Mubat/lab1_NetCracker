package Sergi.MVC.Model;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
import Sergi.MVC.Model.parse.MyErrHandler;
import Sergi.MVC.Model.parse.XMLreadWrite;

/**
 * @author Sergienko Oleg
 * 
 */
public class Model {
	/**
	 * @uml.property name="arrList"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private ArrayTaskList arrTaskList = new ArrayTaskList();
	private ArrayList<MainFrameObserverInterface> addEditObservers 
				= new ArrayList<MainFrameObserverInterface>();
	private ArrayList<Task> arrList;
	private String fileName = "Tasks.xml";
	private XMLreadWrite analyzer;

	public Model() throws ParserConfigurationException, SAXException,
			IOException, DOMException, ParseException {
		arrList = new ArrayList<Task>();
		addTaskArray(readTasksFromFile());

	}

	public void addNewTask(Task task) {
		arrTaskList.add(task);
		notifyObservers();
	}

	/*
	 * public void addNewTask(ArrayList<Task> tasks) { for (Task task : tasks) {
	 * arrList.add(task); } }
	 */

	public void addTaskArray(Task[] tasks) {
		for (int i = 0; i < tasks.length; i++) {
			arrTaskList.add(tasks[i]);
		}
		notifyObservers();
	}

	public void removeTask(Task task) {
		arrTaskList.remove(task);
		notifyObservers();
	}

	public ArrayTaskList getArrayTaskList() {
		return arrTaskList;
	}

	public ArrayList<Task> getArrayList() {
		for (int i = 0; i < arrList.size(); i++)
			arrList.add(arrTaskList.getTask(i));
		return arrList;
	}

	public Task getTask(String name) {
		for (Task task : arrTaskList)
			if (task.getTitle().equals(name))
				return task;
		return null;
	}

	public Task getTask(Date date) {
		for (Task task : arrTaskList)
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

	public void notifyObservers() {
		for (int i = 0; i < addEditObservers.size(); i++) {
			addEditObservers.get(i).update();
		}
	}

	public Task[] readTasksFromFile() throws ParserConfigurationException,
			SAXException, IOException, DOMException, ParseException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		db.setErrorHandler(new MyErrHandler());
		Document doc = db.parse(fileName);
		if (doc != null) {
			analyzer = new XMLreadWrite();
			analyzer.analyze(doc);
		}
		return analyzer.getTaskArray();
	}

	public Document writeTasksToFile(Task[] taskArray) {
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
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		return null;
	}

	public int getTaskIndex(String taskTitle) {
		for (int i = 0; i < arrTaskList.size(); i++) {
			if (taskTitle.equals(arrTaskList.getTask(i).getTitle()))
				return i;
		}
		return -1;
	}
}
