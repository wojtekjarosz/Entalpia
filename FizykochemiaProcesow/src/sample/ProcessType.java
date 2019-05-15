package sample;

/**
 * Created by Wojtek on 20.03.2017.
 */
public enum ProcessType {
     EVEN{
        @Override public String toString() {
            return "Równomierna";
        }
    },
    NONE {
        @Override public String toString() {
            return "-";
        }
    },
}
