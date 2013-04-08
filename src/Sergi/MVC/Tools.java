package Sergi.MVC;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Sergi.MVC.Viewer.MainFrame;

/**
 * Класс-инструментарий. Несколько полезных методов.
 * @author Sergienko Oleg
 *
 */
public class Tools {

    /**
     * Вывод ошибки с привязкой к фрейму-родителю.
     * @param frame фрейм, к которому будет привязано сообщение об ошибке.
     * @param errorString Сообщение об ошибке, которое будет выведено.
     */
    public static void error(JFrame frame, String errorString) {
        MainFrame.showErrorMessage(frame, errorString);
    }

    /**
     * Вывод ошибки без привязки к фрейму-родителю.
     * @param errorString Сообщение об ошибке, которое будет выведено.
     */
    public static void error(String errorString) {
        error(null, errorString);
    }
    
    /** 
     * Вывод информационного сообщения без привязки к фрему-родителю.
     * @param string Информационное собщение, которое нужно вывести.
     */
    public static void info(String string) {
        JOptionPane.showMessageDialog(null, string,"Задача наступила", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static Date toDateFormat(Date date) {
        Calendar newDate = new GregorianCalendar();
        newDate.setTime(date);
        return new Date(date.getTime() - newDate.get(Calendar.SECOND)* 1000 - newDate.get(Calendar.MILLISECOND));
    }
    
    public static Date toCurentDateFormat() {
        return toDateFormat(currentTime());
    }
    
    public static Date currentTime() {
        return new Date();
    }
    
    public static void sysOut(Object outString) {
        System.out.println("[" + toCurentDateFormat() + "]: " + outString.toString());
    }
}
