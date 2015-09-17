package lu.snt.util;

/**
 * Created by assaad on 17/09/15.
 */
public class MathUtil {
    public static double getAvg(double[] values){
        if(values.length==0){
            return 0;
        }
        double avg=0;
        for(int i=0;i<values.length;i++){
            avg+=values[i];
        }
        return avg/values.length;
    }
}
