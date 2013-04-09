package Sergi.MVC.Viewer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Sergi.MVC.Tools;
import Sergi.MVC.Controller.ActionListenerTM;
import Sergi.MVC.Model.Task;

import com.toedter.calendar.JDateChooser;

/**
 * Class who represent form to add or edit task
 * @author Oleg Sergienko
 *
 */
public class TaskDialog extends JDialog {

	private static final long serialVersionUID = 1630046389235533533L;

	private static final String DEFAULT_WINDOW_NAME = "Новая задача";

	private static Integer[] hours = new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8,
			9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 };
	private static Integer[] minutes = new Integer[] { 
			 0,  1,  2,  3,  4,  5,  6,  7,  8,  9,  10, 11, 12, 13, 14, 
			 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 
			 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 
			 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60 };

	private static JSpinner jSpinner;// количество повторений
	private ButtonGroup jRadioVisible;// группа радиокнопок активности задачи
	private JDateChooser jDateChooserBegin;// выбор даты начала задачи
	private JDateChooser jDateChooserEnd;// выбор даты окончания задачи
	private JComboBox jHourBegin;// выбор времени начала задачи
	private JComboBox jHourEnd;// выбор времени окончания задачи
	private JTextField jTaskName;// наименование задачи
	private JComboBox jMinuteBegin;
	private JComboBox jMinuteEnd;

	private static JButton jAddButton;// кнопка добавления новой задачи
	private static JButton jCancelButton;// кнопка отмены

	private JRadioButton jRadioYes;
	private JRadioButton jRadioNo;

    private Task oldTask;

	// =============================================================================
	
	/**
	 * @param owner
	 *            the owner Dialog from which the dialog is displayed or null
	 *            if this dialog has no owner
	 * @param windowName name of Dialog window
	 * @param rootPaneCheckingEnabled
	 *            If true then calls to add and setLayout will be forwarded to
	 *            the contentPane.
	 */
	public TaskDialog(JFrame owner, Task task, boolean rootPaneCheckingEnabled) {
		super(owner, rootPaneCheckingEnabled);
		initComponents();
		setLocationRelativeTo(owner);
		this.pack();
		this.setMinimumSize(this.getSize());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        if(task != null ) {
            this.setName("Изменить задачу " + task);
            setTask(task);
        }
	}

    // -----------------------------------------------------------------------------
    /**
     * @param owner
     *            the owner Dialog from which the dialog is displayed or null
     *            if this dialog has no owner
     * @param rootPaneCheckingEnabled
     *            If true then calls to add and setLayout will be forwarded to
     *            the contentPane.
     */

	public TaskDialog(JFrame owner, boolean rootPaneCheckingEnabled) {
		this(owner, null, rootPaneCheckingEnabled);
		setName(DEFAULT_WINDOW_NAME);
		setTask(new Task());
	}

	// -----------------------------------------------------------------------------
	/**
	 * Оформление диалога для добавления/редактирования задачи
	 */
	private void initComponents() throws IllegalArgumentException {
		
		this.setLayout(new GridBagLayout());
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// Нименование задачи
		this.add(new JLabel("Наименование задачи", JLabel.RIGHT),
				new GridBagConstraints(0, 0, 1, 1, 0, 0,
						GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));
		jTaskName = new JTextField();
		this.add(jTaskName, new GridBagConstraints(1, 0, 4, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 0, 5, 10), 0, 0));

		// Поля для ввода времени начало и окончания задачи
		this.add(new JLabel("Дата", JLabel.RIGHT), new GridBagConstraints(0, 1,
				1, 2, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		getDatePanel();

		// Количество повторений
		this.add(new JLabel("Количество повторений", JLabel.RIGHT),
				new GridBagConstraints(0, 3, 1, 1, 0, 0,
						GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));
		jSpinner = new JSpinner(new RepeatSpinnerModel());
		this.add(jSpinner, new GridBagConstraints(1, 3, 1, 1, 0, 0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		// Выдор статуса активности задачи: активна или не активна будет задача
		this.add(new JLabel("Статус:", JLabel.RIGHT), new GridBagConstraints(0,
				4, 1, 1, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(getRadioButtonGroup(), new GridBagConstraints(1,
				4, 3, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		// Кнопки добавления/редактирования задачи и отмены
		jAddButton = new JButton(this.getName() == DEFAULT_WINDOW_NAME ? 
				ButtonNames.BUTTON_NAME_ADD_TASK.getTypeValue() : 
				ButtonNames.BUTTON_NAME_EDIT_TASK.getTypeValue());
		jAddButton.setPreferredSize(new Dimension(180, 50));
		this.add(jAddButton, new GridBagConstraints(1, 5, 2, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						5, 5, 5, 5), 0, 0));
		jCancelButton = new JButton(ButtonNames.BUTTON_NAME_CANCEL_TASK.getTypeValue());
		this.add(jCancelButton, new GridBagConstraints(3, 5, 2, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		pack();
		setLocationRelativeTo(null);
	}

	// -----------------------------------------------------------------------------
	/**
	 * Создание панели для выбора начала и окончания действия задачи Имеет вид:
	 * <html>
	 * <table border='1px'>
	 * <tr>
	 * <td><b>Начало</b></td>
	 * <td><i>дата</i></td>
	 * <td><i>время</i></td>
	 * </tr>
	 * <tr>
	 * <td><b>Окончание</b></td>
	 * <td><i>дата</i></td>
	 * <td><i>время</i></td>
	 * </tr>
	 * </table>
	 * </html>
	 * 
	 * @param isEDITdialog
	 * 
	 * @return JPanel панель выбора дат
	 */
	private void getDatePanel() {
		this.add(new JLabel("Начало"), new GridBagConstraints(1, 1, 1, 1, 0, 0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,
						0, 5, 5), 0, 0));

		jDateChooserBegin = new JDateChooser();
		this.add(jDateChooserBegin, new GridBagConstraints(2, 1, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						5, 5, 5, 5), 5, 0));
		jHourBegin = new JComboBox(hours);
		this.add(jHourBegin, new GridBagConstraints(3, 1, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
						5, 5, 0), 0, 0));
		jMinuteBegin = new JComboBox(minutes);
		this.add(jMinuteBegin, new GridBagConstraints(4, 1, 1, 1, 0, 0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,
						0, 5, 10), 0, 0));

		this.add(new JLabel("Окончание"), new GridBagConstraints(1, 2, 1, 1, 0,
				0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 0, 5, 5), 0, 0));

		jDateChooserEnd = new JDateChooser();
		this.add(jDateChooserEnd, new GridBagConstraints(2, 2, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						5, 5, 5, 5), 5, 0));
		jHourEnd = new JComboBox(hours);
		this.add(jHourEnd, new GridBagConstraints(3, 2, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
						5, 5, 0), 0, 0));
		jMinuteEnd = new JComboBox(minutes);
		this.add(jMinuteEnd, new GridBagConstraints(4, 2, 1, 1, 0, 0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,
						0, 5, 10), 0, 0));

	}

	// -----------------------------------------------------------------------------
	/**
	 * Создания Группы радиокнопок для выбора, запущена ли задача
	 * 
	 * @param isEDITdialog
	 * 
	 * @return JPanel панель с группой радиокнопок
	 */
	private Component getRadioButtonGroup() {
		JPanel jVisibilityPanel = new JPanel();
		jVisibilityPanel.setLayout(new GridLayout(1, 2));
		jRadioVisible = new ButtonGroup();
		jRadioYes = new JRadioButton("Запущена");
		jRadioNo = new JRadioButton("Незапущена");

		jRadioVisible.add(jRadioYes);
		jRadioVisible.add(jRadioNo);
		jVisibilityPanel.add(jRadioYes);
		jVisibilityPanel.add(jRadioNo);

		return jVisibilityPanel;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Method of declaring a listener for the buttons
	 * @param listener listener who will respond to button presses
	 */
	public void addActionListener(ActionListener listener) {
		jAddButton.addActionListener	(new ActionListenerTM(this, 0, listener));
		jCancelButton.addActionListener (new ActionListenerTM(this, 0, listener));
	}
	
	// -----------------------------------------------------------------------------
	private Date getStartDate() {
		Calendar cal = new GregorianCalendar();
		cal.setTime(jDateChooserBegin.getDate());
		cal.set(Calendar.HOUR_OF_DAY, (Integer) jHourBegin.getSelectedItem());
		cal.set(Calendar.MINUTE, (Integer) jMinuteBegin.getSelectedItem());
		return cal.getTime();
	}

	// -----------------------------------------------------------------------------
	private Date getEndData() {
		Calendar cal = new GregorianCalendar();
		cal.setTime(jDateChooserEnd.getDate());
		cal.set(Calendar.HOUR_OF_DAY, (Integer) jHourEnd.getSelectedItem());
		cal.set(Calendar.MINUTE, (Integer) jMinuteEnd.getSelectedItem());
		return cal.getTime();
	}

	// -----------------------------------------------------------------------------
	private void setStartDate(Date startDate) {
		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(startDate);
		jDateChooserBegin.setDate(startDate);
		jHourBegin.setSelectedItem(startCalendar.get(Calendar.HOUR_OF_DAY));
		jMinuteBegin.setSelectedItem(startCalendar.get(Calendar.MINUTE));
	}

	// -----------------------------------------------------------------------------
	private void setEndDate(Date endDate) {
		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(endDate);
		jDateChooserEnd.setDate(endDate);
		jHourEnd.setSelectedItem(endCalendar.get(Calendar.HOUR_OF_DAY));
		jMinuteEnd.setSelectedItem(endCalendar.get(Calendar.MINUTE));
	}

	// -----------------------------------------------------------------------------
	private void setActiveStatus(boolean isActive) {
		jRadioYes.setSelected(isActive);
		jRadioNo.setSelected(!isActive);
	}

	// -----------------------------------------------------------------------------
	/**
	 * Data entry task you want to edit
	 * @param task Data of the task
	 */
	public void setTask(Task task) {
	    setOldTask(task);
	    jTaskName.setText(task.getTitle());
		setStartDate(task.getStartTime());
		setEndDate(task.getEndTime());
		setActiveStatus(task.isActive());
		jSpinner.setValue(task.getRepeatCount());
	}

	// -----------------------------------------------------------------------------
    private void setOldTask(Task oldTask) {
	    this.oldTask = oldTask;
	}
	
    // -----------------------------------------------------------------------------
    /**
     * find out what the task was edited
     * @return the initial view of the task (before editing) 
     */
    public Task getOldTask() {
        return oldTask;
    }
    
	// -----------------------------------------------------------------------------
    /**
     * Returns the final form of the task
     * @return the final edited form of the task (after editing)
     */
	public Task getTask() {
		Task createdTask = new Task();
		createdTask.setTitle      (jTaskName.getText());
		createdTask.setActive     (jRadioYes.isSelected());
        createdTask.setRepeatCount((Integer) jSpinner.getValue());
		createdTask.setStartTime  (Tools.toDateFormat(getStartDate()));
		if((Integer) jSpinner.getValue() > 0)
			createdTask.setEndTime(Tools.toDateFormat(getEndData()));
		return createdTask; 
	}

	// -----------------------------------------------------------------------------
	private class RepeatSpinnerModel implements SpinnerModel {
		List<ChangeListener> listenerList = new LinkedList<ChangeListener>();
		int value = 1;

		@Override
		public void addChangeListener(ChangeListener l) {
			listenerList.add(l);
		}

		@Override
		public Object getNextValue() {
			++value;
			for (ChangeListener listener : listenerList) {
				listener.stateChanged(new ChangeEvent(value));
			}
			return value;
		}

		@Override
		public Object getPreviousValue() {

			if (value > 0)
				--value;
			else
				throw new IllegalArgumentException("Value cann`t be negative");
			for (ChangeListener listener : listenerList) {
				listener.stateChanged(new ChangeEvent(value));
			}
			return value;

		}

		@Override
		public Object getValue() {
			return value;
		}

		@Override
		public void removeChangeListener(ChangeListener l) {
			listenerList.remove(l);
		}

		@Override
		public void setValue(Object value) {
			if ((Integer) value >= 0)
				this.value = (Integer) value;
			else
				throw new IllegalArgumentException("Value cann`t be negative");
			for (ChangeListener listener : listenerList) {
				listener.stateChanged(new ChangeEvent(value));
			}
		}
	}

}
