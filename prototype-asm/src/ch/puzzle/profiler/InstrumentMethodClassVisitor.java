package ch.puzzle.profiler;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class InstrumentMethodClassVisitor extends ClassVisitor {

	private String className;

	public InstrumentMethodClassVisitor(ClassVisitor classVisitor,
			String className) {
		super(Opcodes.ASM5, classVisitor);
		this.className = className;
	}

	@Override
	public MethodVisitor visitMethod(int access, String methodName,
			String desc, String signature, String[] exceptions) {
		MethodVisitor parentMethodVisitor = super.visitMethod(access,
				methodName, desc, signature, exceptions);
		if (!methodName.equals("<init>")) {
			//System.out.println("   instrumenting method " + methodName);
			MethodVisitor instrumentMeasurementPonitsMethodVisitor = new InstrumentMeasurementPonitsMethodVisitor(
					parentMethodVisitor, methodName, className);
			return instrumentMeasurementPonitsMethodVisitor;
		}
		return parentMethodVisitor;
	}

}
