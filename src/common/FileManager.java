package common;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileManager {
    public static boolean checkIfFile(String filePath) {
        return (new File(filePath)).isFile();
    }

    public static List<File> getFileList(String inputDirPath) {
        List<File> fileList = new ArrayList<>();
        File[] files = (new File(inputDirPath)).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                fileList.add(file);
            }
        }

        Collections.sort(fileList);
        return fileList;
    }

    public static List<String> getFilePathList(String inputDirPath) {
        List<String> filePathList = new ArrayList<>();
        File[] files = (new File(inputDirPath)).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                filePathList.add(file.toString());
            }
        }

        Collections.sort(filePathList);
        return filePathList;
    }

    public static void makeParentDir(File file) {
        if (file.getParent() != null) {
            File dir = new File(file.getParent());
            if (dir != null && !dir.exists()) {
                dir.mkdirs();
            }
        }
    }

    public static String removeExtension(String filePath) {
        if (filePath == null) {
            return null;
        }

        int index = filePath.lastIndexOf(".");
        if (index != -1) {
            return filePath.substring(0, index);
        }
        return filePath;
    }

    public static String[] readFile(File file) {
        List<String> lineList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                lineList.add(line);
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error @ readFile(File) : " + e.toString());
        }
        return lineList.toArray(new String[0]);
    }

    public static String[] readFile(String filePath) {
        return readFile(new File(filePath));
    }

    public static void writeFile(String[] lines, File file) {
        makeParentDir(file);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (int i = 0; i < lines.length; i++) {
                bw.write(lines[i]);
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            System.out.println("Error @ writeFile(String[], File) : " + e.toString());
        }
    }

    public static void writeFile(String[] lines, String filePath) {
        writeFile(lines, new File(filePath));
    }
}
