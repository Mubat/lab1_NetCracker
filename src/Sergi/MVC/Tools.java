package Sergi.MVC;

import java.awt.Component;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

 /**
  * Class-based tools. A few useful methods.
  * @author Sergienko Oleg
  *
  */
public class Tools {

    /**
     * Displays error with reference to the parent frame.
     * @param frame the frame, which will be assigned an error.
     * @param errorString error message that appears.
     */
    public static void error(Component component, Object errorString) {
        JOptionPane.showMessageDialog(component,
                errorString.toString(), "Ошибка!", JOptionPane.ERROR_MESSAGE);
    }


    /**
     * Displays error without reference to the parent frame.
     * @param errorString The error message that appears.
     */
    public static void error(String errorString) {
        error(null, errorString);
    }
    
    /** 
     * The output data message without reference to frame-parent.
     * @param string Information message here to be displayed.
     */
    public static void info(String string) {
        JOptionPane.showMessageDialog(null, string,"Задача наступила", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * method forms the time without seconds and milliseconds
     * @param date the time you want to convert
     * @return time without seconds and milliseconds
     */
    public static Date toDateFormat(Date date) {
        Calendar newDate = new GregorianCalendar();
        newDate.setTime(date);
        return new Date(date.getTime() - newDate.get(Calendar.SECOND)* 1000 - newDate.get(Calendar.MILLISECOND));
    }
    
    /**
     * method forms the current time without seconds and milliseconds
     * @return current time without seconds and milliseconds
     */
    public static Date toCurentDateFormat() {
        return toDateFormat(Calendar.getInstance().getTime());
    }
    
    
}
