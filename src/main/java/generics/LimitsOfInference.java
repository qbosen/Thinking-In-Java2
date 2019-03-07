//: generics/LimitsOfInference.java
package generics; /* Added by Eclipse.py */

import net.mindview.util.New;
import typeinfo.pets.Person;
import typeinfo.pets.Pet;

import java.util.List;
import java.util.Map;

public class LimitsOfInference {
    static void
    f(Map<Person, List<? extends Pet>> petPeople) {
    }

    public static void main(String[] args) {
        // jdk8 类型推断增强 可以编译
        f(New.map()); // Does not compile
    }
} ///:~
