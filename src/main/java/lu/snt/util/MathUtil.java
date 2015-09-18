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

    public static double normalDist (double[] features, double[] avg, double[][] cov){
return 0;
    }

    public static double similar(double[] doubles, double[] doubles1,double eps) {
        if(doubles==null||doubles1==null){
            return 00.0;
        }
        double res=0;
        for(int i=0;i<doubles.length;i++){
            if(Math.abs(doubles[i]-doubles1[i])<=eps){
                res+=1;
            }
        }
        return res;
    }

    public static double getAvg(double[][] values){
        if(values.length==0){
            return 0;
        }
        double avg=0;
        for(int i=0;i<values.length;i++){
            for(int j=0;j<values[i].length;j++) {
                avg += values[i][j];
            }
        }
        return avg/values.length;
    }

    public static double similar(double[][] doubles, double[][] doubles1,double eps) {
        double res=0;
        for(int i=0;i<doubles.length;i++){
            for(int j=0;j<doubles[i].length;j++) {
                if (Math.abs(doubles[i][j] - doubles1[i][j]) <= eps) {
                    res += 1;
                }
            }
        }
        return res;
    }

    public static double range(double[] min, double[] max, double[] min2, double[] max2) {
        if(min==null||max==null||min2==null||max2==null){
            return 0;
        }
        double d=0;
        for(int i=0;i<min.length;i++){
            d+=Math.abs(max[i]-max2[i])+Math.abs(min[i]-min2[i]);
        }
        return d;
    }
}
