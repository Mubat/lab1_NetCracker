package Sergi.MVC.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Sergi.MVC.Model.Task;

public class ActionListenerTM implements ActionListener {
	private Object frame;
	private int id;
	private ActionListener parentListener;
//    private Task task;

	public ActionListenerTM(Object frame, int id) {
		this.frame  = frame;
		this.id 	= id;
	}

	public ActionListenerTM(Object frame, int id, ActionListener event) {
		this.frame	= frame;
		this.id 	= id;
		this.parentListener = event;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ActionEvent event = new ActionEvent(frame, id, e.getActionCommand());
		parentListener.actionPerformed(event);
	}

	/**
	 * @return the listener
	 */
	public ActionListener getParentListener() {
		return parentListener;
	}

	/**
	 * @param listener
	 *            the listener to set
	 */
	public ActionListener setParentListener(ActionListener listener) {
		this.parentListener = listener;
		return this;
	}
	
//	public ActionListenerTM setTask(Task task) {
//	    this.task = task;
//	    return this;
//	}
//	
//	public Task getTask(Task task) {
//	    return task;
//	}
}
