package lu.snt.gmm;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by assaad on 11/02/16.
 */
public class MixtureModel extends Gaussian{

    private int level;
    private Configration config;

   // private Component overallProfile;
    private ArrayList<Component> dataset;


    //root mixture
    public MixtureModel(Configration config){
        super();
        this.config=config;
        this.level=0;
        dataset= new ArrayList<Component>(config.compression);
    }



    @Override
    public void feed(double[] feat) {
        if(dataset!=null) {
            for (Component c : dataset) {
                if (checkInside(feat, config.err)) {
                    c.feed(feat);
                    super.feed(feat);
                    return;
                }
            }
            dataset.add(new Gaussian(feat));

            if (dataset.size() == config.compression) {
                compress();
            }
        }
        super.feed(feat);
    }


    @Override
    public boolean checkInside(double[] features, double[] err) {
         return super.checkInside(features,err);
    }

    @Override
    public double[] getAvg() {
        return super.getAvg();
    }

    @Override
    public double[][] getCovariance(double[] means) {
        return super.getCovariance(means);
    }


    public double[] calculateArray(double[][] features, ProgressReport reporting){
        if(super.getTotal()==0){
            return new double[features.length];
        }
        double[] res;

        if(this.level<config.calcLevel && dataset!=null) {
            res = new double[features.length];
            int counter = 0;
            for (Component c : dataset) {
                if (this.level == 0) {
                    reporting.updateProgress(counter * (1.0 / dataset.size()));
                }
                double[] temp;
                if (c instanceof MixtureModel) {
                    MixtureModel tt = (MixtureModel) c;
                    temp = tt.calculateArray(features, reporting);
                } else {
                    temp = c.evaluateArray(features, config.err);
                }
                if (reporting.isCancelled()) {
                    return null;
                }
                for (int i = 0; i < features.length; i++) {
                    res[i] += temp[i] * c.getWeight() / this.getWeight();
                }
                counter++;
            }
        }
        else {
            res=super.evaluateArray(features,config.err);
        }

        return res;
    }

    private void compress(){
        double[][] centroids = new double[config.capacity][getFeatures()];
        int[] categories=new int[dataset.size()];
        int[] totals= new int[config.capacity];
        double[] min = this.getMin();
        double[] max= this.getMax();
        double[] err=config.err;

        for(int i=0;i<config.capacity;i++){
            double[] avg=dataset.get(i).getAvg();
            System.arraycopy(avg, 0, centroids[i], 0, getFeatures());
        }

        //Loop through kmeans
        for(int iter=0;iter<config.kmeanIterations;iter++){

            for(int i=0;i<totals.length;i++){
                totals[i]=0;
            }
            //Assign categories
            for(int i=0;i<dataset.size();i++){
                categories[i]=calculateCategory(dataset.get(i),centroids,min,max,err);
                totals[categories[i]]++;
            }

            //Clear centroids
            for(int i=0;i<centroids.length;i++){
                for(int j=0;j<getFeatures();j++){
                    centroids[i][j]=0;
                }
            }

            //Add up centroids
            for(int i=0;i<dataset.size();i++){
                double[] avg=dataset.get(i).getAvg();
                for(int j=0;j<getFeatures();j++){
                    centroids[categories[i]][j]+=avg[j];
                }
            }

            for(int i=0;i<centroids.length;i++){
                if(totals[i]!=0){
                    for(int j=0;j<getFeatures();j++) {
                        centroids[i][j] = centroids[i][j] / totals[i];
                    }
                }
                else {
                    Random rand=new Random();
                    double[] avg=dataset.get(rand.nextInt(dataset.size())).getAvg();
                    System.arraycopy(avg, 0, centroids[i], 0, getFeatures());
                  //  System.out.println("centroid 0, should be impossible");
                }
            }
        }


        ArrayList< ArrayList<Component>>temp = new ArrayList<ArrayList<Component>>();

        for(int i=0;i<config.capacity;i++){
            temp.add(new ArrayList<Component>());
        }

        for(int i=0;i<dataset.size();i++){
            temp.get(categories[i]).add(dataset.get(i));
        }

        dataset=new ArrayList<Component>(config.compression);

        for(int i=0;i<config.capacity;i++){
            if(temp.get(i).size()==1){
                dataset.add(temp.get(i).get(0));
            }
            if(temp.get(i).size()>1) {
                boolean tag = true;
                for (Component c : temp.get(i)) {
                    if (c instanceof MixtureModel) {
                        tag=false;
                        for (Component d : temp.get(i)) {
                            if(d==c){
                                continue;
                            }
                            ((MixtureModel) c).addComponent(d);
                        }
                        dataset.add(c);
                        break;
                    }
                }

                if (tag) {
                    MixtureModel mixtureModel = new MixtureModel(config);
                    mixtureModel.setLevel(level + 1);
                    for (Component c : temp.get(i)) {
                        mixtureModel.addComponent(c);
                    }
                    dataset.add(mixtureModel);
                }
            }
        }

    }

    public void addComponent(Component component) {

        if (dataset != null) {
            dataset.add(component);
        }
        if (component instanceof MixtureModel) {
            MixtureModel mm = (MixtureModel) component;
            mm.setLevel(level + 1);

        }

        if (component instanceof Gaussian) {
            Gaussian g = (Gaussian) component;
            super.addComponent(g);
        }
        else {
            System.out.println("Error in types");
        }

        if (dataset != null&& dataset.size() == config.compression) {
            compress();
        }


    }

    public int totalComponents(){
        if(dataset==null){
            return 1;
        }
        int total=1;
        for(Component c: dataset){
            if(c instanceof MixtureModel){
                MixtureModel mm = (MixtureModel) c;
                total+=mm.totalComponents();
            }
            else {
                total++;
            }
        }
        return total;
    }



    private int calculateCategory(Component component, double[][] centroids, double[] minD, double[] maxD, double[] err) {
        double[] avg=component.getAvg();
        double[] div = new double[avg.length];

        for(int i=0;i<div.length;i++){
            if(maxD==null||maxD==minD){
                div[i]=err[i]*err[i];
            }
            else {
                div[i]=(maxD[i]-minD[i])*(maxD[i]-minD[i]);
            }
        }

        double min=Double.MAX_VALUE;
        int pos=0;
        for(int i=0;i<centroids.length;i++){
            double d=0;
            for(int j=0;j<getFeatures();j++){
                d+=(centroids[i][j]-avg[j])*(centroids[i][j]-avg[j])/div[j];
            }
            if(d<min){
                min=d;
                pos=i;
            }
        }
        return pos;
    }

    public int getTopLevelComp(){
        if(dataset!=null){
            return dataset.size();
        }
        else return 0;
    }




    public void setLevel(int level) {
        this.level = level;
        if(level>=config.maxLevels){
            dataset=null;
        }
        else {
            for(Component c: dataset){
                if(c instanceof MixtureModel){
                    MixtureModel mm = (MixtureModel) c;
                    mm.setLevel(level+1);
                }
            }
        }
    }
}
