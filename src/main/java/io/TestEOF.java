//: io/TestEOF.java
package io; /* Added by Eclipse.py */
// Testing for end of file while reading a byte at a time.

import custom.Utils;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class TestEOF {
    public static void main(String[] args)
            throws IOException {
        DataInputStream in = new DataInputStream(
                new BufferedInputStream(
                        new FileInputStream(Utils.relativeFilePath(TestEOF.class))));
        while (in.available() != 0)
            System.out.print((char) in.readByte());
    }


    @Test
    public void test() throws Exception {
        byte[] buf = new byte[512];
        DataInputStream inputStream = new DataInputStream(
                new BufferedInputStream(
                        new FileInputStream(Utils.relativeFilePath(getClass()))));

        while (inputStream.available() != 0) {
            int read = inputStream.read(buf);
            System.out.print(new String(buf, 0, read));
        }
        inputStream.close();
    }
} /* (Execute to see output) *///:~
