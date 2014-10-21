package twitter;

import java.io.BufferedReader;
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
        boolean run = true;
        String input = "";
        long time = 0, startNano = 0, nowNano;
        boolean firstInput = true;

        Twitter tw = new Twitter();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        do {
            try {
                input = br.readLine();

                if (firstInput) {
                    startNano = System.nanoTime();
                    firstInput = false;
                    time = 0;
                } else {
                    nowNano = System.nanoTime();
                    time = TimeUnit.SECONDS.convert(nowNano - startNano, TimeUnit.NANOSECONDS);
                }
                tw.tweet(input, time);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (CannotFindUser c) {
                CannotFindUser.showMessage();
            }
        } while (run);

    }
}
