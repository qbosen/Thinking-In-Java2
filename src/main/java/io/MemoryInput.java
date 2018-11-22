//: io/MemoryInput.java
package io; /* Added by Eclipse.py */

import java.io.IOException;
import java.io.StringReader;

public class MemoryInput {
    public static void main(String[] args)
            throws IOException {
        StringReader in = new StringReader(
                BufferedInputFile.read("src/main/java/io/MemoryInput.java"));
        int c;
        while ((c = in.read()) != -1)
            System.out.print((char) c);
    }
} /* (Execute to see output) *///:~
