package Sergi.MVC.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import Sergi.MVC.Model.ArrayTaskList;
import Sergi.MVC.Model.Model;
import Sergi.MVC.Model.Task;
import Sergi.MVC.Model.TaskObserver;
import Sergi.MVC.Viewer.AddEditDialog;
import Sergi.MVC.Viewer.MainFrame;

public class Controller implements ActionListener, MainFrameObserverInterface,
		ListSelectionListener {

	static SimpleDateFormat sdf;
	private static Model model;
	private static MainFrame mainFrame;
	private AddEditDialog addDialog;

	public Controller() throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		mainFrame = new MainFrame("Диспетчер задач");
		mainFrame.initComponents();
		this.update();
		model.registerObserver(this);
		mainFrame.addActionListeners(this);
		mainFrame.addListSelections(this);

		mainFrame.setVisible(true);
		new TaskObserver(model, this);
	}

	public static void main(String[] args) {

		try {
			model = new Model();
			new Controller();
		} catch (ClassNotFoundException e) {
			MainFrame.showErrorMessage(mainFrame, e.getMessage());
		} catch (InstantiationException e) {
			MainFrame.showErrorMessage(mainFrame, e.getMessage());
		} catch (IllegalAccessException e) {
			MainFrame.showErrorMessage(mainFrame, e.getMessage());
		} catch (UnsupportedLookAndFeelException e) {
			MainFrame.showErrorMessage(mainFrame, e.getMessage());
		} catch (ParserConfigurationException e) {
			MainFrame.showErrorMessage(mainFrame, e.getMessage());
		} catch (SAXException e) {
			MainFrame.showErrorMessage(mainFrame, e.getMessage());
		} catch (IOException e) {
			MainFrame.showErrorMessage(mainFrame, e.getMessage());
		} catch (DOMException e) {
			MainFrame.showErrorMessage(mainFrame, e.getMessage());
		} catch (ParseException e) {
			MainFrame.showErrorMessage(mainFrame, e.getMessage());
		}
	}

	public void showAddDialog(int i) {
		addDialog = new AddEditDialog(mainFrame, true);
		if (i == -1) {
			addDialog.setTaskName(null);
			addDialog.setStartDate(Calendar.getInstance().getTime());
			addDialog.setEndDate(Calendar.getInstance().getTime());
			addDialog.setActiveStatus(false);
			addDialog.setRepeatInterval(0);
		} else {
			Task task = model.getArrayTaskList().getTask(i);
			addDialog.setTaskName(task.getTitle());
			addDialog.setStartDate(task.getStartTime());
			addDialog.setEndDate(task.getEndTime());
			addDialog.setActiveStatus(task.isActive());
			addDialog.setRepeatInterval(task.getRepeatCount());
		}
		addDialog.addListeners(this);
		addDialog.setVisible(true);

	}

	public Task[] getTasks() {
		ArrayTaskList arrList = model.getArrayTaskList();
		Task[] tasks = new Task[arrList.size()];
		for (int i = 0; i < arrList.size(); i++) {
			tasks[i] = arrList.getTask(i);
		}
		return tasks;
	}

	public void findTaskIndex(String text) {
		int findedTaskInList = model.getTaskIndex(text);
		if (findedTaskInList != -1)
			mainFrame.enableInList(findedTaskInList);
	}

	public void removeTask(List<Object> list) {
		for (Object task : list) {
			model.removeTask((Task) task);
		}
	}

	public void itsTimeToTask(Task task) {
		JOptionPane.showMessageDialog(mainFrame,
				"Время для задачи \"" + task.getTitle() + "\" настало.",
				task.getTitle(), JOptionPane.INFORMATION_MESSAGE);
		model.notifyObservers();
	}

	public void exit() {
		model.writeTasksToFile(getTasks());
		System.exit(0);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (MainFrame.BUTTTON_NAME_ADD.equals(event.getActionCommand())) {
			this.showAddDialog(-1);
		} else if (MainFrame.BUTTON_NAME_REPALCE.equals(event
				.getActionCommand())) {
			if (mainFrame.getSelectedIndicies().length == 1) {
				this.showAddDialog(mainFrame.getSelectedIndex());
			} else
				mainFrame.showErrorMessage(mainFrame,
						"Выбрано больше одного объекта для изменения!");
		} else if (MainFrame.BUTTON_NAME_REMOVE
				.equals(event.getActionCommand())) {
			this.removeTask(mainFrame.getSelectasValuesList());
		} else if (MainFrame.BUTTON_NAME_EXIT.equals(event.getActionCommand())) {
			this.exit();
		} else if (MainFrame.BUTTON_NAME_FIND.equals(event.getActionCommand())) {
			this.findTaskIndex(mainFrame.getFindString());
		} else if (AddEditDialog.BUTTON_NAME_ADD_TASK.equals(event
				.getActionCommand())) {
			int repeats = addDialog.getRepeatValue();
			if (repeats == 0) {
				Task newTask = new Task(addDialog.getTitle(),
						addDialog.getStartDate());
				newTask.setActive(addDialog.getActiveStatus());
				model.addNewTask(newTask);
			} else {
				Task newTask = new Task(addDialog.getTitle(),
						addDialog.getStartDate(), addDialog.getEndData(),
						repeats);
				newTask.setActive(addDialog.getActiveStatus());
				model.addNewTask(newTask);

			}

			addDialog.jButtonMouseClicked();
		} else if (AddEditDialog.BUTTON_NAME_CANCEL_TASK.equals(event
				.getActionCommand())) {
			addDialog.jButtonMouseClicked();
		}
	}

	@Override
	public void update() {
		mainFrame.updateList(getTasks());
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		mainFrame.setButtonEnabled(MainFrame.BUTTON_NAME_REMOVE, true);
	}
}
