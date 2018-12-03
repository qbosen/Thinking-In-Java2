//: io/BufferToText.java
package io; /* Added by Eclipse.py */
// Converting text to and from ByteBuffers

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class BufferToText {
    private static final int BSIZE = 1024;

    public static void main(String[] args) throws Exception {
        FileChannel fc =
                new FileOutputStream("data2.txt").getChannel();
        String content = "Some text中文；。123@#";
        fc.write(ByteBuffer.wrap(content.getBytes()));
        fc.close();
        fc = new FileInputStream("data2.txt").getChannel();
        ByteBuffer buff = ByteBuffer.allocate(BSIZE);
        fc.read(buff);
        buff.flip();
        // Doesn't work:
        System.out.println("直接打印CharBuffer: 乱码，因为文件是默认的utf-8, 打印解码使用 utf-16be");
        System.out.println(buff.asCharBuffer());
        // Decode using this system's default Charset:
        buff.rewind();
        String encoding = System.getProperty("file.encoding");
        System.out.println("使用文件的默认编码解码： " + encoding);
        System.out.println(Charset.forName(encoding).decode(buff));

        // Or, we could encode with something that will print:
        fc = new FileOutputStream("data2.txt").getChannel();
        fc.write(ByteBuffer.wrap(
                content.getBytes("UTF-16BE")));
        fc.close();
        // Now try reading again:
        fc = new FileInputStream("data2.txt").getChannel();
        buff.clear();
        fc.read(buff);
        buff.flip();
        System.out.println("使用utf-16be写文件，然后直接打印");
        System.out.println(buff.asCharBuffer());
        // Use a CharBuffer to write through:
        fc = new FileOutputStream("data2.txt").getChannel();


        buff = ByteBuffer.allocate(50); // More than needed
        System.out.println("通过asCharBuffer放入数据。两个buffer修改共享,标记独立");
        CharBuffer charBuffer = buff.asCharBuffer();
        charBuffer.put(content);
        System.out.println("手动控制标记");
        int charPos = charBuffer.position();
        buff.limit(charPos * 2);    // charbuffer 一个顶俩

        fc.write(buff);
        fc.close();
        // Read and display:
        fc = new FileInputStream("data2.txt").getChannel();
        buff.clear();
        fc.read(buff);
        buff.flip();
        System.out.println(buff.asCharBuffer());
    }
} /* Output:
????
Decoded using Cp1252: Some text
Some text
Some text
*///:~
