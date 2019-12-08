# Apache Commons BCEL

Used Maven dependency:

*pom.xml*

```xml
<dependency>
    <groupId>org.apache.bcel</groupId>
    <artifactId>bcel</artifactId>
    <version>6.4.0</version>
</dependency>
```

## About BCEL

The Byte Code Engineering Library (Apache Commons BCEL™) is intended to give users a convenient way to analyze, create, and manipulate (binary) Java class files (those ending with .class). Classes are represented by objects which contain all the symbolic information of the given class: methods, fields and byte code instructions, in particular.

Such objects can be read from an existing file, be transformed by a program (e.g. a class loader at run-time) and written to a file again. An even more interesting application is the creation of classes from scratch at run-time. The Byte Code Engineering Library (BCEL) may be also useful if you want to learn about the Java Virtual Machine (JVM) and the format of Java .class files.

BCEL contains a byte code verifier named JustIce, which usually gives you much better information about what's wrong with your code than the standard JVM message.

BCEL is already being used successfully in several projects such as compilers, optimizers, obsfuscators, code generators and analysis tools. Unfortunately there hasn't been much development going on over the past few years. Feel free to help out or you might want to have a look into the ASM project at objectweb.

## About this repository

This folder (BCEL) a  part of my JVM-research.