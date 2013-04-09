package Sergi.MVC.Model;

import java.util.*;
import java.io.Serializable;

/**
* @author Sergienko Oleg
* Klass, realizuyushiy rabotu so spiskon zadach 
*/
public abstract class TaskList implements Iterable<Task>,Serializable, Cloneable{

	private static final long serialVersionUID = 1L;
//============================================================
    /**
     * Metod dobavleniya massivTask v spisok
     */    
    public abstract void add(Task task);
    
//------------------------------------------------------------    
    /**
     * Metod udaleniya zadachi
     */
    public abstract void remove(Task task);    
    
//------------------------------------------------------------    
    /**
     * Metod vozvrashet kolichestvo zadach v tekushem spiske
     */
    public abstract int size();

//------------------------------------------------------------    
    /**
     * Vozvrashaet zadachu po zadannomy nomeru
     */
    public abstract Task getTask(int index);
        
//------------------------------------------------------------    
    /**
     * Override the clone of Object method 
     */
    @Override
    public Object clone () { 
        try {
            return super.clone();
        }
        catch(CloneNotSupportedException e) {
        System.out.println("ERROR");
            throw new InternalError();
        }
    }
//------------------------------------------------------------    
    @Override 
    public String toString () {
        StringBuilder answer = new StringBuilder(getClass().getSimpleName() + " [");
        int i = 0;
        for (Task task : this ) {
            answer.append(task.toString());
            if(i+1 < size()) answer.append(", ");
            ++i;
        }    
        answer.append("]");
        return answer.toString();
    }
//------------------------------------------------------------     
    @Override
    public int hashCode() {
        int res = 0;
        for(Task task : this) {
            res ^=task.hashCode();
        }
        return res;
    }
//------------------------------------------------------------     
    @Override
    public boolean equals (Object target) {
        if(target instanceof TaskList) {
            TaskList taskList = (TaskList) target;
            if(target.getClass() != this.getClass() ||
               this.hashCode() != taskList.hashCode())
                    return false;
            else {
                Iterator<Task> iterator = taskList.iterator();
                for(Task task : this) {
                    if(iterator.hasNext() && 
                       !task.equals( iterator.next() )) {
                            return false;
                        }
                }
            }
            return true;
        }
        else 
            return false;
    }
}