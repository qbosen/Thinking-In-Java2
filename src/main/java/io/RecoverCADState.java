//: io/RecoverCADState.java
package io; /* Added by Eclipse.py */
// Restoring the state of the pretend CAD system.
// {RunFirst: StoreCADState}

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.util.List;

public class RecoverCADState {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("CADState.out"));
        // Read in the same order they were written:

        // static 变量在反序列化的时候，读取的是jvm中class的static变量值。
        // 和序列化的时候 static 变量值无关。
        // 通过改变当前jvm中 static 变量的值 可以观察到这一点。
        Field circleColor = Circle.class.getDeclaredField("color");
        circleColor.setAccessible(true);
        circleColor.set(null, 4);

        List<Class<? extends Shape>> shapeTypes =
                (List<Class<? extends Shape>>) in.readObject();
        Line.deserializeStaticState(in);
        List<Shape> shapes = (List<Shape>) in.readObject();
        System.out.println(shapes);
    }
} /* Output:
[class Circlecolor[1] xPos[58] yPos[55] dim[93]
, class Squarecolor[0] xPos[61] yPos[61] dim[29]
, class Linecolor[3] xPos[68] yPos[0] dim[22]
, class Circlecolor[1] xPos[7] yPos[88] dim[28]
, class Squarecolor[0] xPos[51] yPos[89] dim[9]
, class Linecolor[3] xPos[78] yPos[98] dim[61]
, class Circlecolor[1] xPos[20] yPos[58] dim[16]
, class Squarecolor[0] xPos[40] yPos[11] dim[22]
, class Linecolor[3] xPos[4] yPos[83] dim[6]
, class Circlecolor[1] xPos[75] yPos[10] dim[42]
]
*///:~
