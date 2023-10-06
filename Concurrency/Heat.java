public class Heat {
    public static final int NX = 2048;
    public static final int NY = 2048;
    public static final int ITERATIONS = 50;

    public static void main(String[] args) {
        double[][] oldm = new double[NX][NY];
        double[][] newm = new double[NX][NY];

        // Everything else is 0
        oldm[NX / 2][NY / 2] = 100000;

        for (int timestep = 0; timestep <= ITERATIONS; timestep++) {

            for (int i = 1; i < NX - 1; i++) {
                for (int j = 1; j < NY - 1; j++) {
                    newm[i][j] = (oldm[i][j] + oldm[i - 1][j] + oldm[i + 1][j] + oldm[i][j - 1] + oldm[i][j + 1]) / 5;
                }
            }

            // Swap matrices
            double[][] tmp = newm;
            newm = oldm;
            oldm = tmp;
        }

        System.out.println(newm[NX / 2 - 10][NY / 2 - 10]); // ~4.9365

    }
}
