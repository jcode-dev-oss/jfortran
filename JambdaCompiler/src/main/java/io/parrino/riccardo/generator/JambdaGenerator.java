package io.parrino.riccardo.generator;

import io.parrino.riccardo.parser.FuncDecl;
import io.parrino.riccardo.parser.Node;
import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class JambdaGenerator {

    ClassWriter cw;
    List<Node> ast;

    public void init(List<Node> ast) {
        this.ast = ast;
    }

    private void openMainClass() {
        this.cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

        this.cw.visit(
                V21,
                ACC_PUBLIC,
                "Hello", // to be overwritten with the real name of the file containing the code TODO
                null,
                "java/lang/Object",
                null
        );
    }

    private void closeMainClass() {
        this.cw.visitEnd();
    }

    // Generate the simple add function add: a,b -> a+b as utility function
    private void generateAddFunction() {
        // Declare the method
        MethodVisitor mv = this.cw.visitMethod(
                ACC_PUBLIC | ACC_STATIC,
                "add",
                "(II)I",
                null,
                null
        );

        // Generate the method body
        mv.visitCode();

        mv.visitVarInsn(ILOAD, 0);
        mv.visitVarInsn(ILOAD, 1);

        mv.visitInsn(IADD);

        mv.visitInsn(IRETURN);

        mv.visitMaxs(0,0);
        mv.visitEnd();
    }

    private void generateMulFunction() {
        // Define the method
        MethodVisitor mv = this.cw.visitMethod(
                ACC_PUBLIC | ACC_STATIC,
                "mul",
                "(II)I",
                null,
                null
        );

        // Generate the method body
        mv.visitCode();

        mv.visitVarInsn(ILOAD, 0);
        mv.visitVarInsn(ILOAD, 1);

        mv.visitInsn(IMUL);
        mv.visitInsn(IRETURN);

        mv.visitMaxs(0,0);
        mv.visitEnd();
    }

    private void generateDivFunction() {
        // Define the method
        MethodVisitor mv = this.cw.visitMethod(
                ACC_PUBLIC | ACC_STATIC,
                "div",
                "(II)I",
                null,
                null
        );

        // Generate method body
        mv.visitCode();

        mv.visitVarInsn(ILOAD, 0);
        mv.visitVarInsn(ILOAD, 1);

        mv.visitInsn(IDIV);
        mv.visitInsn(IRETURN);

        mv.visitMaxs(0,0);
        mv.visitEnd();
    }

    private void generateSubFunction() {
        // Define the method
        MethodVisitor mv = this.cw.visitMethod(
                ACC_PUBLIC | ACC_STATIC,
                "sub",
                "(II)I",
                null,
                null
        );

        // Generate method body
        mv.visitCode();

        mv.visitVarInsn(ILOAD, 0);
        mv.visitVarInsn(ILOAD, 1);

        mv.visitInsn(ISUB);
        mv.visitInsn(IRETURN);

        mv.visitMaxs(0,0);
        mv.visitEnd();
    }

    private void generateIdFunction() {
        // Define the method
        MethodVisitor mv = this.cw.visitMethod(
                ACC_PUBLIC | ACC_STATIC,
                "id",
                "(I)I",
                null,
                null
        );

        // Generate method body
        mv.visitCode();

        mv.visitVarInsn(ILOAD, 0);

        mv.visitInsn(IRETURN);

        mv.visitMaxs(0,0);
        mv.visitEnd();
    }

    private void generateSimpleConstructor() {
        // Generate the constructor of the class
        MethodVisitor mv = this.cw.visitMethod(
                ACC_PUBLIC,
                "<init>",
                "()V",
                null,
                null
        );

        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(
                INVOKESPECIAL,
                "java/lang/Object",
                "<init>",
                "()V",
                false
        );

        mv.visitInsn(RETURN);
        mv.visitMaxs(0,0);
        mv.visitEnd();
    }

    private String generateFunctionDescriptor(FuncDecl funcDecl) {
        String paramsDescr = funcDecl
                .getParams()
                .stream()
                .map(p -> StringUtils.capitalize(p.getType()
                        .getValue()
                        .substring(0,1)))
                .reduce((a,b)->a+b)
                .orElseGet(String::new);
        paramsDescr = "(" + paramsDescr + ")";
        String returnTypeDescr = StringUtils.capitalize(funcDecl.getReturnType().getValue().substring(0,1));
        return paramsDescr+returnTypeDescr;
    }

    private void generateFuncDecl(FuncDecl funcDecl) {
        MethodVisitor mv = cw.visitMethod(
                ACC_PUBLIC,
                funcDecl.getFunctionName(),
                this.generateFunctionDescriptor(funcDecl),
                null,
                null
        );

        // body code generation goes here
        mv.visitCode();
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);

        mv.visitEnd();
    }

    private void generateMainMethod() {
        MethodVisitor mv = cw.visitMethod(
                ACC_PUBLIC | ACC_STATIC,
                "main",
                "([Ljava/lang/String;)V",
                null,
                null
        );

        // body code generation goes here
        mv.visitCode();

        mv.visitMethodInsn(
                INVOKESTATIC,
                "Hello",
                "main",
                "()V",
                false
        );

        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);

        mv.visitEnd();
    }

    // TODO: input() function as take input utility function
    // TODO: print() function as System.out.println(...) utility

    public byte[] generate() {

        // Generate the class
        this.openMainClass();

        // Generate the constructor of the class
        generateSimpleConstructor();

        // Generate the first function of the file
        for (Node node : ast) {
            generateFuncDecl((FuncDecl) node);
        }

        // generate main mathematical operation function as utility
        this.generateSubFunction();
        this.generateAddFunction();
        this.generateDivFunction();
        this.generateMulFunction();
        this.generateIdFunction();

        // generate test condition function
        
        // generate the public static void main(String [] args) method calling the main function
        this.generateMainMethod();

        this.closeMainClass();

        return this.cw.toByteArray();
    }

}
