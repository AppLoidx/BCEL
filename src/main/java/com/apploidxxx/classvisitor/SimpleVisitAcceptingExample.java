package com.apploidxxx.classvisitor;

import com.apploidxxx.parse.ClassParserExample;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Arthur Kupriyanov
 */
public class SimpleVisitAcceptingExample {
    public static void main(String[] args) throws IOException {

        InputStream inputStream = ClassParserExample.class.getResourceAsStream("/compiled/ClassA.class");
        if (inputStream == null) throw new FileNotFoundException();

        ClassParser parser = new ClassParser(inputStream, "ClassA.class");

        JavaClass clazz = parser.parse();


        clazz.accept(new SimpleVisitor());

        // Visited JavClass with name: com.apploidxxx.parse.ClassA


        clazz.getMethods()[0].accept(new SimpleVisitor());

//      Visited method with name: <init>
//      And with return type: void


        clazz.getMethods()[1].accept(new SimpleVisitor());

//      Visited method with name: main
//      And with return type: void

    }
}

/*

public class ClassA {
    public ClassA() {
    }

    public static void main(String[] args) {
        System.out.println("Hello world");
    }
}

 */
