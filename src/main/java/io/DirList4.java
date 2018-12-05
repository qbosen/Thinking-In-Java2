//: io/DirList3.java
package io; /* Added by Eclipse.py */
// Building the anonymous inner class "in-place."
// {Args: "D.*\.java"}

import custom.Utils;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Pattern;

public class DirList4 {
    public static void main(final String[] args) {
        File path = new File(".");
        String[] list;
        if (args.length == 0)
            list = path.list();
        else
            list = path.list(
                    (dir, name) -> {
                        Pattern pattern = Pattern.compile(args[0]);
                        return pattern.matcher(name).matches();
                    });
        Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
        for (String dirItem : list)
            System.out.println(dirItem);
    }

    @Test
    public void testFilter() {
        DirList.main(Utils.toArr(new String[]{}));
        System.out.println("--- filter: contains 'git' ---");
        DirList.main(Utils.toArr(".*?git.*"));
    }
} /* Output:
DirectoryDemo.java
DirList.java
DirList2.java
DirList3.java
*///:~
