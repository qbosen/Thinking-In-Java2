//: typeinfo/ClassCasts.java
package typeinfo; /* Added by Eclipse.py */

import org.junit.Test;

class Building {
}

class House extends Building {
}

public class ClassCasts {
    public static void main(String[] args) {
        Building b = new House();
        Class<House> houseType = House.class;
        House h = houseType.cast(b);
        h = (House) b; // ... or just do this.
    }

    @Test
    public void test() {
        Object a = new A();
        try {
            B b = (B) a;
        } catch (Exception e) {
            System.out.println("error when force cast a to b");
        }

        try {
            B.class.cast(a);
        } catch (Exception e) {
            System.out.println("error when force cast a to b");
        }
    }

    @Test
    public void test2() {
        A a = new A();
        try {
            B b = (B) ((Object) a);
            // !编译失败
            // B b = (B) a;
        } catch (Exception e) {
            System.out.println("error when force cast a to b");
        }

        try {
            B.class.cast(a);
        } catch (Exception e) {
            System.out.println("error when force cast a to b");
        }
    }

    class A {
    }

    class B {
    }
} ///:~
