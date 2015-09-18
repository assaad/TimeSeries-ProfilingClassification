package lu.snt.profilers;

import lu.snt.timeseries.TimePoint;
import lu.snt.timeseries.TimeSerie;
import lu.snt.util.MathUtil;
import lu.snt.util.mlmodels.GaussianMatrix;

/**
 * Created by assaad on 17/09/15.
 */
public class EuclideanProfiler extends Profiler {

    private int id;

    GaussianMatrix[] profiles;

    public EuclideanProfiler(int id, long timeResolution, long profileDuration) {
        super(timeResolution, profileDuration);
        this.id=id;
        profiles =new GaussianMatrix[this.totalSlot];
        for(int i=0;i<totalSlot;i++){
            profiles[i]=new GaussianMatrix();
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
            scores[i]=profiles[i].getCovarianceMatrixSmall(avgs[i], true);
        }
        return scores;
    }

    public double[][] getMin(){
        double[][] scores = new double[totalSlot][];
        for(int i=0;i<totalSlot;i++){
            scores[i]=profiles[i].getMin();
        }
        return scores;
    }


    public double[][] getMax(){
        double[][] scores = new double[totalSlot][];
        for(int i=0;i<totalSlot;i++){
            scores[i]=profiles[i].getMax();
        }
        return scores;
    }


    @Override
    public double[][] profileSerie(TimeSerie ts) {
        EuclideanProfiler ep= new EuclideanProfiler(-1,timeResolution, profileDuration);
        ep.trainSerie(ts);
        double[][] avg= ep.getAvg();
        double[][] min=ep.getMin();
        double[][] max=ep.getMax();

        double[][] thisavg=getAvg();
        double[][] thismin=getMin();
        double[][] thismax=getMax();


        double[][] scores = new double[totalSlot][1];
        for(int i=0;i<totalSlot;i++){
            scores[i][0]= MathUtil.euclidean(avg[i],thisavg[i]);
          //  scores[i][1]= MathUtil.range(min[i],max[i],thismin[i],thismax[i]);
        }
        return scores;
    }
}
