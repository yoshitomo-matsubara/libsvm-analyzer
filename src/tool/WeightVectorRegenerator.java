package tool;

import common.FileManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class WeightVectorRegenerator
{
    public static double[][] loadSupportVectors(File modelFile)
    {
        String[] lines = FileManager.readFile(modelFile);
        ArrayList<String> svLineList = new ArrayList<String>();
        boolean isSv = false;
        for(int i=0;i<lines.length;i++)
        {
            if(lines[i].startsWith("SV"))
                isSv = true;
            else if(isSv)
                svLineList.add(lines[i]);
        }

        int maxIndex = 1;
        for(int i=0;i<lines.length;i++)
        {
            String[] elements = lines[i].split(" ");
            int lastIndex = Integer.parseInt(elements[elements.length - 1].split(":")[0]);
            if(lastIndex > maxIndex)
                maxIndex = lastIndex;
        }

        double[][] supportVectorMatrix = new double[svLineList.size()][maxIndex + 1];
        for(int i=0;i<supportVectorMatrix.length;i++)
            for(int j=0;j<supportVectorMatrix[0].length;j++)
                supportVectorMatrix[i][j] = 0.0d;

        for(int i=0;i<supportVectorMatrix.length;i++)
        {
            String svLine = svLineList.get(i);
            String[] elements = svLine.split(" ");
            supportVectorMatrix[i][0] = Double.parseDouble(elements[0]);
            for(int j=1;j<elements.length;j++)
            {
                String[] params = elements[j].split(":");
                int index = Integer.parseInt(params[0]);
                double value = Double.parseDouble(params[1]);
                supportVectorMatrix[i][index] = value;
            }
        }

        return supportVectorMatrix;
    }

    public static double[] regenerateWeightVector(double[][] supportVectorMatrix)
    {
        double[] weightVector = new double[supportVectorMatrix[0].length - 1];
        for(int i=0;i<weightVector.length;i++)
            weightVector[i] = 0.0d;

        for(int i=0;i<supportVectorMatrix.length;i++)
        {
            double w = supportVectorMatrix[i][0];
            for(int j=1;j<supportVectorMatrix[0].length;j++)
                weightVector[j - 1] += w * supportVectorMatrix[i][j];
        }

        return  weightVector;
    }

    public static void outputWeightVector(double[] weightVector, String inputFileName, String outputFilePath)
    {
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath));
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<weightVector.length;i++)
                sb.append("\t" + String.valueOf(weightVector[i]));

            bw.write(inputFileName + "\t1" + sb.toString());
            bw.close();
        }
        catch(Exception e)
        {
            System.out.println("Error @ outputWeightVector(double[], String, String) : " + e.toString());
        }
    }

    public static void regenerate(String inputFilePath, String outputDirPath)
    {
        File inputFile = new File(inputFilePath);
        double[][] supportVectorMatrix = loadSupportVectors(inputFile);
        double[] weightVector = regenerateWeightVector(supportVectorMatrix);
        String inputFileName = inputFile.getName();
        String outputFilePath = outputDirPath + FileManager.removeExtension(inputFile.getName()) + ".wvec";
        File parentDir = new File((new File(outputFilePath)).getParent());
        if(!parentDir.exists())
            parentDir.mkdirs();

        outputWeightVector(weightVector, inputFileName , outputFilePath);
    }

    public static void main(String[] args)
    {
        String inputPath = args[0];
        String outputDirPath = args[1];
        if(FileManager.checkIfFile(inputPath))
            regenerate(inputPath, outputDirPath);
        else
        {
            ArrayList<String> inputFilePathList = FileManager.getFilePathList(inputPath);
            for(String inputFilePath : inputFilePathList)
                regenerate(inputFilePath, outputDirPath);
        }
    }
}
