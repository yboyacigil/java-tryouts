/**
 * You can perform the following operation on some string, A:
 * <ol>
 * <li>Capitalize zero or more of A's lowercase letters at some index i (i.e., make them uppercase).
 * <li>Delete all of the remaining lowercase letters in A.
 * </ol>
 * Given Q queries in the form of two strings, A and B, determine if it's possible to make A equal to B by performing the above operation on A.
 * If A can be transformed into B, print YES on a new line; otherwise, print NO.
 * <p>
 * <b>Input Format</b>
 * <p>
 * The first line contains a single integer, Q, denoting the number of queries. The 2Q subsequent lines describe each query in the following format:
 * <ol>
 * <li>The first line of each query contains a single string, A.
 * <li>The second line of each query contains a single string, B.
 * </ol>
 * <p>
 * <b>Constraints</b>
 * <ol>
 * <li> 1 <= q <= 10
 * <li> 1 <= |a|, |b| <= 1000
 * <li>String A consists only of uppercase and lowercase English letters.
 * <li>String B consists only of uppercase English letters.
 * </ol>
 * <b>Output Format</b>
 * <p>
 * For each query, print YES on a new line if it's possible to make string  equal to string  by performing the operation specified above;
 * otherwise, print NO.
 * <p>
 * <b>Sample Input</b>
 * <pre>
 * 1
 * daBcd
 * ABC
 * </pre>
 * <p>
 * <b>Sample Output</b>
 * <pre>
 * YES
 * </pre>
 * <b>Explanation</b>
 *
 * We have  daBcd and  ABC. We perform the following operation:
 * <ol>
 * <li>Capitalize the letters a and c in  so that  dABCd.
 * <li>Delete all the remaining lowercase letters in A so that A = ABC.
 * </ol>
 * Because we were able to successfully convert A to B, we print YES on a new line.
 */
package hackerrank.dynamicprogramming.abbreviation;