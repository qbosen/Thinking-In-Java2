//: io/GetChannel.java
package io; /* Added by Eclipse.py */
// Getting channels from streams

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class GetChannel {
    private static final int BSIZE = 1024;

    public static void main(String[] args) throws Exception {
        // Write a file:
        FileChannel fc =
                new FileOutputStream("data.txt").getChannel();
        fc.write(ByteBuffer.wrap("Some text ".getBytes()));
        fc.close();
        // Add to the end of the file:
        fc =
                new RandomAccessFile("data.txt", "rw").getChannel();
        fc.position(fc.size()); // Move to the end
        fc.write(ByteBuffer.wrap("Some more".getBytes()));
        fc.close();
        // Read the file:
        fc = new FileInputStream("data.txt").getChannel();
        ByteBuffer buff = ByteBuffer.allocate(BSIZE);
        fc.read(buff);
        buff.flip();
        while (buff.hasRemaining())
            System.out.print((char) buff.get());
    }

    @Test
    public void test() throws Exception {
        FileChannel channel = new FileOutputStream("data.txt").getChannel();
        channel.write(ByteBuffer.wrap("中文123!@#$；：abcd".getBytes()));
        channel.close();
        channel = new RandomAccessFile("data.txt", "rw").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);

        print(buffer);
        int mark = buffer.position();
        System.out.println("rewind: 重置pos");
        buffer.rewind();
        print(buffer);

        System.out.println("reset: 重置pos -> mark");
        buffer.position(mark);
        print(buffer);

        System.out.println("flip: 重置pos limit");
        buffer.flip();
        print(buffer);

        System.out.println("无法读取中文部分");
        while (buffer.hasRemaining()) {
            // 中文 用byte无法读取
            System.out.print((char) buffer.get());
        }
        System.out.println();
        System.out.println("使用 new String(...) 读取中文");

        System.out.println(new String(buffer.array(), 0, buffer.limit()));

        System.out.println("使用 BufferedReader 读取中文");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer.array(), 0, buffer.limit())));

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }
    }

    private void print(ByteBuffer buffer) {
        System.out.println(String.format("pos:%d\tlimit:%d\tcapacity:%d\n", buffer.position(), buffer.limit(), buffer.capacity()));
    }
} /* Output:
Some text Some more
*///:~
