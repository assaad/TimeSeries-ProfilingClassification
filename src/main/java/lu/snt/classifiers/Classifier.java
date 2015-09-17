package lu.snt.classifiers;

import lu.snt.timeseries.TimeSerie;
import lu.snt.util.SolutionSpace;

import java.util.ArrayList;

/**
 * Created by assaad on 17/09/15.
 */
public abstract class Classifier {
    int numClass;
    private long timeResolution;
    private long profileDuration;

    public Classifier(int numClass, long timeResolution, long profileDuration){
        this.numClass=numClass;
        this.timeResolution=timeResolution;
        this.profileDuration=profileDuration;
    }

    public abstract void train(TimeSerie ts, int classNum);

    public abstract ArrayList<SolutionSpace> classify(TimeSerie ts);
}
