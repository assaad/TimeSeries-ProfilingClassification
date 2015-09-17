package lu.snt.util.mlmodels;

import org.apache.commons.math4.distribution.MultivariateNormalDistribution;

import java.util.ArrayList;

/**
 * Created by assaad on 17/09/15.
 */
public class Gaussian {
    private double[] min;
    private double[] max;
    private double[] sum;
    private double[] sumSquare;
    private int nb;


    public double[] getAverage(){
        if(nb!=0) {
            int size=sum.length;
            double[] avg= new double[size];
            for(int i=0;i<size;i++){
                avg[i]=sum[i] / nb;
            }
            return avg;
        }
        else {
            return null;
        }
    }

    public void trainArray(double[][] features){
        for(int i=0;i<features.length;i++){
            train(features[i]);
        }
    }

    public void train(double[] features){
        int size=features.length;
        if(nb==0){
            sum=new double[size];
            min=new double[size];
            max=new double[size];
            sumSquare=new double[size*(size+1)/2];
        }
        if(nb==0) {
            for (int i = 0; i < size; i++) {
                min[i]=features[i];
                max[i]=features[i];
                sum[i] += features[i];
            }
        }
        else{
            for (int i = 0; i < size; i++) {
                if(features[i]<min[i]){
                    min[i]=features[i];
                }
                if(features[i]>max[i]){
                    max[i]=features[i];
                }
                sum[i] += features[i];
            }
        }

        int k=0;
        for(int i=0;i<size;i++) {
            for(int j=0;j<=i;j++) {
                sumSquare[k] += features[i]*features[j];
                k++;
            }
        }
        nb++;
    }

    public double[][] getCovarianceMatrix(double[] avg, boolean biasCorrected){
        int dim=sum.length;

        if(nb<2){
            return null;
        }

        double correction=1;
        if(biasCorrected){
            correction=nb;
            correction=correction/(nb-1);
        }

        double[][] cov=new double[dim][dim];
        int k=0;
        for(int i=0;i<dim;i++){
            for(int j=0;j<=i;j++){
                cov[i][j]=(sumSquare[k]/ nb - avg[i] * avg[j])*correction;
                cov[j][i]=cov[i][j];
                k++;
            }
        }
        return cov;
    }

    public double[] getCovarianceMatrixSmall(double[] avg, boolean biasCorrected){
        int dim=sum.length;

        if(nb<2){
            return null;
        }

        double correction=1;
        if(biasCorrected){
            correction=nb;
            correction=correction/(nb-1);
        }

        double[] cov=new double[dim*(dim+1)/2];
        int k=0;
        for(int i=0;i<dim;i++){
            for(int j=0;j<=i;j++){
                cov[k]=(sumSquare[k]/ nb - avg[i] * avg[j])*correction;
                k++;
            }
        }
        return cov;
    }


    public double getProbability(double[] features){
        double[] avg=getAverage();
        double[][] cov = getCovarianceMatrix(getAverage(),true);
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(avg,cov);
        return mnd.density(features);
    }

    public double[] getProbabilityArray(double[][] features){
        double[] avg=getAverage();
        double[][] cov = getCovarianceMatrix(getAverage(), true);
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(avg,cov);
        double[] results=new double[features.length];
        for(int i=0;i<results.length;i++){
            results[i]= mnd.density(features[i]);
        }
        return results;
    }

    public double[] getProbabilityArrayList(ArrayList<double[]> features){
        double[] avg=getAverage();
        double[][] cov = getCovarianceMatrix(getAverage(),true);
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(avg,cov);
        double[] results=new double[features.size()];
        for(int i=0;i<results.length;i++){
            results[i]= mnd.density(features.get(i));
        }
        return results;
    }


}
