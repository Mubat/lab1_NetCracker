package Sergi.MVC.Model;

import java.util.*;
import java.io.Serializable;

/**
* @author Sergienko Oleg
* Klass spiska zadach.
* Hranit i imeet metodi raboti s spiskom ob`ektov
*/
public class ArrayTaskList extends TaskList implements Iterable<Task>, Cloneable,Serializable {

	private static final long serialVersionUID = 1L;
    /**
	 * @uml.property  name="razmerMassivaTask"
	 */
    private int razmerMassivaTask=0;
    /**
	 * @uml.property  name="massivTask"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
    private Task[] massivTask = new Task[100];
//============================================================

    /**
     * Metod dobavleniya massivTask v spisok
     */    
    public void add(Task task) {
        if(task == null)
            throw new NullPointerException();
        if(massivTask[massivTask.length-1]!=null)  {
            Task[] noviyMassiv = new Task[this.size()+50];
            for(int i=0; i<this.size();i++)
                noviyMassiv[i]=massivTask[i];
            massivTask=noviyMassiv;
        }
        massivTask[razmerMassivaTask]=task;
        ++razmerMassivaTask;
    }
//------------------------------------------------------------    
    /**
     * Metod udaleniya zadachi
     */
    public void remove(Task task) {
        if(task == null)
            throw new NullPointerException();
        int numberOfElement=-1; //nomer iskomogo elementa
        for(int i = 0; i < size(); i++) {
            if(massivTask[i].equals(task))
                numberOfElement = i;
        }
        if(numberOfElement != -1) {
            Task[] temp = new Task[this.size()-1];
            for(int i = 0; i < this.size(); i++) {
                if(i < numberOfElement)
                    temp[i] = massivTask[i];
                if(i > numberOfElement)
                    temp[i-1] = massivTask[i];
            }
            massivTask = temp;
            --razmerMassivaTask;
        }
        
    }
//------------------------------------------------------------    
    /**
    * Metod vozvrashet kolichestvo zadach v tekushem spiske
    */
    public int size()  {
        return razmerMassivaTask;
    }
//------------------------------------------------------------ 
    /**
    * Vozvrashaet zadachu po zadannomy nomeru
    */
    public Task getTask(int index) {
        if(index < 0 || index >= size())
            throw new IndexOutOfBoundsException();
        return massivTask[index];
    }
//------------------------------------------------------------ 
    @Override
    public Object clone() {
            ArrayTaskList cloned = (ArrayTaskList)super.clone();
            Task[] massivCloned = new Task[this.size()];
            for(int i = 0; i < this.size(); i++)
                massivCloned[i] = massivTask[i];
            cloned.massivTask = massivCloned;
            return cloned;
    }
//------------------------------------------------------------ 
    public Iterator<Task> iterator() {
        return new ArrayListForIterator(this);
    }
//------------------------------------------------------------ 
	/**
	 * Iterator dlya ArrayTaskList
	 */
    public class ArrayListForIterator implements Iterator<Task> {
        
        /**
		 * @uml.property  name="arrayList"
		 * @uml.associationEnd  
		 */
        private ArrayTaskList arrayList;
        private int current;
        private int lastCalled;
        
        public ArrayListForIterator (ArrayTaskList value) {
            arrayList = value;
            current = -1; 
            lastCalled = -1;
        }
        
        public boolean hasNext() {
            if( current < arrayList.size() - 1)
                return true;
            return false;
        }
        
        public Task next() {
            if(!hasNext())
                throw new NoSuchElementException();
            lastCalled = -1;
            return arrayList.massivTask[++current];
        }
        
        public void remove() {
            if(current < 0 || lastCalled == current + 1)
                throw new IllegalStateException();
            lastCalled = current;
            arrayList.remove(arrayList.massivTask[current--]);
        }
    }
}