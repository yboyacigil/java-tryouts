package hackerrank.dynamicprogramming.candies;

import java.util.*;

public class Solution {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int[] ratings = new int[n];
        int i = 0;
        while(i < n && scanner.hasNextInt()) {
            ratings[i] = scanner.nextInt();
            i++;
        }

        int r = getOutput(ratings);

        System.out.println(r);
    }

    static int getOutput(int[] ratings) {
        if (ratings.length == 0) {
            return 0;
        }

        if (ratings.length == 1) {
            return 1;
        }

        int[] candies = new int[ratings.length];
        for(int i=0; i < ratings.length; i++) {
            candies[i] = 1;
        }

        candies = g(ratings, 1, candies);

        int sum = 0;
        for(int i=0; i < candies.length; i++) {
            sum += candies[i];
        }

        return sum;
    }

    private static int[] g(int[] ratings, int index, int[] candies) {
        int pRating = ratings[index - 1];
        int rating = ratings[index];

        if (rating > pRating) {
            candies[index] = candies[index - 1] + 1;
        } else if (rating < pRating) {
            int i = index;
            int j = index - 1;
            while(j >= 0 && ratings[j] > ratings[i]) {
                if (candies[j] <= candies[i]) {
                    candies[j] += 1;
                }
                i--;
                j--;
            }
        }

        if (index == ratings.length - 1) {
            return candies;
        }

        return g(ratings, index + 1, candies);
    }

    static int getOutputXXX(int[] ratings) {

        List<Student> students = new ArrayList<>();
        for(int i = 0; i < ratings.length; i++) {
            students.add(new Student(i, ratings[i]));
        }

        Collections.sort(students, Comparator.comparingInt(s -> s.rating));

        int pRank = students.get(0).rank;
        int pRating = students.get(0).rating;
        int sum = 1;
        int more = 0;
        for(int i = 1; i < students.size(); i++) {
            sum++;

            Student s = students.get(i);
            if (Math.abs(pRank - s.rank) == 1) {
                if (s.rating > pRating) {
                    more++;
                } else {
                    more = Math.max(--more, 0);
                }
                sum += more;
            }

            pRank = s.rank;
            pRating = s.rating;
        }

        return sum;
    }

    static class Student {
        final int rank;
        final int rating;

        public Student(int rank, int rating) {
            this.rank = rank;
            this.rating = rating;
        }
    }

    static int getOutputXX(int[] ratings) {

        Map<Integer, Integer> map = new TreeMap<>();

        for(int i = 0; i < ratings.length; i++) {
            map.put(ratings[i], i);
        }

        int pRank = -1;
        int sum = 0;
        int more = 0;

        for (Map.Entry<Integer, Integer> e: map.entrySet()) {
            int rating = e.getKey();
            int rank = e.getValue();

            if (pRank != -1 && Math.abs(pRank - rank) == 1) {
                more++;
            } else {
                more = Math.max(more--, 0);
            }

            pRank = rank;

            sum += 1;
            sum += more;
        }

        return sum;
    }

    static int getOutputX(int[] ratings) {
        return f(ratings, 0, 0);
    }

    private static int f(int[] ratings, int index, int sum) {
        int prevIndex = index - 1;

        if (prevIndex < 0) {
            prevIndex = 0;
        }

        int prevRating = ratings[prevIndex];
        int rating = ratings[index];

        if (rating <= prevRating) {
            sum = sum + 1;
        } else {
            sum = sum + 2;
        }

        if (prevRating > rating) {
            sum = sum + 1;
        }

        if (index == ratings.length - 1) {
            return sum;
        }

        return f(ratings, index + 1, sum);
    }

}