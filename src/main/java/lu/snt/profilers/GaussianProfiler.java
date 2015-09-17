package lu.snt.profilers;

import lu.snt.timeseries.TimePoint;
import lu.snt.timeseries.TimeSerie;
import lu.snt.util.MathUtil;
import lu.snt.util.mlmodels.Gaussian;

import java.util.ArrayList;

/**
 * Created by assaad on 17/09/15.
 */
public class GaussianProfiler extends Profiler {
    Gaussian[] profiles;

    public GaussianProfiler(long timeResolution, long profileDuration) {
        super(timeResolution, profileDuration);
        profiles =new Gaussian[this.totalSlot];
    }


    @Override
    public void trainPoint(TimePoint tp) {
        int time=getSlot(tp);
        profiles[time].train(tp.getFeatures());
    }

    @Override
    public void trainSerie(TimeSerie ts) {
        for(TimePoint tp: ts.getTimePoints()){
            trainPoint(tp);
        }
    }

    @Override
    public double profilePoint(TimePoint tp) {
        int time=getSlot(tp);
        return profiles[time].getProbability(tp.getFeatures());
    }

    public double[][] getAvg(){
        double[][] scores = new double[totalSlot][];
        for(int i=0;i<totalSlot;i++){
            scores[i]=profiles[i].getAverage();
        }
        return scores;
    }

    public double[][] getVariances(double[][] avgs){
        double[][] scores = new double[totalSlot][];
        for(int i=0;i<totalSlot;i++){
            scores[i]=profiles[i].getCovarianceMatrixSmall(avgs[i],true);
        }
        return scores;
    }

    @Override
    public double[] profileSerie(TimeSerie ts) {

        ArrayList<ArrayList<double[]>> dispatcher = new ArrayList<ArrayList<double[]>>();
        for(int i=0;i<totalSlot;i++){
            dispatcher.add(i,new ArrayList<double[]>());
        }

        for(TimePoint tp: ts.getTimePoints()){
            int time=getSlot(tp);
            dispatcher.get(time).add(tp.getFeatures());
        }

        double[] scores = new double[totalSlot];
        for(int i=0;i<totalSlot;i++){
            double[] getProba = profiles[i].getProbabilityArrayList(dispatcher.get(i));
            scores[i]= MathUtil.getAvg(getProba);
        }

        return scores;
    }
}
