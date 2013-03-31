package Sergi.MVC.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Sergi.MVC.Model.Model;
import Sergi.MVC.Model.Task;
import Sergi.MVC.Viewer.ButtonNames;
import Sergi.MVC.Viewer.InformDialog;
import Sergi.MVC.Viewer.MainFrame;
import Sergi.MVC.Viewer.TaskDialog;

public class Controller extends WindowAdapter implements ActionListener, MainFrameObserverInterface,
		ListSelectionListener, WindowListener {

	static SimpleDateFormat sdf;
	private static Model model;
	private static MainFrame mainFrame;
	private Task taskForEdit;
	private TaskDialog addEditDialog;
    private InformDialog informFrame;

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
		this.update(model.getTaskList());
		model.registerObserver(this);
		mainFrame.addActionListener(this);
		mainFrame.addListSelections(this);
		mainFrame.addWindowListener(this);
		model.startTaskCheking();
		mainFrame.pack();
		mainFrame.setMinimumSize(mainFrame.getSize());
		mainFrame.setVisible(true);

	}

	public static void main(String[] args) {
		try {
			new Controller();
		} catch (ModelException e) {
			MainFrame.showErrorMessage(mainFrame, e.toString());
		}
	}

/*	public void showAddDialog(int i) {
		if (i == -1) {
			addEditDialog.setTask(new Task());
		} else {
			taskForEdit = model.getTaskList().get(i);
			addEditDialog.setTask(taskForEdit);
		}
		addEditDialog.addActionListener(this);
		addEditDialog.setVisible(true);

	}
*/
	public void findTaskIndex(String text) {
		int findedTaskInList = model.getTaskIndex(text);
		if (findedTaskInList != -1)
			mainFrame.enableInList(findedTaskInList);
	}

//	public void removeTask(List<Object> list) {
//		
//	}

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

		if(ButtonNames.BUTTON_NAME_ADD_TASK.getTypeValue().equals(buttonName)  ||
		   ButtonNames.BUTTON_NAME_EDIT_TASK.getTypeValue().equals(buttonName) ||
		   ButtonNames.BUTTON_NAME_CANCEL_TASK.getTypeValue().equals(buttonName)) {
		    addEditDialog = (TaskDialog) e.getSource(); 
		}
		
		if(ButtonNames.BUTTON_NAME_DEACTIVATE.getTypeValue().equals(buttonName) ||
		   ButtonNames.BUTTON_NAME_SET_ASIDE.getTypeValue().equals(buttonName)) {
		    informFrame = (InformDialog) e.getSource();
		}
	}
	
	@SuppressWarnings("unchecked")
    @Override
	public void update(Object value) {
	    System.out.println("Update method: " + value.getClass());
		if (value instanceof LinkedList<?>)
			info((LinkedList<Task>) value);
		else if (value instanceof ArrayList<?>)
			mainFrame.updateList(model.getArrayTaskList());
		else showErrorMessage("Unexpectable type.");
	}

	private void info(LinkedList<Task> values) {
	    if(informFrame == null) {
	        informFrame = new InformDialog(mainFrame, false, values);
	        informFrame.addActionListener(this);
	        informFrame.setVisible(true);
	    }
	    for (Task task : values) {
            informFrame.addElement(task);
        }
	    update(model.getTaskList());
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
	
    public void windowClosing(WindowEvent event) {
        try {
            this.storeTasks();
        } catch (ModelException e) {
            showErrorMessage(e.toString());
        }
        event.getWindow().dispose();
    }
	
	@Override
	public void actionPerformed(ActionEvent event) {
		setWindowEvent(event);
		String actionCommand = event.getActionCommand();
		ButtonNames buttonName = ButtonNames.getType(actionCommand);
		ActionHandler handler = this.new ActionHandler();
		System.out.println(actionCommand + ":\t" + event.getSource());
		switch (buttonName) {
		case BUTTON_NAME_ADD_DIALOG:  handler.addDialog(); break;
		case BUTTON_NAME_REPALCE:     handler.editDialog(); break;
		case BUTTON_NAME_REMOVE:      handler.remove(); break; 
		case BUTTON_NAME_EXIT:        handler.exit();	break;
		case BUTTON_NAME_FIND:        handler.find(); break;
		case BUTTON_NAME_ADD_TASK:    handler.addTask(); break;
		case BUTTON_NAME_EDIT_TASK:   handler.editTask();	break;
		case BUTTON_NAME_CANCEL_TASK: handler.cancel(); break;
		case BUTTON_NAME_SET_ASIDE:   handler.setAside(); break;
		case BUTTON_NAME_DEACTIVATE:  handler.deactivate(); break;
        default: 						showErrorMessage("Неизвестное название команды: " + buttonName);
		}
		System.out.println("Выходит");
		model.checkTasks();
		model.notifyObservers(model.getTaskList());
//		mainFrame.setVisible(false);
//		mainFrame.repaint();
//		mainFrame.setVisible(true);
	}
	
	private class ActionHandler {

		public void addDialog(){
			addEditDialog = new TaskDialog(mainFrame, false);
			addEditDialog.setTask(new Task());
			addEditDialog.addActionListener(Controller.this);
			addEditDialog.setVisible(true);
		}

        private void deleteTasksFromInformFrame(Task task) {
            if(informFrame.removeElement(task))
                showErrorMessage("Задача " + task.getTitle() + " не была удалена из оповещений.");
            if(informFrame.isEmpty()) {
                informFrame.dispose();
                informFrame = null;
                System.out.println("InformFrame (must null, because empty)" + informFrame);
            }
        }

        public void cancel() {
			addEditDialog.dispose();
		}

		public void editTask() {
		    if(model.getTaskList().contains(addEditDialog.getTask())) {
		        showErrorMessage("Задача уже присутствует в списке");
		        return;
		    } 
			model.replaceTask(taskForEdit, addEditDialog.getTask());
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
						mainFrame,"Изменить задачу " + model.getTaskIndex(mainFrame.getSelectedIndex()).getTitle(), 
						false);
				taskForEdit = model.getTaskList().get(mainFrame.getSelectedIndex());
				addEditDialog.setTask(taskForEdit);
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
        
        public void deactivate() {
            Object[] selectedValuesToDeactivate = informFrame.getSelectedValues();
            if(selectedValuesToDeactivate.length == 0) {
                showErrorMessage("Выбирете одно или несколько значений");
            }
            for (Object value : selectedValuesToDeactivate) {
                int i = model.getTaskIndex(((Task) value).getTitle());
                if(i == -1) {
                    showErrorMessage("Невозможно найти задачу в базе данных");
                    return;
                }
                deleteTasksFromInformFrame(model.getTaskList().get(i));
                model.getTaskList().get(i).setActive(false);
                
            }
            model.notifyObservers(model.getTaskList());
        }

        public void setAside() {
            Object[] selectedValuesToAside = informFrame.getSelectedValues();
            if(selectedValuesToAside.length == 0) {
                showErrorMessage("Выбирете одно или несколько значений");
            }
            
            for (Object value : selectedValuesToAside) {
                int i = model.getTaskIndex(((Task) value).getTitle());
                if(i == -1) {
                    showErrorMessage("Невозможно найти задачу в базе данных");
                    return;
                }
                
                Task task = model.getTaskList().get(i);
                deleteTasksFromInformFrame(task);
                Date time = model.getTaskList().get(i).getTime();
                time.setTime(Calendar.getInstance().getTime().getTime() + 5 * 60 * 1000);
                
                if(!task.isRepeated()) {
                    model.getTaskList().get(i).setTime(time);
                }
                else {
                    if(task.getEndTime().after(time))
                        task.setStartTime(time);
                    else
                        task.setEndTime(time);
                }
                
                model.notifyObservers(model.getTaskList());
            }
        }
	}
}
