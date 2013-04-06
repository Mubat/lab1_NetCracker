package Sergi.MVC;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    
    public static Date toDateFormat(Date date) {
        Calendar newDate = new GregorianCalendar();
        newDate.setTime(date);
        return new Date(date.getTime() - newDate.get(Calendar.SECOND) - newDate.get(Calendar.MILLISECOND));
    }
    
    public static Date toCurentDateFormat() {
        return toDateFormat(currentTime());
    }
    
    public static Date currentTime() {
        return new Date();
    }
}
