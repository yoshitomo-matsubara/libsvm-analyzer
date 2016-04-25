package tool;

import common.Constant;
import common.FileManager;
import common.LibsvmFileUtil;

import java.io.File;

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

    public static void analyze(String inputResultFilePath, String inputTestFilePath)
    {
        System.out.println("FRR[%],FAR[%]");
        double[] rates = calcFrrFar(inputResultFilePath, inputTestFilePath);
        System.out.println(inputTestFilePath + Constant.COMMA_DELIMITER + String.valueOf(rates[0]) + Constant.COMMA_DELIMITER + String.valueOf(rates[1]));
    }

    public static void main(String[] args)
    {
        String inputResultFilePath = args[0];
        String inputTestFilePath = args[1];
        analyze(inputResultFilePath, inputTestFilePath);
    }
}
