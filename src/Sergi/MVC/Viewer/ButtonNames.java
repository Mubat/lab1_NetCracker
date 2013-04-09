package Sergi.MVC.Viewer;

/**
 * assembly of all the labels that are used in the program
 * @author Mubat
 *
 */
public enum ButtonNames {
	BUTTON_NAME_ADD_DIALOG  ("<html><body align = center>��������<br>����� ������</body></html>"), 
	BUTTON_NAME_REPALCE		("<html><body align = center>�������������<br/>������</body></html>"), 
	BUTTON_NAME_REMOVE		("������� ������"), 
	BUTTON_NAME_EXIT		("�����"), 
	BUTTON_NAME_FIND		("����� ������"),
	
	BUTTON_NAME_ADD_TASK	("�������� ������"),
	BUTTON_NAME_EDIT_TASK	("�������� ������"),
	BUTTON_NAME_CANCEL_TASK	("������"), 
	BUTTON_NAME_SET_ASIDE	("�������� ������ �� 5 ���"), 
	BUTTON_NAME_DEACTIVATE	("�������������� ������");

	private String buttonName;

	ButtonNames(String buttonName) {
		this.buttonName = buttonName;
	}

	/**
	 * method returns the string value of the button name
	 * @return the string value of the button name
	 */
	public String getTypeValue() {
		return buttonName;
	}
	
	/**
	 * method returns an enum value from its string value of the button name
	 * @param pType string name of the button
	 * @return enum value from its string value of the button name   
	 * @throws RuntimeException unknown Name of the button
	 */
    static public ButtonNames getType(String pType) throws RuntimeException {
        for (ButtonNames type: ButtonNames.values()) {
            if (type.getTypeValue().equals(pType)) {
                return type;
            }
        }
        throw new RuntimeException("unknown Name of the button");
    }


};