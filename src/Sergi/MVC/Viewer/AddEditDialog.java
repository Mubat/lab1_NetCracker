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

import Sergi.MVC.Model.Task;

import com.toedter.calendar.JDateChooser;

public class AddEditDialog extends JDialog {

	private static final long serialVersionUID = 1630046389235533533L;

	private static Integer[] hours = new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8,
			9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 };
	private static Integer[] minutes = new Integer[] { 
			 0,  1,  2,  3,  4,  5,  6,  7,  8,  9,  10, 11, 12, 13, 14, 
			 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 
			 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 
			 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60 };

	private static JSpinner jSpinner;// ���������� ����������
	private ButtonGroup jRadioVisible;// ������ ����������� ���������� ������
	private JDateChooser jDateChooserBegin;// ����� ���� ������ ������
	private JDateChooser jDateChooserEnd;// ����� ���� ��������� ������
	private JComboBox jHourBegin;// ����� ������� ������ ������
	private JComboBox jHourEnd;// ����� ������� ��������� ������
	private JTextField jTaskName;// ������������ ������
	private JComboBox jMinuteBegin;
	private JComboBox jMinuteEnd;

	private static JButton jAddButton;// ������ ���������� ����� ������
	private static JButton jCancelButton;// ������ ������

	private JRadioButton jRadioYes;
	private JRadioButton jRadioNo;

	// =============================================================================
	/**
	 * @param owner
	 *            - the owner Dialog from which the dialog is displayed or null
	 *            if this dialog has no owner
	 * @param rootPaneCheckingEnabled
	 *            If true then calls to add and setLayout will be forwarded to
	 *            the contentPane.
	 */
	public AddEditDialog(JFrame owner, boolean rootPaneCheckingEnabled) {
		super(owner, rootPaneCheckingEnabled);
		initComponents();
	}

	// -----------------------------------------------------------------------------
	/**
	 * ���������� ������� ��� ����������/�������������� ������
	 */
	private void initComponents() throws IllegalArgumentException {
		
		this.setLayout(new GridBagLayout());
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// ����������� ������
		this.add(new JLabel("������������ ������", JLabel.RIGHT),
				new GridBagConstraints(0, 0, 1, 1, 0, 0,
						GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));
		jTaskName = new JTextField();
		this.add(jTaskName, new GridBagConstraints(1, 0, 4, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 0, 5, 10), 0, 0));

		// ���� ��� ����� ������� ������ � ��������� ������
		this.add(new JLabel("����", JLabel.RIGHT), new GridBagConstraints(0, 1,
				1, 2, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		getDatePanel();

		// ���������� ����������
		this.add(new JLabel("���������� ����������", JLabel.RIGHT),
				new GridBagConstraints(0, 3, 1, 1, 0, 0,
						GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));
		jSpinner = new JSpinner(new RepeatSpinnerModel());
		this.add(jSpinner, new GridBagConstraints(1, 3, 1, 1, 0, 0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		// ����� ������� ���������� ������: ������� ��� �� ������� ����� ������
		this.add(new JLabel("������:", JLabel.RIGHT), new GridBagConstraints(0,
				4, 1, 1, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(getRadioButtonGroup(), new GridBagConstraints(1,
				4, 3, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		// ������ ����������/�������������� ������ � ������
		jAddButton = new JButton(ButtonNames.BUTTON_NAME_ADD_TASK.getTypeValue());
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
	 * ����� ��� ������������ �������
	 * 
	 * @param evt
	 */
	public void jButtonMouseClicked() {
		this.dispose();
	}

	// -----------------------------------------------------------------------------
	/**
	 * �������� ������ ��� ������ ������ � ��������� �������� ������ ����� ���:
	 * <html>
	 * <table border='1px'>
	 * <tr>
	 * <td><b>������</b></td>
	 * <td><i>����</i></td>
	 * <td><i>�����</i></td>
	 * </tr>
	 * <tr>
	 * <td><b>���������</b></td>
	 * <td><i>����</i></td>
	 * <td><i>�����</i></td>
	 * </tr>
	 * </table>
	 * </html>
	 * 
	 * @param isEDITdialog
	 * 
	 * @return JPanel ������ ������ ���
	 */
	private void getDatePanel() {
		this.add(new JLabel("������"), new GridBagConstraints(1, 1, 1, 1, 0, 0,
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

		this.add(new JLabel("���������"), new GridBagConstraints(1, 2, 1, 1, 0,
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
	 * �������� ������ ����������� ��� ������, �������� �� ������
	 * 
	 * @param isEDITdialog
	 * 
	 * @return JPanel ������ � ������� �����������
	 */
	private Component getRadioButtonGroup() {
		JPanel jVisibilityPanel = new JPanel();
		jVisibilityPanel.setLayout(new GridLayout(1, 2));
		jRadioVisible = new ButtonGroup();
		jRadioYes = new JRadioButton("��������");
		jRadioNo = new JRadioButton("����������");

		jRadioVisible.add(jRadioYes);
		jRadioVisible.add(jRadioNo);
		jVisibilityPanel.add(jRadioYes);
		jVisibilityPanel.add(jRadioNo);

		return jVisibilityPanel;
	}

	// -----------------------------------------------------------------------------
	/**
	 * ����� ���������� ���������� ��� ������
	 */
	public void addListeners(ActionListener listener) {
		jCancelButton.addActionListener(listener);
		jAddButton.addActionListener(listener);
	}

	// -----------------------------------------------------------------------------
	private void setTaskName(String taskName) {
		jTaskName.setText(taskName);
	}

	private String getTaskName() {
		return jTaskName.getText();
	}

	private int getRepeatValue() {
		return (Integer) jSpinner.getValue();
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
	private boolean getActiveStatus() {
		return jRadioYes.isSelected();
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
	private void setRepeatInterval(int repeats) {
		jSpinner.setValue(repeats);
	}

	// -----------------------------------------------------------------------------
	private void setActiveStatus(boolean isActive) {
		jRadioYes.setSelected(isActive);
		jRadioNo.setSelected(!isActive);
	}

	// -----------------------------------------------------------------------------
	public void setTask(Task task) {
		this.setTaskName(task.getTitle());
		this.setStartDate(task.getStartTime());
		this.setEndDate(task.getEndTime());
		this.setActiveStatus(task.isActive());
		this.setRepeatInterval(task.getRepeatCount());
	}

	// -----------------------------------------------------------------------------
	public Task getTask() {
		Task createdTask = new Task();
		createdTask.setTitle(this.getTaskName());
		createdTask.setActive(this.getActiveStatus());
		createdTask.setStartTime(this.getStartDate());
		createdTask.setRepeatCount(this.getRepeatValue());
		if(this.getRepeatValue() > 0)
			createdTask.setEndTime(this.getEndData());
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
