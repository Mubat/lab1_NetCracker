package Sergi.MVC.Viewer;

import java.awt.BorderLayout;
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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.UIManager;

import Sergi.MVC.Controller.ActionListenerTM;
import Sergi.MVC.Model.Task;

public class InformDialog extends JDialog {

    private static final long serialVersionUID = 1476500591459125384L;

    private static final String INFORM_MESSAGE = "Настало время.";

    Task task;

    private JButton jDeactivate;
    private JButton jAside;

    public InformDialog(JFrame owner, boolean modal, Task task) {
        super(owner, "Информационное окно: " + task.getTitle().toUpperCase(), modal);
        this.task = task;
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        JLabel jInformText = new JLabel(INFORM_MESSAGE + task.toString());
        JLabel image = new JLabel(UIManager.getIcon("OptionPane.informationIcon"));
        add(image, BorderLayout.WEST);
        add(jInformText, BorderLayout.CENTER);
        add(buttonPanel(), BorderLayout.SOUTH);
        this.pack();
        this.setMinimumSize(this.getSize());
    }

    private Component buttonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        jDeactivate = new JButton(
                ButtonNames.BUTTON_NAME_DEACTIVATE.getTypeValue());
        jAside = new JButton(ButtonNames.BUTTON_NAME_SET_ASIDE.getTypeValue());

        buttonPanel.add(jDeactivate, new GridBagConstraints(0, 0, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                        5, 5, 0, 10), 0, 0));
        buttonPanel.add(jAside, new GridBagConstraints(1, 0, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                        5, 5, 0, 10), 0, 0));
        return buttonPanel;
    }

    public void addActionListener(ActionListener listener) {
        // parentListener = listener;
        jAside.addActionListener(new ActionListenerTM(this, 0, listener));
        jDeactivate.addActionListener(new ActionListenerTM(this, 0, listener));

    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

}