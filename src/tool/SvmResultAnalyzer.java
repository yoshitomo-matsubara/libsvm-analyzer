package tool;

import common.Constant;
import common.FileManager;
import common.LibsvmFileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class SvmResultAnalyzer
{
    public static double[] calcFrrFar(String resultFilePath, String testFilePath)
    {
        String[] resultLines = FileManager.readFile(new File(resultFilePath));
        String[] testLines = FileManager.readFile(new File(testFilePath));
        int trueCount = 0;
        int falseCount = 0;
        int fr = 0;
        int fa = 0;
        String delimiter = LibsvmFileUtil.getDelimiter(testLines[0]);
        for(int i=0;i<resultLines.length;i++)
        {
            int resultLabel = (int)Double.parseDouble(resultLines[i]);
            int testLabel = Integer.parseInt(testLines[i].split(delimiter)[0]);
            if(testLabel == Constant.TARGET_VALUE)
            {
                trueCount++;
                if(resultLabel != testLabel)
                    fr++;
            }
            else
            {
                falseCount++;
                if(resultLabel != testLabel)
                    fa++;
            }
        }

        double frr = (double)fr / (double)trueCount * 100.0d;
        double far = (double)fa / (double)falseCount * 100.0d;
        return new double[]{frr, far};
    }

    public static void analyze(String inputResultPath, String inputTestPath, String outputFilePath)
    {
        if(FileManager.checkIfFile(inputResultPath))
        {
            double[] rates = calcFrrFar(inputResultPath, inputTestPath);
            String[] lines = {"Test file,FRR,FAR", inputTestPath + Constant.COMMA_DELIMITER + String.valueOf(rates[0]) + Constant.COMMA_DELIMITER + String.valueOf(rates[1])};
            FileManager.writeFile(lines, outputFilePath);
        }
        else
        {
            ArrayList<String> resultFilePathList = FileManager.getFilePathList(inputResultPath);
            ArrayList<String> testFilePathList = FileManager.getFilePathList(inputTestPath);
            Collections.sort(resultFilePathList);
            Collections.sort(testFilePathList);
            String[] lines = new String[resultFilePathList.size() + 1];
            lines[0] = "Test file,FRR,FAR";
            int resultFileSize = resultFilePathList.size();
            for(int i=0;i<resultFileSize;i++)
            {
                String testFilePath = testFilePathList.get(i);
                double[] rates = calcFrrFar(resultFilePathList.get(i), testFilePath);
                lines[i + 1] = testFilePath + Constant.COMMA_DELIMITER + String.valueOf(rates[0]) + Constant.COMMA_DELIMITER + String.valueOf(rates[1]);
            }

            FileManager.writeFile(lines, outputFilePath);
        }
    }

    public static void main(String[] args)
    {
        String inputResultPath = args[0];
        String inputTestPath = args[1];
        String outputFilePath = args[2];
        analyze(inputResultPath, inputTestPath, outputFilePath);
    }
}
