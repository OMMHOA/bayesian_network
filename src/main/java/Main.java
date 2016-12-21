import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class Main {

    public static void main(String[] args) {
        Reader.read();

        RealMatrix U;
        RealMatrix V = new Array2DRowRealMatrix(Reader.getL(), Reader.getJ());
        RealMatrix uOut = new Array2DRowRealMatrix(Reader.getL(), Reader.getI());
        RealMatrix vOut = new Array2DRowRealMatrix(Reader.getL(), Reader.getJ());

        int burnIn = 10;
        int maxIteration = 100;

        for (int i = 0; i < maxIteration; i++) {
            U = updateValue(V, Reader.getI());
            V = updateValue(U, Reader.getJ());

            if (i > burnIn) {
                uOut = uOut.add(U);
                vOut = vOut.add(V);
            }
        }

        uOut = uOut.scalarMultiply(1.0 / (maxIteration - burnIn));
        vOut = vOut.scalarMultiply(1.0 / (maxIteration - burnIn));
        printResult(uOut.transpose(), vOut.transpose());
    }

    private static void printResult(RealMatrix uOut, RealMatrix vOut) {
        double[][] u = uOut.getData();
        double[][] v = vOut.getData();

        printDoubleArray(u);
        System.out.println();
        printDoubleArray(v);
    }

    private static void printDoubleArray(double[][] array) {
        for (double[] row : array) {
            String line = "" + row[0];
            for (int i = 1; i < row.length; i++) {
                line += "," + row[i];
            }
            System.out.println(line);
        }
    }

    private static RealMatrix updateValue(RealMatrix otherMatrix, int columnDimension) {
        RealMatrix out = new Array2DRowRealMatrix(Reader.getL(), columnDimension);
        MultivariateNormalDistribution multivariateNormalDistribution;

        for (int i = 0; i < columnDimension; i++) {
            RealMatrix lambdaInverse = Solver.getLambdaInverse(otherMatrix);
            RealVector psi = Solver.getPsi(otherMatrix, i, lambdaInverse);

            multivariateNormalDistribution =
                    new MultivariateNormalDistribution(psi.toArray(), lambdaInverse.getData());
            out.setColumn(i, multivariateNormalDistribution.sample());
        }

        return out;
    }
}
