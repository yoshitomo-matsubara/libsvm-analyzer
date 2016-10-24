package common;

public class Calculator {
    public static double calcInnerProduct(double[] arrayX, double[] arrayY) {
        double ip = 0.0d;
        for (int i = 0; i < arrayX.length; i++) {
            ip += arrayX[i] * arrayY[i];
        }
        return ip;
    }

    public static double calcEuclideanDistance(double[] arrayX, double[] arrayY) {
        double dist = 0.0d;
        for (int i = 0; i < arrayX.length; i++) {
            dist += Math.pow(arrayX[i] - arrayY[i], 2.0d);
        }
        return Math.sqrt(dist);
    }
}
