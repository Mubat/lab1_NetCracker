package Sergi.MVC.Model;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.Serializable;

/**
 * @author Sergienko Oleg Danniy klass opisivaet zadachu kotoraya imeet opisanie
 *         i vramya opovesheniya o ney.
 */
public class Task implements Cloneable, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Sozdet object bez povtoreniya
	 */
	public Task(String title, Date time) {
		if (title == null)
			throw new IllegalArgumentException();
		if (time == null)
			throw new NullPointerException(
					"End time can not be less than can not be negative");
		itsTitle = title;
		itsStart = time;
		itsEnd = time;
		itsRepeat = 0;
		itsActive = false;
	}

	/**
	 * Konstructor (zadacha iznachalno neaktivna)
	 */
	public Task(String title, Date start, Date end, int repeat) {
		if (title == null)
			throw new IllegalArgumentException();
		if (start == null || end == null || repeat < 0)
			throw new NullPointerException();
		if (start.after(end))
			throw new IllegalArgumentException(
					"End time can not be less than can not be negative");
		itsTitle = title;

		itsStart.setTime(start.getTime());

		itsEnd.setTime(end.getTime());

		itsRepeat = repeat;
		itsActive = false;
	}

	public Task() {
		itsTitle = "";
		itsStart = new Date();
		itsEnd = new Date();
		itsRepeat = 0;
		setActive(false);
	}

	/**
	 * Polucheniya zagolovka zadachi
	 */
	public String getTitle() {
		return itsTitle;
	}

	/**
	 * Ustanovka zagolovka zadachi
	 */
	public void setTitle(String title) {
		if (title == null)
			throw new NullPointerException();
		itsTitle = title;
	}

	/**
	 * Proverka statusa zadachi
	 */
	public boolean isActive() {
		return itsActive;
	}

	/**
	 * Ustanovka statusa zadachi
	 */
	public void setActive(boolean active) {
		itsActive = active;
	}

	/**
	 * Ustanovka vremeni dlya edinarazovoy zadachi
	 */
	public void setTime(Date time) {
		if (time.getTime() < 0)
			throw new NullPointerException();
		itsStart.setTime(time.getTime());
		if (itsRepeat != 0) {
			itsEnd.setTime(time.getTime());
			itsRepeat = 0;
		}
	}

	/**
	 * Ustanovka vremeni dlya povtoryausheysya zadachi
	 */
	public void setTime(Date start, Date end, int repeat) {
		if (start == null || end == null || repeat < 0)
			throw new NullPointerException();
		if (!start.before(end))
			throw new IllegalArgumentException();
		itsStart = start;
		// itsEnd.setTime(end.getTime());
		itsEnd = end;
		itsRepeat = repeat;
	}
	
	public void setStartTime(Date startTime) {
		itsStart = startTime;
	}
	
	public void setEndTime(Date endTime) {
		itsEnd = endTime;
	}

	/**
	 * Vremya pervogo ili edinstvennogo opovesheniya
	 */
	public Date getTime() {
		return itsStart;
	}

	/**
	 * Metod zadaet vremya nachala zadachi
	 */
	public Date getStartTime() {
		return itsStart;
	}

	/**
	 * Metod zadaet vremya konca zadachi. Esli zadacha ne povtoryaetsya, to
	 * prosto vremya sobitiya
	 */
	public Date getEndTime() {
		if (itsRepeat == 0)
			return itsStart;
		else
			return itsEnd;
	}

	/**
	 * Metod zadaet interval megdu vipolneniyami zadachi. Esli zadacha ne
	 * povtoryaetsya, to prosto 0
	 */
	public int getRepeatCount() {
		if (itsRepeat != 0)
			return itsRepeat;
		else
			return 0;
	}

	/**
	 * Metod rpoveryaet povtoryaetsya li sobitie.
	 */
	public boolean isRepeated() {
		return itsRepeat == 0 ? false : true;
	}
	

	public void setRepeatCount(int repeatInterval) {
		itsRepeat = repeatInterval;
	}

	/**
	 * Metod vozvrashyaet opisanie zadachi. vozvrashsaet stroki vida:
	 * <ul>
	 * <li>Task У[Title]Ф (inactive) at [Time] - bez povtoreniya</li>
	 * <li>Task У[Title]Ф from [Start] to [End] every [Repeat] s - s povtoreniem
	 * </li>
	 * </ul>
	 */
	public String toString() {
		Locale locale = new Locale("ru");
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm",
				locale);
		StringBuilder taskString = new StringBuilder(itsTitle);

		taskString.append(" (" + (isActive() ? "" : "in") + "active): ");
		if (!isRepeated())
			taskString.append(sdf.format(itsStart));
		else {
			taskString.append(sdf.format(itsStart));
			taskString.append(" - ");
			taskString.append(sdf.format(itsEnd));
			taskString.append(" каждые ");
			taskString.append(itsRepeat);
			taskString.append(" минут(-у, -ы)");
		}
		return taskString.toString();
	}

	/**
	 * Metod vozvrashsaet vremya sleduushego opovesheniya, posle ukazannogo
	 * vremeni. Esli posle ukazannogo vremeni sobitiy bolshe net ili zadacha
	 * neaktivna return null. Esli metod ne aktivniy, to vozvrashaetsya null.
	 * 
	 * @param time
	 *            current time
	 * @return time null, if there is not events after this time
	 * @throw IllegalArgumentException if param time < 0
	 */
	public Date nextTimeAfter(Date time) {
		if (time == null)
			throw new IllegalArgumentException();
		if (itsRepeat == 0 && time.before(itsStart))
			return itsStart;
		else if (itsRepeat != 0 && time.before(itsEnd)) {
			long itsTime = itsStart.getTime();
			for (; itsTime <= itsEnd.getTime(); itsTime += itsRepeat * 60 * 1000)
				if (time.getTime() < itsTime)
					return new Date(itsTime);
		}
		return null;
	}

	/**
	 * Metod vozvrashaet samuyu bligayshuyu datu k arguentu. Esli poslednyaya
	 * vozmognaya data uge proshla, vozvrashaetsya poslednyaya vozmognaya data.
	 * 
	 * @param time 
	 * @return bligayshaya vozmognaya data
	 */
	public Date lastTimeBefore(Date time) {
		if (time == null)
			throw new IllegalArgumentException();
		if(!isRepeated())
			return itsStart;
		
		if (time.after(itsEnd))
			return itsEnd;
		else if (time.after(itsStart)) {
			long itsTime = itsStart.getTime();
			while(time.getTime() > itsTime) {
				itsTime += itsRepeat * 60 * 1000;
			}
			return new Date(itsTime - itsRepeat * 60 * 1000);
		}
		return null;
	}

	/**
	 * Pereopredelenie metoda super.clone()
	 * 
	 * @throw Error if the class was not cloned
	 * @return cloned object
	 */
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error();
		}
	}

	/**
	 * Compare two tasks
	 * 
	 * @return thrue if this lists are equals, or false if it is not
	 */
	@Override
	public boolean equals(Object target) {
		if (target != null && target.getClass() == this.getClass()) {
			Task temp = (Task) target;
			if (temp.getTitle().equals(this.getTitle())
					&& this.isActive() == temp.isActive()
					&& this.getStartTime().compareTo(temp.getStartTime()) == 0
					&& this.getEndTime().compareTo(temp.getEndTime()) == 0
					&& this.getRepeatCount() == temp.getRepeatCount())
				return true;
		}
		return false;
	}

	/**
	 * Create the hash code of this object
	 * 
	 * @return the hash code
	 */
	@Override
	public int hashCode() {
		return itsStart.hashCode() ^ itsEnd.hashCode() ^ (itsActive ? 1 : 0)
				^ itsTitle.hashCode();
	}

	/**
	 * @uml.property name="itsTitle"
	 */
	private String itsTitle;
	/**
	 * @uml.property name="itsActive"
	 */
	private boolean itsActive;
	/**
	 * @uml.property name="itsStart"
	 */
	private Date itsStart = new Date();
	/**
	 * @uml.property name="itsEnd"
	 */
	private Date itsEnd = new Date();
	/**
	 * @uml.property name="itsRepeat"
	 */
	private int itsRepeat = 0;

}
