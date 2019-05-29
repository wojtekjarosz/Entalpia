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

    EVERY_SECOND_HAS_MORE {
        @Override public String toString() {
            return "Co drugi";
        }
    },
    NONE {
        @Override public String toString() {
            return "-";
        }
    },
    PARABOLA{
         @Override public  String toString() { return "Parabola";}
    }

}
