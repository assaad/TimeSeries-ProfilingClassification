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

    public static double euclidean(double[] features1, double[] features2) {
        double d=0;
        if(features1==null || features2==null){
            return 0;
        }
        for(int i=0;i<features1.length;i++){
            d+=(features1[i]-features2[i])*(features1[i]-features2[i]);
        }
        return Math.sqrt(d);
    }
}
