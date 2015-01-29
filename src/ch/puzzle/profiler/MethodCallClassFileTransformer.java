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

	@Override
	public byte[] transform(ClassLoader loader, String className,
			Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {

		System.out.println(" Loading class: " + className);

		ClassPool pool = ClassPool.getDefault();
		pool.insertClassPath(new ByteArrayClassPath(className, classfileBuffer));
		CtClass ctClass = null;
		try {
			ctClass = pool.get(className.replaceAll("/", "."));
			if (!ctClass.isFrozen()) {
				for (CtMethod declaredMethod : ctClass.getDeclaredMethods()) {
					declaredMethod.insertBefore(
							createEnterInstrumentationString(className, declaredMethod.getName()));
					declaredMethod.insertAfter(createExitInstrumentationString(className, declaredMethod.getName()));
				}
				return ctClass.toBytecode();
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (CannotCompileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classfileBuffer;
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
