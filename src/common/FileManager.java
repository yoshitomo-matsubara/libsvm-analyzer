package common;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class FileManager
{
    public static ArrayList<File> getFileList(String inputDirPath)
    {
        ArrayList<File> fileList = new ArrayList<File>();
        File[] files = (new File(inputDirPath)).listFiles();
        for(File file : files)
            if(file.isFile())
                fileList.add(file);

        Collections.sort(fileList);
        return fileList;
    }

    public static ArrayList<String> getFilePathList(String inputDirPath)
    {
        ArrayList<String> filePathList = new ArrayList<String>();
        File[] files = (new File(inputDirPath)).listFiles();
        for(File file : files)
            if(file.isFile())
                filePathList.add(file.toString());

        Collections.sort(filePathList);
        return filePathList;
    }

    public static void makeParentDir(File file)
    {
        if(file.getParent() != null)
        {
            File dir = new File(file.getParent());
            if(dir != null && !dir.exists())
                dir.mkdirs();
        }
    }

    public static String[] readFile(File file)
    {
        ArrayList<String> lineList = new ArrayList<String>();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while((line = br.readLine()) != null)
                lineList.add(line);

            br.close();
        }
        catch(Exception e)
        {
            System.out.println("Error @ readFile(File) : " + e.toString());
        }

        return lineList.toArray(new String[0]);
    }

    public static String[] readFile(String filePath)
    {
        return readFile(new File(filePath));
    }

    public static void writeFile(String[] lines, File file)
    {
        makeParentDir(file);
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for(int i=0;i<lines.length;i++)
            {
                bw.write(lines[i]);
                bw.newLine();
            }

            bw.close();
        }
        catch(Exception e)
        {
            System.out.println("Error @ writeFile(String[], File) : " + e.toString());
        }
    }

    public static void writeFile(String[] lines, String filePath)
    {
        writeFile(lines, new File(filePath));
    }
}
