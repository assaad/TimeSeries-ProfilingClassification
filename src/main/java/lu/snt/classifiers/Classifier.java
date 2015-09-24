package lu.snt.classifiers;

import lu.snt.timeseries.TimePoint;
import lu.snt.timeseries.TimeSerie;
import lu.snt.util.SolutionSpace;
import lu.snt.util.TimeUtil;

import java.util.ArrayList;

/**
 * Created by assaad on 17/09/15.
 */
public abstract class Classifier {
    int numClass;
    protected long timeResolution;
    protected long profileDuration;

    public Classifier(int numClass, long timeResolution, long profileDuration){
        this.numClass=numClass;
        this.timeResolution=timeResolution;
        this.profileDuration=profileDuration;
    }

    public abstract void train(TimeSerie ts, int classNum);

    public abstract ArrayList<SolutionSpace> classify(TimeSerie ts);

    protected int getSlot(TimePoint tp){
        return TimeUtil.getIntTime(tp.getTime(), timeResolution, profileDuration);
    }

}
