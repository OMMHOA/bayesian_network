import org.apache.commons.math3.linear.*;

abstract class Solver {

    static RealMatrix getLambdaInverse(RealMatrix otherMatrix) {
        int L = Reader.getL();
        RealMatrix lambda = new Array2DRowRealMatrix(L, L);
        RealMatrix I = MatrixUtils.createRealIdentityMatrix(L);

        for (int i = 0; i < otherMatrix.getColumnDimension(); i++) {
            RealVector columnVector = otherMatrix.getColumnVector(i);
            lambda = lambda.add(columnVector.outerProduct(columnVector));
        }

        lambda = lambda.scalarMultiply(Reader.getBeta());
        lambda = lambda.add(I);
        lambda = new LUDecomposition(lambda).getSolver().getInverse();

        return lambda;
    }

    static RealVector getPsi(RealMatrix otherMatrix, int index, RealMatrix lambdaInverse) {
        RealVector psi = new ArrayRealVector(Reader.getL());
        int columnDimension = otherMatrix.getColumnDimension();

        for (int i = 0; i < columnDimension; i++) {
            RealVector columnVector = otherMatrix.getColumnVector(i);
            double entry = getEntry(i, index, columnDimension);
            psi = psi.add(columnVector.mapMultiply(entry));
        }
        psi = psi.mapMultiply(Reader.getBeta());
        psi = lambdaInverse.operate(psi);

        return psi;
    }

    private static double getEntry(int i, int index, int columnDimension) {
        if (columnDimension == Reader.getJ())
            return Reader.getH().getEntry(index, i);
        else
            return Reader.getH().getEntry(i, index);
    }

}
