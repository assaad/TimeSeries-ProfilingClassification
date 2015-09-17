package lu.snt.util.mlmodels;

import lu.snt.util.MathUtil;
import lu.snt.util.MultivariateNormalDistribution;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by assaad on 17/09/15.
 */
public class GaussianMatrix {
    private double[] min;
    private double[] max;
    private double[] sum;
    private double[][] sumSquare;
    private int nb;


    public double[] getAverage(){
        if(sum==null){
            return null;
        }
        int size=sum.length;
        double[] avg= new double[size];
        if(nb!=0) {
            for(int i=0;i<size;i++){
                avg[i]=sum[i] / nb;
            }
        }
        return avg;
    }

    public void trainArray(double[][] features){
        if (features.length==0){
            return;
        }
        for(int i=0;i<features.length;i++){
            linearSubTrain(features[i]);
        }
        try {
            DenseMatrix64F cov = new DenseMatrix64F(sumSquare);
            DenseMatrix64F matA = new DenseMatrix64F(features);
            CommonOps.multAddTransA(1, matA, matA, cov);
            for(int i=0;i<sum.length;i++){
                for(int j=0;j<sum.length;j++){
                    sumSquare[i][j]=cov.get(i,j);
                }
            }
        }
        catch (Exception ex){
            int x=0;
            ex.printStackTrace();
        }
    }


    public void linearSubTrain(double[] features){
        int size=features.length;
        if(nb==0){
            sum=new double[size];
            min=new double[size];
            max=new double[size];
            sumSquare=new double[size][size];
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
        nb++;
    }

    public void train(double[] features){
        linearSubTrain(features);
        for(int i=0;i<features.length;i++){
            for(int j=0;j<=i;j++){
                double d=features[i]*features[j];
                sumSquare[i][j]+=d;
                sumSquare[j][i]+=d;
            }
        }

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
                cov[i][j]=(sumSquare[i][j]/ nb - avg[i] * avg[j])*correction;
                cov[j][i]=cov[i][j];
                k++;
            }
        }


        return cov;
    }


    public double getDistance(double[] features){
        double[] avg=getAverage();
        return MathUtil.euclidean(features,avg);
    }


    public ArrayList<Integer> getDimensions(){
        ArrayList<Integer> dim=new ArrayList<Integer>();
        for(int i=0;i<max.length;i++){
            if(max[i]!=min[i]){
                dim.add(i);
            }
        }
        return dim;
    }


    public double getProbability(double[] features){
        if(nb==0){
            return 0; //or return 1
        }
        double[] avg=getAverage();
        if(avg==null){
            return 0;
        }
        double[][] cov = getCovarianceMatrix(getAverage(), true);
        ArrayList<Integer> dims=getDimensions();
        if(dims.size()==0){
            return 0;
        }
        avg=filter(avg, dims);
        cov=filterArray(cov, dims);
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(avg,cov);

        features=filter(features,dims);
        return mnd.density(features);
    }

    private double[][] filterArray(double[][] cov, ArrayList<Integer> dims) {
        return cov;
   /*     if(dims.size()==cov.length){
            return cov;
        }
        double[][] result=new double[dims.size()][dims.size()];
        for(int i=0;i<dims.size();i++){
            for(int j=0;j<dims.size();j++){
                result[i][j]=cov[dims.get(i)][dims.get(j)];
            }
        }
        return result;*/
    }

    private double[] filter(double[] features, ArrayList<Integer> dims) {
        return features;
      /*  if(dims.size()==features.length){
            return features;
        }
        double[] result = new double[dims.size()];
        for(int i=0;i<dims.size();i++){
            result[i]=features[dims.get(i)];
        }
        return result;*/
    }

    public double[] getProbabilityArray(double[][] features){
        double[] results=new double[features.length];
        double[] avg=getAverage();
        if(avg==null){
            return results;
        }

        double[][] cov = getCovarianceMatrix(getAverage(), true);
        ArrayList<Integer> dims=getDimensions();
        if(dims.size()==0){
            return results;
        }
        avg=filter(avg, dims);
        cov=filterArray(cov, dims);
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(avg,cov);


        for(int i=0;i<results.length;i++){
            results[i]= mnd.density(filter(features[i],dims));
        }
        return results;
    }


    public double[] getProbabilityArrayList(ArrayList<double[]> features){
        double[] results = new double[features.size()];
        double[] avg=getAverage();
        if(avg==null){
            return results;
        }

        double[][] cov = getCovarianceMatrix(getAverage(), true);

        try {
            ArrayList<Integer> dims=getDimensions();
            if(dims.size()==0){
                return results;
            }
            avg=filter(avg, dims);
            cov=filterArray(cov, dims);
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(avg,cov);

            for (int i = 0; i < results.length; i++) {
                results[i] = mnd.density(filter(features.get(i),dims));
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            //int x=0;
        }

        return results;
    }


    public double[] getCovarianceMatrixSmall(double[] avg, boolean biasCorrected){
        if(sum==null){
            return null;
        }
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
                cov[k]=(sumSquare[i][j]/ nb - avg[i] * avg[j])*correction;
                k++;
            }
        }
        return cov;
    }

    public double[] getDistanceArrayList(ArrayList<double[]> features) {
        double[] results = new double[features.size()];
        double[] avg=getAverage();
        if(avg!=null) {
            for (int i = 0; i < results.length; i++) {
                results[i] = MathUtil.euclidean(features.get(i), avg);
            }
        }
        return results;
    }
}
