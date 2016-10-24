package common;

public class Kernel {
    public static final String LINEAR_KERNEL_TYPE = "linear";
    public static final String POLYNOMIAL_KERNEL_TYPE = "polynomial";
    public static final String RBF_KERNEL_TYPE = "rbf";
    public static final String SIGMOID_KERNEL_TYPE = "sigmoid";
    private String type;
    private double[] params;

    public Kernel(String type, double... params) {
        this.type = type;
        this.params = new double[params.length];
        for (int i = 0; i < params.length; i++) {
            this.params[i] = params[i];
        }
    }

    public Kernel(String type) {
        this(type, Double.NaN);
    }

    public String getType() {
        return this.type;
    }

    public double[] getParams() {
        return this.params;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setParams(double[] params) {
        this.params = new double[params.length];
        for (int i = 0; i < params.length; i++) {
            this.params[i] = params[i];
        }
    }

    public void setParam(double param, int index) {
        this.params[index] = param;
    }

    public double linearKernel(double[] arrayX, double[] arrayY) {
        return Calculator.calcInnerProduct(arrayX, arrayY);
    }

    public double polynomialKernel(double[] arrayX, double[] arrayY, double gamma, double coef0, double degree) {
        return Math.pow(gamma * Calculator.calcInnerProduct(arrayX, arrayY) + coef0, degree);
    }

    public double radialBasisFunctionKernel(double[] arrayX, double[] arrayY, double gamma) {
        return Math.exp(-gamma * Math.pow(Calculator.calcEuclideanDistance(arrayX, arrayY), 2.0d));
    }

    public double sigmoidKernel(double[] arrayX, double[] arrayY, double gamma, double coef0) {
        return Math.tanh(gamma * Calculator.calcInnerProduct(arrayX, arrayY) + coef0);
    }

    public double kernelFunction(double[] arrayX, double[] arrayY) {
        if (this.type.equals(LINEAR_KERNEL_TYPE)) {
            return linearKernel(arrayX, arrayY);
        }
        else if (this.type.equals(POLYNOMIAL_KERNEL_TYPE)) {
            return polynomialKernel(arrayX, arrayY, this.params[0], this.params[1], this.params[2]);
        }
        else if (this.type.equals(RBF_KERNEL_TYPE)) {
            return radialBasisFunctionKernel(arrayX, arrayY, this.params[0]);
        }
        else if (this.type.equals(SIGMOID_KERNEL_TYPE)) {
            return sigmoidKernel(arrayX, arrayY, this.params[0], this.params[1]);
        }
        return Double.NaN;
    }
}
