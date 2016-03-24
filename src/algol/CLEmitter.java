/**
 * Created by Gabriel on 06/03/2016.
 */

package algol;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.TreeMap;

import static algol.CLConstants.*;
import static algol.CLConstants.Category.*;

public class CLEmitter {

    private String name;

    private boolean toFile;

    private String destDir;

    private CLFile clFile;

    private CLConstantPool constantPool;

    private ArrayList<Integer> interfaces;

    private ArrayList<CLFieldInfo> fields;

    private ArrayList<CLAttributeInfo> fAttributes;

    private ArrayList<CLMethodInfo> methods;

    private ArrayList<CLAttributeInfo> mAttributes;

    private ArrayList<CLAttributeInfo> attributes;

    private ArrayList<CLInnerClassInfo> innerClasses;

    private ArrayList<CLInstruction> mCode;

    private ArrayList<CLException> mExceptionHandlers;

    private int mAccessFlags;

    private int mNameIndex;

    private int mDescriptorIndex;

    private int mArgumentCount;

    private ArrayList<CLAttributeInfo> mCodeAttributes;

    private boolean isMethodOpen;

    private Hashtable<String, Integer> mLabels;

    private int mLabelCount;

    private boolean mInstructionAfterLabel = false;

    private int mPC;

    private String eCurrentMethod;

    private boolean errorHasOccurred;

    private static ByteClassLoader byteClassLoader;

    private void initializeMethodVariables() {
        mAccessFlags = 0;
        mNameIndex = -1;
        mDescriptorIndex = -1;
        mArgumentCount = 0;
        mPC = 0;
        mAttributes = new ArrayList<CLAttributeInfo>();
        mExceptionHandlers = new ArrayList<CLException>();
        mCode = new ArrayList<CLInstruction>();
        mCodeAttributes = new ArrayList<CLAttributeInfo>();
        mLabels = new Hashtable<String, Integer>();
        mLabelCount = 1;
        mInstructionAfterLabel = false;
    }

    private void endOpenMethodIfAny() {
        if (isMethodOpen) {
            isMethodOpen = false;
            if (!mInstructionAfterLabel) {
                addNoArgInstruction(NOP);
            }

            ArrayList<CLExceptionInfo> exceptionTable = new ArrayList<CLExceptionInfo>();
            for (int i = 0; i < mExceptionHandlers.size(); i++) {
                CLException e = mExceptionHandlers.get(i);
                if (!e.resolveLabels(mLabels)) {
                    reportEmitterError(
                            "%s: Unable to resolve exception handler "
                                    + "label(s)", eCurrentMethod);
                }

                int catchTypeIndex = (e.catchType == null) ? 0 : constantPool
                        .constantClassInfo(e.catchType);
                CLExceptionInfo c = new CLExceptionInfo(e.startPC, e.endPC,
                        e.handlerPC, catchTypeIndex);
                exceptionTable.add(c);
            }

            ArrayList<Integer> byteCode = new ArrayList<Integer>();
            int maxLocals = mArgumentCount;
            for (int i = 0; i < mCode.size(); i++) {
                CLInstruction instr = mCode.get(i);

                int localVariableIndex = instr.localVariableIndex();
                switch (instr.opcode()) {
                    case LLOAD:
                    case LSTORE:
                    case DSTORE:
                    case DLOAD:
                    case LLOAD_0:
                    case LLOAD_1:
                    case LLOAD_2:
                    case LLOAD_3:
                    case LSTORE_0:
                    case LSTORE_1:
                    case LSTORE_2:
                    case LSTORE_3:
                    case DLOAD_0:
                    case DLOAD_1:
                    case DLOAD_2:
                    case DLOAD_3:
                    case DSTORE_0:
                    case DSTORE_1:
                    case DSTORE_2:
                    case DSTORE_3:
                        localVariableIndex++;
                }
                maxLocals = Math.max(maxLocals, localVariableIndex + 1);

                if (instr instanceof CLFlowControlInstruction) {
                    if (!((CLFlowControlInstruction) instr)
                            .resolveLabels(mLabels)) {
                        reportEmitterError(
                                "%s: Unable to resolve jump label(s)",
                                eCurrentMethod);
                    }
                }

                byteCode.addAll(instr.toBytes());
            }

            if (!((mAccessFlags & ACC_NATIVE) == ACC_NATIVE || (mAccessFlags & ACC_ABSTRACT) == ACC_ABSTRACT)) {
                addMethodAttribute(codeAttribute(byteCode, exceptionTable,
                        stackDepth(), maxLocals));
            }

            methods.add(new CLMethodInfo(mAccessFlags, mNameIndex,
                    mDescriptorIndex, mAttributes.size(), mAttributes));
        }

        if (innerClasses.size() > 0) {
            addClassAttribute(innerClassesAttribute());
        }

        clFile.constantPoolCount = constantPool.size() + 1;
        clFile.constantPool = constantPool;
        clFile.interfacesCount = interfaces.size();
        clFile.interfaces = interfaces;
        clFile.fieldsCount = fields.size();
        clFile.fields = fields;
        clFile.methodsCount = methods.size();
        clFile.methods = methods;
        clFile.attributesCount = attributes.size();
        clFile.attributes = attributes;
    }

    private void addFieldInfo(ArrayList<String> accessFlags, String name,
                              String type, boolean isSynthetic, int c) {
        if (!validTypeDescriptor(type)) {
            reportEmitterError("'%s' is not a valid type descriptor for field",
                    type);
        }
        int flags = 0;
        int nameIndex = constantPool.constantUtf8Info(name);
        int descriptorIndex = constantPool.constantUtf8Info(type);
        fAttributes = new ArrayList<CLAttributeInfo>();
        if (accessFlags != null) {
            for (int i = 0; i < accessFlags.size(); i++) {
                flags |= CLFile.accessFlagToInt(accessFlags.get(i));
            }
        }
        if (isSynthetic) {
            addFieldAttribute(syntheticAttribute());
        }
        if (c != -1) {
            addFieldAttribute(constantValueAttribute(c));
        }
        fields.add(new CLFieldInfo(flags, nameIndex, descriptorIndex,
                fAttributes.size(), fAttributes));
    }

    private int typeStackResidue(String descriptor) {
        int i = 0;
        char c = descriptor.charAt(0);
        switch (c) {
            case 'B':
            case 'C':
            case 'I':
            case 'F':
            case 'L':
            case 'S':
            case 'Z':
            case '[':
                i = 1;
                break;
            case 'J':
            case 'D':
                i = 2;
                break;
        }
        return i;
    }

    private int methodStackResidue(String descriptor) {
        int i = 0;

        String argTypes = descriptor.substring(1, descriptor.lastIndexOf(")"));
        String returnType = descriptor
                .substring(descriptor.lastIndexOf(")") + 1);

        for (int j = 0; j < argTypes.length(); j++) {
            char c = argTypes.charAt(j);
            switch (c) {
                case 'B':
                case 'C':
                case 'I':
                case 'F':
                case 'S':
                case 'Z':
                    i -= 1;
                    break;
                case '[':
                    break;
                case 'J':
                case 'D':
                    i -= 2;
                    break;
                case 'L':
                    int k = argTypes.indexOf(";", j);
                    j = k;
                    i -= 1;
                    break;
            }
        }

        i += typeStackResidue(returnType);
        return i;
    }

    private int argumentCount(String descriptor) {
        int i = 0;

        String argTypes = descriptor.substring(1, descriptor.lastIndexOf(")"));

        for (int j = 0; j < argTypes.length(); j++) {
            char c = argTypes.charAt(j);
            switch (c) {
                case 'B':
                case 'C':
                case 'I':
                case 'F':
                case 'S':
                case 'Z':
                    i += 1;
                    break;
                case '[':
                    break;
                case 'J':
                case 'D':
                    i += 2;
                    break;
                case 'L':
                    int k = argTypes.indexOf(";", j);
                    j = k;
                    i += 1;
                    break;
            }
        }
        return i;
    }

    private boolean validInternalForm(String name) {
        if ((name == null) || name.equals("") || name.startsWith("/")
                || name.endsWith("/")) {
            return false;
        }
        StringTokenizer t = new StringTokenizer(name, "/");
        while (t.hasMoreTokens()) {
            String s = t.nextToken();
            for (int i = 0; i < s.length(); i++) {
                if (i == 0) {
                    if (!Character.isJavaIdentifierStart(s.charAt(i))) {
                        return false;
                    }
                } else {
                    if (!Character.isJavaIdentifierPart(s.charAt(i))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean validTypeDescriptor(String descriptor) {
        if (descriptor != null) {
            try {
                char c = descriptor.charAt(0);
                switch (c) {
                    case 'B':
                    case 'C':
                    case 'I':
                    case 'F':
                    case 'S':
                    case 'Z':
                    case 'J':
                    case 'D':
                        return (descriptor.length() == 1);
                    case 'L':
                        if (descriptor.endsWith(";")) {
                            return validInternalForm(descriptor.substring(1,
                                    descriptor.length() - 1));
                        }
                        return descriptor.length() == 1;
                    case '[':
                        return validTypeDescriptor(descriptor.substring(1));
                }
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        }
        return false;
    }

    private boolean validMethodDescriptor(String descriptor) {
        if ((descriptor != null) && (descriptor.length() > 0)) {
            try {
                String argTypes = descriptor.substring(1, descriptor
                        .lastIndexOf(")"));
                String returnType = descriptor.substring(descriptor
                        .lastIndexOf(")") + 1);

                if (argTypes.endsWith("[")) {
                    return false;
                }
                for (int i = 0; i < argTypes.length(); i++) {
                    char c = argTypes.charAt(i);
                    switch (c) {
                        case 'B':
                        case 'C':
                        case 'I':
                        case 'F':
                        case 'S':
                        case 'Z':
                        case 'J':
                        case 'D':
                        case '[':
                            break;
                        case 'L':
                            int j = argTypes.indexOf(";", i);
                            String s = argTypes.substring(i, j + 1);
                            i = j;
                            if (!validTypeDescriptor(s)) {
                                return false;
                            }
                            break;
                        default:
                            return false;
                    }
                }

                return (returnType.equals("V") || validTypeDescriptor(returnType));
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        }
        return false;
    }

    private CLInstruction instruction(int pc) {
        for (int j = 0; j < mCode.size(); j++) {
            CLInstruction i = mCode.get(j);
            if (i.pc() == pc) {
                return i;
            }
        }
        return null;
    }

    private int instructionIndex(int pc) {
        int j = 0;
        for (; j < mCode.size(); j++) {
            CLInstruction i = mCode.get(j);
            if (i.pc() == pc) {
                return j;
            }
        }
        return j;
    }

    private int stackDepth() {
        CLBranchStack branchTargets = new CLBranchStack();
        for (int i = 0; i < mExceptionHandlers.size(); i++) {
            CLException e = mExceptionHandlers.get(i);
            CLInstruction h = instruction(e.handlerPC);
            if (h != null) {
                branchTargets.push(h, 1);
            }
        }
        int stackDepth = 0, maxStackDepth = 0, c = 0;
        CLInstruction instr = (mCode.size() == 0) ? null : mCode.get(c);
        while (instr != null) {
            int opcode = instr.opcode();
            int stackUnits = instr.stackUnits();
            if (stackUnits == EMPTY_STACK) {
                stackDepth = 0;
            } else if (stackUnits == UNIT_SIZE_STACK) {
                stackDepth = 1;
            } else {
                stackDepth += stackUnits;
            }
            if (stackDepth > maxStackDepth) {
                maxStackDepth = stackDepth;
            }

            if (instr instanceof CLFlowControlInstruction) {
                CLFlowControlInstruction b = (CLFlowControlInstruction) instr;
                int jumpToIndex = b.pc() + b.jumpToOffset();
                CLInstruction instrAt = null;
                switch (opcode) {
                    case JSR:
                    case JSR_W:
                    case RET:
                        instr = null;
                        break;
                    case GOTO:
                    case GOTO_W:
                        instr = null;
                    default:
                        instrAt = instruction(jumpToIndex);
                        if (instrAt != null) {
                            branchTargets.push(instrAt, stackDepth);
                        }
                }
            } else {
                if ((opcode == ATHROW)
                        || ((opcode >= IRETURN) && (opcode <= RETURN))) {
                    instr = null;
                }
            }
            if (instr != null) {
                c++;
                instr = (c >= mCode.size()) ? null : mCode.get(c);
            }
            if (instr == null) {
                CLBranchTarget bt = branchTargets.pop();
                if (bt != null) {
                    instr = bt.target;
                    stackDepth = bt.stackDepth;
                    c = instructionIndex(instr.pc());
                }
            }
        }
        return maxStackDepth;
    }

    private void ldcInstruction(int index) {
        CLLoadStoreInstruction instr = null;
        if (index <= 255) {
            instr = new CLLoadStoreInstruction(LDC, mPC++, index);
        } else {
            instr = new CLLoadStoreInstruction(LDC_W, mPC++, index);
        }
        mPC += instr.operandCount();
        mCode.add(instr);
        mInstructionAfterLabel = true;
    }

    private void ldc2wInstruction(int index) {
        CLLoadStoreInstruction instr = new CLLoadStoreInstruction(LDC2_W,
                mPC++, index);
        mPC += instr.operandCount();
        mCode.add(instr);
        mInstructionAfterLabel = true;
    }

    private CLConstantValueAttribute constantValueAttribute(int c) {
        int attributeNameIndex = constantPool
                .constantUtf8Info(ATT_CONSTANT_VALUE);
        return new CLConstantValueAttribute(attributeNameIndex, 2, c);
    }

    private CLCodeAttribute codeAttribute(ArrayList<Integer> byteCode,
                                          ArrayList<CLExceptionInfo> exceptionTable, int stackDepth,
                                          int maxLocals) {
        int codeLength = byteCode.size();
        int attributeNameIndex = constantPool.constantUtf8Info(ATT_CODE);
        int attributeLength = codeLength + 8 * exceptionTable.size() + 12;
        for (int i = 0; i < mCodeAttributes.size(); i++) {
            attributeLength += 6 + mCodeAttributes.get(i).attributeLength;
        }
        return new CLCodeAttribute(attributeNameIndex, attributeLength,
                stackDepth, maxLocals, (long) codeLength, byteCode,
                exceptionTable.size(), exceptionTable, mCodeAttributes.size(),
                mCodeAttributes);
    }

    private CLExceptionsAttribute exceptionsAttribute(
            ArrayList<String> exceptions) {
        int attributeNameIndex = constantPool.constantUtf8Info(ATT_EXCEPTIONS);
        ArrayList<Integer> exceptionIndexTable = new ArrayList<Integer>();
        for (int i = 0; i < exceptions.size(); i++) {
            String e = exceptions.get(i);
            exceptionIndexTable.add(new Integer(constantPool
                    .constantClassInfo(e)));
        }
        return new CLExceptionsAttribute(attributeNameIndex,
                exceptionIndexTable.size() * 2 + 2, exceptionIndexTable.size(),
                exceptionIndexTable);
    }

    private CLInnerClassesAttribute innerClassesAttribute() {
        int attributeNameIndex = constantPool
                .constantUtf8Info(ATT_INNER_CLASSES);
        long attributeLength = innerClasses.size() * 8 + 2;
        return new CLInnerClassesAttribute(attributeNameIndex, attributeLength,
                innerClasses.size(), innerClasses);
    }

    private CLAttributeInfo syntheticAttribute() {
        int attributeNameIndex = constantPool.constantUtf8Info(ATT_SYNTHETIC);
        return new CLSyntheticAttribute(attributeNameIndex, 0);
    }

    private void reportOpcodeError(int opcode) {
        if (!CLInstruction.isValid(opcode)) {
            reportEmitterError("%s: Invalid opcode '%d'", eCurrentMethod,
                    opcode);
        } else {
            reportEmitterError(
                    "%s: Incorrect method used to add instruction '%s'",
                    eCurrentMethod,
                    CLInstruction.instructionInfo[opcode].mnemonic);
        }
    }

    private void reportEmitterError(String message, Object... args) {
        System.err.printf(message, args);
        System.err.println();
        errorHasOccurred = true;
    }

    public CLEmitter(boolean toFile) {
        destDir = ".";
        this.toFile = toFile;
    }

    public void destinationDir(String destDir) {
        this.destDir = destDir;
    }

    public boolean errorHasOccurred() {
        return errorHasOccurred;
    }

    public void addClass(ArrayList<String> accessFlags, String thisClass,
                         String superClass, ArrayList<String> superInterfaces,
                         boolean isSynthetic) {
        clFile = new CLFile();
        constantPool = new CLConstantPool();
        interfaces = new ArrayList<Integer>();
        fields = new ArrayList<CLFieldInfo>();
        methods = new ArrayList<CLMethodInfo>();
        attributes = new ArrayList<CLAttributeInfo>();
        innerClasses = new ArrayList<CLInnerClassInfo>();
        errorHasOccurred = false;
        clFile.magic = MAGIC;
        clFile.majorVersion = MAJOR_VERSION;
        clFile.minorVersion = MINOR_VERSION;
        if (!validInternalForm(thisClass)) {
            reportEmitterError("'%s' is not in internal form", thisClass);
        }
        if (!validInternalForm(superClass)) {
            reportEmitterError("'%s' is not in internal form", superClass);
        }
        if (accessFlags != null) {
            for (int i = 0; i < accessFlags.size(); i++) {
                clFile.accessFlags |= CLFile
                        .accessFlagToInt(accessFlags.get(i));
            }
        }
        name = thisClass;
        clFile.thisClass = constantPool.constantClassInfo(thisClass);
        clFile.superClass = constantPool.constantClassInfo(superClass);
        for (int i = 0; superInterfaces != null && i < superInterfaces.size(); i++) {
            if (!validInternalForm(superInterfaces.get(i))) {
                reportEmitterError("'%s' is not in internal form",
                        superInterfaces.get(i));
            }
            interfaces.add(new Integer(constantPool
                    .constantClassInfo(superInterfaces.get(i))));
        }
        if (isSynthetic) {
            addClassAttribute(syntheticAttribute());
        }
    }

    public void addInnerClass(ArrayList<String> accessFlags, String innerClass,
                              String outerClass, String innerName) {
        int flags = 0;
        if (accessFlags != null) {
            for (int j = 0; j < accessFlags.size(); j++) {
                flags |= CLFile.accessFlagToInt(accessFlags.get(j));
            }
        }
        CLInnerClassInfo innerClassInfo = new CLInnerClassInfo(constantPool
                .constantClassInfo(innerClass), constantPool
                .constantClassInfo(outerClass), constantPool
                .constantUtf8Info(innerName), flags);
        innerClasses.add(innerClassInfo);
    }

    public void addField(ArrayList<String> accessFlags, String name,
                         String type, boolean isSynthetic) {
        addFieldInfo(accessFlags, name, type, isSynthetic, -1);
    }

    public void addField(ArrayList<String> accessFlags, String name,
                         String type, boolean isSynthetic, int i) {
        addFieldInfo(accessFlags, name, type, isSynthetic, constantPool
                .constantIntegerInfo(i));
    }

    public void addField(ArrayList<String> accessFlags, String name,
                         boolean isSynthetic, float f) {
        addFieldInfo(accessFlags, name, "F", isSynthetic, constantPool
                .constantFloatInfo(f));
    }

    public void addField(ArrayList<String> accessFlags, String name,
                         boolean isSynthetic, long l) {
        addFieldInfo(accessFlags, name, "J", isSynthetic, constantPool
                .constantLongInfo(l));
    }

    public void addField(ArrayList<String> accessFlags, String name,
                         boolean isSynthetic, double d) {
        addFieldInfo(accessFlags, name, "D", isSynthetic, constantPool
                .constantDoubleInfo(d));
    }

    public void addField(ArrayList<String> accessFlags, String name,
                         boolean isSynthetic, String s) {
        addFieldInfo(accessFlags, name, "Ljava/lang/String;", isSynthetic,
                constantPool.constantStringInfo(s));
    }

    public void addMethod(ArrayList<String> accessFlags, String name,
                          String descriptor, ArrayList<String> exceptions, boolean isSynthetic) {
        if (!validMethodDescriptor(descriptor)) {
            reportEmitterError(
                    "'%s' is not a valid type descriptor for method",
                    descriptor);
        }
        endOpenMethodIfAny(); // close any previous method
        isMethodOpen = true;
        initializeMethodVariables();
        eCurrentMethod = name + descriptor;
        if (accessFlags != null) {
            for (int i = 0; i < accessFlags.size(); i++) {
                mAccessFlags |= CLFile.accessFlagToInt(accessFlags.get(i));
            }
        }
        mArgumentCount = argumentCount(descriptor)
                + (accessFlags.contains("static") ? 0 : 1);
        mNameIndex = constantPool.constantUtf8Info(name);
        mDescriptorIndex = constantPool.constantUtf8Info(descriptor);
        if (exceptions != null && exceptions.size() > 0) {
            addMethodAttribute(exceptionsAttribute(exceptions));
        }
        if (isSynthetic) {
            addMethodAttribute(syntheticAttribute());
        }
    }

    public void addExceptionHandler(String startLabel, String endLabel,
                                    String handlerLabel, String catchType) {
        if (catchType != null && !validInternalForm(catchType)) {
            reportEmitterError("'%s' is not in internal form", catchType);
        }
        CLException e = new CLException(startLabel, endLabel, handlerLabel,
                catchType);
        mExceptionHandlers.add(e);
    }

    public void addNoArgInstruction(int opcode) {
        CLInstruction instr = null;
        switch (CLInstruction.instructionInfo[opcode].category) {
            case ARITHMETIC1:
                instr = new CLArithmeticInstruction(opcode, mPC++);
                break;
            case ARRAY2:
                instr = new CLArrayInstruction(opcode, mPC++);
                break;
            case BIT:
                instr = new CLBitInstruction(opcode, mPC++);
                break;
            case COMPARISON:
                instr = new CLComparisonInstruction(opcode, mPC++);
                break;
            case CONVERSION:
                instr = new CLConversionInstruction(opcode, mPC++);
                break;
            case LOAD_STORE1:
                instr = new CLLoadStoreInstruction(opcode, mPC++);
                break;
            case METHOD2:
                instr = new CLMethodInstruction(opcode, mPC++);
                break;
            case MISC:
                instr = new CLMiscInstruction(opcode, mPC++);
                break;
            case STACK:
                instr = new CLStackInstruction(opcode, mPC++);
                break;
            default:
                reportOpcodeError(opcode);
        }
        if (instr != null) {
            mPC += instr.operandCount();
            mCode.add(instr);
            mInstructionAfterLabel = true;
        }
    }

    public void addOneArgInstruction(int opcode, int arg) {
        CLInstruction instr = null;
        boolean isWidened = false;
        switch (CLInstruction.instructionInfo[opcode].category) {
            case LOAD_STORE2:
                isWidened = arg > 255;
                if (isWidened) {
                    CLLoadStoreInstruction wideInstr = new CLLoadStoreInstruction(
                            WIDE, mPC++);
                    mCode.add(wideInstr);
                }
                instr = new CLLoadStoreInstruction(opcode, mPC++, arg, isWidened);
                break;
            case LOAD_STORE3:
                instr = new CLLoadStoreInstruction(opcode, mPC++, arg);
                break;
            case FLOW_CONTROL2:
                isWidened = arg > 255;
                if (isWidened) {
                    CLLoadStoreInstruction wideInstr = new CLLoadStoreInstruction(
                            WIDE, mPC++);
                    mCode.add(wideInstr);
                }
                instr = new CLFlowControlInstruction(mPC++, arg, isWidened);
                break;
            default:
                reportOpcodeError(opcode);
        }
        if (instr != null) {
            mPC += instr.operandCount();
            mCode.add(instr);
            mInstructionAfterLabel = true;
        }
    }

    public void addOneArgInstruction(int opcode,double arg) {
        CLInstruction instr = null;
        boolean isWidened = false;
        switch (CLInstruction.instructionInfo[opcode].category) {
            case LOAD_STORE4:
                instr = new CLLoadStoreInstruction(opcode, mPC++, arg);
                break;
            default:
                reportOpcodeError(opcode);
        }
        if (instr != null) {
            mPC += instr.operandCount();
            mCode.add(instr);
            mInstructionAfterLabel = true;
        }
    }

    public void addIINCInstruction(int index, int constVal) {
        boolean isWidened = index > 255 || constVal < Byte.MIN_VALUE
                || constVal > Byte.MAX_VALUE;
        if (isWidened) {
            CLLoadStoreInstruction wideInstr = new CLLoadStoreInstruction(WIDE,
                    mPC++);
            mCode.add(wideInstr);
        }
        CLArithmeticInstruction instr = new CLArithmeticInstruction(IINC,
                mPC++, index, constVal, isWidened);
        mPC += instr.operandCount();
        mCode.add(instr);
        mInstructionAfterLabel = true;
    }

    public void addMemberAccessInstruction(int opcode, String target,
                                           String name, String type) {
        if (!validInternalForm(target)) {
            reportEmitterError("%s: '%s' is not in internal form",
                    eCurrentMethod, target);
        }
        CLInstruction instr = null;
        int index, stackUnits;
        switch (CLInstruction.instructionInfo[opcode].category) {
            case FIELD:
                if (!validTypeDescriptor(type)) {
                    reportEmitterError(
                            "%s: '%s' is not a valid type descriptor for field",
                            eCurrentMethod, type);
                }
                index = constantPool.constantFieldRefInfo(target, name, type);
                stackUnits = typeStackResidue(type);
                if ((opcode == GETFIELD) || (opcode == PUTFIELD)) {
                    // This is because target of this method is also
                    // consumed from the operand stack
                    stackUnits--;
                }
                instr = new CLFieldInstruction(opcode, mPC++, index, stackUnits);
                break;
            case METHOD1:
                if (!validMethodDescriptor(type)) {
                    reportEmitterError(
                            "%s: '%s' is not a valid type descriptor for "
                                    + "method", eCurrentMethod, type);
                }
                if (opcode == INVOKEINTERFACE) {
                    index = constantPool.constantInterfaceMethodRefInfo(target,
                            name, type);
                } else {
                    index = constantPool.constantMethodRefInfo(target, name, type);
                }
                stackUnits = methodStackResidue(type);
                if (opcode != INVOKESTATIC) {
                    // This is because target of this method is also
                    // consumed from the operand stack
                    stackUnits--;
                }
                instr = new CLMethodInstruction(opcode, mPC++, index, stackUnits);

                // INVOKEINTERFACE expects the number of arguments in
                // the method to be specified explicitly.
                if (opcode == INVOKEINTERFACE) {
                    // We add 1 to account for "this"
                    ((CLMethodInstruction) instr)
                            .setArgumentCount(argumentCount(type) + 1);
                }
                break;
            default:
                reportOpcodeError(opcode);
        }
        if (instr != null) {
            mPC += instr.operandCount();
            mCode.add(instr);
        }
    }

    public void addReferenceInstruction(int opcode, String type) {
        if (!validTypeDescriptor(type) && !validInternalForm(type)) {
            reportEmitterError("%s: '%s' is neither a type descriptor nor in "
                    + "internal form", eCurrentMethod, type);
        }
        CLInstruction instr = null;
        switch (CLInstruction.instructionInfo[opcode].category) {
            case OBJECT:
                int index = constantPool.constantClassInfo(type);
                instr = new CLObjectInstruction(opcode, mPC++, index);
                break;
            default:
                reportOpcodeError(opcode);
        }
        if (instr != null) {
            mPC += instr.operandCount();
            mCode.add(instr);
        }
    }

    public void addArrayInstruction(int opcode, String type) {
        CLInstruction instr = null;
        switch (CLInstruction.instructionInfo[opcode].category) {
            case ARRAY1:
                int index = 0;
                if (opcode == NEWARRAY) {
                    if (type.equalsIgnoreCase("Z")) {
                        index = 4;
                    } else if (type.equalsIgnoreCase("C")) {
                        index = 5;
                    } else if (type.equalsIgnoreCase("F")) {
                        index = 6;
                    } else if (type.equalsIgnoreCase("D")) {
                        index = 7;
                    } else if (type.equalsIgnoreCase("B")) {
                        index = 8;
                    } else if (type.equalsIgnoreCase("S")) {
                        index = 9;
                    } else if (type.equalsIgnoreCase("I")) {
                        index = 10;
                    } else if (type.equalsIgnoreCase("J")) {
                        index = 11;
                    } else {
                        reportEmitterError(
                                "%s: '%s' is not a valid primitive type",
                                eCurrentMethod, type);
                    }
                } else {
                    if (!validTypeDescriptor(type) && !validInternalForm(type)) {
                        reportEmitterError(
                                "%s: '%s' is not a valid type descriptor "
                                        + "for an array", eCurrentMethod, type);
                    }
                    index = constantPool.constantClassInfo(type);
                }
                instr = new CLArrayInstruction(opcode, mPC++, index);
                break;
            default:
                reportOpcodeError(opcode);
        }
        if (instr != null) {
            mPC += instr.operandCount();
            mCode.add(instr);
        }
    }

    public void addMULTIANEWARRAYInstruction(String type, int dim) {
        CLInstruction instr = null;
        if (!validTypeDescriptor(type)) {
            reportEmitterError(
                    "%s: '%s' is not a valid type descriptor for an array",
                    eCurrentMethod, type);
        }
        int index = constantPool.constantClassInfo(type);
        instr = new CLArrayInstruction(MULTIANEWARRAY, mPC++, index, dim);
        if (instr != null) {
            mPC += instr.operandCount();
            mCode.add(instr);
        }
    }

    public void addBranchInstruction(int opcode, String label) {
        CLInstruction instr = null;
        switch (CLInstruction.instructionInfo[opcode].category) {
            case FLOW_CONTROL1:
                instr = new CLFlowControlInstruction(opcode, mPC++, label);
                break;
            default:
                reportOpcodeError(opcode);
        }
        if (instr != null) {
            mPC += instr.operandCount();
            mCode.add(instr);
            mInstructionAfterLabel = true;
        }
    }

    public void addTABLESWITCHInstruction(String defaultLabel, int low,
                                          int high, ArrayList<String> labels) {
        CLFlowControlInstruction instr = new CLFlowControlInstruction(
                TABLESWITCH, mPC++, defaultLabel, low, high, labels);
        mPC += instr.operandCount();
        mCode.add(instr);
        mInstructionAfterLabel = true;
    }

    public void addLOOKUPSWITCHInstruction(String defaultLabel, int numPairs,
                                           TreeMap<Integer, String> matchLabelPairs) {
        CLFlowControlInstruction instr = new CLFlowControlInstruction(
                LOOKUPSWITCH, mPC++, defaultLabel, numPairs, matchLabelPairs);
        mPC += instr.operandCount();
        mCode.add(instr);
        mInstructionAfterLabel = true;
    }

    public void addLDCInstruction(int i) {
        ldcInstruction(constantPool.constantIntegerInfo(i));
    }

    public void addLDCInstruction(float f) {
        ldcInstruction(constantPool.constantFloatInfo(f));
    }

    public void addLDCInstruction(long l) {
        ldc2wInstruction(constantPool.constantLongInfo(l));
    }

    public void addLDCInstruction(double d) {
        ldc2wInstruction(constantPool.constantDoubleInfo(d));
    }

    public void addLDCInstruction(String s) {
        ldcInstruction(constantPool.constantStringInfo(s));
    }

    public void addClassAttribute(CLAttributeInfo attribute) {
        if (attributes != null) {
            attributes.add(attribute);
        }
    }

    public void addMethodAttribute(CLAttributeInfo attribute) {
        if (mAttributes != null) {
            mAttributes.add(attribute);
        }
    }

    public void addFieldAttribute(CLAttributeInfo attribute) {
        if (fAttributes != null) {
            fAttributes.add(attribute);
        }
    }

    public void addCodeAttribute(CLAttributeInfo attribute) {
        if (mCodeAttributes != null) {
            mCodeAttributes.add(attribute);
        }
    }
    public void addLabel(String label) {
        mLabels.put(label, mPC);
        mInstructionAfterLabel = false;
    }

    public String createLabel() {
        return "Label" + mLabelCount++;
    }

    public int pc() {
        return mPC;
    }

    public CLConstantPool constantPool() {
        return constantPool;
    }

    public static void initializeByteClassLoader() {
        byteClassLoader = new ByteClassLoader();
    }

    public CLFile clFile() {
        return clFile;
    }

    public Class toClass() {
        endOpenMethodIfAny();
        Class theClass = null;
        try {
            // Extract the bytes from the class representation in
            // memory into an array of bytes
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            CLOutputStream out = new CLOutputStream(new BufferedOutputStream(
                    byteStream));
            clFile.write(out);
            out.close();
            byte[] classBytes = byteStream.toByteArray();
            byteStream.close();

            // Load a Java Class instance from its byte
            // representation
            byteClassLoader.setClassBytes(classBytes);
            theClass = byteClassLoader.loadClass(name, true);
        } catch (IOException e) {
            reportEmitterError("Cannot write class to byte stream");
        } catch (ClassNotFoundException e) {
            reportEmitterError("Cannot load class from byte stream");
        }
        return theClass;
    }

    public void write() {
        endOpenMethodIfAny();
        if (!toFile) {
            return;
        }
        String outFile = destDir + File.separator + name + ".class";
        try {
            File file = new File(destDir + File.separator
                    + name.substring(0, name.lastIndexOf("/") + 1));
            file.mkdirs();
            CLOutputStream out = new CLOutputStream(new BufferedOutputStream(
                    new FileOutputStream(outFile)));
            clFile.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            reportEmitterError("File %s not found", outFile);
        } catch (IOException e) {
            reportEmitterError("Cannot write to file %s", outFile);
        }
    }
}


class CLException {

    public String startLabel;

    public String endLabel;

    public String handlerLabel;

    public String catchType;

    public int startPC;

    public int endPC;

    public int handlerPC;


    public CLException(String startLabel, String endLabel, String handlerLabel,
                       String catchType) {
        this.startLabel = startLabel;
        this.endLabel = endLabel;
        this.handlerLabel = handlerLabel;
        this.catchType = catchType;
    }

    public boolean resolveLabels(Hashtable<String, Integer> labelToPC) {
        boolean allLabelsResolved = true;
        if (labelToPC.containsKey(startLabel)) {
            startPC = labelToPC.get(startLabel);
        } else {
            startPC = 0;
            allLabelsResolved = false;
        }
        if (labelToPC.containsKey(endLabel)) {
            endPC = labelToPC.get(endLabel);
        } else {
            endPC = 0;
            allLabelsResolved = false;
        }
        if (labelToPC.containsKey(handlerLabel)) {
            handlerPC = labelToPC.get(handlerLabel);
        } else {
            handlerPC = 0;
            allLabelsResolved = false;
        }
        return allLabelsResolved;
    }
}

class ByteClassLoader extends ClassLoader {

    private byte[] bytes;

    private boolean pkgDefined = false;


    public void setClassBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException {
        Class cls = findLoadedClass(name);
        if (cls == null) {
            try {
                cls = findSystemClass(name);
            } catch (Exception e) {
            }
        }
        if (cls == null) {
            name = name.replace("/", ".");
            String pkg = name.lastIndexOf('.') == -1 ? "" : name.substring(0,
                    name.lastIndexOf('.'));
            if (!pkgDefined) {
                definePackage(pkg, "", "", "", "", "", "", null);
                pkgDefined = true;
            }
            cls = defineClass(name, bytes, 0, bytes.length);
            if (resolve && cls != null) {
                resolveClass(cls);
            }
        }
        return cls;
    }

}

class CLOutputStream extends DataOutputStream {

    public CLOutputStream(OutputStream out) {
        super(out);
    }

    /**
     * Write four bytes to the output stream to represent the value of the
     * argument. The byte values to be written, in the order shown, are:
     *     (byte) ( 0xFF &amp; ( v &gt;&gt; 24 ) )
     *     (byte) ( 0xFF &amp; ( v &gt;&gt; 16 ) )
     *     (byte) ( 0xFF &amp; ( v &gt;&gt; 8 ) )
     *     (byte) ( 0xFF &amp; v )
     */

    public final void writeInt(long v) throws IOException {
        long mask = 0xFF;
        out.write((byte) (mask & (v >> 24)));
        out.write((byte) (mask & (v >> 16)));
        out.write((byte) (mask & (v >> 8)));
        out.write((byte) (mask & v));
    }

}

class CLBranchTarget {

    public CLInstruction target;

    public int stackDepth;

    public CLBranchTarget(CLInstruction target, int stackDepth) {
        this.target = target;
        this.stackDepth = stackDepth;
    }

}

class CLBranchStack {

    private Stack<CLBranchTarget> branchTargets;

    private Hashtable<CLInstruction, CLBranchTarget> visitedTargets;

    private CLBranchTarget visit(CLInstruction target, int stackDepth) {
        CLBranchTarget bt = new CLBranchTarget(target, stackDepth);
        visitedTargets.put(target, bt);
        return bt;
    }

    private boolean visited(CLInstruction target) {
        return (visitedTargets.get(target) != null);
    }

    public CLBranchStack() {
        this.branchTargets = new Stack<CLBranchTarget>();
        this.visitedTargets = new Hashtable<CLInstruction, CLBranchTarget>();
    }

    public void push(CLInstruction target, int stackDepth) {
        if (visited(target)) {
            return;
        }
        branchTargets.push(visit(target, stackDepth));
    }

    public CLBranchTarget pop() {
        if (!branchTargets.empty()) {
            CLBranchTarget bt = (CLBranchTarget) branchTargets.pop();
            return bt;
        }
        return null;
    }

}