package algol;

/**
 * Created by Gabriel on 06/03/2016.
 */
import java.io.IOException;
import java.util.ArrayList;
import static algol.CLConstants.*;

abstract class CLAttributeInfo {

    public int attributeNameIndex;

    public long attributeLength;


    protected CLAttributeInfo(int attributeNameIndex, long attributeLength) {
        this.attributeNameIndex = attributeNameIndex;
        this.attributeLength = attributeLength;
    }

    public void write(CLOutputStream out) throws IOException {
        out.writeShort(attributeNameIndex);
        out.writeInt((long) attributeLength);
    }
}

class CLConstantValueAttribute extends CLAttributeInfo {

    public int constantValueIndex;

    public CLConstantValueAttribute(int attributeNameIndex,
                                    long attributeLength, int constantValueIndex) {
        super(attributeNameIndex, attributeLength);
        this.constantValueIndex = constantValueIndex;
    }

    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        out.writeShort(constantValueIndex);
    }

}

class CLExceptionInfo {

    public int startPC;

    public int endPC;

    public int handlerPC;

    public int catchType;

    public CLExceptionInfo(int startPC, int endPC, int handlerPC, int catchType) {
        this.startPC = startPC;
        this.endPC = endPC;
        this.handlerPC = handlerPC;
        this.catchType = catchType;
    }

    public void write(CLOutputStream out) throws IOException {
        out.writeShort(startPC);
        out.writeShort(endPC);
        out.writeShort(handlerPC);
        out.writeShort(catchType);
    }

}

class CLCodeAttribute extends CLAttributeInfo {

    public int maxStack;

    public int maxLocals;

    public long codeLength;

    public ArrayList<Integer> code;

    public int exceptionTableLength;

    public ArrayList<CLExceptionInfo> exceptionTable;

    public int attributesCount;

    public ArrayList<CLAttributeInfo> attributes;

    private int intValue(int a, int b, int c, int d) {
        return (a << 24) | (b << 16) | (c << 8) | d;
    }


    public CLCodeAttribute(int attributeNameIndex, long attributeLength,
                           int maxStack, int maxLocals, long codeLength,
                           ArrayList<Integer> code, int exceptionTableLength,
                           ArrayList<CLExceptionInfo> exceptionTable, int attributesCount,
                           ArrayList<CLAttributeInfo> attributes) {
        super(attributeNameIndex, attributeLength);
        this.maxStack = maxStack;
        this.maxLocals = maxLocals;
        this.codeLength = codeLength;
        this.code = code;
        this.exceptionTableLength = exceptionTableLength;
        this.exceptionTable = exceptionTable;
        this.attributesCount = attributesCount;
        this.attributes = attributes;
    }

    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        out.writeShort(maxStack);
        out.writeShort(maxLocals);
        out.writeInt(codeLength);
        for (int i = 0; i < code.size(); i++) {
            out.writeByte(code.get(i));
        }
        out.writeShort(exceptionTableLength);
        for (int i = 0; i < exceptionTable.size(); i++) {
            exceptionTable.get(i).write(out);
        }
        out.writeShort(attributesCount);
        for (int i = 0; i < attributes.size(); i++) {
            attributes.get(i).write(out);
        }
    }

}

class CLExceptionsAttribute extends CLAttributeInfo {

    public int numberOfExceptions;

    public ArrayList<Integer> exceptionIndexTable;

    public CLExceptionsAttribute(int attributeNameIndex, long attributeLength,
                                 int numberOfExceptions, ArrayList<Integer> exceptionIndexTable) {
        super(attributeNameIndex, attributeLength);
        this.numberOfExceptions = numberOfExceptions;
        this.exceptionIndexTable = exceptionIndexTable;
    }

    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        out.writeShort(numberOfExceptions);
        for (int i = 0; i < exceptionIndexTable.size(); i++) {
            out.writeShort(exceptionIndexTable.get(i));
        }
    }

}

class CLInnerClassInfo {

    public int innerClassInfoIndex;

    public int outerClassInfoIndex;

    public int innerNameIndex;

    public int innerClassAccessFlags;

    public CLInnerClassInfo(int innerClassInfoIndex, int outerClassInfoIndex,
                            int innerNameIndex, int innerClassAccessFlags) {
        this.innerClassInfoIndex = innerClassInfoIndex;
        this.outerClassInfoIndex = outerClassInfoIndex;
        this.innerNameIndex = innerNameIndex;
        this.innerClassAccessFlags = innerClassAccessFlags;
    }

    public void write(CLOutputStream out) throws IOException {
        out.writeShort(innerClassInfoIndex);
        out.writeShort(outerClassInfoIndex);
        out.writeShort(innerNameIndex);
        out.writeShort(innerClassAccessFlags);
    }

}


class CLInnerClassesAttribute extends CLAttributeInfo {

    public int numberOfClasses;

    public ArrayList<CLInnerClassInfo> classes;


    public CLInnerClassesAttribute(int attributeNameIndex,
                                   long attributeLength, int numberOfClasses,
                                   ArrayList<CLInnerClassInfo> classes) {
        super(attributeNameIndex, attributeLength);
        this.numberOfClasses = numberOfClasses;
        this.classes = classes;
    }

    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        out.writeShort(numberOfClasses);
        for (int i = 0; i < classes.size(); i++) {
            classes.get(i).write(out);
        }
    }

}

class CLEnclosingMethodAttribute extends CLAttributeInfo {

    public int classIndex;

    public int methodIndex;

    public CLEnclosingMethodAttribute(int attributeNameIndex,
                                      long attributeLength, int classIndex, int methodIndex) {
        super(attributeNameIndex, attributeLength);
        this.classIndex = classIndex;
        this.methodIndex = methodIndex;
    }

    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        out.writeShort(classIndex);
        out.writeShort(methodIndex);
    }
}

class CLSyntheticAttribute extends CLAttributeInfo {

    public CLSyntheticAttribute(int attributeNameIndex, long attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    public void write(CLOutputStream out) throws IOException {
        super.write(out);
    }
}

class CLSignatureAttribute extends CLAttributeInfo {

    public int signatureIndex;

    public CLSignatureAttribute(int attributeNameIndex, long attributeLength,
                                int signatureIndex) {
        super(attributeNameIndex, attributeLength);
        this.signatureIndex = signatureIndex;
    }

    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        out.writeShort(signatureIndex);
    }

}

class CLSourceFileAttribute extends CLAttributeInfo {

    public int sourceFileIndex;

    public CLSourceFileAttribute(int attributeNameIndex, long attributeLength,
                                 int sourceFileIndex) {
        super(attributeNameIndex, attributeLength);
        this.sourceFileIndex = sourceFileIndex;
    }

    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        out.writeShort(sourceFileIndex);
    }
}

class CLSourceDebugExtensionAttribute extends CLAttributeInfo {

    public byte[] debugExtension;


    public CLSourceDebugExtensionAttribute(int attributeNameIndex,
                                           long attributeLength, byte[] debugExtension) {
        super(attributeNameIndex, attributeLength);
        this.debugExtension = debugExtension;
    }


    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        for (int i = 0; i < debugExtension.length; i++) {
            out.writeByte(debugExtension[i]);
        }
    }
}


class CLLineNumberInfo {

    public int startPC;

    public int lineNumber;

    public CLLineNumberInfo(int startPC, int lineNumber) {
        this.startPC = startPC;
        this.lineNumber = lineNumber;
    }

    public void write(CLOutputStream out) throws IOException {
        out.writeShort(startPC);
        out.writeShort(lineNumber);
    }

    public boolean equals(Object obj) {
        if (obj instanceof CLLineNumberInfo) {
            CLLineNumberInfo c = (CLLineNumberInfo) obj;
            if (c.lineNumber == lineNumber) {
                return true;
            }
        }
        return false;
    }

}

class CLLineNumberTableAttribute extends CLAttributeInfo {

    public int lineNumberTableLength;

    public ArrayList<CLLineNumberInfo> lineNumberTable;

    public CLLineNumberTableAttribute(int attributeNameIndex,
                                      long attributeLength, int lineNumberTableLength,
                                      ArrayList<CLLineNumberInfo> lineNumberTable) {
        super(attributeNameIndex, attributeLength);
        this.lineNumberTableLength = lineNumberTableLength;
        this.lineNumberTable = lineNumberTable;
    }

    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        out.writeShort(lineNumberTableLength);
        for (int i = 0; i < lineNumberTable.size(); i++) {
            lineNumberTable.get(i).write(out);
        }
    }
}

class CLLocalVariableInfo {

    public int startPC;

    public int length;

    public int nameIndex;

    public int descriptorIndex;

    public int index;

    public CLLocalVariableInfo(int startPC, int length, int nameIndex,
                               int descriptorIndex, int index) {
        this.startPC = startPC;
        this.length = length;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.index = index;
    }

    public void write(CLOutputStream out) throws IOException {
        out.writeShort(startPC);
        out.writeShort(length);
        out.writeShort(nameIndex);
        out.writeShort(descriptorIndex);
        out.writeShort(index);
    }

}


class CLLocalVariableTableAttribute extends CLAttributeInfo {

    public int localVariableTableLength;

    public ArrayList<CLLocalVariableInfo> localVariableTable;

    public CLLocalVariableTableAttribute(int attributeNameIndex,
                                         long attributeLength, int localVariableTableLength,
                                         ArrayList<CLLocalVariableInfo> localVariableTable) {
        super(attributeNameIndex, attributeLength);
        this.localVariableTableLength = localVariableTableLength;
        this.localVariableTable = localVariableTable;
    }

    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        out.writeShort(localVariableTableLength);
        for (int i = 0; i < localVariableTable.size(); i++) {
            localVariableTable.get(i).write(out);
        }
    }

}

class CLLocalVariableTypeInfo {

    public int startPC;

    public int length;

    public int nameIndex;

    public int signatureIndex;

    public int index;


    public CLLocalVariableTypeInfo(int startPC, int length, int nameIndex,
                                   int signatureIndex, int index) {
        this.startPC = startPC;
        this.length = length;
        this.nameIndex = nameIndex;
        this.signatureIndex = signatureIndex;
        this.index = index;
    }

    public void write(CLOutputStream out) throws IOException {
        out.writeShort(startPC);
        out.writeShort(length);
        out.writeShort(nameIndex);
        out.writeShort(signatureIndex);
        out.writeShort(index);
    }

}


class CLLocalVariableTypeTableAttribute extends CLAttributeInfo {

    public int localVariableTypeTableLength;

    public ArrayList<CLLocalVariableTypeInfo> localVariableTypeTable;

    public CLLocalVariableTypeTableAttribute(int attributeNameIndex,
                                             long attributeLength, int localVariableTypeTableLength,
                                             ArrayList<CLLocalVariableTypeInfo> localVariableTypeTable) {
        super(attributeNameIndex, attributeLength);
        this.localVariableTypeTableLength = localVariableTypeTableLength;
        this.localVariableTypeTable = localVariableTypeTable;
    }

    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        out.writeShort(localVariableTypeTableLength);
        for (int i = 0; i < localVariableTypeTable.size(); i++) {
            localVariableTypeTable.get(i).write(out);
        }
    }
}

class CLDeprecatedAttribute extends CLAttributeInfo {

    public CLDeprecatedAttribute(int attributeNameIndex, long attributeLength) {
        super(attributeNameIndex, attributeLength);
    }
    public void write(CLOutputStream out) throws IOException {
        super.write(out);
    }
}

class CLAnnotation {
    public int typeIndex;

    public int numElementValuePairs;

    public ArrayList<CLElementValuePair> elementValuePairs;

    public CLAnnotation(int typeIndex, int numElementValuePairs,
                        ArrayList<CLElementValuePair> elementValuePairs) {
        this.typeIndex = typeIndex;
        this.numElementValuePairs = numElementValuePairs;
        this.elementValuePairs = elementValuePairs;
    }

    public void write(CLOutputStream out) throws IOException {
        out.writeShort(typeIndex);
        out.writeShort(numElementValuePairs);
        for (int i = 0; i < elementValuePairs.size(); i++) {
            elementValuePairs.get(i).write(out);
        }
    }
}

class CLElementValue {

    public short tag;

    public int constValueIndex;

    public int typeNameIndex;

    public int constNameIndex;

    public int classInfoIndex;

    public CLAnnotation annotationValue;

    public int numValues;

    public ArrayList<CLElementValue> values;

    public CLElementValue(short tag, int constValueIndex) {
        this.tag = tag;
        this.constValueIndex = constValueIndex;
    }

    public CLElementValue(int typeNameIndex, int constNameIndex) {
        this.tag = ELT_e;
        this.typeNameIndex = typeNameIndex;
        this.constNameIndex = constNameIndex;
    }

    public CLElementValue(int classInfoIndex) {
        this.tag = ELT_c;
        this.classInfoIndex = classInfoIndex;
    }

    public CLElementValue(CLAnnotation annotationValue) {
        this.tag = ELT_ANNOTATION;
        this.annotationValue = annotationValue;
    }

    public CLElementValue(int numValues, ArrayList<CLElementValue> values) {
        this.tag = ELT_ARRAY;
        this.numValues = numValues;
        this.values = values;
    }

    public void write(CLOutputStream out) throws IOException {
        out.writeByte(tag);
        switch (tag) {
            case ELT_B:
            case ELT_C:
            case ELT_D:
            case ELT_F:
            case ELT_I:
            case ELT_J:
            case ELT_S:
            case ELT_Z:
            case ELT_s:
                out.writeInt(constValueIndex);
                break;
            case ELT_e:
                out.writeInt(typeNameIndex);
                out.writeInt(constNameIndex);
                break;
            case ELT_c:
                out.writeInt(classInfoIndex);
                break;
            case ELT_ANNOTATION:
                annotationValue.write(out);
                break;
            case ELT_ARRAY:
                out.writeInt(numValues);
                for (int i = 0; i < numValues; i++) {
                    values.get(i).write(out);
                }
        }
    }
}

class CLElementValuePair {

    public int elementNameIndex;

    public CLElementValue value;

    public CLElementValuePair(int elementNameIndex, CLElementValue value) {
        this.elementNameIndex = elementNameIndex;
        this.value = value;
    }

    public void write(CLOutputStream out) throws IOException {
        out.writeShort(elementNameIndex);
        value.write(out);
    }
}

class CLRuntimeVisibleAnnotationsAttribute extends CLAttributeInfo {

    public int numAnnotations;

    public ArrayList<CLAnnotation> annotations;

    public CLRuntimeVisibleAnnotationsAttribute(int attributeNameIndex,
                                                long attributeLength, int numAnnotations,
                                                ArrayList<CLAnnotation> annotations) {
        super(attributeNameIndex, attributeLength);
        this.numAnnotations = numAnnotations;
        this.annotations = annotations;
    }

    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        out.writeShort(numAnnotations);
        for (int i = 0; i < annotations.size(); i++) {
            annotations.get(i).write(out);
        }
    }
}

class CLRuntimeInvisibleAnnotationsAttribute extends CLAttributeInfo {

    public int numAnnotations;

    public ArrayList<CLAnnotation> annotations;

    public CLRuntimeInvisibleAnnotationsAttribute(int attributeNameIndex,
                                                  long attributeLength, int numAnnotations,
                                                  ArrayList<CLAnnotation> annotations) {
        super(attributeNameIndex, attributeLength);
        this.numAnnotations = numAnnotations;
        this.annotations = annotations;
    }

    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        out.writeShort(numAnnotations);
        for (int i = 0; i < annotations.size(); i++) {
            annotations.get(i).write(out);
        }
    }

}
class CLParameterAnnotationInfo {

    public int numAnnotations;

    public ArrayList<CLAnnotation> annotations;

    public CLParameterAnnotationInfo(int numAnnotations,
                                     ArrayList<CLAnnotation> annotations) {
        this.numAnnotations = numAnnotations;
        this.annotations = annotations;
    }

    public void write(CLOutputStream out) throws IOException {
        out.writeShort(numAnnotations);
        for (int i = 0; i < annotations.size(); i++) {
            annotations.get(i).write(out);
        }
    }

}

class CLRuntimeVisibleParameterAnnotationsAttribute extends CLAttributeInfo {

    public short numParameters;

    public ArrayList<CLParameterAnnotationInfo> parameterAnnotations;

    public CLRuntimeVisibleParameterAnnotationsAttribute(
            int attributeNameIndex, long attributeLength, short numParameters,
            ArrayList<CLParameterAnnotationInfo> parameterAnnotations) {
        super(attributeNameIndex, attributeLength);
        this.numParameters = numParameters;
        this.parameterAnnotations = parameterAnnotations;
    }

    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        out.writeByte(numParameters);
        for (int i = 0; i < parameterAnnotations.size(); i++) {
            parameterAnnotations.get(i).write(out);
        }
    }
}

class CLRuntimeInvisibleParameterAnnotationsAttribute extends CLAttributeInfo {

    public short numParameters;

    public ArrayList<CLParameterAnnotationInfo> parameterAnnotations;

    public CLRuntimeInvisibleParameterAnnotationsAttribute(
            int attributeNameIndex, long attributeLength, short numParameters,
            ArrayList<CLParameterAnnotationInfo> parameterAnnotations) {
        super(attributeNameIndex, attributeLength);
        this.numParameters = numParameters;
        this.parameterAnnotations = parameterAnnotations;
    }

    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        out.writeByte(numParameters);
        for (int i = 0; i < parameterAnnotations.size(); i++) {
            parameterAnnotations.get(i).write(out);
        }
    }

}

class CLAnnotationDefaultAttribute extends CLAttributeInfo {

    public CLElementValue defaultValue;

    public CLAnnotationDefaultAttribute(int attributeNameIndex,
                                        long attributeLength, CLElementValue defaultValue) {
        super(attributeNameIndex, attributeLength);
        this.defaultValue = defaultValue;
    }

    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        defaultValue.write(out);
    }
}

