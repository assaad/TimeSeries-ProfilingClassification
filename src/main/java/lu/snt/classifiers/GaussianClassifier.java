package lu.snt.classifiers;

import lu.snt.profilers.GaussianProfiler;
import lu.snt.timeseries.TimeSerie;
import lu.snt.util.MathUtil;
import lu.snt.util.SolutionSpace;

import java.util.ArrayList;

/**
 * Created by assaad on 17/09/15.
 */
public class GaussianClassifier extends Classifier {
    GaussianProfiler[] profilers;

    public GaussianClassifier(int numClass, long timeResolution, long profileDuration) {
        super(numClass, timeResolution, profileDuration);
        profilers=new GaussianProfiler[numClass];
        for(int i=0;i<numClass;i++){
            profilers[i]=new GaussianProfiler(timeResolution,profileDuration);
        }
    }

    @Override
    public void train(TimeSerie ts, int classNum) {
        profilers[classNum].trainSerie(ts);
    }

    @Override
    public ArrayList<SolutionSpace> classify(TimeSerie ts) {
        double[] results;
        ArrayList<SolutionSpace> ss=new ArrayList<SolutionSpace>();

        for(int i=0 ;i<numClass;i++){
            results=profilers[i].profileSerie(ts);
            ss.add(new SolutionSpace(i,score(results),false));
        }
        return ss;

    }

    private double score(double[] results) {
        return MathUtil.getAvg(results); //can be dynamic time wrap
    }
}
