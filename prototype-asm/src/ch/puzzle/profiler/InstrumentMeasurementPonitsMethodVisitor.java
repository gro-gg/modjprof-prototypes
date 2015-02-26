package ch.puzzle.profiler;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class InstrumentMeasurementPonitsMethodVisitor extends MethodVisitor {

	private String methodName;
	private String className;
	
	public InstrumentMeasurementPonitsMethodVisitor(
			MethodVisitor methodVisitor, String methodName, String className) {
		super(Opcodes.ASM5, methodVisitor);
		this.methodName = methodName;
		this.className = className;
	}

	@Override
	public void visitCode() {
		super.visitCode();
		mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn(createEnterInstrumentationString(className, methodName));
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
	}

	@Override
	public void visitInsn(int opcode) {
		if (opcode == Opcodes.RETURN) {
			mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			mv.visitLdcInsn(createExitInstrumentationString(className, methodName));
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
		}
		super.visitInsn(opcode);
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
