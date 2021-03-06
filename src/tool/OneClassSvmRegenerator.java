package tool;

import common.Constant;
import common.FileManager;
import common.Kernel;
import common.LibsvmFileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OneClassSvmRegenerator {
    public static Kernel createKernel(String[] lines) {
        String kernelType = lines[1].split(" ")[1];
        if (kernelType.equals(Kernel.LINEAR_KERNEL_TYPE)) {
            return new Kernel(kernelType);
        } else if (kernelType.equals(Kernel.POLYNOMIAL_KERNEL_TYPE)) {
            double degree = Double.parseDouble(lines[2].split(" ")[1]);
            double gamma = Double.parseDouble(lines[3].split(" ")[1]);
            double coef0 = Double.parseDouble(lines[4].split(" ")[1]);
            return new Kernel(kernelType, gamma, coef0, degree);
        } else if (kernelType.equals(Kernel.RBF_KERNEL_TYPE)) {
            double gamma = Double.parseDouble(lines[2].split(" ")[1]);
            return new Kernel(kernelType, gamma);
        } else if (kernelType.equals(Kernel.SIGMOID_KERNEL_TYPE)) {
            double gamma = Double.parseDouble(lines[2].split(" ")[1]);
            double coef0 = Double.parseDouble(lines[3].split(" ")[1]);
            return new Kernel(kernelType, gamma, coef0);
        }
        return null;
    }

    public static double[][] createSupportVectorMatrix(String[] lines, int startIndex, double[] alphas) {
        int maxIndex = Integer.MIN_VALUE;
        for (int i = startIndex; i < lines.length; i++) {
            String[] elements = lines[i].split(" ");
            int lastIndex = Integer.parseInt(elements[elements.length - 1].split(Constant.KEY_VALUE_DELIMITER)[0]);
            if (lastIndex > maxIndex) {
                maxIndex = lastIndex;
            }
        }

        double[][] vectorMatrix = new double[alphas.length][maxIndex];
        for (int i = 0; i < vectorMatrix.length; i++) {
            for (int j = 0; j < vectorMatrix[0].length; j++) {
                vectorMatrix[i][j] = 0.0d;
            }
        }

        for (int i = startIndex; i < lines.length; i++) {
            String[] elements = lines[i].split(" ");
            int svIndex = i - startIndex;
            alphas[svIndex] = Double.parseDouble(elements[0]);
            for (int j = 1; j < elements.length; j++) {
                String[] params = elements[j].split(Constant.KEY_VALUE_DELIMITER);
                int index = Integer.parseInt(params[0]) - 1;
                double value = Double.parseDouble(params[1]);
                vectorMatrix[svIndex][index] = value;
            }
        }
        return vectorMatrix;
    }

    public static double[][] createTestVectorMatrix(String testFilePath, int featureSize, List<Integer> labelList) {
        String[] lines = FileManager.readFile(testFilePath);
        double[][] vectorMatrix = new double[lines.length][featureSize];
        for (int i = 0; i < vectorMatrix.length; i++) {
            for (int j = 0; j < vectorMatrix[0].length; j++) {
                vectorMatrix[i][j] = 0.0d;
            }
        }

        String delimiter = LibsvmFileUtil.getBestDelimiter(lines[0]);
        for (int i = 0; i < lines.length; i++) {
            String[] elements = lines[i].split(delimiter);
            labelList.add(Integer.parseInt(elements[0]));
            for (int j = 1; j < elements.length; j++) {
                String[] params = elements[j].split(Constant.KEY_VALUE_DELIMITER);
                int index = Integer.parseInt(params[0]) - 1;
                double value = Double.parseDouble(params[1]);
                vectorMatrix[i][index] = value;
            }
        }
        return vectorMatrix;
    }

    public static double regenerateValue(double[] testVector, Kernel kernel, double[] alphas, double[][] supportVectorMatrix, double rho) {
        double sum = 0.0d;
        for (int i = 0; i < supportVectorMatrix.length; i++) {
            sum += alphas[i] * kernel.kernelFunction(testVector, supportVectorMatrix[i]);
        }
        return sum - rho;
    }

    public static void regenerate(String modelFilePath, String testFilePath, String outputDirPath) {
        String[] inputLines = FileManager.readFile(modelFilePath);
        if (!inputLines[0].endsWith("one_class")) {
            return;
        }

        Kernel kernel = createKernel(inputLines);
        int nextIndex = Integer.MIN_VALUE;
        if (kernel.getType().equals(Kernel.LINEAR_KERNEL_TYPE)) {
            nextIndex = 3;
        } else if (kernel.getType().equals(Kernel.POLYNOMIAL_KERNEL_TYPE)) {
            nextIndex = 6;
        } else if (kernel.getType().equals(Kernel.RBF_KERNEL_TYPE)) {
            nextIndex = 4;
        } else if (kernel.getType().equals(Kernel.SIGMOID_KERNEL_TYPE)) {
            nextIndex = 5;
        }

        int svSize = Integer.parseInt(inputLines[nextIndex].split(" ")[1]);
        double rho = Double.parseDouble(inputLines[nextIndex + 1].split(" ")[1]);
        double[] alphas = new double[svSize];
        double[][] supportVectorMatrix = createSupportVectorMatrix(inputLines, nextIndex + 3, alphas);
        List<Integer> labelList = new ArrayList<>();
        double[][] testVectorMatrix = createTestVectorMatrix(testFilePath, supportVectorMatrix[0].length, labelList);
        String[] outputLines = new String[testVectorMatrix.length];
        for (int i = 0; i < testVectorMatrix.length; i++) {
            outputLines[i] = String.valueOf(labelList.get(i)) + "," + String.valueOf(regenerateValue(testVectorMatrix[i], kernel, alphas, supportVectorMatrix, rho));
        }

        File testFile = new File(testFilePath);
        FileManager.writeFile(outputLines, outputDirPath + FileManager.removeExtension(testFile.getName()) + ".rgn");
    }

    public static void main(String[] args) {
        String inputModelPath = args[0];
        String inputTestPath = args[1];
        String outputDirPath = (args.length >= 3) ? args[2] : "";
        if (FileManager.checkIfFile(inputModelPath)) {
            regenerate(inputModelPath, inputTestPath, outputDirPath);
        } else {
            List<String> modelFilePathList = FileManager.getFilePathList(inputModelPath);
            List<String> testFilePathList = FileManager.getFilePathList(inputTestPath);
            int size = modelFilePathList.size();
            for (int i = 0; i < size; i++) {
                regenerate(modelFilePathList.get(i), testFilePathList.get(i), outputDirPath);
            }
        }
    }
}
