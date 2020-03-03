package com.hezhihu89.groovydemo

import com.android.build.api.transform.Context
import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarInputStream
import java.util.jar.JarOutputStream

/**
 * @author hezhihu89
 * 创建时间 2020 年 03 月 02 日 14:54
 * 功能描述:
 */
class DemoTransform extends Transform {

    Project mProject;

    public DemoTransform(Project project) {
        this.mProject = project;
    }

    @Override
    public String getName() {
        return "DemoTransform";
    }


    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }


    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }


    @Override
    public boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)

        try {
            StringBuilder stringBuilder = new StringBuilder()
            stringBuilder.append("临时文件已被删除\n")

            def inputs = transformInvocation.getInputs();
            def outputProvider = transformInvocation.outputProvider;

            for (TransformInput input : inputs) {

                for (JarInput jarInput : input.jarInputs) {
                    String log = transformJar(transformInvocation.getContext(), outputProvider, jarInput)
                    stringBuilder.append(log)
                }

                for (DirectoryInput directoryInput : input.directoryInputs) {
                    transformClass(outputProvider, directoryInput)

                    def dest = outputProvider.getContentLocation(
                            directoryInput.name,
                            directoryInput.getContentTypes(),
                            directoryInput.getScopes(),
                            Format.DIRECTORY
                    )
                    println("文件源路径： " + directoryInput.file.absolutePath + "    文件输出路径： " + dest.absolutePath)
                    FileUtils.copyDirectory(directoryInput.file, dest)
                }
            }

            File logFile = new File(transformInvocation.getContext().getTemporaryDir(),"临时文件操作日志.txt")
            if(logFile.exists() && !logFile.delete()){
                logFile.delete()
            }
            logFile.createNewFile()
            FileOutputStream fileOutputStream = new FileOutputStream(logFile)
            byte[] logByte = stringBuilder.toString().bytes
            fileOutputStream.write(logByte)
            fileOutputStream.flush()
            fileOutputStream.close()

        }catch(Exception e){
            e.printStackTrace()
        }



    }

    /**
     * 处理 class 文件
     * @param outputProvider
     * @param directoryInput
     */
    static void transformClass(TransformOutputProvider outputProvider, DirectoryInput directoryInput){
        directoryInput.file.eachFileRecurse{File file ->
            def fileName = file.name
            if(fileName.endsWith(".class")
                    && !fileName.endsWith("R.class")
                    && !fileName.endsWith("BuildConfig.class")
                    && !fileName.contains("R\$")){

                byte[] srcBytes = IOUtils.toByteArray(new FileInputStream(file))
                //toByteArray方法会将最终修改的字节码以 byte 数组形式返回。
                byte[] bytes = modifyClass(srcBytes,fileName)
                if(null == bytes){
                    bytes = srcBytes
                }
                //通过文件流写入方式覆盖掉原先的内容，实现class文件的改写。
                //这个地址在javac目录下
                FileOutputStream outputStream = new FileOutputStream(file.path)
                outputStream.write(bytes)
                outputStream.close()
            }
        }
    }

    /**
     * 处理 jar 包文件
     * 流程：
     * 1.创建临时jar 包输出路径
     *      context.getTemporaryDir() 获取项目临时路径
     *      创建jar 包输入流：
     *      JarOutputStream 将修改的字节码输入到 临时包中

     * 2.创建新的class 文件节点 -->>> 节点获取输入流
     *      将节点添加到 临时jar包流 new JarEntry(节点名)
     *
     * 3.解析jar包获取class文件节点
     *      JarFile(inputJar.file).entries() 获取节点
     *      获取 节点的 字节流
     *      JarEntry.getInputStream()
     * 4.修改class 字节流 将修改后的字节流 写入临时jar 字节流
     *
     * 5.将临时文件拷贝到本地地址
     * @param outputProvider
     * @param jarInput
     */
    static String transformJar(Context context, TransformOutputProvider outputProvider, JarInput jarInput){
        ///获取临时jar包路径
        String jarTempPath = context.getTemporaryDir()
        String tempJarName = "temp_" + jarInput.file.name
        File tempJarFile = new File(jarTempPath,tempJarName)
        ///创建临时jar包写入流
        JarOutputStream tempJarOutputStream = new JarOutputStream(new FileOutputStream(tempJarFile))

        ///遍历原始jar包资源节点
        JarFile jarFile = new JarFile(jarInput.file)
        Enumeration<JarEntry> jarEntrys = jarFile.entries()
        while (jarEntrys.hasMoreElements()){
            JarEntry jarEntry = jarEntrys.nextElement()
            String jarEntryClassName = jarEntry.name
            if(jarEntryClassName.endsWith(".class")) {
                InputStream jarEntryInputStream = jarFile.getInputStream(jarEntry)
                byte[] jarClassByte = IOUtils.toByteArray(jarEntryInputStream)
                byte[] modifyClassByte = modifyClass(jarClassByte,jarEntryClassName)
                if (null == modifyClassByte) {
                    modifyClassByte = jarClassByte
                }

                JarEntry modifyClassEntry = new JarEntry(jarEntryClassName)
                tempJarOutputStream.putNextEntry(modifyClassEntry)
                tempJarOutputStream.write(modifyClassByte)
                tempJarOutputStream.closeEntry()
            }
        }
        tempJarOutputStream.close()
        jarFile.close()


        ///jar包名字
        String destName = jarInput.file.name
        ///序列化名字
        String hexName = DigestUtils.md5Hex(jarInput.getFile().getAbsolutePath().substring(0,8))
        if(destName.endsWith(".jar")){
            destName = destName.substring(0,destName.length()-4)
        }
        ///目标目录
        File dest = outputProvider.getContentLocation(destName + "_" + hexName,
                jarInput.getContentTypes(),
                jarInput.getScopes(),Format.JAR)

        StringBuilder stringBuilder = new StringBuilder()
        stringBuilder.append(tempJarFile.absolutePath + "------------>>>> " + dest.absolutePath + "\n")
        println("jar临时路径： " + tempJarFile.absolutePath + " jar包输出路径： " + dest.absolutePath)
        FileUtils.copyFile(tempJarFile, dest);

        if(tempJarFile.exists() && !tempJarFile.delete()){
            tempJarFile.deleteOnExit()
        }
        return stringBuilder.toString()
    }


    private static byte[] modifyClass(byte[] classByte,String name){
        if(name.endsWith("Activity.class")) {
            //读取class 文件
            ClassReader classReader = new ClassReader(classByte)
            //对class文件的写入
            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
            //访问class文件相应的内容，解析到某一个结构就会通知到ClassVisitor的相应方法
            ClassVisitor classVisitor = new DemoClassVisitor(classWriter)
            //依次调用 ClassVisitor接口的各个方法
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
            return classWriter.toByteArray()
        }
        return classByte
    }

}
///遍历