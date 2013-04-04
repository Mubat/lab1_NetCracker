package Sergi.MVC;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Sergi.MVC.Viewer.MainFrame;

public class Tools {

    public static void error(JFrame frame, String errorString) {
        MainFrame.showErrorMessage(frame, errorString);
    }

    public static void error(String errorString) {
        error(null, errorString);
    }
    
    public static void info(String string) {
        JOptionPane.showMessageDialog(null, string,"Задача наступила", JOptionPane.INFORMATION_MESSAGE);
    }
}
