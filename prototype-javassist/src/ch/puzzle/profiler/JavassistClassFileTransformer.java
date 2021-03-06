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
import javassist.Modifier;
import javassist.NotFoundException;

public class JavassistClassFileTransformer implements ClassFileTransformer {

    private ClassPool pool;

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        if (className.startsWith("ch/puzzle") || className.startsWith("najs")) {
            // System.out.println(" Loading class: " + className);
            pool = ClassPool.getDefault();
            pool.insertClassPath(new ByteArrayClassPath(className, classfileBuffer));
            try {
                CtClass ctClass = pool.get(className.replaceAll("/", "."));
                int modifiers = ctClass.getModifiers();
                if (!Modifier.isAnnotation(modifiers) && !Modifier.isEnum(modifiers)
                        && !Modifier.isInterface(modifiers)) {
                    return instrumentAllMethods(ctClass).toBytecode();
                }
            } catch (NotFoundException e) {
                // do nothing
            } catch (CannotCompileException | IOException e) {
                e.printStackTrace();
            }
        }
        return classfileBuffer;
    }

    private CtClass instrumentAllMethods(CtClass ctClass) throws CannotCompileException {
        if (!ctClass.isFrozen()) {
            for (CtMethod declaredMethod : ctClass.getDeclaredMethods()) {
                // System.out.println("   instrumenting method " +
                // declaredMethod.getName());
                int modifiers = declaredMethod.getModifiers();
                if (!Modifier.isAbstract(modifiers)) {
                    declaredMethod.insertBefore("ch.puzzle.profiler.Profiler.notifyEnterMethod(\"" + ctClass.getName()
                            + "\", \"" + declaredMethod.getName() + "\");");
                    declaredMethod.insertAfter("ch.puzzle.profiler.Profiler.notifyExitMethod(\"" + ctClass.getName()
                            + "\", \"" + declaredMethod.getName() + "\");");
                }
            }
        }
        return ctClass;
    }

}
