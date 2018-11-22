package custom;

import org.junit.Test;

/**
 * @author qiubaisen
 * @date 2018/11/21
 */
public class Utils {
    public static <T> T[] toArr(T... ts) {
        return ts;
    }

    public static String relativeFilePath(Class clazz) {
        return relativeFilePath(clazz, "java");
    }


    public static String relativeFilePath(Class clazz, String ext) {
        return String.format("src/main/java/%s/%s.%s", clazz.getPackage().getName(), clazz.getSimpleName(), ext);
    }

    @Test
    public void test() {
        System.out.println(relativeFilePath(getClass()));
    }
}
