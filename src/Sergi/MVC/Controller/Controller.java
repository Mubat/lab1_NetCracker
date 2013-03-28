package Sergi.MVC.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Sergi.MVC.Model.Model;
import Sergi.MVC.Model.Task;
import Sergi.MVC.Viewer.AddEditDialog;
import Sergi.MVC.Viewer.ButtonNames;
import Sergi.MVC.Viewer.MainFrame;

public class Controller implements ActionListener, MainFrameObserverInterface,
		ListSelectionListener {

	static SimpleDateFormat sdf;
	private static Model model;
	private static MainFrame mainFrame;
	private Task taskForEdit;
	private AddEditDialog addEditDialog;

	/**
	 * 
	 * @throws ClassNotFoundException
	 *             if the LookAndFeel class could not be found
	 * @throws InstantiationException
	 *             if a new instance of the class couldn't be created
	 * @throws IllegalAccessException
	 *             if the class or initializer isn't accessible
	 * @throws UnsupportedLookAndFeelException
	 *             if lnf.isSupportedLookAndFeel() is false
	 */
	public Controller() throws ModelException {
		model = new Model();
		try {
			setLookAndFeel();
		} catch (ModelException e) {
			MainFrame.showErrorMessage(mainFrame, e.toString());
		}
		mainFrame = new MainFrame("Диспетчер задач");
		mainFrame.initComponents();
		this.update(model.getTaskList());
		model.registerObserver(this);
		mainFrame.addActionListener(this);
		mainFrame.addListSelections(this);

		model.startTaskCheking();
		mainFrame.setVisible(true);

	}

	public static void main(String[] args) {
		try {
			new Controller();
		} catch (ModelException e) {
			MainFrame.showErrorMessage(mainFrame, e.toString());
		}
	}

	public void showAddDialog(int i) {
		if (i == -1) {
			getWindowEvent().setTask(new Task());
		} else {
			taskForEdit = model.getTaskList().get(i);
			getWindowEvent().setTask(taskForEdit);
		}
		getWindowEvent().addListeners(this);
		getWindowEvent().setVisible(true);

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

	public void exit() throws ModelException {
		model.writeTasksToFile(model.getArrayTaskList());// включить когда все
															// работает
		System.exit(0);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String actionCommand = event.getActionCommand();
		ButtonNames buttonName = ButtonNames.getType(actionCommand);
		
		if(ButtonNames.BUTTON_NAME_ADD_TASK.equals(actionCommand) &&
				ButtonNames.BUTTON_NAME_REPALCE.equals(actionCommand))
			setWindowEvent(event);
		
		switch (buttonName) {
		case BUTTON_NAME_ADD_DIALOG: {
			addEditDialog = new AddEditDialog(mainFrame, false);
			this.showAddDialog(-1);
			break;
		}
		case BUTTON_NAME_REPALCE: {
			if (mainFrame.getSelectedIndicies().length == 1) {
				addEditDialog = new AddEditDialog(mainFrame, "Изменить задачу "
						+ model.getTaskIndex(mainFrame.getSelectedIndex()).getTitle(), false);
				this.showAddDialog(mainFrame.getSelectedIndex());
			} else
				showErrorMessage("Выбрано больше одного объекта для изменения!");
			break;
		}
		case BUTTON_NAME_REMOVE: {
			this.removeTask(mainFrame.getSelectasValuesList());
			break;
		}
		case BUTTON_NAME_EXIT: {
			try {
				this.exit();
			} catch (ModelException e) {
				MainFrame.showErrorMessage(mainFrame, e.getMessage());
			}
			break;
		}
		case BUTTON_NAME_FIND: {
			this.findTaskIndex(mainFrame.getFindString());
			break;
		}
		case BUTTON_NAME_ADD_TASK: {
			Task task = getWindowEvent().getTask();
			
			if (model.contains(task)) {
				showErrorMessage("Задача уже есть в списке");
				break;
			}
			model.addNewTask(getWindowEvent().getTask());
			getWindowEvent().dispose();
			model.checkTasks();
			break;
		}
		case BUTTON_NAME_EDIT_TASK: {
			if (taskForEdit != null &&	model.getTaskList().contains(taskForEdit))
				model.removeTask(taskForEdit);

			model.addNewTask(getWindowEvent().getTask());
			getWindowEvent().dispose();
			model.checkTasks();
			break;
		}
		case BUTTON_NAME_CANCEL_TASK: {
			getWindowEvent().dispose();
			break;
		}
		default: {
			showErrorMessage("Неизвестное название команды: " + buttonName);
		}
		}

		mainFrame.setVisible(false);
		mainFrame.repaint();
		mainFrame.setVisible(true);
	}
	
	/**
	 * @author Pochkun Taras
	 * @param e
	 */
	private void setWindowEvent(ActionEvent e) {
		addEditDialog = (AddEditDialog) e.getSource();
	}
	
	/**
	 * @author Pochkun Taras
	 * @param e
	 */

	private AddEditDialog getWindowEvent() {
		return addEditDialog;
	}
	
	@Override
	public void update(Object value) {
		if (value instanceof Task)
			JOptionPane.showMessageDialog(null, "It`s time for task \""
					+ ((Task) value).getTitle() + "\".",
					((Task) value).getTitle(), JOptionPane.DEFAULT_OPTION);
		else if (value instanceof ArrayList<?>)
			mainFrame.updateList(model.getArrayTaskList());
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		mainFrame.setButtonEnabled(
				ButtonNames.BUTTON_NAME_REMOVE.getTypeValue(), true);
	}

	public static void showErrorMessage(String errorString) {
		MainFrame.showErrorMessage(mainFrame, errorString);
	}

	/**
	 * This work if using java 1.7
	 * 
	 * @throws ModelException
	 */
	private static void setLookAndFeel() throws ModelException {
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			throw new ModelException(e, "Файл со стилем не найден");
		} catch (InstantiationException e) {
			throw new ModelException(e);
		} catch (IllegalAccessException e) {
			throw new ModelException(e);
		} catch (UnsupportedLookAndFeelException e) {
			throw new ModelException(e);
		}
	}

}
