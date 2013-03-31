package Sergi.MVC.Viewer;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

import Sergi.MVC.Controller.ActionListenerTM;
import Sergi.MVC.Model.Task;

public class InformDialog extends JDialog {

    private static final long serialVersionUID = 1476500591459125384L;

    LinkedList<Task> taskList;

    private JButton jDeactivate;
    private JButton jAside;
    private JList jList;

    private DefaultListModel listModel;

//    private ActionListener parentListener;

    public InformDialog(JFrame owner, boolean modal, LinkedList<Task> task) {
        super(owner, "Информационное окно", modal);
        taskList = task;
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new GridBagLayout());
        listModel = new DefaultListModel();
        for (Task task : taskList) {
            listModel.addElement(task);
        }
        jList = new JList(listModel);
        
        this.add(jList,         new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 0, 10), 0, 0));
        this.add(buttonPanel(), new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 10, 0, 5), 0, 0));
        this.pack();
        this.setMinimumSize(this.getSize());
    }

    private Component buttonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        jDeactivate = new JButton(ButtonNames.BUTTON_NAME_DEACTIVATE.getTypeValue());
        jAside      = new JButton(ButtonNames.BUTTON_NAME_SET_ASIDE .getTypeValue());
        
        buttonPanel.add(jDeactivate, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 0, 10), 0, 0));
        buttonPanel.add(jAside, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 0, 10), 0, 0));
        return buttonPanel;
    }

    public void addActionListener(ActionListener listener) {
//        parentListener = listener;
        jAside.addActionListener     (new ActionListenerTM(this, 0, listener));
        jDeactivate.addActionListener(new ActionListenerTM(this, 0, listener));

    }

    @SuppressWarnings("deprecation")
    public Object[] getSelectedValues() {
        return jList.getSelectedValues();
    }

    public boolean isEmpty() {
        return listModel.isEmpty();
    }
    
    public void addElement(Task element) {
        if(!listModel.contains(element))
            listModel.addElement(element);
    }

    public boolean removeElement(Task element) {
        return listModel.removeElement(element);
    }
    
}