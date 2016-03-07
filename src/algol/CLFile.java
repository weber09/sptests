/**
 * Created by Gabriel on 06/03/2016.
 */

package algol;

import java.io.IOException;
import java.util.ArrayList;

import static algol.CLConstants.*;

public class CLFile {

    public long magic; // 0xCAFEBABE

    public int minorVersion;

    public int majorVersion;

    public int constantPoolCount;

    public CLConstantPool constantPool;

    public int accessFlags;

    public int thisClass;

    public int superClass;

    public int interfacesCount;

    public ArrayList<Integer> interfaces;

    public int fieldsCount;

    public ArrayList<CLFieldInfo> fields;

    public int methodsCount;

    public ArrayList<CLMethodInfo> methods;

    public int attributesCount;

    public ArrayList<CLAttributeInfo> attributes;

    public void write(CLOutputStream out) throws IOException {
        out.writeInt(magic);
        out.writeShort(minorVersion);
        out.writeShort(majorVersion);
        out.writeShort(constantPoolCount);
        constantPool.write(out);
        out.writeShort(accessFlags);
        out.writeShort(thisClass);
        out.writeShort(superClass);
        out.writeShort(interfacesCount);
        for (int i = 0; i < interfaces.size(); i++) {
            Integer index = interfaces.get(i);
            out.writeShort(index.intValue());
        }
        out.writeShort(fieldsCount);
        for (int i = 0; i < fields.size(); i++) {
            CLMemberInfo fieldInfo = fields.get(i);
            if (fieldInfo != null) {
                fieldInfo.write(out);
            }
        }
        out.writeShort(methodsCount);
        for (int i = 0; i < methods.size(); i++) {
            CLMemberInfo methodInfo = methods.get(i);
            if (methodInfo != null) {
                methodInfo.write(out);
            }
        }
        out.writeShort(attributesCount);
        for (int i = 0; i < attributes.size(); i++) {
            CLAttributeInfo attributeInfo = attributes.get(i);
            if (attributeInfo != null) {
                attributeInfo.write(out);
            }
        }
    }

    public static String classAccessFlagsToString(int accessFlags) {
        StringBuffer b = new StringBuffer();
        if ((accessFlags & ACC_PUBLIC) != 0) {
            b.append("public ");
        }
        if ((accessFlags & ACC_FINAL) != 0) {
            b.append("final ");
        }
        if ((accessFlags & ACC_SUPER) != 0) {
            b.append("super ");
        }
        if ((accessFlags & ACC_INTERFACE) != 0) {
            b.append("interface ");
        }
        if ((accessFlags & ACC_ABSTRACT) != 0) {
            b.append("abstract ");
        }
        if ((accessFlags & ACC_SYNTHETIC) != 0) {
            b.append("synthetic ");
        }
        if ((accessFlags & ACC_ANNOTATION) != 0) {
            b.append("annotation ");
        }
        if ((accessFlags & ACC_ENUM) != 0) {
            b.append("enum ");
        }
        return b.toString().trim();
    }

    public static String innerClassAccessFlagsToString(int accessFlags) {
        StringBuffer b = new StringBuffer();
        if ((accessFlags & ACC_PUBLIC) != 0) {
            b.append("public ");
        }
        if ((accessFlags & ACC_PRIVATE) != 0) {
            b.append("private ");
        }
        if ((accessFlags & ACC_PROTECTED) != 0) {
            b.append("protected ");
        }
        if ((accessFlags & ACC_STATIC) != 0) {
            b.append("static ");
        }
        if ((accessFlags & ACC_FINAL) != 0) {
            b.append("final ");
        }
        if ((accessFlags & ACC_INTERFACE) != 0) {
            b.append("interface ");
        }
        if ((accessFlags & ACC_ABSTRACT) != 0) {
            b.append("abstract ");
        }
        if ((accessFlags & ACC_SYNTHETIC) != 0) {
            b.append("synthetic ");
        }
        if ((accessFlags & ACC_ANNOTATION) != 0) {
            b.append("annotation ");
        }
        if ((accessFlags & ACC_ENUM) != 0) {
            b.append("enum ");
        }
        return b.toString().trim();
    }

    public static String fieldAccessFlagsToString(int accessFlags) {
        StringBuffer b = new StringBuffer();
        if ((accessFlags & ACC_PUBLIC) != 0) {
            b.append("public ");
        }
        if ((accessFlags & ACC_PRIVATE) != 0) {
            b.append("private ");
        }
        if ((accessFlags & ACC_PROTECTED) != 0) {
            b.append("protected ");
        }
        if ((accessFlags & ACC_STATIC) != 0) {
            b.append("static ");
        }
        if ((accessFlags & ACC_FINAL) != 0) {
            b.append("final ");
        }
        if ((accessFlags & ACC_VOLATILE) != 0) {
            b.append("volatile ");
        }
        if ((accessFlags & ACC_TRANSIENT) != 0) {
            b.append("transient ");
        }
        if ((accessFlags & ACC_NATIVE) != 0) {
            b.append("native ");
        }
        if ((accessFlags & ACC_SYNTHETIC) != 0) {
            b.append("synthetic ");
        }
        if ((accessFlags & ACC_ENUM) != 0) {
            b.append("enum ");
        }
        return b.toString().trim();
    }

    public static String methodAccessFlagsToString(int accessFlags) {
        StringBuffer b = new StringBuffer();
        if ((accessFlags & ACC_PUBLIC) != 0) {
            b.append("public ");
        }
        if ((accessFlags & ACC_PRIVATE) != 0) {
            b.append("private ");
        }
        if ((accessFlags & ACC_PROTECTED) != 0) {
            b.append("protected ");
        }
        if ((accessFlags & ACC_STATIC) != 0) {
            b.append("static ");
        }
        if ((accessFlags & ACC_FINAL) != 0) {
            b.append("final ");
        }
        if ((accessFlags & ACC_SYNCHRONIZED) != 0) {
            b.append("synchronized ");
        }
        if ((accessFlags & ACC_BRIDGE) != 0) {
            b.append("bridge ");
        }
        if ((accessFlags & ACC_VARARGS) != 0) {
            b.append("varargs ");
        }
        if ((accessFlags & ACC_NATIVE) != 0) {
            b.append("native ");
        }
        if ((accessFlags & ACC_ABSTRACT) != 0) {
            b.append("abstract ");
        }
        if ((accessFlags & ACC_STRICT) != 0) {
            b.append("strict ");
        }
        if ((accessFlags & ACC_SYNTHETIC) != 0) {
            b.append("synthetic ");
        }
        return b.toString().trim();
    }

    public static int accessFlagToInt(String accessFlag) {
        int flag = 0;
        if (accessFlag.equals("public")) {
            flag = ACC_PUBLIC;
        }
        if (accessFlag.equals("private")) {
            flag = ACC_PRIVATE;
        }
        if (accessFlag.equals("protected")) {
            flag = ACC_PROTECTED;
        }
        if (accessFlag.equals("static")) {
            flag = ACC_STATIC;
        }
        if (accessFlag.equals("final")) {
            flag = ACC_FINAL;
        }
        if (accessFlag.equals("super")) {
            flag = ACC_SUPER;
        }
        if (accessFlag.equals("synchronized")) {
            flag = ACC_SYNCHRONIZED;
        }
        if (accessFlag.equals("volatile")) {
            flag = ACC_VOLATILE;
        }
        if (accessFlag.equals("bridge")) {
            flag = ACC_BRIDGE;
        }
        if (accessFlag.equals("transient")) {
            flag = ACC_TRANSIENT;
        }
        if (accessFlag.equals("varargs")) {
            flag = ACC_VARARGS;
        }
        if (accessFlag.equals("native")) {
            flag = ACC_NATIVE;
        }
        if (accessFlag.equals("interface")) {
            flag = ACC_INTERFACE;
        }
        if (accessFlag.equals("abstract")) {
            flag = ACC_ABSTRACT;
        }
        if (accessFlag.equals("strict")) {
            flag = ACC_STRICT;
        }
        if (accessFlag.equals("synthetic")) {
            flag = ACC_SYNTHETIC;
        }
        if (accessFlag.equals("annotation")) {
            flag = ACC_ANNOTATION;
        }
        if (accessFlag.equals("enum")) {
            flag = ACC_ENUM;
        }
        return flag;
    }

}
