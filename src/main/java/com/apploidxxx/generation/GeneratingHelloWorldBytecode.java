package com.apploidxxx.generation;

import java.io.IOException;

import org.apache.bcel.Const;
import org.apache.bcel.generic.*;

/**
 * @author Arthur Kupriyanov
 */
public class GeneratingHelloWorldBytecode {
    public static void main(final String[] argv) {
        final ClassGen cg = new ClassGen("HelloWorld", "java.lang.Object",
                "<generated>", Const.ACC_PUBLIC |
                Const.ACC_SUPER,
                null);
        final ConstantPoolGen cp = cg.getConstantPool(); // cg creates constant pool
        final InstructionList il = new InstructionList();

        final MethodGen mg = new MethodGen(
                Const.ACC_STATIC | Const.ACC_PUBLIC,           // access_flag
                Type.VOID,                                                  // return type
                new Type[]{new ArrayType(Type.STRING, 1)},                  // argument type
                new String[]{"argv"}, // arg names
                "main", "HelloWorld",               // method, class
                il, cp);
        final InstructionFactory factory = new InstructionFactory(cg);

        final ObjectType i_stream = new ObjectType("java.io.InputStream");
        final ObjectType p_stream = new ObjectType("java.io.PrintStream");

        // Create BufferedReader object and store it in local variable `in'.
        il.append(factory.createNew("java.io.BufferedReader"));          // new #8 // class java/io/BufferedReader
        il.append(InstructionConst.DUP);                                    // Use predefined constant, i.e. flyweight
        il.append(factory.createNew("java.io.InputStreamReader"));      // new #10 // class java/io/InputStreamReader
        il.append(InstructionConst.DUP);
        il.append(factory.createFieldAccess("java.lang.System", "in", i_stream, Const.GETSTATIC));

        // Call constructors, i.e. BufferedReader(InputStreamReader())
        il.append(factory.createInvoke("java.io.InputStreamReader", "<init>",
                Type.VOID, new Type[]{i_stream},
                Const.INVOKESPECIAL));
        il.append(factory.createInvoke("java.io.BufferedReader", "<init>", Type.VOID,
                new Type[]{new ObjectType("java.io.Reader")},
                Const.INVOKESPECIAL));

        // Create local variable `in'
        LocalVariableGen lg = mg.addLocalVariable("in", new ObjectType("java.io.BufferedReader"), null, null);
        final int in = lg.getIndex();
        lg.setStart(il.append(new ASTORE(in))); // `i' valid from here

        // Create local variable `name'
        lg = mg.addLocalVariable("name", Type.STRING, null, null);
        final int name = lg.getIndex();
        il.append(InstructionConst.ACONST_NULL);
        lg.setStart(il.append(new ASTORE(name))); // `name' valid from here

        // try { ...
        final InstructionHandle try_start =
                il.append(factory.createFieldAccess("java.lang.System", "out", p_stream, Const.GETSTATIC));

        il.append(new PUSH(cp, "Please enter your name> "));
        il.append(factory.createInvoke("java.io.PrintStream", "print", Type.VOID,
                new Type[]{Type.STRING}, Const.INVOKEVIRTUAL));
        il.append(new ALOAD(in));
        il.append(factory.createInvoke("java.io.BufferedReader", "readLine",
                Type.STRING, Type.NO_ARGS, Const.INVOKEVIRTUAL));
        il.append(new ASTORE(name));

        // Upon normal execution we jump behind exception handler, the target address is not known yet.
        final GOTO g = new GOTO(null);
        final InstructionHandle try_end = il.append(g);

        /* } catch() { ... }
         * Add exception handler: print exception and return from method
         */
        final InstructionHandle handler =
                il.append(factory.createFieldAccess("java.lang.System", "out", p_stream, Const.GETSTATIC));
        // Little trick in order not to save exception object temporarily
        il.append(InstructionConst.SWAP);

        il.append(factory.createInvoke("java.io.PrintStream", "println", Type.VOID, new Type[]{Type.OBJECT}, Const.INVOKEVIRTUAL));
        il.append(InstructionConst.RETURN);
        mg.addExceptionHandler(try_start, try_end, handler, new ObjectType("java.io.IOException"));

        // Normal code continues, now we can set the branch target of the GOTO that jumps over the handler code.
        final InstructionHandle ih =
                il.append(factory.createFieldAccess("java.lang.System", "out", p_stream, Const.GETSTATIC));
        g.setTarget(ih);

        // String concatenation compiles to StringBuffer operations.
        il.append(factory.createNew(Type.STRINGBUFFER));
        il.append(InstructionConst.DUP);
        il.append(new PUSH(cp, "Hello, "));
        il.append(factory.createInvoke("java.lang.StringBuffer", "<init>",
                Type.VOID, new Type[]{Type.STRING},
                Const.INVOKESPECIAL));
        il.append(new ALOAD(name));

        // Concatenate strings using a StringBuffer and print them.
        il.append(factory.createInvoke("java.lang.StringBuffer", "append",
                Type.STRINGBUFFER, new Type[]{Type.STRING},
                Const.INVOKEVIRTUAL));
        il.append(factory.createInvoke("java.lang.StringBuffer", "toString",
                Type.STRING, Type.NO_ARGS,
                Const.INVOKEVIRTUAL));

        il.append(factory.createInvoke("java.io.PrintStream", "println",
                Type.VOID, new Type[]{Type.STRING},
                Const.INVOKEVIRTUAL));

        il.append(InstructionConst.RETURN);

        mg.setMaxStack(5); // Needed stack size
        cg.addMethod(mg.getMethod());

        il.dispose(); // Reuse instruction handles

        // Add public <init> method, i.e. empty constructor
        cg.addEmptyConstructor(Const.ACC_PUBLIC);

        // Get JavaClass object and dump it to file.
        try {
            cg.getJavaClass().dump("HelloWorld.class");
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}

/*
    Generated class:

    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStreamReader;

    public class HelloWorld {
        public static void main(String[] argv) {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String name = null;

            try {
                System.out.print("Please enter your name> ");
                name = in.readLine();
            } catch (IOException var3) {
                System.out.println(var3);
                return;
            }

            System.out.println("Hello, " + name);
        }

        public HelloWorld() {
        }
    }
 */

/*
    Assemble code:

    Classfile /C:/java/BCEL/HelloWorld.class
      Last modified 09.12.2019; size 1063 bytes
      MD5 checksum ad55e3c20a751ccb067f2ab9ff27feb4
      Compiled from "<generated>"
    public class HelloWorld
      minor version: 3
      major version: 45
      flags: ACC_PUBLIC, ACC_SUPER
    Constant pool:
       #1 = Utf8               SourceFile
       #2 = Utf8               <generated>
       #3 = Utf8               HelloWorld
       #4 = Class              #3             // HelloWorld
       #5 = Utf8               java/lang/Object
       #6 = Class              #5             // java/lang/Object
       #7 = Utf8               java/io/BufferedReader
       #8 = Class              #7             // java/io/BufferedReader
       #9 = Utf8               java/io/InputStreamReader
      #10 = Class              #9             // java/io/InputStreamReader
      #11 = Utf8               java/lang/System
      #12 = Class              #11            // java/lang/System
      #13 = Utf8               in
      #14 = Utf8               Ljava/io/InputStream;
      #15 = NameAndType        #13:#14        // in:Ljava/io/InputStream;
      #16 = Fieldref           #12.#15        // java/lang/System.in:Ljava/io/InputStream;
      #17 = Utf8               <init>
      #18 = Utf8               (Ljava/io/InputStream;)V
      #19 = NameAndType        #17:#18        // "<init>":(Ljava/io/InputStream;)V
      #20 = Methodref          #10.#19        // java/io/InputStreamReader."<init>":(Ljava/io/InputStream;)V
      #21 = Utf8               (Ljava/io/Reader;)V
      #22 = NameAndType        #17:#21        // "<init>":(Ljava/io/Reader;)V
      #23 = Methodref          #8.#22         // java/io/BufferedReader."<init>":(Ljava/io/Reader;)V
      #24 = Utf8               out
      #25 = Utf8               Ljava/io/PrintStream;
      #26 = NameAndType        #24:#25        // out:Ljava/io/PrintStream;
      #27 = Fieldref           #12.#26        // java/lang/System.out:Ljava/io/PrintStream;
      #28 = Utf8               Please enter your name>
      #29 = String             #28            // Please enter your name>
      #30 = Utf8               print
      #31 = Utf8               (Ljava/lang/String;)V
      #32 = NameAndType        #30:#31        // print:(Ljava/lang/String;)V
      #33 = Utf8               java/io/PrintStream
      #34 = Class              #33            // java/io/PrintStream
      #35 = Methodref          #34.#32        // java/io/PrintStream.print:(Ljava/lang/String;)V
      #36 = Utf8               readLine
      #37 = Utf8               ()Ljava/lang/String;
      #38 = NameAndType        #36:#37        // readLine:()Ljava/lang/String;
      #39 = Methodref          #8.#38         // java/io/BufferedReader.readLine:()Ljava/lang/String;
      #40 = Utf8               println
      #41 = Utf8               (Ljava/lang/Object;)V
      #42 = NameAndType        #40:#41        // println:(Ljava/lang/Object;)V
      #43 = Methodref          #34.#42        // java/io/PrintStream.println:(Ljava/lang/Object;)V
      #44 = Utf8               java/lang/StringBuffer
      #45 = Class              #44            // java/lang/StringBuffer
      #46 = Utf8               Hello,
      #47 = String             #46            // Hello,
      #48 = NameAndType        #17:#31        // "<init>":(Ljava/lang/String;)V
      #49 = Methodref          #45.#48        // java/lang/StringBuffer."<init>":(Ljava/lang/String;)V
      #50 = Utf8               append
      #51 = Utf8               (Ljava/lang/String;)Ljava/lang/StringBuffer;
      #52 = NameAndType        #50:#51        // append:(Ljava/lang/String;)Ljava/lang/StringBuffer;
      #53 = Methodref          #45.#52        // java/lang/StringBuffer.append:(Ljava/lang/String;)Ljava/lang/StringBuffer;
      #54 = Utf8               toString
      #55 = NameAndType        #54:#37        // toString:()Ljava/lang/String;
      #56 = Methodref          #45.#55        // java/lang/StringBuffer.toString:()Ljava/lang/String;
      #57 = NameAndType        #40:#31        // println:(Ljava/lang/String;)V
      #58 = Methodref          #34.#57        // java/io/PrintStream.println:(Ljava/lang/String;)V
      #59 = Utf8               main
      #60 = Utf8               ([Ljava/lang/String;)V
      #61 = Utf8               argv
      #62 = Utf8               [Ljava/lang/String;
      #63 = Utf8               Ljava/io/BufferedReader;
      #64 = Utf8               name
      #65 = Utf8               Ljava/lang/String;
      #66 = Utf8               LocalVariableTable
      #67 = Utf8               java/io/IOException
      #68 = Class              #67            // java/io/IOException
      #69 = Utf8               Code
      #70 = Utf8               ()V
      #71 = NameAndType        #17:#70        // "<init>":()V
      #72 = Methodref          #6.#71         // java/lang/Object."<init>":()V
      #73 = Utf8               this
      #74 = Utf8               LHelloWorld;
    {
      public static void main(java.lang.String[]);
        descriptor: ([Ljava/lang/String;)V
        flags: ACC_PUBLIC, ACC_STATIC
        Code:
          stack=5, locals=3, args_size=1
             0: new           #8                  // class java/io/BufferedReader
             3: dup
             4: new           #10                 // class java/io/InputStreamReader
             7: dup
             8: getstatic     #16                 // Field java/lang/System.in:Ljava/io/InputStream;
            11: invokespecial #20                 // Method java/io/InputStreamReader."<init>":(Ljava/io/InputStream;)V
            14: invokespecial #23                 // Method java/io/BufferedReader."<init>":(Ljava/io/Reader;)V
            17: astore_1
            18: aconst_null
            19: astore_2
            20: getstatic     #27                 // Field java/lang/System.out:Ljava/io/PrintStream;
            23: ldc           #29                 // String Please enter your name>
            25: invokevirtual #35                 // Method java/io/PrintStream.print:(Ljava/lang/String;)V
            28: aload_1
            29: invokevirtual #39                 // Method java/io/BufferedReader.readLine:()Ljava/lang/String;
            32: astore_2
            33: goto          44
            36: getstatic     #27                 // Field java/lang/System.out:Ljava/io/PrintStream;
            39: swap
            40: invokevirtual #43                 // Method java/io/PrintStream.println:(Ljava/lang/Object;)V
            43: return
            44: getstatic     #27                 // Field java/lang/System.out:Ljava/io/PrintStream;
            47: new           #45                 // class java/lang/StringBuffer
            50: dup
            51: ldc           #47                 // String Hello,
            53: invokespecial #49                 // Method java/lang/StringBuffer."<init>":(Ljava/lang/String;)V
            56: aload_2
            57: invokevirtual #53                 // Method java/lang/StringBuffer.append:(Ljava/lang/String;)Ljava/lang/StringBuffer;
            60: invokevirtual #56                 // Method java/lang/StringBuffer.toString:()Ljava/lang/String;
            63: invokevirtual #58                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
            66: return
          Exception table:
             from    to  target type
                20    36    36   Class java/io/IOException
          LocalVariableTable:
            Start  Length  Slot  Name   Signature
                0      67     0  argv   [Ljava/lang/String;
               17      50     1    in   Ljava/io/BufferedReader;
               19      48     2  name   Ljava/lang/String;

      public HelloWorld();
        descriptor: ()V
        flags: ACC_PUBLIC
        Code:
          stack=1, locals=1, args_size=1
             0: aload_0
             1: invokespecial #72                 // Method java/lang/Object."<init>":()V
             4: return
          LocalVariableTable:
            Start  Length  Slot  Name   Signature
                0       5     0  this   LHelloWorld;
    }
    SourceFile: "<generated>"

 */
