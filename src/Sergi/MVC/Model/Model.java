package Sergi.MVC.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
	private ArrayList<Task> arrList; ///база данных задач
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
	 * добавление новой задачи в базу данных
	 * @param task задача, которую надо добавить
	 * @throws ModelException ошибка, когда добавляемая задача уже присутствует в списке
	 */
	public void addNewTask(Task task) throws ModelException {
	    if(arrList.contains(task)) 
	        throw new ModelException("Данная задача уже существет.");
		arrList.add(task);
		notifyObservers(arrList);
	}

	/**
	 * Добавление несколько задач в базу данных
	 * @param tasks массив задач, которые надо добавить
	 * @throws ModelException ошибка, когда добавляемая задача уже присутствует в списке
	 */
	public void addTaskArray(Task[] tasks) throws ModelException {
		for (int i = 0; i < tasks.length; i++) {
			addNewTask(tasks[i]);
		}
		notifyObservers(arrList);
	}

	/**
	 * Удаление задачи из базы данных
	 * @param task задача, которая должна быть удалена
	 * @throws ModelException ошибка, когда такой задачи нет в базе данных
	 */
	public void removeTask(Task task) throws ModelException {
	    if(!arrList.contains(task))
	        throw new ModelException(
	                "Ошибка удаления. Задача \"" + task.getTitle() + 
	                "\" не найдена в базе данных");
		arrList.remove(task);
		notifyObservers(arrList);
	}

	/**
     * Узнать список задач, которые присутствуют в базе данных 
     * @return список задач, которые присутствуют в базе данных
     */
	public ArrayList<Task> getTaskList() {
		return arrList;
	}

	/**
	 * Найти задачу в базе данных
	 * @param name Имя задачи, которую нужно найти (будет найдена первая попавшаяся задача)
	 * @return задачу по её имени, либо null - если задача не была найдена 
	 */
	public Task getTask(String name) {
		for (Task task : arrList)
			if (task.getTitle().equals(name))
				return task;
		return null;
	}
	
	/**
	 * Регистрация наблюдателя из реестра
	 * @param observer
	 */
	public void registerObserver(MainFrameObserverInterface observer) {
		addEditObservers.add(observer);
	}

	/**
	 * Удаление наблюдателя из реестра
	 * @param observer
	 */
	public void removeObserver(MainFrameObserverInterface observer) {
		addEditObservers.remove(observer);
	}

	/**
	 * Оповещение наблюдателей об изменениях
	 * @param value
	 * @throws ModelException 
	 */
    public void notifyObservers(Object value) throws ModelException {
        for (MainFrameObserverInterface iterable_element : addEditObservers) {
            iterable_element.update(value);
        }
    }

    /**
     * Оповещение наблюдателей об изменениях
     * @throws ModelException 
     */
    public void notifyObservers() throws ModelException {
        for (MainFrameObserverInterface iterable_element : addEditObservers) {
            iterable_element.update(getTaskList());
        }
    }

    /**
     * Считывание списка задач из XML файла Tasks.xml
     * @return
     * @throws ModelException
     */
	private ArrayList<Task> readTasksFromFile() throws ModelException {
		analyzer = new XMLreadWrite();
		try {
            return analyzer.parseXMLFile(fileName);
        } catch (DOMException e) {
            throw new ModelException(e);
        } catch (FileNotFoundException e) {
            throw new ModelException(e,"XML file with saved taskList not found. "
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
	 * Запись списка задач в XML файл Tasks.xml (старые данные перетираются)
	 * @param taskArray
	 * @return
	 * @throws ModelException
	 */
	public Document writeTasksToFile() throws ModelException {
		if (arrList.size() == 0)
			return null;
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
		return null;
	}

	/**
	 * Возвращает номер позиции задачи в базе данных по её названию.
	 * (Осторожно! Возможны колизии с именами.)
	 * @param taskTitle
	 * @return
	 * @throws ModelException
	 */
	public int getTaskIndex(String taskTitle) throws ModelException {
		for (int i = 0; i < arrList.size(); i++) {
			if (taskTitle.equals(arrList.get(i).getTitle()))
				return i;
		}
		throw new ModelException("Невозможно найти задачу");
	}
	
	/**
	 * Возвращает номер позиции задачи в базе данных
	 * @param task
	 * @return
	 * @throws ModelException
	 */
   public int getTaskIndex(Task task) throws ModelException {
        for (int i = 0; i < arrList.size(); i++) {
            if (task.equals(arrList.get(i)))
                return i;
        }
        throw new ModelException("Невозможно найти задачу в базе данных");
    }

   /**
    * Метод, который выводит информационное окно с задачами, которые наступили
    * (они передаются в качестве параметра)
    * @param onsetTaskList
 * @throws ModelException 
    */
   public void itsTimeToTask(LinkedList<Task> onsetTaskList) throws ModelException {
	    if(!onsetTaskList.isEmpty()) {
	        notifyObservers(onsetTaskList);
	        notifyObservers(arrList);
	    }
	}
	
   /**
    * Запуск наблюдателся задач для вывода их, когда придет их время
    */
   public void startTaskCheking() {
		thread = new Thread(taskCheking);
		thread.start();
	}
	
   /**
    * Метод возвращает задачу из базы данных по её номеру
    * @param selectedIndex
    * @return
    */
   public Task getTaskByIndex(int selectedIndex) {
		return arrList.get(selectedIndex);
	}

   /**
    * Проверка, существует ли задача в базе данных
    * @param chekingTask
    * @return
    */
   public boolean contains(Task chekingTask) {
		for (Task task : arrList) {
			if(task.equals(chekingTask))
				return true;
		}
		return false;
	}

   /**
    * Замена информации о задаче
    * @param oldTaskData старые данные
    * @param newTaskData новые данные
    * @throws ModelException
    */
    public void replaceTask(Task oldTaskData, Task newTaskData) throws ModelException {
        if(newTaskData == null) 
            throw new ModelException("Ошибка изменения параметров задачи. Новая задача не найдена.");
        if(getTaskList().contains(newTaskData))
            throw new ModelException("Задача уже присутствует в списке");

        oldTaskData.setTitle(newTaskData.getTitle());
        oldTaskData.setActive(newTaskData.isActive());
        oldTaskData.setRepeatCount(newTaskData.getRepeatCount());
        oldTaskData.setStartTime(newTaskData.getStartTime());
        if(newTaskData.isRepeated())
            oldTaskData.setEndTime(newTaskData.getEndTime());
        notifyObservers(arrList);
    }

}
