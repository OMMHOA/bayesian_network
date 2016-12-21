import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Scanner;

abstract class Reader {
    private static int I, J, L;
    private static double beta;
    private static RealMatrix H;
    private static Scanner scanner = new Scanner(System.in);

    static void read() {
        String[] splitFirstRow = scanner.nextLine().split(",");
        I = Integer.parseInt(splitFirstRow[0]);
        J = Integer.parseInt(splitFirstRow[1]);
        L = Integer.parseInt(splitFirstRow[2]);
        beta = Double.parseDouble(splitFirstRow[3]);
        H = MatrixUtils.createRealMatrix(readH(I, J));
    }

    private static double[][] readH(int I, int J) {
        double[][] h = new double[I][J];
        for (int i = 0; i < I; i++) {
            h[i] = readRow(J);
        }
        return h;
    }

    private static double[] readRow(int J) {
        double[] out = new double[J];
        String[] splitRow = scanner.nextLine().split(",");
        for (int j = 0; j < J; j++) {
            out[j] = Double.parseDouble(splitRow[j]);
        }
        return out;
    }

    public static int getI() {
        return I;
    }

    public static int getJ() {
        return J;
    }

    public static int getL() {
        return L;
    }

    public static double getBeta() {
        return beta;
    }

    public static RealMatrix getH() {
        return H;
    }
}
