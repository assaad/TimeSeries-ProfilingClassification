package lu.snt.util;

import lu.snt.timeseries.TimePoint;
import lu.snt.timeseries.TimeSerie;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by assaad on 17/09/15.
 */
public class ExcelLoader {
    public static HashMap<String,TimeSerie> load(String directory){
        HashMap<String,TimeSerie> result = new HashMap<String, TimeSerie>();

        double apmax=0;
        double ammax=0;
        double rpmax=0;
        double rmmax=0;
        int errCounter=0;
        int globaltotal=0;

        String s="";
        try {
            File dir = new File(directory);
            File[] directoryListing = dir.listFiles();
            //  System.out.println("Found " + directoryListing.length + " files");
            if (directoryListing != null) {
                for (File file : directoryListing) {

                    s=file.getName();
                    if(file.getName().equals(".DS_Store")){
                        continue;
                    }
                    FileInputStream file2 = new FileInputStream(file);

                    //Create Workbook instance holding reference to .xlsx file
                    XSSFWorkbook workbook = new XSSFWorkbook(file2);

                    //Get first/desired sheet from the workbook
                    XSSFSheet sheet = workbook.getSheetAt(0);

                    //Iterate through each rows one by one
                    Iterator<Row> rowIterator = sheet.iterator();
                    int rowNum=0;

                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();
                        if (rowNum == 0)
                            row = rowIterator.next();
                        String equipment = row.getCell(0).getStringCellValue();

                        if (!equipment.startsWith("ZIVS")) {
                            Date timestamp = row.getCell(1).getDateCellValue();
                            double[] features= new double[4];

                            double aplus = row.getCell(2).getNumericCellValue();
                            double aminus = row.getCell(3).getNumericCellValue();
                            double rplus = row.getCell(4).getNumericCellValue();
                            double rminus = row.getCell(5).getNumericCellValue();


                            if(aplus<1e7&&aminus<1e7&&rplus<1e7&&rminus<1e7) {

                                if (aplus > apmax) {
                                    apmax = aplus;

                                }
                                if (aminus > ammax) {
                                    ammax = aminus;
                                }
                                if (rplus > rpmax) {
                                    rpmax = rplus;
                                }
                                if (rminus > rmmax) {
                                    rmmax = rminus;
                                }

                                features[0] = aplus;
                                features[1] = aminus;
                                features[2] = rplus;
                                features[3] = rminus;

                                TimeSerie ad;
                                if (result.containsKey(equipment)) {
                                    ad = result.get(equipment);
                                    TimePoint tp =new TimePoint(timestamp.getTime(),features);
                                    ad.addTimePoint(tp);
                                } else {
                                    ad = new TimeSerie();
                                    TimePoint tp =new TimePoint(timestamp.getTime(),features);
                                    ad.addTimePoint(tp);
                                    result.put(equipment, ad);
                                }

                                globaltotal++;
                                rowNum++;
                            }
                            else{
                                errCounter++;
                                System.out.println("Error in file "+s+ " line: "+rowNum);
                            }
                        }
                    }
                    //  System.out.println("file "+file.getName()+" read "+rowNum);
                    //fileInputStream.close();
                }
            }
        }
        catch (Exception e)
        {
            //  System.out.println("Error in file: "+s);
            e.printStackTrace();
        }
        // System.out.println("Number of Error: "+errCounter);
        // System.out.println("Read "+globaltotal+" power records!");
        // System.out.println(apmax+","+ammax+","+rpmax+","+rmmax);





        return result;
    }
}
