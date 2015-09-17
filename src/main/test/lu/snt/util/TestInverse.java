package lu.snt.util;

import lu.snt.util.MultivariateNormalDistribution;

/**
 * Created by assaad on 17/09/15.
 */
public class TestInverse {
    public static void main(String[] arg){
        double[] avg=new double[3];
      /*  double[][] cov = new double[3][3];
        cov[0][0]= 1; cov[0][1]=0; cov[0][2]=0;
        cov[1][0]= 1; cov[1][1]=0; cov[1][2]=0;
        cov[2][0]= 0; cov[2][1]=0; cov[2][2]=5;*/


        double[][] cov = new double[2][2];
        cov[0][0]= 1; cov[0][1]=0;
        cov[1][0]= 0; cov[1][1]=5;

        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(avg,cov);


    }
}
