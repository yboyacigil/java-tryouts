package hackerrank.booking;

import java.util.Scanner;

public class Solution1 {

    public static void main(String args[] ) throws Exception {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT */

        Scanner scanner = new Scanner(System.in);

        int squares = 0;
        int rectangles = 0;
        int polygons = 0;

        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            String[] dimensions = s.split(" ");

            if (dimensions.length != 4) {
                continue;
            }

            int a = Integer.parseInt(dimensions[0].trim());
            int b = Integer.parseInt(dimensions[1].trim());
            int c = Integer.parseInt(dimensions[2].trim());
            int d = Integer.parseInt(dimensions[3].trim());

            if (isValid(a, b, c, d) && isSquare(a, b, c, d)) {
                squares++;
            } else if (isValid(a, b, c, d) && isRectangle(a, b, c, d)) {
                rectangles++;
            } else {
                polygons++;
            }

        }

        System.out.printf("%d %d %d\n", squares, rectangles, polygons);

    }

    private static boolean isValid(int a, int b, int c, int d) {
        return a >= 0 && b >= 0 && c >= 0 && d >= 0;
    }

    private static boolean isRectangle(int a, int b, int c, int d) {
        return a == c && b == d;
    }

    private static boolean isSquare(int a, int b, int c, int d) {
        return a == b && a == c && a == d;
    }


}
