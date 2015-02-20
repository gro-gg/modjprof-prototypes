package ch.puzzle.profiler;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class InstrumentMethodClassVisitor extends ClassVisitor {

	private String className;

	public InstrumentMethodClassVisitor(ClassVisitor classVisitor, String className) {
		super(Opcodes.ASM5, classVisitor);
		this.className = className;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		System.out.println("   instrumenting method " + name);
		// + " access:" + access + " desc:" + desc + " signature:" + signature);
		MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
		return methodVisitor;
	}

	private String createEnterInstrumentationString(String className,
			String methodName) {
		return createInstrumentationString(className, methodName, ">");
	}

	private String createExitInstrumentationString(String className,
			String methodName) {
		return createInstrumentationString(className, methodName, "<");
	}

	private String createInstrumentationString(String className,
			String methodName, String flowPattern) {
		StringBuilder sb = new StringBuilder();
		sb.append("System.out.println(\" ");
		sb.append(flowPattern);
		sb.append(" ");
		sb.append(className);
		sb.append(".");
		sb.append(methodName);
		sb.append("()\");");
		return sb.toString();
	}

}
