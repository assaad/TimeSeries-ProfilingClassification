package lu.snt.util;

import java.time.LocalDateTime;

/**
 * Created by assaad on 17/09/15.
 */
public class TimeUtil {
    public static double convertTimeToDay(Long timestamp){
        java.sql.Timestamp tiempoint= new java.sql.Timestamp(timestamp);
        LocalDateTime ldt= tiempoint.toLocalDateTime();
        double res= ((double)ldt.getHour())/24+((double)ldt.getMinute())/(24*60)+((double)ldt.getSecond())/(24*60*60);
        return res;
    }

    //get time in 15 minutes chunks
    public static int getIntTime(long time, long slotSize, long periodSize){
        long res=time%periodSize;
        res=res/slotSize;
        return (int)res;
    }
    public static double convertTimeToHour(long time){
        return convertTimeToDay(time)*24;
    }


}
