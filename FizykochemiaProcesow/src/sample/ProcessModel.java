package sample;


import javafx.beans.property.*;

/**
 * Created by Wojtek on 20.03.2017.
 */
public class ProcessModel {

    private final IntegerProperty tp;
    private final IntegerProperty tk;
    private final DoubleProperty ec;
    private final ObjectProperty<ProcessType> processType;

    public ProcessModel() {
            this(0,0,0, ProcessType.NONE);
    }


    public ProcessModel(int tp, int tk, double ec, ProcessType processType) {
        this.tp = new SimpleIntegerProperty(tp);
        this.tk = new SimpleIntegerProperty(tk);
        this.ec = new SimpleDoubleProperty(ec);
        this.processType = new SimpleObjectProperty<ProcessType>(processType) ;
    }

    public int getTp() {
        return tp.get();
    }

    public IntegerProperty tpProperty() {
        return tp;
    }

    public void setTp(int tp) {
        this.tp.set(tp);
    }

    public int getTk() {
        return tk.get();
    }

    public IntegerProperty tkProperty() {
        return tk;
    }

    public void setTk(int tk) {
        this.tk.set(tk);
    }

    public ProcessType getProcessType() {
        return processType.get();
    }

    public ObjectProperty processTypeProperty() {
        return processType;
    }

    public void setProcessType(ProcessType processType) {
        this.processType.set(processType);
    }

    public double getEc() {
        return ec.get();
    }

    public DoubleProperty ecProperty() {
        return ec;
    }

    public void setEc(double ec) {
        this.ec.set(ec);
    }
    /*public ProcessType getProcessType() {

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
    }*/


}
