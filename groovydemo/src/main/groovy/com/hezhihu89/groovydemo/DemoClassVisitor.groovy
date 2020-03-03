package com.hezhihu89.groovydemo

import org.apache.http.util.TextUtils
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.TypePath;


/**
 * @author hezhihu89
 * 创建时间 2020 年 03 月 02 日 17:34
 * 功能描述:
 */

class DemoClassVisitor extends ClassVisitor {

    String className;

    DemoClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM6, classVisitor)
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
//        println("visit 读取class： version: " + version + "  name: " + name + " signature: " + signature +
//        "  superName: " + superName + " interface: " + (null != interfaces?Arrays.toString(interfaces):"空"))
        super.visit(version, access, name, signature, superName, interfaces);
        className = name
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        println("visitMethod 读取class： access: " + access + "  name: " + name + " descriptor: " + descriptor +
                    "  signature: " + signature + " exceptions: " + (null != exceptions ? Arrays.toString(exceptions) : "空"))
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions)
        return new DemoMethodVisitor(mv,className,name)
    }


    @Override
    AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        println("visitAnnotation 读取class： desc: " + desc + "  visible: " + visible)
        if("Lcom/hezhihu89/adapter/DemoClassInject;".equals(desc)){
            MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC,"demoTestFunction","()V",null,null)
            mv.visitLdcInsn(className);
            mv.visitLdcInsn(className + "------->" + "demoTestFunction");
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false);
            mv.visitInsn(Opcodes.RETURN)
            mv.visitMaxs(50,50)
            mv.visitEnd()
        }
        return super.visitAnnotation(desc, visible)
    }

    @Override
    void visitEnd() {
        super.visitEnd()
    }
}
