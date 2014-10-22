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
public class StringsManager {
    public static String removeExtraWhiteSpaces(String str) {
        str = str.trim();
        while (str.contains("  ")) {
            str = str.replace("  "," ");
        }
        return str;
    }
}
