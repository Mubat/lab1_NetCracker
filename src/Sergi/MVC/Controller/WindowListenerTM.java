package Sergi.MVC.Controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Eyes that implements the response to user actions with respect to the main window
 * @author Mubat
 *
 */
public class WindowListenerTM extends WindowAdapter {
    
    private Controller controller;

    public WindowListenerTM(Controller controller) {
        this.controller = controller;
    }
    
    @Override
    public void windowClosing(WindowEvent event) {
        controller.storeTasksAndExit();
        event.getWindow().dispose();
    }
    
}
