package lu.snt.util;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by assaad on 17/09/15.
 */
public class SolutionSpace implements Comparable<SolutionSpace> {
    public double score;
    public int id;
    public boolean isDistance=false;

    public SolutionSpace(int id, double score, boolean isDistance){
        this.id=id;
        this.score=score;
        this.isDistance=isDistance;
    }

    @Override
    public int compareTo(SolutionSpace o) {
        if(isDistance){
            return Double.compare(this.score,o.score);
        }
        else {
            return Double.compare(o.score, this.score);
        }
    }

    public static int rank (ArrayList<SolutionSpace> sol, int id){
        Collections.sort(sol);
        for(int i=0; i<sol.size();i++){
            if(sol.get(i).id==id){
                return i+1;
            }
        }
        return sol.size()+1;
    }

    public static boolean contain (ArrayList<SolutionSpace> sol, int id, int count){
        Collections.sort(sol);
        for(int i=0; i<count;i++){
            if(sol.get(i).id==id){
                return true;
            }
        }
        return false;
    }
}

