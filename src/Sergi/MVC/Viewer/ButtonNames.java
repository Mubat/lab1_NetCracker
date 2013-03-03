package Sergi.MVC.Viewer;

public enum ButtonNames {
	BUTTON_NAME_ADD_DIALOG(
			"<html><body align = center>Добавить<br>новую задачу</body></html>"), 
	BUTTON_NAME_REPALCE(
			"<html><body align = center>Редактировать<br/>задачу</body></html>"), 
	BUTTON_NAME_REMOVE("Удалить задачу"), 
	BUTTON_NAME_EXIT("Выход"), 
	BUTTON_NAME_FIND("Найти задачу"),
	
	BUTTON_NAME_ADD_TASK("Добавить задачу"),
	BUTTON_NAME_CANCEL_TASK("Отмена");

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