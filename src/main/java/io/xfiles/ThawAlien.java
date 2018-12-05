//: io/xfiles/ThawAlien.java
package io.xfiles; /* Added by Eclipse.py */
// Try to recover a serialized file without the
// class of object that's stored in that file.
// {RunByHand}

import io.FreezeAlien;
import org.junit.Test;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

public class ThawAlien {
    public static void main(String[] args) throws Exception {
        ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(new File(".", "X.file")));
        Object mystery = in.readObject();
        System.out.println(mystery.getClass());
    }

    @Test
    public void test() throws Exception {
        // compile Alien.class
        compile("src/main/java/io/Alien.java");
        main(null);
    }

    // class加载到内存中，删除物理文件一样能在内存中获取
    // 所以反序列化后的class需要被jvm读取才能使用
    @Test(expected = ClassNotFoundException.class)
    public void withoutAlienClass() throws Exception {
        // delete Alien.class
        new File("target/classes/io/Alien.class").delete();
        main(null);
    }


    public void compile(String... files) {
        //获取系统Java编译器
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        //获取Java文件管理器
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, Charset.forName("UTF-8"));

        //定义要编译的源文件
//        File file = new File("src/main/java/io/Alien.java");
        //通过源文件获取到要编译的Java类源码迭代器，包括所有内部类，其中每个类都是一个 JavaFileObject，也被称为一个汇编单元
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(files);

        List<String> options = Arrays.asList("-d", "target/classes");
        //生成编译任务
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, options, null, compilationUnits);
        //执行编译任务
        task.call();
    }
} /* Output:
class Alien
*///:~
