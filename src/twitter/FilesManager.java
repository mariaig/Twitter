/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maria
 */
public class FilesManager {

    private ArrayList<Long> times;
    private ArrayList<String> lines;
    private final int TIMECONSTANT = 30;

    public FilesManager() {
        times = new ArrayList<>();
        lines = new ArrayList<>();
    }

    public void readFile(String pathToFile) {
        BufferedReader br;
        boolean first = true;
        long time = 0;
        long startNano = 0, nowSeconds;

        try {
            br = new BufferedReader(new FileReader(pathToFile));
            String line;
            while ((line = br.readLine()) != null) {
                if (first) {
                    startNano = System.nanoTime();
                    times.add(time);
                    lines.add(line);
                    first = false;
                    time = (long) (Math.random() * TIMECONSTANT);
                    Thread.sleep(time*1000);
                } else {
                    times.add(time);
                    lines.add(line);
                    nowSeconds = TimeUnit.SECONDS.convert(System.nanoTime()-startNano, TimeUnit.NANOSECONDS)+TIMECONSTANT;
                    Random rand = new Random();
                    time = rand.nextInt((int) (nowSeconds - time) + 1) + time;
                    Thread.sleep(time*100);
                    System.out.println();
                }
            }
        } catch (IOException io) {
            //io.printStackTrace();
        } catch (InterruptedException ex) {
            Logger.getLogger(FilesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void appendToFile() {

    }

    public ArrayList<Long> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<Long> times) {
        this.times = times;
    }

    public ArrayList<String> getLines() {
        return lines;
    }

    public void setLines(ArrayList<String> lines) {
        this.lines = lines;
    }

}
