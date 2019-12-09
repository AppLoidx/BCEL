package com.apploidxxx.parse;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.Type;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Arthur Kupriyanov
 */
public class ParsedClassUsingExample {


    public static void main(String[] args) throws IOException {
        JavaClass classWithMethods = readJavaClass();

        for (Method method: classWithMethods.getMethods()
             ) {
            switch (method.getName()){
                case "publicMethod":
                    System.out.println("publicMethod()");

                    assertEquals(method.getReturnType(), Type.VOID);
                    assertTrue(method.isPublic());
                    System.out.println("max locals : " + method.getCode().getMaxLocals());
                    System.out.println("max stack : " + method.getCode().getMaxStack());
            }
        }

    }
    private static JavaClass readJavaClass() throws IOException {
        InputStream inputStream = ClassParserExample.class.getResourceAsStream("/compiled/ClassWithMethods.class");
        if (inputStream == null) throw new FileNotFoundException();

        ClassParser parser = new ClassParser(inputStream, "ClassWithMethods.class");

        return parser.parse();
    }
}
