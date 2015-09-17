package lu.snt.profilers;

import lu.snt.timeseries.TimePoint;
import lu.snt.timeseries.TimeSerie;
import lu.snt.util.TimeUtil;

/**
 * Created by assaad on 17/09/15.
 */
public abstract class Profiler {
    protected long timeResolution;
    protected long profileDuration;
    protected int totalSlot;

    public Profiler(long timeResolution, long profileDuration){
        this.profileDuration=profileDuration;
        this.timeResolution=timeResolution;
        if (timeResolution<=0){
            timeResolution=1;
        }
        if(profileDuration<timeResolution){
            profileDuration=timeResolution;
        }
        totalSlot=(int) (profileDuration/timeResolution);
    }

    protected int getSlot(TimePoint tp){
        return TimeUtil.getIntTime(tp.getTime(),timeResolution,profileDuration);
    }


    abstract public void trainPoint(TimePoint tp);
    abstract public void trainSerie(TimeSerie ts);

    abstract public double profilePoint(TimePoint tp);

    abstract public double[] profileSerie(TimeSerie ts);

}
