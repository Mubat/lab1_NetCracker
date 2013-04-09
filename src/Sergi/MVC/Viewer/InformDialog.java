package Sergi.MVC.Viewer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import Sergi.MVC.Controller.ActionListenerTM;
import Sergi.MVC.Model.Task;

/**
 * Form who show Inform message which indicates the task for which it is time to
 * @author serhienko
 * 
 */
public class InformDialog extends JDialog {

    private static final long serialVersionUID = 1476500591459125384L;

    private static final String INFORM_MESSAGE = "Настало время.";

    Task task;

    private JButton jDeactivate;
    private JButton jAside;

    /**
     * 
     * @param owner
     *            the owner Dialog from which the dialog is displayed or null if
     *            this dialog has no owner
     * @param modal
     *            specifies whether dialog blocks user input to other top-level
     *            windows when shown. If true, the modality type property is set
     *            to DEFAULT_MODALITY_TYPE, otherwise the dialog is modeless.
     * @param task
     *            task what should be printed in window
     */
    public InformDialog(JFrame owner, boolean modal, Task task) {
        super(owner, "Информационное окно: " + task.getTitle().toUpperCase(),
                modal);
        this.task = task;
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        JLabel jInformText = new JLabel(INFORM_MESSAGE + task.toString());
        JLabel image = new JLabel(
                UIManager.getIcon("OptionPane.informationIcon"));
        add(image, BorderLayout.WEST);
        add(jInformText, BorderLayout.CENTER);
        add(buttonPanel(), BorderLayout.SOUTH);
        this.pack();
        this.setMinimumSize(getSize());
        this.setMaximumSize(getSize());
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

    /**
     * Method of declaring a listener for the buttons
     * 
     * @param listener
     *            listener who will respond to button presses
     */
    public void addActionListener(ActionListener listener) {
        // parentListener = listener;
        jAside.addActionListener(new ActionListenerTM(this, 0, listener));
        jDeactivate.addActionListener(new ActionListenerTM(this, 0, listener));

    }

    /**
     * Returns the final form of the task
     * 
     * @return the final edited form of the task (after editing)
     */
    public Task getTask() {
        return task;
    }

    /**
     * Data entry task you want to show
     * 
     * @param task
     *            task who will show
     */
    public void setTask(Task task) {
        this.task = task;
    }

}