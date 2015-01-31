package ch.puzzle.profiler;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ByteArrayClassPath;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class MethodCallClassFileTransformer implements ClassFileTransformer {

	private ClassPool pool;

	@Override
	public byte[] transform(ClassLoader loader, String className,
			Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {

		System.out.println(" Loading class: " + className);
		if (className.startsWith("ch/puzzle/sample/")) {
			pool = ClassPool.getDefault();
			pool.insertClassPath(new ByteArrayClassPath(className, classfileBuffer));
			try {
				CtClass ctClass = pool.get(className.replaceAll("/", "."));
				return instrumentAllMethods(ctClass).toBytecode();
			} catch (NotFoundException | CannotCompileException | IOException e) {
				e.printStackTrace();
			}
		}
		return classfileBuffer;
	}

	private CtClass instrumentAllMethods(CtClass ctClass)
			throws CannotCompileException {
		if (!ctClass.isFrozen()) {
			for (CtMethod declaredMethod : ctClass.getDeclaredMethods()) {
				declaredMethod.insertBefore(createEnterInstrumentationString(
						ctClass.getName(), declaredMethod.getName()));
				declaredMethod.insertAfter(createExitInstrumentationString(
						ctClass.getName(), declaredMethod.getName()));
			}
		}
		return ctClass;
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
