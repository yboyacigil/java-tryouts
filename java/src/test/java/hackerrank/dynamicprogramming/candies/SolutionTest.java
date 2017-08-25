package hackerrank.dynamicprogramming.candies;

import org.junit.Test;

import static org.junit.Assert.*;

public class SolutionTest {


    @Test
    public void test1() throws Exception {
        int[] ratings = {1, 2, 2};
        assertEquals(4, Solution.getOutput(ratings));
    }

    @Test
    public void test2() throws Exception {
        int[] ratings = {2, 1, 2};
        assertEquals(5, Solution.getOutput(ratings));
    }

    @Test
    public void test3() throws Exception {
        int[] ratings = {3, 2, 1};
        assertEquals(6, Solution.getOutput(ratings));
    }

    @Test
    public void test4() throws Exception {
        int[] ratings = {2, 4, 2, 6, 1, 7, 8, 9, 2, 1};
        assertEquals(19, Solution.getOutput(ratings));
    }

    @Test
    public void test5() throws Exception {
        int[] ratings = {};
        assertEquals(0, Solution.getOutput(ratings));
    }

    @Test
    public void test6() throws Exception {
        int[] ratings = {9};
        assertEquals(1, Solution.getOutput(ratings));
    }

    @Test
    public void test7() throws Exception {
        int[] ratings = new int[100000];
        int sum = 0;
        for(int i=0, j=1; i < 100000; i++, j++) {
            ratings[i] = j;
            sum += j;
        }
        assertEquals(sum, Solution.getOutput(ratings));
    }

    @Test
    public void test8() throws Exception {
        int[] ratings = new int[100000];
        int sum = 0;
        for(int i=0, j=100000; i < 100000; i--, j--) {
            ratings[i] = j;
            sum += j;
        }
        assertEquals(sum, Solution.getOutput(ratings));
    }
}