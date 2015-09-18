package lu.snt.smartGrid;

import lu.snt.classifiers.EuclideanClassifier;
import lu.snt.classifiers.GaussianClassifier;
import lu.snt.timeseries.TimeSerie;
import lu.snt.util.ExcelLoader;
import lu.snt.util.SolutionSpace;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by assaad on 17/09/15.
 */
public class TestSmartGrid {
    public static void main(String[] arg){
        String dir="/Users/assaad/work/github/data/consumption/";
        HashMap<String,TimeSerie> smartmeters = ExcelLoader.load(dir);
        int numOfUser=smartmeters.size();
        System.out.println("Loaded measures for "+numOfUser+" users");
        HashMap<String,Integer> dictionary=new HashMap<String,Integer>();

        EuclideanClassifier gc = new EuclideanClassifier(numOfUser,15*60000,24*3600000);

        int user=0;
        for(String k: smartmeters.keySet()) {
            if(user==10){
                int x=0;
            }
            dictionary.put(k,user);
            gc.train(smartmeters.get(k), user);
            user++;
        }
        System.out.println("Trained completed");






        dir="/Users/assaad/work/github/data/validation/";
        HashMap<String,TimeSerie> toguess = ExcelLoader.load(dir);
       // HashMap<String,TimeSerie> toguess =smartmeters;
        numOfUser=toguess.size();
        System.out.println("Loaded measures for "+numOfUser+" users");




        //   for(int maxPoss=5;maxPoss<100;maxPoss++) {

        int maxPossibility=10;
        int total=0;
        int guess=0;
        int noguess=0;

        for (String k : toguess.keySet()) {
            int id=dictionary.get(k);
            ArrayList<SolutionSpace> ss= gc.classify(toguess.get(k));




            if (SolutionSpace.contain(ss, id, maxPossibility)) {
                // System.out.println("Guessed " + k + " Exactly!");
                guess++;
                total++;
            } else {
                //System.out.println("Wrong guess, Right value is " + k);
                noguess++;
                total++;
            }
        }


        double acc = 100.0 * guess / total;
        System.out.print("Possibilities: "+maxPossibility+" Right guesses: " + guess + " wrong guesses: " + noguess + " out of " + total);
        System.out.println(" Accuracy " + acc + " %");
        //}


    }

}
