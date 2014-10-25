package twitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maria
 */
public class MainClass {

    public static int convertNanoToSeconds(long timeInNano) {
        return (int) (timeInNano / Math.pow(10, 9));
    }

    public static void main(String[] args) {
        //long start=System.nanoTime();
//        boolean run = true;
//        String input;
//        long time, startNano = 0, nowNano;
//        boolean firstInput = true;
//
//        Twitter tw = new Twitter();
//
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//
//        do {
//            try {
//                input = br.readLine();
//
//                if (firstInput) {
//                    startNano = System.nanoTime();
//                    firstInput = false;
//                    time = 0;
//                } else {
//                    nowNano = System.nanoTime();
//                    time = TimeUnit.SECONDS.convert(nowNano - startNano, TimeUnit.NANOSECONDS);
//                }
//                
//                tw.tweet(input, time);
//            } catch (IOException ex) {
//                //ex.printStackTrace();
//            } catch (CannotFindUser c) {
//                CannotFindUser.showMessage();
//            }
//        } while (run);

        FilesManager fm = FilesManager.getInstance();
        ArrayList<Long> times = fm.getTimes();
        ArrayList<String> lines = fm.getLines();
        
        String inputFile = "Input3.txt";
        String outputFile = "Output.txt";
        String corOutput="CorrectOutput3.txt";
        
        //clear file
        try { 
            BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));
            out.write("");
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        Twitter tw = new Twitter();

        try {
            fm.setInputFile(inputFile);
            fm.setOutputFile(outputFile);
            fm.readFile();
            for (int i = 0; i < times.size(); i++) {
                System.out.println(lines.get(i));
                fm.appendToOutputFile(lines.get(i));
                tw.tweet(lines.get(i), times.get(i));
            }
            
            fm.compareOutputWith(corOutput);
        } catch (InccorectFileIndentation ex) {
            InccorectFileIndentation.showMessage();
        } catch (CannotFindUser ex) {
            CannotFindUser.showMessage();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (UserNotInTheFollowers ex) {
            UserNotInTheFollowers.showMessage();
        }

    }
}
