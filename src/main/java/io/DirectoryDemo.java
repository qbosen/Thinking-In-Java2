//: io/DirectoryDemo.java
package io; /* Added by Eclipse.py */
// Sample use of Directory utilities.

import net.mindview.util.Directory;
import net.mindview.util.PPrint;

import java.io.File;

import static net.mindview.util.Print.print;

public class DirectoryDemo {
    public static void main(String[] args) {
        // All directories:
        String start = "src/main/java/io";
        PPrint.pprint(Directory.walk(start).dirs);
        // All files beginning with 'T'
        for (File file : Directory.local(start, "T.*"))
            print(file);
        print("----------------------");
        // All Java files beginning with 'T':
        for (File file : Directory.walk(start, "T.*\\.java"))
            print(file);
        print("======================");
        // Java files containing "Z" or "z":
        for (File file : Directory.walk(start, ".*[Zz].*\\.java"))
            print(file);
    }
} /* Output: (Sample)
[.\xfiles]
.\TestEOF.class
.\TestEOF.java
.\TransferTo.class
.\TransferTo.java
----------------------
.\TestEOF.java
.\TransferTo.java
.\xfiles\ThawAlien.java
======================
.\FreezeAlien.class
.\GZIPcompress.class
.\ZipCompress.class
*///:~
