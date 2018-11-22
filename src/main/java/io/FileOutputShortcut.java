//: io/FileOutputShortcut.java
package io; /* Added by Eclipse.py */

import custom.Utils;
import org.junit.Test;

import java.io.*;

public class FileOutputShortcut {
    static String file = "FileOutputShortcut.out";

    public static void main(String[] args)
            throws IOException {
        BufferedReader in = new BufferedReader(
                new StringReader(
                        BufferedInputFile.read(Utils.relativeFilePath(FileOutputShortcut.class))));
        // Here's the shortcut:
        PrintWriter out = new PrintWriter(file);
        int lineCount = 1;
        String s;
        while ((s = in.readLine()) != null)
            out.println(lineCount++ + ": " + s);
        out.close();
        // Show the stored file:
        System.out.println(BufferedInputFile.read(file));
    }

    @Test
    public void test() throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(Utils.relativeFilePath(FileOutputShortcut.class)));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        int lineCount = 1;
        String s;
        while ((s = in.readLine()) != null) {
            out.println(lineCount++ + ": " + s);
        }
        out.close();
        in.close();

        System.out.println(BufferedInputFile.read(file));
    }
} /* (Execute to see output) *///:~
