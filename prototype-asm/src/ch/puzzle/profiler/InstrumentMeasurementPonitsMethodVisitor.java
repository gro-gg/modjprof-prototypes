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
		mv.visitLdcInsn(className);
		mv.visitLdcInsn(methodName);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "ch/puzzle/profiler/Profiler", "notifyEnterMethod",
                "(Ljava/lang/String;Ljava/lang/String;)V", false);
	}

	@Override
	public void visitInsn(int opcode) {
        if (opcode == Opcodes.RETURN) {
			mv.visitLdcInsn(className);
			mv.visitLdcInsn(methodName);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "ch/puzzle/profiler/Profiler", "notifyExitMethod",
                    "(Ljava/lang/String;Ljava/lang/String;)V", false);
		}
		super.visitInsn(opcode);
	}
}
