package com.apploidxxx.parse;

import org.apache.bcel.classfile.*;
import org.apache.commons.codec.binary.Hex;

import java.io.*;

/**
 * @author Arthur Kupriyanov
 */
public class ClassParserExample {


    public static void main(String... args) throws IOException, ClassNotFoundException {


        // read file from resources/complied/ClassA.class
        InputStream inputStream = ClassParserExample.class.getResourceAsStream("/compiled/ClassA.class");
        if (inputStream == null) throw new FileNotFoundException();

        ClassParser parser = new ClassParser(inputStream, "ClassA.class");

        JavaClass clazz = parser.parse();

        final String HEX_BYTECODE = getHex(clazz.getBytes());

        System.out.println("Hex bytecode: ");
        System.out.println(HEX_BYTECODE);
        System.out.println();

        final String MINOR_VER      =   getHex(clazz.getMinor());
        final String MAJOR_VER      =   getHex(clazz.getMajor());
        final String CONSTANT_POOL  =   getHex(clazz.getConstantPool().getConstantPool());
        final String ACCESS_FLAGS   =   getHex(clazz.getAccessFlags());
        final String THIS_CLASS     =   getHex(clazz.getClassName().getBytes());
        final String SUPER_CLASS    =   getHex(clazz.getSuperClass().getBytes());
        final String INTERFACES     =   getHex(clazz.getInterfaces());
        final String FIELDS         =   getHex(clazz.getFields());
        final String METHODS        =   getHex(clazz.getMethods());
        final String ATTRIBUTES     =   getHex(clazz.getAttributes());

        System.out.println( "minor: "           + MINOR_VER    );   // 0
        System.out.println( "major: "           + MAJOR_VER    );   // 34
        System.out.println( "constant pool: "   + CONSTANT_POOL);   // not correctly
        System.out.println( "access flags: "    + ACCESS_FLAGS );   // 21
        System.out.println( "this class: "      + THIS_CLASS   );
        System.out.println( "super class: "     + SUPER_CLASS  );   // Object
        System.out.println( "interfaces: "      + INTERFACES   );   // <empty>
        System.out.println( "fields: "          + FIELDS       );   // <empty>
        System.out.println( "methods: "         + METHODS      );   // one method: psvm hello world
        System.out.println( "attributes: "      + ATTRIBUTES   );   // 536f7572636546696c65 - I think it's instructions for Java tools

    }

    private static String getHex(byte[] bytes){
        return Hex.encodeHexString(bytes);
    }

    private static String getHex(int intNum){
        return Integer.toHexString(intNum);
    }

    private static String getHex(Constant[] constants){
        if (constants == null) return null;

        StringBuilder sb = new StringBuilder();
        for (Constant c : constants){
            if (c == null) continue;
            sb.append(getHex(c.getTag())).append(" ");
        }

        return sb.toString();
    }

    private static String getHex(JavaClass[] clazzes){
        if (clazzes == null) return null;

        StringBuilder sb = new StringBuilder();
        for (JavaClass c : clazzes){
            sb.append(getHex(c.getClassName().getBytes())).append(" ");
        }

        return sb.toString();
    }

    private static String getHex(Field[] fields){
        if (fields == null) return null;

        StringBuilder sb = new StringBuilder();
        for (Field c : fields){
            sb.append(getHex(c.getName().getBytes())).append(" ");
        }

        return sb.toString();
    }

    private static String getHex(Method[] methods){
        if (methods == null) return null;

        StringBuilder sb = new StringBuilder();
        for (Method c : methods){
            sb.append(getHex(c.getName().getBytes())).append(" ");
        }

        return sb.toString();
    }

    private static String getHex(Attribute[] attributes){
        if (attributes == null) return null;

        StringBuilder sb = new StringBuilder();
        for (Attribute c : attributes){
            sb.append(getHex(c.getName().getBytes())).append(" ");
        }

        return sb.toString();
    }

}

/*
    Class A:

    public class ClassA {
        public static void main(String[] args) {
            System.out.println("Hello world");
        }
    }
 */

/*
    Class A bytecode:

    cafe babe 0000 0034 0022 0a00 0600 1409
    0015 0016 0800 170a 0018 0019 0700 1a07
    001b 0100 063c 696e 6974 3e01 0003 2829
    5601 0004 436f 6465 0100 0f4c 696e 654e
    756d 6265 7254 6162 6c65 0100 124c 6f63
    616c 5661 7269 6162 6c65 5461 626c 6501
    0004 7468 6973 0100 1d4c 636f 6d2f 6170
    706c 6f69 6478 7878 2f70 6172 7365 2f43
    6c61 7373 413b 0100 046d 6169 6e01 0016
    285b 4c6a 6176 612f 6c61 6e67 2f53 7472
    696e 673b 2956 0100 0461 7267 7301 0013
    5b4c 6a61 7661 2f6c 616e 672f 5374 7269
    6e67 3b01 000a 536f 7572 6365 4669 6c65
    0100 0b43 6c61 7373 412e 6a61 7661 0c00
    0700 0807 001c 0c00 1d00 1e01 000b 4865
    6c6c 6f20 776f 726c 6407 001f 0c00 2000
    2101 001b 636f 6d2f 6170 706c 6f69 6478
    7878 2f70 6172 7365 2f43 6c61 7373 4101
    0010 6a61 7661 2f6c 616e 672f 4f62 6a65
    6374 0100 106a 6176 612f 6c61 6e67 2f53
    7973 7465 6d01 0003 6f75 7401 0015 4c6a
    6176 612f 696f 2f50 7269 6e74 5374 7265
    616d 3b01 0013 6a61 7661 2f69 6f2f 5072
    696e 7453 7472 6561 6d01 0007 7072 696e
    746c 6e01 0015 284c 6a61 7661 2f6c 616e
    672f 5374 7269 6e67 3b29 5600 2100 0500
    0600 0000 0000 0200 0100 0700 0800 0100
    0900 0000 2f00 0100 0100 0000 052a b700
    01b1 0000 0002 000a 0000 0006 0001 0000
    0006 000b 0000 000c 0001 0000 0005 000c
    000d 0000 0009 000e 000f 0001 0009 0000
    0037 0002 0001 0000 0009 b200 0212 03b6
    0004 b100 0000 0200 0a00 0000 0a00 0200
    0000 0800 0800 0900 0b00 0000 0c00 0100
    0000 0900 1000 1100 0000 0100 1200 0000
    0200 13
 */

/*
    Assembled code:

    Classfile /C:/java/BCEL/src/main/resources/compiled/ClassA.class
      Last modified 08.12.2019; size 563 bytes
      MD5 checksum bcd0198f6764a1dc2f3967fef701452e
      Compiled from "ClassA.java"
    public class com.apploidxxx.parse.ClassA
      minor version: 0
      major version: 52
      flags: ACC_PUBLIC, ACC_SUPER
    Constant pool:
       #1 = Methodref          #6.#20         // java/lang/Object."<init>":()V
       #2 = Fieldref           #21.#22        // java/lang/System.out:Ljava/io/PrintStream;
       #3 = String             #23            // Hello world
       #4 = Methodref          #24.#25        // java/io/PrintStream.println:(Ljava/lang/String;)V
       #5 = Class              #26            // com/apploidxxx/parse/ClassA
       #6 = Class              #27            // java/lang/Object
       #7 = Utf8               <init>
       #8 = Utf8               ()V
       #9 = Utf8               Code
      #10 = Utf8               LineNumberTable
      #11 = Utf8               LocalVariableTable
      #12 = Utf8               this
      #13 = Utf8               Lcom/apploidxxx/parse/ClassA;
      #14 = Utf8               main
      #15 = Utf8               ([Ljava/lang/String;)V
      #16 = Utf8               args
      #17 = Utf8               [Ljava/lang/String;
      #18 = Utf8               SourceFile
      #19 = Utf8               ClassA.java
      #20 = NameAndType        #7:#8          // "<init>":()V
      #21 = Class              #28            // java/lang/System
      #22 = NameAndType        #29:#30        // out:Ljava/io/PrintStream;
      #23 = Utf8               Hello world
      #24 = Class              #31            // java/io/PrintStream
      #25 = NameAndType        #32:#33        // println:(Ljava/lang/String;)V
      #26 = Utf8               com/apploidxxx/parse/ClassA
      #27 = Utf8               java/lang/Object
      #28 = Utf8               java/lang/System
      #29 = Utf8               out
      #30 = Utf8               Ljava/io/PrintStream;
      #31 = Utf8               java/io/PrintStream
      #32 = Utf8               println
      #33 = Utf8               (Ljava/lang/String;)V
    {
      public com.apploidxxx.parse.ClassA();
        descriptor: ()V
        flags: ACC_PUBLIC
        Code:
          stack=1, locals=1, args_size=1
             0: aload_0
             1: invokespecial #1                  // Method java/lang/Object."<init>":()V
             4: return
          LineNumberTable:
            line 6: 0
          LocalVariableTable:
            Start  Length  Slot  Name   Signature
                0       5     0  this   Lcom/apploidxxx/parse/ClassA;

      public static void main(java.lang.String[]);
        descriptor: ([Ljava/lang/String;)V
        flags: ACC_PUBLIC, ACC_STATIC
        Code:
          stack=2, locals=1, args_size=1
             0: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
             3: ldc           #3                  // String Hello world
             5: invokevirtual #4                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
             8: return
          LineNumberTable:
            line 8: 0
            line 9: 8
          LocalVariableTable:
            Start  Length  Slot  Name   Signature
                0       9     0  args   [Ljava/lang/String;
    }
    SourceFile: "ClassA.java
 */