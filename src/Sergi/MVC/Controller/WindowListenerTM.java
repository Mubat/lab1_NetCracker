package Sergi.MVC.Controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowListenerTM extends WindowAdapter {
    
    private Controller controller;

    public WindowListenerTM(Controller controller) {
        this.controller = controller;
    }
    
    public void windowClosing(WindowEvent event) {
        try {
            controller.storeTasksAndExit();
        } catch (ModelException e) {
            Controller.error(e.toString());
        }
        event.getWindow().dispose();
    }
    
}
