package com.hezhihu89.groovydemo

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class DemoMethodVisitor extends MethodVisitor {
    private String className;
    private String methodName;

    DemoMethodVisitor(MethodVisitor methodVisitor, String className, String methodName) {
        super(Opcodes.ASM6, methodVisitor);
        this.className = className;
        this.methodName = methodName;
    }

    //方法执行前插入
    @Override
    void visitCode() {
        super.visitCode();

        mv.visitLdcInsn(className);
        mv.visitLdcInsn(className + "------->" + methodName);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(Opcodes.POP);

    }

    //方法执行后插入
    @Override
    void visitInsn(int opcode) {
        super.visitInsn(opcode);

    }

    @Override
    void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack, maxLocals)
    }

    @Override
    AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        println("注解输出："+ className + "  注解描述" + desc)
        return super.visitAnnotation(desc, visible)
    }

    @Override
    void visitEnd() {
        super.visitEnd();
    }
}