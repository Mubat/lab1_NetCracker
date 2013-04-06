package Sergi.MVC.Model;


public class ModelException extends Exception {

    Exception e = new Exception();
    String string = "";
    private static final long serialVersionUID = 4360693429330353093L;

    public ModelException(Exception e, String string) {
        this.e = e;
        this.string = string;
    }
    
    public ModelException(Exception e) {
        this.e = e;
    }
       
    public ModelException(String errorMessage) {
        string = errorMessage;
    }

    private String getExceptionDetals(Exception e) {
        StringBuilder str = new StringBuilder(e.getMessage() + "\n");
        for(int i = 0; i < e.getStackTrace().length; i++)
            str.append("    " + e.getStackTrace()[i] + "\n");
        return str.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if(string != "")
            return "Îøèáêà! " + string;
        return getExceptionDetals(e);
    }
    
    
}
