package com.apploidxxx.parse;

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
        final MethodGen mg = new MethodGen(Const.ACC_STATIC |
                Const.ACC_PUBLIC,// access flags
                Type.VOID,              // return type
                new Type[]{            // argument types
                        new ArrayType(Type.STRING, 1)
                },
                new String[]{"argv"}, // arg names
                "main", "HelloWorld",    // method, class
                il, cp);
        final InstructionFactory factory = new InstructionFactory(cg);

        final ObjectType i_stream = new ObjectType("java.io.InputStream");
        final ObjectType p_stream = new ObjectType("java.io.PrintStream");

        // Create BufferedReader object and store it in local variable `in'.
        il.append(factory.createNew("java.io.BufferedReader"));
        il.append(InstructionConst.DUP); // Use predefined constant, i.e. flyweight
        il.append(factory.createNew("java.io.InputStreamReader"));
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
            System.err.println(e);
        }
    }
}
