package hackerrank.booking;

import java.util.*;
import java.util.stream.Collectors;

public class Solution2 {

    public static void main(String args[] ) throws Exception {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT */
        Scanner scanner = new Scanner(System.in);

        String mentionString = scanner.nextLine();

        List<String> mentions = Arrays.asList(mentionString.split(" ")).stream().collect(Collectors.toList());

        int numRevs = scanner.nextInt();

        Map<Long, Long> ratingMap = new HashMap<>();

        for(int i = 0; i < numRevs; i++) {

            long hotelId = scanner.nextLong();
            String review = scanner.nextLine().replaceAll("[^a-zA-Z]", "");

            long appearCount = Arrays.asList(review.split(" ")).parallelStream()
                    .filter(w -> mentions.contains(w))
                    .count();


            if (ratingMap.containsKey(hotelId)) {
                ratingMap.put(hotelId, ratingMap.get(hotelId) + appearCount);
            } else {
                ratingMap.put(hotelId, appearCount);
            }

        }

        List<Long> hotelIds = ratingMap.entrySet().stream()
                .sorted((o1, o2) -> -1 * o1.getValue().compareTo(o2.getValue()))
                .map(e -> e.getKey())
                .collect(Collectors.toList());


        hotelIds.stream().forEach(
                id -> System.out.printf("%d ", id)
        );
        System.out.println();

    }

}
