package tool;

import common.FileManager;
import common.Kernel;

import java.io.File;
import java.util.ArrayList;

public class OneClassSvmAnalyzer
{
    public static Kernel createKernel(String[] lines)
    {
        String kernelType = lines[1].split(" ")[1];
        if(kernelType.equals(Kernel.LINEAR_KERNEL_TYPE))
            return new Kernel(kernelType);
        else if(kernelType.equals(Kernel.POLYNOMIAL_KERNEL_TYPE))
        {
            double degree = Double.parseDouble(lines[2].split(" ")[1]);
            double gamma = Double.parseDouble(lines[3].split(" ")[1]);
            double coef0 = Double.parseDouble(lines[4].split(" ")[1]);
            return new Kernel(kernelType, gamma, coef0, degree);
        }
        else if(kernelType.equals(Kernel.RBF_KERNEL_TYPE))
        {
            double gamma = Double.parseDouble(lines[2].split(" ")[1]);
            return new Kernel(kernelType, gamma);
        }
        else if(kernelType.equals(Kernel.SIGMOID_KERNEL_TYPE))
        {
            double gamma = Double.parseDouble(lines[2].split(" ")[1]);
            double coef0 = Double.parseDouble(lines[3].split(" ")[1]);
            return new Kernel(kernelType, gamma, coef0);
        }

        return null;
    }

    public static double[][] createSupportVectorMatrix(String[] lines, int startIndex, double[] alphas)
    {
        int maxIndex = Integer.MIN_VALUE;
        for(int i=startIndex;i<lines.length;i++)
        {
            String[] elements = lines[i].split(" ");
            int lastIndex = Integer.parseInt(elements[elements.length - 1].split(":")[0]);
            if(lastIndex > maxIndex)
                maxIndex = lastIndex;
        }

        double[][] vectorMatrix = new double[alphas.length][maxIndex];
        for(int i=0;i<vectorMatrix.length;i++)
            for(int j=0;j<vectorMatrix[0].length;j++)
                vectorMatrix[i][j] = 0.0d;

        for(int i=startIndex;i<lines.length;i++)
        {
            String[] elements = lines[i].split(" ");
            int svIndex = i - startIndex;
            alphas[svIndex] = Double.parseDouble(elements[0]);
            for(int j=1;j<elements.length;j++)
            {
                String[] params = elements[j].split(":");
                int index = Integer.parseInt(params[0]) - 1;
                double value = Double.parseDouble(params[1]);
                vectorMatrix[svIndex][index] = value;
            }
        }

        return vectorMatrix;
    }

    public static double[][] createTestVectorMatrix(String testFilePath, int featureSize, ArrayList<Integer> labelList)
    {
        String[] lines = FileManager.readFile(testFilePath);
        double[][] vectorMatrix = new double[lines.length][featureSize];
        for(int i=0;i<vectorMatrix.length;i++)
            for(int j=0;j<vectorMatrix[0].length;j++)
                vectorMatrix[i][j] = 0.0d;

        for(int i=0;i<lines.length;i++)
        {
            String[] elements = lines[i].split("\t");
            labelList.add(Integer.parseInt(elements[0]));
            for(int j=1;j<elements.length;j++)
            {
                String[] params = elements[j].split(":");
                int index = Integer.parseInt(params[0]) - 1;
                double value = Double.parseDouble(params[1]);
                vectorMatrix[i][index] = value;
            }
        }

        return vectorMatrix;
    }

    public static double estimate(double[] testVector, Kernel kernel, double[] alphas, double[][] supportVectorMatrix, double rho)
    {
        double sum = 0.0d;
        for(int i=0;i<supportVectorMatrix.length;i++)
            sum += alphas[i] * kernel.kernelFunction(testVector, supportVectorMatrix[i]);

        return sum - rho;
    }

    public static void analyze(String modelFilePath, String testFilePath, String outputDirPath)
    {
        String[] inputLines = FileManager.readFile(modelFilePath);
        if(!inputLines[0].endsWith("one_class"))
            return;

        Kernel kernel = createKernel(inputLines);
        int nextIndex = Integer.MIN_VALUE;
        if(kernel.getType().equals(Kernel.LINEAR_KERNEL_TYPE))
            nextIndex = 3;
        else if(kernel.getType().equals(Kernel.POLYNOMIAL_KERNEL_TYPE))
            nextIndex = 6;
        else if(kernel.getType().equals(Kernel.RBF_KERNEL_TYPE))
            nextIndex = 4;
        else if(kernel.getType().equals(Kernel.SIGMOID_KERNEL_TYPE))
            nextIndex = 5;

        int svSize = Integer.parseInt(inputLines[nextIndex].split(" ")[1]);
        double rho = Double.parseDouble(inputLines[nextIndex + 1].split(" ")[1]);
        double[] alphas = new double[svSize];
        double[][] supportVectorMatrix = createSupportVectorMatrix(inputLines, nextIndex + 3, alphas);
        ArrayList<Integer> labelList = new ArrayList<Integer>();
        double[][] testVectorMatrix = createTestVectorMatrix(testFilePath, supportVectorMatrix[0].length, labelList);
        String[] outputLines = new String[testVectorMatrix.length];
        for(int i=0;i<testVectorMatrix.length;i++)
            outputLines[i] = String.valueOf(labelList.get(i)) + "," + String.valueOf(estimate(testVectorMatrix[i], kernel, alphas, supportVectorMatrix, rho));

        FileManager.writeFile(outputLines, outputDirPath + (new File(testFilePath)).getName() + ".est");
    }

    public static void main(String[] args)
    {
        String inputModelDirPath = args[0];
        String inputTestDirPath = args[1];
        String outputDirPath = args[2];
        ArrayList<String> modelFilePathList = FileManager.getFilePathList(inputModelDirPath);
        ArrayList<String> testFilePathList = FileManager.getFilePathList(inputTestDirPath);
        int size = modelFilePathList.size();
        for(int i=0;i<size;i++)
            analyze(modelFilePathList.get(i), testFilePathList.get(i), outputDirPath);
    }
}
