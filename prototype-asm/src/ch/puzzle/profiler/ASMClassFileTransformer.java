package ch.puzzle.profiler;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class ASMClassFileTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        if (className.startsWith("ch/puzzle") || className.startsWith("najs")) {
            // System.out.println(" Loading class: " + className);

            ClassReader classReader = new ClassReader(classfileBuffer);
            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);
            ClassVisitor instrumentMethodClassVisitor = new InstrumentMethodClassVisitor(classWriter, className);
            if ((classReader.getAccess() & (Opcodes.ACC_INTERFACE + Opcodes.ACC_ENUM + Opcodes.ACC_ANNOTATION)) == 0) {
                classReader.accept(instrumentMethodClassVisitor, 0);
                return classWriter.toByteArray();
            }
        }
        return classfileBuffer;
    }

}
