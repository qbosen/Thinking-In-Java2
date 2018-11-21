//: io/BufferedInputFile.java
package io; /* Added by Eclipse.py */

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BufferedInputFile {

    private final static String FILENAME = "src/main/java/io/BufferedInputFile.java";

    // Throw exceptions to console:
    public static String
    read(String filename) throws IOException {
        // Reading input by lines:
        BufferedReader in = new BufferedReader(
                new FileReader(filename));
        String s;
        StringBuilder sb = new StringBuilder();
        while ((s = in.readLine()) != null)
            sb.append(s + "\n");
        in.close();
        return sb.toString();
    }

    public static void main(String[] args)
            throws IOException {
        System.out.print(read(FILENAME));
    }

    @Test
    public void testDirectlyFileReader() throws IOException {
        FileReader fileReader = new FileReader(FILENAME);
        StringBuilder stringBuilder = new StringBuilder();
        int i;
        while ((i = fileReader.read()) != -1)
            stringBuilder.append(((char) i));
        System.out.println(stringBuilder);
    }

    @Test
    public void testDirectlyFileReader2() throws IOException {
        FileReader fileReader = new FileReader(FILENAME);
        StringBuilder stringBuilder = new StringBuilder();
        char[] buff = new char[1024];
        int size;
        while ((size = fileReader.read(buff)) != -1)
            stringBuilder.append(buff, 0, size);

        System.out.println(stringBuilder);
    }
} /* (Execute to see output) *///:~
