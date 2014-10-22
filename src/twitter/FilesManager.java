/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maria
 */
public class FilesManager {

    private static FilesManager instance = new FilesManager();
    private ArrayList<Long> times;
    private ArrayList<String> lines;
    private final String TIMESEPARATOR = "\\|";
    private String inputFile;
    private String outputFile;

    private FilesManager() {
        times = new ArrayList<>();
        lines = new ArrayList<>();
        inputFile = "";
        outputFile = "";
    }

    public static FilesManager getInstance() {
        return instance;
    }

    public void readFile() throws InccorectFileIndentation, IOException {
        if (inputFile.equals("")) {
            System.err.println("EMPTY FILENAME");
            return;
        }
        BufferedReader br;

        br = new BufferedReader(new FileReader(inputFile));
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.equals("")) {
                String[] parts = line.split(TIMESEPARATOR);
                parts[0] = StringsManager.removeExtraWhiteSpaces(parts[0]);
                if (!parts[0].equals("") && !parts[1].equals("")) {
                    times.add(Long.parseLong(parts[0]));
                    lines.add(parts[1]);
                } else {
                    throw new InccorectFileIndentation();
                }
            }
        }

    }

    public void appendToOutputFile(String strToAppend) {
        if (outputFile.equals("")) {
            System.err.println("EMPTY FILENAME");
            return;
        }
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(outputFile, true));
            bw.write(strToAppend);
            bw.newLine();
            bw.close();
        } catch (IOException ex) {
        }

    }

    public void compareOutputWith(String pathToResultsFile) throws FileNotFoundException, IOException {
        BufferedReader brOut, brResult;
        System.out.println("----------------------------------------------");
        
        File outFile=new File(this.outputFile);
        File resFile=new File(pathToResultsFile);
        
        if(outFile.length()!=resFile.length()){
            System.err.println("INVALID OUTPUT");
            return;
        }
        brOut = new BufferedReader(new FileReader(this.outputFile));
        brResult = new BufferedReader(new FileReader(pathToResultsFile));

        String lineOut, lineResult;
        while ((lineOut = brOut.readLine()) != null && (lineResult = brResult.readLine()) != null) {
            if (!lineOut.equals(lineResult)) {
                System.err.println("INVALID OUTPUT");
                return;
            }
        }
        System.out.println("ALL GOOD HERE!");
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

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

}
