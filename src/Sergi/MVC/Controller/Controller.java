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

//сделать проверку, что наступившая задача уже показывается

public class Controller extends Tools implements ActionListener, MainFrameObserverInterface,
        ListSelectionListener {

    static SimpleDateFormat sdf;
    private static Model model;
    private static MainFrame mainFrame;
    private ShadowTasks shadowTasks;
    private ArrayList<Task> printedTasks = new ArrayList<Task>();
                                // индексы задач, которые уже выведены. 
                                // Нужно для того, чтобы не выводить одну и ту же
                                // задачу несколько раз
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
        shadowTasks = new ShadowTasks();
        try {
            setLookAndFeel();
        } catch (ModelException e) {
            error(mainFrame, e.toString());
        }
        mainFrame = new MainFrame("Диспетчер задач"); 
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
            error(mainFrame, e.toString());
        }
    }

    public void storeTasksAndExit(){
        try {
            model.writeTasksToFile();
            System.exit(0);
        } catch (ModelException e) {
            error(e.toString());
        }

    }

    @Override
    public void update(Object value) throws ModelException {
        if (value instanceof LinkedList<?>)
            info((LinkedList<Task>) value);
        else if (value instanceof ArrayList<?>)
            mainFrame.updateList(model.getTaskList().toArray());
        else error("Unexpectable type.");
    }

    private void info(LinkedList<Task> values) throws ModelException {
        for (Task task : values) {
            if(!printedTasks.contains(task) 
               && !shadowTasks.containsTask(task)) {
                showInformDialog(task);
            }
        }
        
        if(!shadowTasks.isEmpty() && shadowTasks.containsTasksByDate(toCurentDateFormat()))
            for(Task shadowTask : shadowTasks.getShadowList(toCurentDateFormat())) {
                showInformDialog(shadowTask);
                shadowTasks.remove(toCurentDateFormat(), shadowTask);
            }
        update(model.getTaskList());
    }
    
    private void showInformDialog(Task task) {
        printedTasks.add(task);
        InformDialog informFrame = new InformDialog(null, false, task);
        informFrame.setLocationRelativeTo(mainFrame);
        informFrame.addActionListener(this);
        informFrame.setVisible(true);
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
        String actionCommand = event.getActionCommand();
        ButtonNames buttonName = ButtonNames.getType(actionCommand);
        try {
            switch (buttonName) {
            case BUTTON_NAME_ADD_DIALOG: {
                TaskDialog addEditDialog1 = new TaskDialog(mainFrame, false);
                addEditDialog1.addActionListener(Controller.this);
                addEditDialog1.setVisible(true);
                break;
            }
            case BUTTON_NAME_REPALCE: {
                MainFrame frame = (MainFrame) event.getSource();

                TaskDialog addEditDialog = new TaskDialog(
                        frame, model.getTaskList().get(frame.getSelectedIndex()), 
                        false);
                
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
            case BUTTON_NAME_ADD_TASK:    {
                TaskDialog object = (TaskDialog) event.getSource();
                model.addNewTask(object.getTask());
                object.dispose();
                break;
            }
            case BUTTON_NAME_EDIT_TASK:   {
                TaskDialog object = (TaskDialog) event.getSource();
                model.replaceTask(object.getOldTask(), object.getTask());
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
                shadowTasks.add(continueTime, task);
                printedTasks.remove(task);
                object.dispose();
                break;
            }
            case BUTTON_NAME_DEACTIVATE:  {
                InformDialog object = (InformDialog) event.getSource();
                Task taskToDeactivate = object.getTask();
                int i = model.getTaskIndex(taskToDeactivate);
                
                printedTasks.remove(model.getTaskByIndex(i));
                taskToDeactivate.setActive(false);
                object.dispose();
                break;
            }
            default: error("Неизвестное название команды: " + buttonName);
            }
        } catch(ModelException e) {
            error(e.toString());
        }
    }
    
 /*   //=========ShadowTasks================

    protected void shadowTasksAdd(Date date, Task task) {
        shadowTasks.add(date, task);
    }

    protected boolean shadowTasksRemove(Date date, Task task) throws ModelException {
        return shadowTasks.remove(date, task);
    }

    protected boolean shadowTasksIsEmpty() {
        return shadowTasks.isEmpty();
    }

    protected boolean shadowTasksContainsTask(Task onsetTask) throws ModelException {
        return shadowTasks.containsTask(onsetTask);
    }

    protected LinkedList<Task> ShadowTasksGetShadowList(Date dueDate) { 
        return shadowTasks.getShadowList(dueDate);
    }
*/
}