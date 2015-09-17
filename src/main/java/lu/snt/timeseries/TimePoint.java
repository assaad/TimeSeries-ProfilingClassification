package lu.snt.timeseries;

/**
 * Created by assaad on 17/09/15.
 */
public class TimePoint {
    private long time;
    private double[] features;

    public TimePoint(long time){
        this.time=time;
    }

    public TimePoint(long time, double[] features){
        this.time=time;
        this.features=features;
    }

    public int getNumFeatures(){
        return features.length;
    }

    public double[] getFeatures() {
        return features;
    }

    public void setFeatures(double[] features){
        this.features=features;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
