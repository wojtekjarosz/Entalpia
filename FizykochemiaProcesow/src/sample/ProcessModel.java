package sample;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Wojtek on 20.03.2017.
 */
public class ProcessModel {

    private final StringProperty tp;
    private final StringProperty tk;
    private final StringProperty ec;
    private ProcessType processType;

    public ProcessModel() {
        this(null, null, null);
    }

    public ProcessModel(String tp, String tk, String ec) {
        this.tp = new SimpleStringProperty(tp);
        this.tk = new SimpleStringProperty(tk);
        this.ec = new SimpleStringProperty(ec);
        this.processType = ProcessType.EVEN;
    }

    public String getTp() {
        return tp.get();
    }

    public StringProperty tpProperty() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp.set(tp);
    }

    public String getTk() {
        return tk.get();
    }

    public StringProperty tkProperty() {
        return tk;
    }

    public void setTk(String tk) {
        this.tk.set(tk);
    }

    public String getEc() {
        return ec.get();
    }

    public StringProperty ecProperty() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec.set(ec);
    }

    public ProcessType getProcessType() {

        return processType;
    }
    public String getProcessType2() {
        String processType2;
        processType2 = "" + processType;
        return processType2;
    }

    public StringProperty typeProperty(){
        String processType2;
        processType2 = "" + processType;
        return new SimpleStringProperty(processType2);
    }

    public void setProcessType(ProcessType pro){
        this.processType = pro;
    }


}
