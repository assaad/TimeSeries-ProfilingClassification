package lu.snt.classifiers;

import lu.snt.timeseries.TimePoint;
import lu.snt.timeseries.TimeSerie;
import lu.snt.util.SolutionSpace;
import lu.snt.util.TimeUtil;
import net.seninp.jmotif.SAXVSMClassifierParams;
import net.seninp.jmotif.sax.NumerosityReductionStrategy;
import net.seninp.jmotif.text.Params;
import net.seninp.jmotif.text.TextProcessor;
import net.seninp.jmotif.text.WordBag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by assaad on 24/09/15.
 */
public class SaxVsmClassifier extends Classifier {

    private Map<String, List<double[]>> trainData;
    private TextProcessor tp;
    private HashMap<String, HashMap<String, Double>> tfidf;

    Params params;

    public SaxVsmClassifier(int numClass, long timeResolution, long profileDuration) {
        super(numClass, timeResolution, profileDuration);
        trainData=new HashMap<String, List<double[]>>(numClass);
        tp = new TextProcessor();
        params = new Params(SAXVSMClassifierParams.SAX_WINDOW_SIZE,
                SAXVSMClassifierParams.SAX_PAA_SIZE, SAXVSMClassifierParams.SAX_ALPHABET_SIZE,
                SAXVSMClassifierParams.SAX_NORM_THRESHOLD, SAXVSMClassifierParams.SAX_NR_STRATEGY);
    }

    public void setParam(int windowSize, int paaSize, int alphabetSize, double nThreshold,
                         NumerosityReductionStrategy nrStartegy){
        params=new Params(windowSize,paaSize,alphabetSize,nThreshold,nrStartegy);
    }


    private  ArrayList<double[]> convert(TimeSerie ts) {
        ArrayList<double[]> days = new ArrayList<double[]>();
        int totalSlot = (int) (profileDuration / timeResolution);
        double[] points = null;
        int count = 0;
        for (TimePoint tp : ts.getTimePoints()) {
            if (count % totalSlot == 0) {
                if (points != null) {
                    days.add(points);
                }
                points = new double[totalSlot];
                count = 0;
            }
            points[count] = tp.getFeatures()[0];
            count++;
        }

        return days;
    }

    @Override
    public void train(TimeSerie ts, int classNum) {
        StringBuilder sb = new StringBuilder();
        sb.append(classNum);
        String strI = sb.toString();
        trainData.put(strI,convert(ts));
    }

    @Override
    public ArrayList<SolutionSpace> classify(TimeSerie ts) {
        // making training bags collection
        SolutionSpace[] ss = new SolutionSpace[numClass];
        try {
            if(tfidf==null) {
                List<WordBag> bags = tp.labeledSeries2WordBags(trainData, params);
                tfidf = tp.computeTFIDF(bags);
            }
            for(int i=0 ;i<numClass;i++){
                ss[i] = new SolutionSpace(i,0,false);
            }
            ArrayList<double[]> conv=convert(ts);

            for (double[] aConv : conv) {
                WordBag testBag = tp.seriesToWordBag("test", aConv, params);
                for (int j = 0; j < numClass; j++) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(j);
                    ss[j].addScore(tp.cosineSimilarity(testBag, tfidf.get(sb.toString())));
                }
            }


            ArrayList<SolutionSpace> res = new ArrayList<SolutionSpace>();
            for(int i=0 ;i<numClass;i++){
                res.add(ss[i]);
            }
            return res;

        }
        catch (Exception ex){
            ex.printStackTrace();

        }
        return null;
    }
}
