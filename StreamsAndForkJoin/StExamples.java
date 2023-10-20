import java.util.stream.IntStream;

public class StExamples {
    public static void main(String[] args) {

        // range(0,1000)
        // [1..1000-1]
        int sumOfFirst1000Plus5Squared = IntStream.range(0, 1000)
                .parallel()
                .map(x -> x * x + 5)
                .sum();

        System.out.println(sumOfFirst1000Plus5Squared);
    }
}
