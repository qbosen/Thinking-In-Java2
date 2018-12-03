//: io/TransferTo.java
package io; /* Added by Eclipse.py */
// Using transferTo() between channels
// {Args: TransferTo.java TransferTo.txt}

import custom.Utils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

@SuppressWarnings("Duplicates")
public class TransferTo {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("arguments: sourcefile destfile");
            System.exit(1);
        }
        FileChannel
                in = new FileInputStream(args[0]).getChannel(),
                out = new FileOutputStream(args[1]).getChannel();
        in.transferTo(0, in.size(), out);
        // Or:
        // out.transferFrom(in, 0, in.size());
    }

    @Test
    public void testMain() throws Exception {
        TransferTo.main(Utils.toArr(Utils.relativeFilePath(TransferTo.class), TransferTo.class.getSimpleName() + ".out"));
    }
} ///:~
