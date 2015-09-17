package lu.snt.timeseries;

import java.util.ArrayList;

/**
 * Created by assaad on 17/09/15.
 */
public class TimeSerie {
    ArrayList<TimePoint> timePoints;

    public TimeSerie(){
        timePoints=new ArrayList<TimePoint>();
    }

    public void addTimePoint(TimePoint tp){
        timePoints.add(tp);
    }

    public ArrayList<TimePoint> getTimePoints(){
        return timePoints;
    }

    public TimePoint[] getTimePointsArray(){
        TimePoint[] res= new TimePoint[timePoints.size()];
        return timePoints.toArray(res);
    }

    public double[][] getMatrix(){
        double[][] res = new double[timePoints.size()][];
        for(int i=0;i<timePoints.size();i++){
            res[i]=timePoints.get(i).getFeatures();
        }
        return res;
    }

    public int getNumberOfPoints(){
        return timePoints.size();
    }

}
