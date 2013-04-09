package Sergi.MVC.Controller;

public interface MainFrameObserverInterface {
	
    /**
     * The method that will be called when an Observer notification of an event. 
     * @param value the value that is passed to the method
     */
	void update(Object value);

}
