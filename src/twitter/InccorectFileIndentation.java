/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter;

/**
 *
 * @author Maria
 */
public class InccorectFileIndentation extends Exception{

    InccorectFileIndentation() {
    }

    public static void showMessage() {
        System.err.println("Every line should have this format:");
        System.err.println("time(>0)|command");
    }

}
