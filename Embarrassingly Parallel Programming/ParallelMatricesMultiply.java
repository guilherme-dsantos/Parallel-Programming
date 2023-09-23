public class ParallelMatricesMultiply {

    public static void main(String[] args) throws InterruptedException {

        final int M = 10;
        final int N = 5;
        final int O = 3;

        int[][] A = new int[M][N];
        int[][] B = new int[N][O];
        int[][] C = new int[M][O];

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                A[i][j] = 1;
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < O; j++) {
                B[i][j] = 2;
            }
        }

        ParallelMatricesMultiply.printMatrix(A);
        ParallelMatricesMultiply.printMatrix(B);

        int nCores = 5;
        int chunckSize = A.length / nCores;
        Thread[] threads = new Thread[nCores];
        for (int i = 0; i < nCores; i++) {
            int start = i * chunckSize;
            int end = (i < nCores - 1) ? (i + 1) * chunckSize : A.length;

            threads[i] = new Thread(() -> {
                for (int j = 0; j < O; j++) {
                    for (int k = start; k < end; k++) {
                        C[k][j] = 0;
                        for (int m = 0; m < N; m++) {
                            C[k][j] += A[k][m] * B[m][j];
                        }
                    }
                }
            });

            threads[i].start();
        }

        for (Thread t : threads)
            t.join();

        ParallelMatricesMultiply.printMatrix(C);
    }

    public static void printMatrix(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
