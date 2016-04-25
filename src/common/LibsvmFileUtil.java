package common;

public class LibsvmFileUtil
{
    public static String getDelimiter(String line)
    {
        int countA = line.split(Constant.SPACE_DELIMITER).length;
        int countB = line.split(Constant.TAB_DELIMITER).length;
        int countC = line.split(Constant.COMMA_DELIMITER).length;
        if(countA >= countB && countA >= countC)
            return Constant.SPACE_DELIMITER;
        else if(countB >= countA && countB >= countC)
            return Constant.TAB_DELIMITER;

        return Constant.COMMA_DELIMITER;
    }
}
