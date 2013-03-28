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
import Sergi.MVC.Viewer.TaskDialog;
import Sergi.MVC.Viewer.ButtonNames;
import Sergi.MVC.Viewer.MainFrame;

public class Controller implements ActionListener, MainFrameObserverInterface,
		ListSelectionListener {

	static SimpleDateFormat sdf;
	private static Model model;
	private static MainFrame mainFrame;
	private Task taskForEdit;
	private TaskDialog addEditDialog;
	public int i;

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
			addEditDialog.setTask(new Task());
		} else {
			taskForEdit = model.getTaskList().get(i);
			addEditDialog.setTask(taskForEdit);
		}
		addEditDialog.addActionListener(this);
		addEditDialog.setVisible(true);

	}

	public void findTaskIndex(String text) {
		int findedTaskInList = model.getTaskIndex(text);
		if (findedTaskInList != -1)
			mainFrame.enableInList(findedTaskInList);
	}

	public void removeTask(List<Object> list) {
		
	}

	public void storeTasks() throws ModelException {
		model.writeTasksToFile(model.getArrayTaskList());
		System.exit(0);
	}

	/**
	 * @author Pochkun Taras
	 * @param e
	 */
	private void setWindowEvent(ActionEvent e) {
		String buttonName = e.getActionCommand();
		System.out.println(e.getSource());
//		System.out.println("Before: "+ addEditDialog + "\n" + e.getSource());
		if(ButtonNames.BUTTON_NAME_ADD_TASK.equals(buttonName)  ||
		   ButtonNames.BUTTON_NAME_EDIT_TASK.equals(buttonName) ||
		   ButtonNames.BUTTON_NAME_CANCEL_TASK.equals(buttonName)) {
			addEditDialog = (TaskDialog) e.getSource(); // здесь не хочет перетираться ссылка. Остается последней, которая создалась.
			
		}
//		System.out.println("After: "+addEditDialog);
	}
	
	@Override
	public void update(Object value) {
		if (value instanceof Task)
			JOptionPane.showMessageDialog(null, "It`s time for task \""
					+ ((Task) value).getTitle() + "\".",
					  ((Task) value).getTitle(), JOptionPane.DEFAULT_OPTION);
		else if (value instanceof ArrayList<?>)
			mainFrame.updateList(model.getArrayTaskList());
		else showErrorMessage("Unexpectable type.");
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		mainFrame.setButtonEnabled(
				ButtonNames.BUTTON_NAME_REMOVE.getTypeValue(), true);
	}

	public static void showErrorMessage(String errorString) {
		MainFrame.showErrorMessage(mainFrame, errorString);
	}

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
	
	@Override
	public void actionPerformed(ActionEvent event) {
		setWindowEvent(event);
		String actionCommand = event.getActionCommand();
		ButtonNames buttonName = ButtonNames.getType(actionCommand);
		ActionHandler handler = this.new ActionHandler();
		
		switch (buttonName) {
		case BUTTON_NAME_ADD_DIALOG: 	handler.addDialog(); break;
		case BUTTON_NAME_REPALCE: 		handler.editDialog(); break;
		case BUTTON_NAME_REMOVE: 		handler.remove(); break; 
		case BUTTON_NAME_EXIT: 			handler.exit();	break;
		case BUTTON_NAME_FIND: 			handler.find(); break;
		case BUTTON_NAME_ADD_TASK: 		handler.addTask(); break;
		case BUTTON_NAME_EDIT_TASK: 	handler.editTask();	break;
		case BUTTON_NAME_CANCEL_TASK: 	handler.cancel(); break;
		default: 						showErrorMessage("Неизвестное название команды: " + buttonName);
		}
//		mainFrame.setVisible(false);
//		mainFrame.repaint();
//		mainFrame.setVisible(true);
	}
	
	private class ActionHandler {

		public void addDialog(){
			addEditDialog = new TaskDialog(mainFrame, Integer.toString(i++), false);
			addEditDialog.setTask(new Task());
			addEditDialog.addActionListener(Controller.this);
			addEditDialog.setVisible(true);
		}
		
		public void cancel() {
//			System.out.println("TEst");
			addEditDialog.dispose();
		}

		public void editTask() {
			if (taskForEdit != null &&	model.getTaskList().contains(taskForEdit))
				model.removeTask(taskForEdit);

			model.addNewTask(addEditDialog.getTask());
			addEditDialog.dispose();
			model.checkTasks();
		}

		public void addTask() {
			Task task = addEditDialog.getTask();
			
			if (model.contains(task)) {
				showErrorMessage("Задача уже есть в списке");
				return;
			}
			model.addNewTask(addEditDialog.getTask());
			addEditDialog.dispose();
			model.checkTasks();
			
		}

		public void find() {
			findTaskIndex(mainFrame.getFindString());
		}

		public void remove() {
			for (Object task : mainFrame.getSelectasValuesList()) {
				model.removeTask((Task) task);
			}
		}

		public void editDialog(){
			if (mainFrame.getSelectedIndicies().length == 1) {
				addEditDialog = new TaskDialog(
						mainFrame,Integer.toString(i++)+ " Изменить задачу " + model.getTaskIndex(mainFrame.getSelectedIndex()).getTitle(), 
						false);
				addEditDialog.setTask(model.getTaskList().get(mainFrame.getSelectedIndex()));
				addEditDialog.addActionListener(Controller.this);
				addEditDialog.setVisible(true);
			} else
				showErrorMessage("Выбрано больше одного объекта для изменения!");
		}
		
		public void exit(){
			
			try {
				storeTasks();
			} catch (ModelException e) {
				MainFrame.showErrorMessage(mainFrame, e.getMessage());
			}
		}
		
	}
}
