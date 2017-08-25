package hackerrank.dynamicprogramming.abbreviation;

import java.util.Locale;
import java.util.Scanner;

public class Solution {

    protected static final String YES = "YES";
    protected static final String NO  = "NO";

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */

        Scanner scanner = new Scanner(System.in);

        int q = scanner.nextInt();
        for (int i = 0; i < q; i++) {
            String a = scanner.next();
            String b = scanner.next();

            String o = getOutput(a, b);
            System.out.println(o);
        }

    }

    static String getOutput(String a, String b) {
        if (a.length() < b.length()) {
            return NO;
        }

        if (b.length() == 0 || b.trim().length() == 0) {
            return NO;
        }

        if (a.toUpperCase(Locale.ENGLISH).equals(b)) {
            return YES;
        }

        return f(a, b);
    }

    private static String f(String a, String b) {
        if (a.replaceAll("[a-z]", "").equals(b)) {
            return YES;
        }

        int i = 1;
        do {
            String x = a.substring(0, i).toUpperCase(Locale.ENGLISH) + a.substring(i);
            String s = x.replaceAll("[a-z]", "");

            if (s.equals(b)) {
                return YES;
            }
            i++;
        } while(i < a.length());

        if (a.length() > 1) {
            return f(a.substring(1), b);
        } else if (a.equals(b)) {
            return YES;
        } else {
            return NO;
        }
    }
}