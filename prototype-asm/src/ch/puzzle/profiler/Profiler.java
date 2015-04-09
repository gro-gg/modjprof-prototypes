package ch.puzzle.profiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.instrument.Instrumentation;

public class Profiler {

    private static PrintWriter writer;

    public static void premain(String agentArgs, Instrumentation inst) throws IOException {
        writer = new PrintWriter(new FileWriter(new File("/tmp/profiler.txt")), true);
        System.out.println("Starting the ASM agent... premain()");
        inst.addTransformer(new ASMClassFileTransformer());
    }

    public static void notifyEnterMethod(String className, String methodName) {
        // System.out.println(createEnterInstrumentationString(className,
        // methodName));
        writer.println(createEnterInstrumentationString(className, methodName));
    }

    public static void notifyExitMethod(String className, String methodName) {
        // System.out.println(createExitInstrumentationString(className,
        // methodName));
        writer.println(createExitInstrumentationString(className, methodName));
    }

    private static String createEnterInstrumentationString(String className, String methodName) {
        return createInstrumentationString(className, methodName, ">");
    }

    private static String createExitInstrumentationString(String className, String methodName) {
        return createInstrumentationString(className, methodName, "<");
    }

    private static String createInstrumentationString(String className, String methodName, String flowPattern) {
        StringBuilder sb = new StringBuilder();
        sb.append(" ");
        sb.append(flowPattern);
        sb.append(" ");
        sb.append(className.replaceAll("/", "."));
        sb.append(".");
        sb.append(methodName);
        sb.append("()");
        return sb.toString();
    }
}
