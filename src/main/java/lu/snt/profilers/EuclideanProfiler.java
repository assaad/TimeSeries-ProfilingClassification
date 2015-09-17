package lu.snt.profilers;

import lu.snt.timeseries.TimePoint;
import lu.snt.timeseries.TimeSerie;
import lu.snt.util.MathUtil;
import lu.snt.util.mlmodels.Gaussian;

import java.util.ArrayList;

/**
 * Created by assaad on 17/09/15.
 */
public class EuclideanProfiler extends Profiler {

    private int id;

    Gaussian[] profiles;

    public EuclideanProfiler(int id, long timeResolution, long profileDuration) {
        super(timeResolution, profileDuration);
        this.id=id;
        profiles =new Gaussian[this.totalSlot];
        for(int i=0;i<totalSlot;i++){
            profiles[i]=new Gaussian();
        }
    }


    @Override
    public void trainPoint(TimePoint tp) {
        int time=getSlot(tp);
        profiles[time].train(tp.getFeatures());
    }

    @Override
    public void trainSerie(TimeSerie ts) {
        if(ts.getNumberOfPoints()==0){
            return;
        }
        for(TimePoint tp: ts.getTimePoints()){
            trainPoint(tp);
        }
    }

    @Override
    public double profilePoint(TimePoint tp) {
        int time=getSlot(tp);
        return profiles[time].getDistance(tp.getFeatures());
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
        EuclideanProfiler ep= new EuclideanProfiler(-1,timeResolution, profileDuration);
        ep.trainSerie(ts);
        double[][] avg= ep.getAvg();
        double[][] thisavg=getAvg();

        double[] scores = new double[totalSlot];
        for(int i=0;i<totalSlot;i++){
            scores[i]= MathUtil.euclidean(avg[i],thisavg[i]);
        }
        return scores;
    }
}
