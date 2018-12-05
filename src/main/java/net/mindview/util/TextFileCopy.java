//: net/mindview/util/TextFile.java
// Static functions for reading and writing text files as
// a single string, and treating a file as an ArrayList.
package net.mindview.util;

import custom.Utils;

import java.io.*;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class TextFileCopy extends ArrayList<String> {
    // Read a file, split by any regular expression:
    public TextFileCopy(String fileName, String splitter) {
        super(Arrays.asList(read(fileName).split(splitter)));
        // Regular expression split() often leaves an empty
        // String at the first position:
        if (get(0).equals("")) remove(0);
    }

    // Normally read by lines:
    public TextFileCopy(String fileName) {
        this(fileName, "\n");
    }

    // Read a file as a single string:
    public static String read(String fileName) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new FileReader(new File(fileName).getCanonicalFile()))) {
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    // Write a single file in one method call:
    public static void write(String fileName, String text) {
        try (PrintWriter out = new PrintWriter(new File(fileName).getCanonicalFile())) {
            out.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String file = read(Utils.relativeFilePath(TextFileCopy.class));
        write("test.txt", file);
        TextFileCopy text = new TextFileCopy("test.txt");
        text.write("test2.txt");
        // Break into unique sorted list of words:
        TextFileCopy brokens = new TextFileCopy(Utils.relativeFilePath(TextFileCopy.class), "\\W+");
        {
            TreeSet<String> words = new TreeSet<String>(brokens);
            brokens.write("test3.txt");
            System.out.println(words);
        }
        {
            // api 计数
            Map<String, Long> collect = brokens.stream().collect(Collectors.groupingBy(String::toString, Collectors.counting()));
            System.out.println(collect.get("text"));
        }
        {
            // 需要用数组保存结果(对象), 最后使用finisher 取值
            Collector<String, int[], Integer> collector = Collector.of(
                    () -> new int[1],
                    (a, b) -> a[0]++,
                    (a1, a2) -> new int[]{a1[0] + a2[0]},
                    (a) -> a[0]
            );
            Map<String, Integer> collect = brokens.stream().collect(Collectors.groupingBy(String::toString, collector));
            System.out.println(collect.get("text"));
        }
        {
            // 如果使用基础类型, 无法保存结果
            Collector<String, Integer, Integer> counter = Collector.of(
                    () -> 0,
                    (a, x) -> a++,
                    (a, b) -> a + b
            );
            Map<String, Integer> collect = brokens.stream().collect(Collectors.groupingBy(String::toString, counter));
            System.out.println(collect.get("text"));
        }
        {
            // 直接构造一个收集器
            Collector<String, Map<String, Integer>, Map<String, Integer>> counter = Collector.of(
                    HashMap::new,
                    (map, key) -> {
                        int put = map.getOrDefault(key, 0) + 1;
                        map.put(key, put);
                    },
                    (ma, mb) -> {
                        Map<String, Integer> less, more;
                        boolean isMore = ma.size() > mb.size();
                        more = isMore ? ma : mb;
                        less = isMore ? mb : ma;
                        less.forEach(
                                (k, v) -> {
                                    int put = more.getOrDefault(k, 0) + v;
                                    more.put(k, put);
                                }
                        );
                        return more;
                    });
            Map<String, Integer> collect = brokens.stream().collect(counter);
            System.out.println(collect.get("text"));
        }

        {
            // 使用reduce构造加法器
            Map<String, Integer> collect = brokens.stream().collect(
                    Collectors.groupingBy(String::toString,
                            Collectors.reducing(0, (s) -> 1, (a, b) -> a + b)
                    ));
            System.out.println(collect.get("text"));
        }
    }

    public void write(String fileName) {
        try (PrintWriter out = new PrintWriter(
                new File(fileName).getCanonicalFile())) {

            for (String item : this)
                out.println(item);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

/* Output:
[0, ArrayList, Arrays, Break, BufferedReader, BufferedWriter, Clean, Display, File, FileReader, FileWriter, IOException, Normally, Output, PrintWriter, Read, Regular, RuntimeException, Simple, Static, String, StringBuilder, System, TextFile, Tools, TreeSet, W, Write]
*///:~
