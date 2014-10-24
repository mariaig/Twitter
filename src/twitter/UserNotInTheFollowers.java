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
public class UserNotInTheFollowers extends Exception {

    UserNotInTheFollowers() {
    }

    public static void showMessage() {
        System.err.println("You are not following this user!");
    }
}
