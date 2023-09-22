public class MultiplyMatrixSeq {

    public static void main(String[] args) {

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

        MultiplyMatrixSeq.printMatrix(A);
        MultiplyMatrixSeq.printMatrix(B);

        for (int i = 0; i < O; i++) {
            for (int j = 0; j < M; j++) {
                C[j][i] = 0;
                for (int k = 0; k < N; k++) {
                    C[j][i] += A[j][k] * B[k][i];
                }
            }
        }

        MultiplyMatrixSeq.printMatrix(C);
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
