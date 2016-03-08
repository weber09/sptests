package algol;

/**
 * Created by Gabriel on 06/03/2016.
 */
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import static algol.CLConstants.*;
import static algol.CLConstants.Category.*;

abstract class CLInstruction {

    protected int opcode;

    protected String mnemonic;

    protected int operandCount;

    protected int pc;

    protected int stackUnits;

    protected int localVariableIndex;

    public static final CLInsInfo[] instructionInfo = {
            new CLInsInfo(NOP, "nop", 0, IRRELEVANT, 0, MISC),
            new CLInsInfo(ACONST_NULL, "aconst_null", 0, IRRELEVANT, 1,
                    LOAD_STORE1),
            new CLInsInfo(ICONST_M1, "iconst_m1", 0, IRRELEVANT, 1, LOAD_STORE1),
            new CLInsInfo(ICONST_0, "iconst_0", 0, IRRELEVANT, 1, LOAD_STORE1),
            new CLInsInfo(ICONST_1, "iconst_1", 0, IRRELEVANT, 1, LOAD_STORE1),
            new CLInsInfo(ICONST_2, "iconst_2", 0, IRRELEVANT, 1, LOAD_STORE1),
            new CLInsInfo(ICONST_3, "iconst_3", 0, IRRELEVANT, 1, LOAD_STORE1),
            new CLInsInfo(ICONST_4, "iconst_4", 0, IRRELEVANT, 1, LOAD_STORE1),
            new CLInsInfo(ICONST_5, "iconst_5", 0, IRRELEVANT, 1, LOAD_STORE1),
            new CLInsInfo(LCONST_0, "lconst_0", 0, IRRELEVANT, 2, LOAD_STORE1),
            new CLInsInfo(LCONST_1, "lconst_1", 0, IRRELEVANT, 2, LOAD_STORE1),
            new CLInsInfo(FCONST_0, "fconst_0", 0, IRRELEVANT, 1, LOAD_STORE1),
            new CLInsInfo(FCONST_1, "fconst_1", 0, IRRELEVANT, 1, LOAD_STORE1),
            new CLInsInfo(FCONST_2, "fconst_2", 0, IRRELEVANT, 1, LOAD_STORE1),
            new CLInsInfo(DCONST_0, "dconst_0", 0, IRRELEVANT, 2, LOAD_STORE1),
            new CLInsInfo(DCONST_1, "dconst_1", 0, IRRELEVANT, 2, LOAD_STORE1),
            new CLInsInfo(BIPUSH, "bipush", 1, IRRELEVANT, 1, LOAD_STORE3),
            new CLInsInfo(SIPUSH, "sipush", 2, IRRELEVANT, 1, LOAD_STORE3),
            new CLInsInfo(LDC, "ldc", 1, IRRELEVANT, 1, LOAD_STORE4),
            new CLInsInfo(LDC_W, "ldc_w", 2, IRRELEVANT, 1, LOAD_STORE4),
            new CLInsInfo(LDC2_W, "ldc2_w", 2, IRRELEVANT, 2, LOAD_STORE4),
            new CLInsInfo(ILOAD, "iload", 1, DYNAMIC, 1, LOAD_STORE2),
            new CLInsInfo(LLOAD, "lload", 1, DYNAMIC, 2, LOAD_STORE2),
            new CLInsInfo(FLOAD, "fload", 1, DYNAMIC, 1, LOAD_STORE2),
            new CLInsInfo(DLOAD, "dload", 1, DYNAMIC, 2, LOAD_STORE2),
            new CLInsInfo(ALOAD, "aload", 1, DYNAMIC, 1, LOAD_STORE2),
            new CLInsInfo(ILOAD_0, "iload_0", 0, 0, 1, LOAD_STORE1),
            new CLInsInfo(ILOAD_1, "iload_1", 0, 1, 1, LOAD_STORE1),
            new CLInsInfo(ILOAD_2, "iload_2", 0, 2, 1, LOAD_STORE1),
            new CLInsInfo(ILOAD_3, "iload_3", 0, 3, 1, LOAD_STORE1),
            new CLInsInfo(LLOAD_0, "lload_0", 0, 0, 2, LOAD_STORE1),
            new CLInsInfo(LLOAD_1, "lload_1", 0, 1, 2, LOAD_STORE1),
            new CLInsInfo(LLOAD_2, "lload_2", 0, 2, 2, LOAD_STORE1),
            new CLInsInfo(LLOAD_3, "lload_3", 0, 3, 2, LOAD_STORE1),
            new CLInsInfo(FLOAD_0, "fload_0", 0, 0, 1, LOAD_STORE1),
            new CLInsInfo(FLOAD_1, "fload_1", 0, 1, 1, LOAD_STORE1),
            new CLInsInfo(FLOAD_2, "fload_2", 0, 2, 1, LOAD_STORE1),
            new CLInsInfo(FLOAD_3, "fload_3", 0, 3, 1, LOAD_STORE1),
            new CLInsInfo(DLOAD_0, "dload_0", 0, 0, 2, LOAD_STORE1),
            new CLInsInfo(DLOAD_1, "dload_1", 0, 1, 2, LOAD_STORE1),
            new CLInsInfo(DLOAD_2, "dload_2", 0, 2, 2, LOAD_STORE1),
            new CLInsInfo(DLOAD_3, "dload_3", 0, 3, 2, LOAD_STORE1),
            new CLInsInfo(ALOAD_0, "aload_0", 0, 0, 1, LOAD_STORE1),
            new CLInsInfo(ALOAD_1, "aload_1", 0, 1, 1, LOAD_STORE1),
            new CLInsInfo(ALOAD_2, "aload_2", 0, 2, 1, LOAD_STORE1),
            new CLInsInfo(ALOAD_3, "aload_3", 0, 3, 1, LOAD_STORE1),
            new CLInsInfo(IALOAD, "iaload", 0, IRRELEVANT, -1, ARRAY2),
            new CLInsInfo(LALOAD, "laload", 0, IRRELEVANT, 0, ARRAY2),
            new CLInsInfo(FALOAD, "faload", 0, IRRELEVANT, -1, ARRAY2),
            new CLInsInfo(DALOAD, "daload", 0, IRRELEVANT, 0, ARRAY2),
            new CLInsInfo(AALOAD, "aaload", 0, IRRELEVANT, -1, ARRAY2),
            new CLInsInfo(BALOAD, "baload", 0, IRRELEVANT, -1, ARRAY2),
            new CLInsInfo(CALOAD, "caload", 0, IRRELEVANT, -1, ARRAY2),
            new CLInsInfo(SALOAD, "saload", 0, IRRELEVANT, -1, ARRAY2),
            new CLInsInfo(ISTORE, "istore", 1, DYNAMIC, -1, LOAD_STORE2),
            new CLInsInfo(LSTORE, "lstore", 1, DYNAMIC, -2, LOAD_STORE2),
            new CLInsInfo(FSTORE, "fstore", 1, DYNAMIC, -1, LOAD_STORE2),
            new CLInsInfo(DSTORE, "dstore", 1, DYNAMIC, -2, LOAD_STORE2),
            new CLInsInfo(ASTORE, "astore", 1, DYNAMIC, -1, LOAD_STORE2),
            new CLInsInfo(ISTORE_0, "istore_0", 0, 0, -1, LOAD_STORE1),
            new CLInsInfo(ISTORE_1, "istore_1", 0, 1, -1, LOAD_STORE1),
            new CLInsInfo(ISTORE_2, "istore_2", 0, 2, -1, LOAD_STORE1),
            new CLInsInfo(ISTORE_3, "istore_3", 0, 3, -1, LOAD_STORE1),
            new CLInsInfo(LSTORE_0, "lstore_0", 0, 0, -2, LOAD_STORE1),
            new CLInsInfo(LSTORE_1, "lstore_1", 0, 1, -2, LOAD_STORE1),
            new CLInsInfo(LSTORE_2, "lstore_2", 0, 2, -2, LOAD_STORE1),
            new CLInsInfo(LSTORE_3, "lstore_3", 0, 3, -2, LOAD_STORE1),
            new CLInsInfo(FSTORE_0, "fstore_0", 0, 0, -1, LOAD_STORE1),
            new CLInsInfo(FSTORE_1, "fstore_1", 0, 1, -1, LOAD_STORE1),
            new CLInsInfo(FSTORE_2, "fstore_2", 0, 2, -1, LOAD_STORE1),
            new CLInsInfo(FSTORE_3, "fstore_3", 0, 3, -1, LOAD_STORE1),
            new CLInsInfo(DSTORE_0, "dstore_0", 0, 0, -2, LOAD_STORE1),
            new CLInsInfo(DSTORE_1, "dstore_1", 0, 1, -2, LOAD_STORE1),
            new CLInsInfo(DSTORE_2, "dstore_2", 0, 2, -2, LOAD_STORE1),
            new CLInsInfo(DSTORE_3, "dstore_3", 0, 3, -2, LOAD_STORE1),
            new CLInsInfo(ASTORE_0, "astore_0", 0, 0, -1, LOAD_STORE1),
            new CLInsInfo(ASTORE_1, "astore_1", 0, 1, -1, LOAD_STORE1),
            new CLInsInfo(ASTORE_2, "astore_2", 0, 2, -1, LOAD_STORE1),
            new CLInsInfo(ASTORE_3, "astore_3", 0, 3, -1, LOAD_STORE1),
            new CLInsInfo(IASTORE, "iastore", 0, IRRELEVANT, -3, ARRAY2),
            new CLInsInfo(LASTORE, "lastore", 0, IRRELEVANT, -4, ARRAY2),
            new CLInsInfo(FASTORE, "fastore", 0, IRRELEVANT, -3, ARRAY2),
            new CLInsInfo(DASTORE, "dastore", 0, IRRELEVANT, -4, ARRAY2),
            new CLInsInfo(AASTORE, "aastore", 0, IRRELEVANT, -3, ARRAY2),
            new CLInsInfo(BASTORE, "bastore", 0, IRRELEVANT, -3, ARRAY2),
            new CLInsInfo(CASTORE, "castore", 0, IRRELEVANT, -3, ARRAY2),
            new CLInsInfo(SASTORE, "sastore", 0, IRRELEVANT, -3, ARRAY2),
            new CLInsInfo(POP, "pop", 0, IRRELEVANT, -1, STACK),
            new CLInsInfo(POP2, "pop2", 0, IRRELEVANT, -2, STACK),
            new CLInsInfo(DUP, "dup", 0, IRRELEVANT, 1, STACK),
            new CLInsInfo(DUP_X1, "dup_x1", 0, IRRELEVANT, 1, STACK),
            new CLInsInfo(DUP_X2, "dup_x2", 0, IRRELEVANT, 1, STACK),
            new CLInsInfo(DUP2, "dup2", 0, IRRELEVANT, 2, STACK),
            new CLInsInfo(DUP2_X1, "dup2_x1", 0, IRRELEVANT, 2, STACK),
            new CLInsInfo(DUP2_X2, "dup2_x2", 0, IRRELEVANT, 2, STACK),
            new CLInsInfo(SWAP, "swap", 0, IRRELEVANT, 0, STACK),
            new CLInsInfo(IADD, "iadd", 0, IRRELEVANT, -1, ARITHMETIC1),
            new CLInsInfo(LADD, "ladd", 0, IRRELEVANT, -2, ARITHMETIC1),
            new CLInsInfo(FADD, "fadd", 0, IRRELEVANT, -1, ARITHMETIC1),
            new CLInsInfo(DADD, "dadd", 0, IRRELEVANT, -2, ARITHMETIC1),
            new CLInsInfo(ISUB, "isub", 0, IRRELEVANT, -1, ARITHMETIC1),
            new CLInsInfo(LSUB, "lsub", 0, IRRELEVANT, -2, ARITHMETIC1),
            new CLInsInfo(FSUB, "fsub", 0, IRRELEVANT, -1, ARITHMETIC1),
            new CLInsInfo(DSUB, "dsub", 0, IRRELEVANT, -2, ARITHMETIC1),
            new CLInsInfo(IMUL, "imul", 0, IRRELEVANT, -1, ARITHMETIC1),
            new CLInsInfo(LMUL, "lmul", 0, IRRELEVANT, -2, ARITHMETIC1),
            new CLInsInfo(FMUL, "fmul", 0, IRRELEVANT, -1, ARITHMETIC1),
            new CLInsInfo(DMUL, "dmul", 0, IRRELEVANT, -2, ARITHMETIC1),
            new CLInsInfo(IDIV, "idiv", 0, IRRELEVANT, -1, ARITHMETIC1),
            new CLInsInfo(LDIV, "ldiv", 0, IRRELEVANT, -2, ARITHMETIC1),
            new CLInsInfo(FDIV, "fdiv", 0, IRRELEVANT, -1, ARITHMETIC1),
            new CLInsInfo(DDIV, "ddiv", 0, IRRELEVANT, -2, ARITHMETIC1),
            new CLInsInfo(IREM, "irem", 0, IRRELEVANT, -1, ARITHMETIC1),
            new CLInsInfo(LREM, "lrem", 0, IRRELEVANT, -2, ARITHMETIC1),
            new CLInsInfo(FREM, "frem", 0, IRRELEVANT, -1, ARITHMETIC1),
            new CLInsInfo(DREM, "drem", 0, IRRELEVANT, -2, ARITHMETIC1),
            new CLInsInfo(INEG, "ineg", 0, IRRELEVANT, 0, ARITHMETIC1),
            new CLInsInfo(LNEG, "lneg", 0, IRRELEVANT, 0, ARITHMETIC1),
            new CLInsInfo(FNEG, "fneg", 0, IRRELEVANT, 0, ARITHMETIC1),
            new CLInsInfo(DNEG, "dneg", 0, IRRELEVANT, 0, ARITHMETIC1),
            new CLInsInfo(ISHL, "ishl", 0, IRRELEVANT, -1, BIT),
            new CLInsInfo(LSHL, "lshl", 0, IRRELEVANT, -2, BIT),
            new CLInsInfo(ISHR, "ishr", 0, IRRELEVANT, -1, BIT),
            new CLInsInfo(LSHR, "lshr", 0, IRRELEVANT, -2, BIT),
            new CLInsInfo(IUSHR, "iushr", 0, IRRELEVANT, -1, BIT),
            new CLInsInfo(LUSHR, "lushr", 0, IRRELEVANT, -2, BIT),
            new CLInsInfo(IAND, "iand", 0, IRRELEVANT, -1, BIT),
            new CLInsInfo(LAND, "land", 0, IRRELEVANT, -2, BIT),
            new CLInsInfo(IOR, "ior", 0, IRRELEVANT, -1, BIT),
            new CLInsInfo(LOR, "lor", 0, IRRELEVANT, -2, BIT),
            new CLInsInfo(IXOR, "ixor", 0, IRRELEVANT, -1, BIT),
            new CLInsInfo(LXOR, "lxor", 0, IRRELEVANT, -2, BIT),
            new CLInsInfo(IINC, "iinc", 2, DYNAMIC, 0, ARITHMETIC2),
            new CLInsInfo(I2L, "i2l", 0, IRRELEVANT, 1, CONVERSION),
            new CLInsInfo(I2F, "i2f", 0, IRRELEVANT, 0, CONVERSION),
            new CLInsInfo(I2D, "i2d", 0, IRRELEVANT, 1, CONVERSION),
            new CLInsInfo(L2I, "l2i", 0, IRRELEVANT, -1, CONVERSION),
            new CLInsInfo(L2F, "l2f", 0, IRRELEVANT, -1, CONVERSION),
            new CLInsInfo(L2D, "l2d", 0, IRRELEVANT, 0, CONVERSION),
            new CLInsInfo(F2I, "f2i", 0, IRRELEVANT, 0, CONVERSION),
            new CLInsInfo(F2L, "f2l", 0, IRRELEVANT, 1, CONVERSION),
            new CLInsInfo(F2D, "f2d", 0, IRRELEVANT, 1, CONVERSION),
            new CLInsInfo(D2I, "d2i", 0, IRRELEVANT, -1, CONVERSION),
            new CLInsInfo(D2L, "d2l", 0, IRRELEVANT, 0, CONVERSION),
            new CLInsInfo(D2F, "d2f", 0, IRRELEVANT, -1, CONVERSION),
            new CLInsInfo(I2B, "i2b", 0, IRRELEVANT, 0, CONVERSION),
            new CLInsInfo(I2C, "i2c", 0, IRRELEVANT, 0, CONVERSION),
            new CLInsInfo(I2S, "i2s", 0, IRRELEVANT, 0, CONVERSION),
            new CLInsInfo(LCMP, "lcmp", 0, IRRELEVANT, -3, COMPARISON),
            new CLInsInfo(FCMPL, "fcmpl", 0, IRRELEVANT, -1, COMPARISON),
            new CLInsInfo(FCMPG, "fcmpg", 0, IRRELEVANT, -1, COMPARISON),
            new CLInsInfo(DCMPL, "dcmpl", 0, IRRELEVANT, -3, COMPARISON),
            new CLInsInfo(DCMPG, "dcmpg", 0, IRRELEVANT, -3, COMPARISON),
            new CLInsInfo(IFEQ, "ifeq", 2, IRRELEVANT, -1, FLOW_CONTROL1),
            new CLInsInfo(IFNE, "ifne", 2, IRRELEVANT, -1, FLOW_CONTROL1),
            new CLInsInfo(IFLT, "iflt", 2, IRRELEVANT, -1, FLOW_CONTROL1),
            new CLInsInfo(IFGE, "ifge", 2, IRRELEVANT, -1, FLOW_CONTROL1),
            new CLInsInfo(IFGT, "ifgt", 2, IRRELEVANT, -1, FLOW_CONTROL1),
            new CLInsInfo(IFLE, "ifle", 2, IRRELEVANT, -1, FLOW_CONTROL1),
            new CLInsInfo(IF_ICMPEQ, "if_icmpeq", 2, IRRELEVANT, -2,
                    FLOW_CONTROL1),
            new CLInsInfo(IF_ICMPNE, "if_icmpne", 2, IRRELEVANT, -2,
                    FLOW_CONTROL1),
            new CLInsInfo(IF_ICMPLT, "if_icmplt", 2, IRRELEVANT, -2,
                    FLOW_CONTROL1),
            new CLInsInfo(IF_ICMPGE, "if_icmpge", 2, IRRELEVANT, -2,
                    FLOW_CONTROL1),
            new CLInsInfo(IF_ICMPGT, "if_icmpgt", 2, IRRELEVANT, -2,
                    FLOW_CONTROL1),
            new CLInsInfo(IF_ICMPLE, "if_icmple", 2, IRRELEVANT, -2,
                    FLOW_CONTROL1),
            new CLInsInfo(IF_ACMPEQ, "if_acmpeq", 2, IRRELEVANT, -2,
                    FLOW_CONTROL1),
            new CLInsInfo(IF_ACMPNE, "if_acmpne", 2, IRRELEVANT, -2,
                    FLOW_CONTROL1),
            new CLInsInfo(GOTO, "goto", 2, IRRELEVANT, 0, FLOW_CONTROL1),
            new CLInsInfo(JSR, "jsr", 2, IRRELEVANT, 1, FLOW_CONTROL1),
            new CLInsInfo(RET, "ret", 1, IRRELEVANT, 0, FLOW_CONTROL2),
            new CLInsInfo(TABLESWITCH, "tableswitch", DYNAMIC, IRRELEVANT, -1,
                    FLOW_CONTROL3),
            new CLInsInfo(LOOKUPSWITCH, "lookupswitch", DYNAMIC, IRRELEVANT,
                    -1, FLOW_CONTROL4),
            new CLInsInfo(IRETURN, "ireturn", 0, IRRELEVANT, EMPTY_STACK,
                    METHOD2),
            new CLInsInfo(LRETURN, "lreturn", 0, IRRELEVANT, EMPTY_STACK,
                    METHOD2),
            new CLInsInfo(FRETURN, "freturn", 0, IRRELEVANT, EMPTY_STACK,
                    METHOD2),
            new CLInsInfo(DRETURN, "dreturn", 0, IRRELEVANT, EMPTY_STACK,
                    METHOD2),
            new CLInsInfo(ARETURN, "areturn", 0, IRRELEVANT, EMPTY_STACK,
                    METHOD2),
            new CLInsInfo(RETURN, "return", 0, IRRELEVANT, EMPTY_STACK, METHOD2),
            new CLInsInfo(GETSTATIC, "getstatic", 2, IRRELEVANT, DYNAMIC, FIELD),
            new CLInsInfo(PUTSTATIC, "putstatic", 2, IRRELEVANT, DYNAMIC, FIELD),
            new CLInsInfo(GETFIELD, "getfield", 2, IRRELEVANT, DYNAMIC, FIELD),
            new CLInsInfo(PUTFIELD, "putfield", 2, IRRELEVANT, DYNAMIC, FIELD),
            new CLInsInfo(INVOKEVIRTUAL, "invokevirtual", 2, IRRELEVANT,
                    DYNAMIC, METHOD1),
            new CLInsInfo(INVOKESPECIAL, "invokespecial", 2, IRRELEVANT,
                    DYNAMIC, METHOD1),
            new CLInsInfo(INVOKESTATIC, "invokestatic", 2, IRRELEVANT, DYNAMIC,
                    METHOD1),
            new CLInsInfo(INVOKEINTERFACE, "invokeinterface", 4, IRRELEVANT,
                    DYNAMIC, METHOD1),
            new CLInsInfo(INVOKEDYNAMIC, "invokedynamic", 2, IRRELEVANT,
                    DYNAMIC, METHOD1),
            new CLInsInfo(NEW, "new", 2, IRRELEVANT, 1, OBJECT),
            new CLInsInfo(NEWARRAY, "newarray", 1, IRRELEVANT, 0, ARRAY1),
            new CLInsInfo(ANEWARRAY, "anewarray", 2, IRRELEVANT, 0, ARRAY1),
            new CLInsInfo(ARRAYLENGTH, "arraylength", 0, IRRELEVANT, 0, ARRAY2),
            new CLInsInfo(ATHROW, "athrow", 0, IRRELEVANT, UNIT_SIZE_STACK,
                    MISC),
            new CLInsInfo(CHECKCAST, "checkcast", 2, IRRELEVANT, 0, OBJECT),
            new CLInsInfo(INSTANCEOF, "instanceof", 2, IRRELEVANT, 0, OBJECT),
            new CLInsInfo(MONITORENTER, "monitorenter", 0, IRRELEVANT, -1, MISC),
            new CLInsInfo(MONITOREXIT, "monitorexit", 0, IRRELEVANT, -1, MISC),
            new CLInsInfo(WIDE, "wide", 3, IRRELEVANT, 0, LOAD_STORE1),
            new CLInsInfo(MULTIANEWARRAY, "multianewarray", 3, IRRELEVANT, 0,
                    ARRAY3),
            new CLInsInfo(IFNULL, "ifnull", 2, IRRELEVANT, -1, FLOW_CONTROL1),
            new CLInsInfo(IFNONNULL, "ifnonnull", 2, IRRELEVANT, -1,
                    FLOW_CONTROL1),
            new CLInsInfo(GOTO_W, "goto_w", 4, IRRELEVANT, 0, FLOW_CONTROL1),
            new CLInsInfo(JSR_W, "jsr_w", 4, IRRELEVANT, 1, FLOW_CONTROL1) };

    public static boolean isValid(int opcode) {
        return NOP <= opcode && opcode <= JSR_W;
    }

    public int opcode() {
        return opcode;
    }

    public String mnemonic() {
        return mnemonic;
    }

    public int operandCount() {
        return operandCount;
    }

    public int pc() {
        return pc;
    }

    public int stackUnits() {
        return stackUnits;
    }

    public int localVariableIndex() {
        return localVariableIndex;
    }

    public abstract ArrayList<Integer> toBytes();

    protected int byteAt(int i, int byteNum) {
        int j = 0, mask = 0xFF;
        switch (byteNum) {
            case 1: // lower order
                j = i & mask;
                break;
            case 2:
                j = (i >> 8) & mask;
                break;
            case 3:
                j = (i >> 16) & mask;
                break;
            case 4: // higher order
                j = (i >> 24) & mask;
                break;
        }
        return j;
    }

}

class CLObjectInstruction extends CLInstruction {

    private int index;

    public CLObjectInstruction(int opcode, int pc, int index) {
        super.opcode = opcode;
        super.pc = pc;
        mnemonic = instructionInfo[opcode].mnemonic;
        operandCount = instructionInfo[opcode].operandCount;
        stackUnits = instructionInfo[opcode].stackUnits;
        localVariableIndex = instructionInfo[opcode].localVariableIndex;
        this.index = index;
    }

    public ArrayList<Integer> toBytes() {
        ArrayList<Integer> bytes = new ArrayList<Integer>();
        bytes.add(opcode);
        bytes.add(byteAt(index, 2));
        bytes.add(byteAt(index, 1));
        return bytes;
    }

}

class CLFieldInstruction extends CLInstruction {

    private int index;

    public CLFieldInstruction(int opcode, int pc, int index, int stackUnits) {
        super.opcode = opcode;
        super.pc = pc;
        mnemonic = instructionInfo[opcode].mnemonic;
        operandCount = instructionInfo[opcode].operandCount;
        super.stackUnits = stackUnits;
        localVariableIndex = instructionInfo[opcode].localVariableIndex;
        this.index = index;
    }

    public ArrayList<Integer> toBytes() {
        ArrayList<Integer> bytes = new ArrayList<Integer>();
        bytes.add(opcode);
        bytes.add(byteAt(index, 2));
        bytes.add(byteAt(index, 1));
        return bytes;
    }

}

class CLMethodInstruction extends CLInstruction {

    private int index;

    private int nArgs;

    public CLMethodInstruction(int opcode, int pc, int index, int stackUnits) {
        super.opcode = opcode;
        super.pc = pc;
        mnemonic = instructionInfo[opcode].mnemonic;
        operandCount = instructionInfo[opcode].operandCount;
        super.stackUnits = stackUnits;
        localVariableIndex = instructionInfo[opcode].localVariableIndex;
        this.index = index;
    }

    public CLMethodInstruction(int opcode, int pc) {
        super.opcode = opcode;
        super.pc = pc;
        mnemonic = instructionInfo[opcode].mnemonic;
        operandCount = instructionInfo[opcode].operandCount;
        stackUnits = instructionInfo[opcode].stackUnits;
        localVariableIndex = instructionInfo[opcode].localVariableIndex;
    }

    public void setArgumentCount(int nArgs) {
        this.nArgs = nArgs;
    }

    public ArrayList<Integer> toBytes() {
        ArrayList<Integer> bytes = new ArrayList<Integer>();
        bytes.add(opcode);
        if (instructionInfo[opcode].category == METHOD1) {
            bytes.add(byteAt(index, 2));
            bytes.add(byteAt(index, 1));

            // INVOKEINTERFACE expects the number of arguments of
            // the method as the third operand and a fourth
            // argument which must always be 0.
            if (opcode == INVOKEINTERFACE) {
                bytes.add(byteAt(nArgs, 1));
                bytes.add(0);
            }
        }
        return bytes;
    }

}

class CLArrayInstruction extends CLInstruction {
    private int type;

    private int dim;

    public CLArrayInstruction(int opcode, int pc, int type) {
        super.opcode = opcode;
        super.pc = pc;
        mnemonic = instructionInfo[opcode].mnemonic;
        operandCount = instructionInfo[opcode].operandCount;
        stackUnits = instructionInfo[opcode].stackUnits;
        localVariableIndex = instructionInfo[opcode].localVariableIndex;
        this.type = type;
    }

    public CLArrayInstruction(int opcode, int pc, int type, int dim) {
        super.opcode = opcode;
        super.pc = pc;
        mnemonic = instructionInfo[opcode].mnemonic;
        operandCount = instructionInfo[opcode].operandCount;
        stackUnits = instructionInfo[opcode].stackUnits;
        localVariableIndex = instructionInfo[opcode].localVariableIndex;
        this.type = type;
        this.dim = dim;
    }

    public CLArrayInstruction(int opcode, int pc) {
        super.opcode = opcode;
        super.pc = pc;
        mnemonic = instructionInfo[opcode].mnemonic;
        operandCount = instructionInfo[opcode].operandCount;
        stackUnits = instructionInfo[opcode].stackUnits;
        localVariableIndex = instructionInfo[opcode].localVariableIndex;
    }

    public ArrayList<Integer> toBytes() {
        ArrayList<Integer> bytes = new ArrayList<Integer>();
        bytes.add(opcode);
        switch (opcode) {
            case NEWARRAY:
                bytes.add(byteAt(type, 1));
                break;
            case ANEWARRAY:
                bytes.add(byteAt(type, 2));
                bytes.add(byteAt(type, 1));
                break;
            case MULTIANEWARRAY:
                bytes.add(byteAt(type, 2));
                bytes.add(byteAt(type, 1));
                bytes.add(byteAt(dim, 1));
                break;
        }
        return bytes;
    }

}
class CLArithmeticInstruction extends CLInstruction {

    private boolean isWidened;

    private int constVal;

    public CLArithmeticInstruction(int opcode, int pc) {
        super.opcode = opcode;
        super.pc = pc;
        mnemonic = instructionInfo[opcode].mnemonic;
        operandCount = instructionInfo[opcode].operandCount;
        stackUnits = instructionInfo[opcode].stackUnits;
        localVariableIndex = instructionInfo[opcode].localVariableIndex;
    }

    public CLArithmeticInstruction(int opcode, int pc, int localVariableIndex,
                                   int constVal, boolean isWidened) {
        super.opcode = opcode;
        super.pc = pc;
        super.localVariableIndex = localVariableIndex;
        mnemonic = instructionInfo[opcode].mnemonic;
        operandCount = instructionInfo[opcode].operandCount;
        stackUnits = instructionInfo[opcode].stackUnits;
        this.constVal = constVal;
        this.isWidened = isWidened;
    }

    public ArrayList<Integer> toBytes() {
        ArrayList<Integer> bytes = new ArrayList<Integer>();
        bytes.add(opcode);
        if (opcode == IINC) {
            if (isWidened) {
                bytes.add(byteAt(localVariableIndex, 2));
                bytes.add(byteAt(localVariableIndex, 1));
                bytes.add(byteAt(constVal, 2));
                bytes.add(byteAt(constVal, 1));
            } else {
                bytes.add(byteAt(localVariableIndex, 1));
                bytes.add(byteAt(constVal, 1));
            }
        }
        return bytes;
    }

}

class CLBitInstruction extends CLInstruction {

    public CLBitInstruction(int opcode, int pc) {
        super.opcode = opcode;
        super.pc = pc;
        mnemonic = instructionInfo[opcode].mnemonic;
        operandCount = instructionInfo[opcode].operandCount;
        stackUnits = instructionInfo[opcode].stackUnits;
        localVariableIndex = instructionInfo[opcode].localVariableIndex;
    }

    public ArrayList<Integer> toBytes() {
        ArrayList<Integer> bytes = new ArrayList<Integer>();
        bytes.add(opcode);
        return bytes;
    }

}

class CLComparisonInstruction extends CLInstruction {


    public CLComparisonInstruction(int opcode, int pc) {
        super.opcode = opcode;
        super.pc = pc;
        mnemonic = instructionInfo[opcode].mnemonic;
        operandCount = instructionInfo[opcode].operandCount;
        stackUnits = instructionInfo[opcode].stackUnits;
        localVariableIndex = instructionInfo[opcode].localVariableIndex;
    }

    public ArrayList<Integer> toBytes() {
        ArrayList<Integer> bytes = new ArrayList<Integer>();
        bytes.add(opcode);
        return bytes;
    }

}

class CLConversionInstruction extends CLInstruction {

    public CLConversionInstruction(int opcode, int pc) {
        super.opcode = opcode;
        super.pc = pc;
        mnemonic = instructionInfo[opcode].mnemonic;
        operandCount = instructionInfo[opcode].operandCount;
        stackUnits = instructionInfo[opcode].stackUnits;
        localVariableIndex = instructionInfo[opcode].localVariableIndex;
    }

    public ArrayList<Integer> toBytes() {
        ArrayList<Integer> bytes = new ArrayList<Integer>();
        bytes.add(opcode);
        return bytes;
    }

}

class CLFlowControlInstruction extends CLInstruction {

    private String jumpToLabel;

    private int jumpToOffset;

    private int index;

    private boolean isWidened;

    private int pad;

    private String defaultLabel;

    private int defaultOffset;

    private int numPairs;

    private TreeMap<Integer, String> matchLabelPairs;

    private TreeMap<Integer, Integer> matchOffsetPairs;

    private int low;

    private int high;

    private ArrayList<String> labels;

    private ArrayList<Integer> offsets;


    public CLFlowControlInstruction(int opcode, int pc, String jumpToLabel) {
        super.opcode = opcode;
        super.pc = pc;
        mnemonic = instructionInfo[opcode].mnemonic;
        operandCount = instructionInfo[opcode].operandCount;
        stackUnits = instructionInfo[opcode].stackUnits;
        localVariableIndex = instructionInfo[opcode].localVariableIndex;
        this.jumpToLabel = jumpToLabel;
    }

    public CLFlowControlInstruction(int pc, int index, boolean isWidened) {
        super.opcode = RET;
        super.pc = pc;
        mnemonic = instructionInfo[opcode].mnemonic;
        operandCount = instructionInfo[opcode].operandCount;
        stackUnits = instructionInfo[opcode].stackUnits;
        localVariableIndex = instructionInfo[opcode].localVariableIndex;
        this.index = index;
        this.isWidened = isWidened;
    }


    public CLFlowControlInstruction(int opcode, int pc, String defaultLabel,
                                    int low, int high, ArrayList<String> labels) {
        super.opcode = opcode;
        super.pc = pc;
        mnemonic = instructionInfo[opcode].mnemonic;
        stackUnits = instructionInfo[opcode].stackUnits;
        localVariableIndex = instructionInfo[opcode].localVariableIndex;
        this.defaultLabel = defaultLabel;
        this.low = low;
        this.high = high;
        this.labels = labels;
        pad = 4 - ((pc + 1) % 4);
        operandCount = pad + 12 + 4 * labels.size();
    }

    public CLFlowControlInstruction(int opcode, int pc, String defaultLabel,
                                    int numPairs, TreeMap<Integer, String> matchLabelPairs) {
        super.opcode = opcode;
        super.pc = pc;
        mnemonic = instructionInfo[opcode].mnemonic;
        stackUnits = instructionInfo[opcode].stackUnits;
        localVariableIndex = instructionInfo[opcode].localVariableIndex;
        this.defaultLabel = defaultLabel;
        this.numPairs = numPairs;
        this.matchLabelPairs = matchLabelPairs;
        pad = 4 - ((pc + 1) % 4);
        operandCount = pad + 8 + 8 * numPairs;
    }

    public boolean resolveLabels(Hashtable<String, Integer> labelToPC) {
        boolean allLabelsResolved = true;
        if (instructionInfo[opcode].category == FLOW_CONTROL1) {
            if (labelToPC.containsKey(jumpToLabel)) {
                jumpToOffset = labelToPC.get(jumpToLabel) - pc;
            } else {
                jumpToOffset = operandCount;
                allLabelsResolved = false;
            }
        } else if (opcode == LOOKUPSWITCH) {
            if (labelToPC.containsKey(defaultLabel)) {
                defaultOffset = labelToPC.get(defaultLabel) - pc;
            } else {
                defaultOffset = operandCount;
                allLabelsResolved = false;
            }
            matchOffsetPairs = new TreeMap<Integer, Integer>();
            Set<Entry<Integer, String>> matches = matchLabelPairs.entrySet();
            Iterator<Entry<Integer, String>> iter = matches.iterator();
            while (iter.hasNext()) {
                Entry<Integer, String> entry = iter.next();
                int match = entry.getKey();
                String label = entry.getValue();
                if (labelToPC.containsKey(label)) {
                    matchOffsetPairs.put(match, labelToPC.get(label) - pc);
                } else {
                    matchOffsetPairs.put(match, operandCount);
                    allLabelsResolved = false;
                }
            }
        } else if (opcode == TABLESWITCH) {
            if (labelToPC.containsKey(defaultLabel)) {
                defaultOffset = labelToPC.get(defaultLabel) - pc;
            } else {
                defaultOffset = operandCount;
                allLabelsResolved = false;
            }
            offsets = new ArrayList<Integer>();
            for (int i = 0; i < labels.size(); i++) {
                if (labelToPC.containsKey(labels.get(i))) {
                    offsets.add(labelToPC.get(labels.get(i)) - pc);
                } else {
                    offsets.add(operandCount);
                    allLabelsResolved = false;
                }
            }
        }
        return allLabelsResolved;
    }

    public int jumpToOffset() {
        return jumpToOffset;
    }

    public ArrayList<Integer> toBytes() {
        ArrayList<Integer> bytes = new ArrayList<Integer>();
        bytes.add(opcode);
        switch (opcode) {
            case RET:
                if (isWidened) {
                    bytes.add(byteAt(index, 2));
                    bytes.add(byteAt(index, 1));
                } else {
                    bytes.add(byteAt(index, 1));
                }
                break;
            case TABLESWITCH:
                for (int i = 0; i < pad; i++) {
                    bytes.add(0);
                }
                bytes.add(byteAt(defaultOffset, 4));
                bytes.add(byteAt(defaultOffset, 3));
                bytes.add(byteAt(defaultOffset, 2));
                bytes.add(byteAt(defaultOffset, 1));
                bytes.add(byteAt(low, 4));
                bytes.add(byteAt(low, 3));
                bytes.add(byteAt(low, 2));
                bytes.add(byteAt(low, 1));
                bytes.add(byteAt(high, 4));
                bytes.add(byteAt(high, 3));
                bytes.add(byteAt(high, 2));
                bytes.add(byteAt(high, 1));
                for (int i = 0; i < offsets.size(); i++) {
                    int jumpOffset = offsets.get(i);
                    bytes.add(byteAt(jumpOffset, 4));
                    bytes.add(byteAt(jumpOffset, 3));
                    bytes.add(byteAt(jumpOffset, 2));
                    bytes.add(byteAt(jumpOffset, 1));
                }
                break;
            case LOOKUPSWITCH:
                for (int i = 0; i < pad; i++) {
                    bytes.add(0);
                }
                bytes.add(byteAt(defaultOffset, 4));
                bytes.add(byteAt(defaultOffset, 3));
                bytes.add(byteAt(defaultOffset, 2));
                bytes.add(byteAt(defaultOffset, 1));
                bytes.add(byteAt(numPairs, 4));
                bytes.add(byteAt(numPairs, 3));
                bytes.add(byteAt(numPairs, 2));
                bytes.add(byteAt(numPairs, 1));
                Set<Entry<Integer, Integer>> matches = matchOffsetPairs.entrySet();
                Iterator<Entry<Integer, Integer>> iter = matches.iterator();
                while (iter.hasNext()) {
                    Entry<Integer, Integer> entry = iter.next();
                    int match = entry.getKey();
                    int offset = entry.getValue();
                    bytes.add(byteAt(match, 4));
                    bytes.add(byteAt(match, 3));
                    bytes.add(byteAt(match, 2));
                    bytes.add(byteAt(match, 1));
                    bytes.add(byteAt(offset, 4));
                    bytes.add(byteAt(offset, 3));
                    bytes.add(byteAt(offset, 2));
                    bytes.add(byteAt(offset, 1));
                }
                break;
            case GOTO_W:
            case JSR_W:
                bytes.add(byteAt(jumpToOffset, 4));
                bytes.add(byteAt(jumpToOffset, 3));
                bytes.add(byteAt(jumpToOffset, 2));
                bytes.add(byteAt(jumpToOffset, 1));
                break;
            default:
                bytes.add(byteAt(jumpToOffset, 2));
                bytes.add(byteAt(jumpToOffset, 1));
        }
        return bytes;
    }

}


class CLLoadStoreInstruction extends CLInstruction {

    private boolean isWidened;

    private int constVal;


    public CLLoadStoreInstruction(int opcode, int pc) {
        super.opcode = opcode;
        super.pc = pc;
        mnemonic = instructionInfo[opcode].mnemonic;
        operandCount = instructionInfo[opcode].operandCount;
        stackUnits = instructionInfo[opcode].stackUnits;
        localVariableIndex = instructionInfo[opcode].localVariableIndex;
    }

    public CLLoadStoreInstruction(int opcode, int pc, int localVariableIndex,
                                  boolean isWidened) {
        super.opcode = opcode;
        super.pc = pc;
        mnemonic = instructionInfo[opcode].mnemonic;
        operandCount = instructionInfo[opcode].operandCount;
        stackUnits = instructionInfo[opcode].stackUnits;
        super.localVariableIndex = localVariableIndex;
        this.isWidened = isWidened;
    }

    public CLLoadStoreInstruction(int opcode, int pc, int constVal) {
        super.opcode = opcode;
        super.pc = pc;
        mnemonic = instructionInfo[opcode].mnemonic;
        operandCount = instructionInfo[opcode].operandCount;
        stackUnits = instructionInfo[opcode].stackUnits;
        localVariableIndex = instructionInfo[opcode].localVariableIndex;
        this.constVal = constVal;
    }


    public ArrayList<Integer> toBytes() {
        ArrayList<Integer> bytes = new ArrayList<Integer>();
        bytes.add(opcode);
        if (instructionInfo[opcode].operandCount > 0) {
            if (localVariableIndex != IRRELEVANT) {
                if (isWidened) {
                    bytes.add(byteAt(localVariableIndex, 2));
                }
                bytes.add(byteAt(localVariableIndex, 1));
            } else {
                switch (opcode) {
                    case BIPUSH:
                    case LDC:
                        bytes.add(byteAt(constVal, 1));
                        break;
                    case SIPUSH:
                    case LDC_W:
                    case LDC2_W:
                        bytes.add(byteAt(constVal, 2));
                        bytes.add(byteAt(constVal, 1));
                }
            }
        }
        return bytes;
    }

}

class CLStackInstruction extends CLInstruction {

    public CLStackInstruction(int opcode, int pc) {
        super.opcode = opcode;
        super.pc = pc;
        mnemonic = instructionInfo[opcode].mnemonic;
        operandCount = instructionInfo[opcode].operandCount;
        stackUnits = instructionInfo[opcode].stackUnits;
        localVariableIndex = instructionInfo[opcode].localVariableIndex;
    }


    public ArrayList<Integer> toBytes() {
        ArrayList<Integer> bytes = new ArrayList<Integer>();
        bytes.add(opcode);
        return bytes;
    }

}

class CLMiscInstruction extends CLInstruction {

    public CLMiscInstruction(int opcode, int pc) {
        super.opcode = opcode;
        super.pc = pc;
        mnemonic = instructionInfo[opcode].mnemonic;
        operandCount = instructionInfo[opcode].operandCount;
        stackUnits = instructionInfo[opcode].stackUnits;
        localVariableIndex = instructionInfo[opcode].localVariableIndex;
    }

    public ArrayList<Integer> toBytes() {
        ArrayList<Integer> bytes = new ArrayList<Integer>();
        bytes.add(opcode);
        return bytes;
    }

}


class CLInsInfo {

    public int opcode;

    public String mnemonic;

    public int operandCount;

    public int stackUnits;

    public int localVariableIndex;

    public Category category;


    public CLInsInfo(int opcode, String mnemonic, int operandCount,
                     int localVariableIndex, int stackUnits, Category category) {
        this.opcode = opcode;
        this.mnemonic = mnemonic;
        this.operandCount = operandCount;
        this.localVariableIndex = localVariableIndex;
        this.stackUnits = stackUnits;
        this.category = category;
    }

}
