package Sergi.MVC.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Sergi.MVC.Tools;
import Sergi.MVC.Model.Model;
import Sergi.MVC.Model.ModelException;
import Sergi.MVC.Model.Task;
import Sergi.MVC.Viewer.ButtonNames;
import Sergi.MVC.Viewer.InformDialog;
import Sergi.MVC.Viewer.MainFrame;
import Sergi.MVC.Viewer.TaskDialog;

//������� ��������, ��� ����������� ������ ��� ������������

public class Controller extends Tools implements ActionListener, MainFrameObserverInterface,
        ListSelectionListener {

    static SimpleDateFormat sdf;
    private static Model model;
    private static MainFrame mainFrame;
    private Task taskForEdit;
    private ArrayList<Task> printedTasks = new ArrayList<Task>();
                                // ������� �����, ������� ��� ��������. 
                                // ����� ��� ����, ����� �� �������� ���� � �� ��
                                // ������ ��������� ���
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
        mainFrame = new MainFrame("��������� �����"); 
        update(model.getTaskList());
        model.registerObserver(this);
        mainFrame.addActionListener(this);
        mainFrame.addListSelections(this);
        mainFrame.addWindowListener(new WindowListenerTM(this));
        
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

    public void storeTasksAndExit(){
        try {
            model.writeTasksToFile(model.getArrayTaskList());
            System.exit(0);
        } catch (ModelException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(Object value) {
        if (value instanceof LinkedList<?>)
            info((LinkedList<Task>) value);
        else if (value instanceof ArrayList<?>)
            mainFrame.updateList(model.getArrayTaskList());
        else error("Unexpectable type.");
    }

    private void info(LinkedList<Task> values) {
        for (Task task : values) {
            if(!printedTasks.contains(task)) {
                printedTasks.add(task);
                InformDialog informFrame = new InformDialog(mainFrame, false, task);
                informFrame.setLocationRelativeTo(mainFrame);
                informFrame.addActionListener(this);
                informFrame.setVisible(true);
            }
        }
        update(model.getTaskList());
    }

    @Override
    public void valueChanged(ListSelectionEvent arg0) {
        mainFrame.setButtonEnabled(
                ButtonNames.BUTTON_NAME_REMOVE.getTypeValue(), true);
    }

    private static void setLookAndFeel() throws ModelException {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            throw new ModelException(e, "���� �� ������ �� ������");
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
        String actionCommand = event.getActionCommand();
        ButtonNames buttonName = ButtonNames.getType(actionCommand);
        try {
            switch (buttonName) {
            case BUTTON_NAME_ADD_DIALOG: {
                TaskDialog addEditDialog1 = new TaskDialog(mainFrame, false);
                addEditDialog1.setTask(new Task());
                addEditDialog1.addActionListener(Controller.this);
                addEditDialog1.setVisible(true);
                break;
            }
            case BUTTON_NAME_REPALCE: {
                MainFrame frame = (MainFrame) event.getSource();
                if (frame.getSelectedIndicies().length == 1)
                    error("������� ������ ������ ������� ��� ���������!");

                TaskDialog addEditDialog = new TaskDialog(
                        frame,"�������� ������ " + model.getTaskByIndex(frame.getSelectedIndex()).getTitle(), 
                        false);
                taskForEdit = model.getTaskList().get(frame.getSelectedIndex());
                addEditDialog.setTask(taskForEdit);
                addEditDialog.addActionListener(Controller.this);
                addEditDialog.setVisible(true);
                    
                break;
            }
            case BUTTON_NAME_REMOVE: {
                MainFrame frame = (MainFrame) event.getSource();
                for (Object task : frame.getSelectasValuesList()) {
                    model.removeTask((Task) task);
                }
                break; 
            }
            case BUTTON_NAME_EXIT:        {
                storeTasksAndExit();
                break;
            }
            case BUTTON_NAME_FIND: {
                MainFrame frame = (MainFrame) event.getSource();
                mainFrame.enableInList(model.getTaskIndex(mainFrame.getFindString()));
                
                break;
            }
            case BUTTON_NAME_ADD_TASK:    {
                TaskDialog object = (TaskDialog) event.getSource();
                model.addNewTask(object.getTask());
                object.dispose();
                break;
            }
            case BUTTON_NAME_EDIT_TASK:   {
                TaskDialog object = (TaskDialog) event.getSource();
                model.replaceTask(taskForEdit, object.getTask());
                object.dispose();
                break;
            }
            case BUTTON_NAME_CANCEL_TASK: {
                ((TaskDialog) event.getSource()).dispose();
                break;
            }
            case BUTTON_NAME_SET_ASIDE:   {
                InformDialog object = (InformDialog) event.getSource(); 
                Task taskToAside = object.getTask();
                
                Task task = model.getTaskList().get(
                        model.getTaskIndex(taskToAside));
                Date continueTime = new Date(currentTime().getTime() + 5 * 60 * 1000);
                model.shadowTasksAdd(toDateFormat(continueTime), task);
                
                printedTasks.remove(task);
                object.dispose();
                break;
            }
            case BUTTON_NAME_DEACTIVATE:  {
                InformDialog object = (InformDialog) event.getSource();
                Task taskToDeactivate = object.getTask();
                int i = model.getTaskIndex(taskToDeactivate);
                
                printedTasks.remove(model.getTaskByIndex(i));
                model.setTaskActiveStatus(i, false);
                object.dispose();
                break;
            }
            default: error("����������� �������� �������: " + buttonName);
            }
        } catch(ModelException e) {
            error(e.toString());
        }
    }
}