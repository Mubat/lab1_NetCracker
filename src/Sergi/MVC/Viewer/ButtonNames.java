package Sergi.MVC.Viewer;

public enum ButtonNames {
	BUTTON_NAME_ADD_DIALOG(
			"<html><body align = center>��������<br>����� ������</body></html>"), 
	BUTTON_NAME_REPALCE(
			"<html><body align = center>�������������<br/>������</body></html>"), 
	BUTTON_NAME_REMOVE("������� ������"), 
	BUTTON_NAME_EXIT("�����"), 
	BUTTON_NAME_FIND("����� ������"),
	
	BUTTON_NAME_ADD_TASK("�������� ������"),
	BUTTON_NAME_CANCEL_TASK("������");

	private String buttonName;

	ButtonNames(String buttonName) {
		this.buttonName = buttonName;
	}

	public String getTypeValue() {
		return buttonName;
	}
	
    static public ButtonNames getType(String pType) throws RuntimeException {
        for (ButtonNames type: ButtonNames.values()) {
            if (type.getTypeValue().equals(pType)) {
                return type;
            }
        }
        throw new RuntimeException("unknown type");
    }


};