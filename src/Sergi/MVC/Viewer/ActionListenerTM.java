package Sergi.MVC.Viewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;

public class ActionListenerTM implements ActionListener {
	private Object frame;
	private int id;
	private EventListener parentListener;

	public ActionListenerTM(Object frame, int id) {
		this.frame = frame;
		this.id = id;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ActionEvent event = new ActionEvent(frame, id, e.getActionCommand());
		((ActionListener) parentListener).actionPerformed(event);
	}

	/**
	 * @return the listener
	 */
	public EventListener getParentListener() {
		return parentListener;
	}

	/**
	 * @param listener
	 *            the listener to set
	 */
	public ActionListener setParentListener(EventListener listener) {
		this.parentListener = listener;
		return this;
	}
}
