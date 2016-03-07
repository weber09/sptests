/**
 * Created by Gabriel on 06/03/2016.
 */

package algol;

import java.io.DataOutputStream;
import java.io.IOException;
import static algol.CLConstants.*;

abstract class CLCPInfo {

    public int cpIndex;
    public short tag;

    public void write(CLOutputStream out) throws IOException {
        out.writeByte(tag);
    }
}

class CLConstantClassInfo extends CLCPInfo {

    public int nameIndex;

    public CLConstantClassInfo(int nameIndex) {
        super.tag = CONSTANT_Class;
        this.nameIndex = nameIndex;
    }

    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        out.writeShort(nameIndex);
    }

    public boolean equals(Object obj) {
        if (obj instanceof CLConstantClassInfo) {
            CLConstantClassInfo c = (CLConstantClassInfo) obj;
            if (c.nameIndex == nameIndex) {
                return true;
            }
        }
        return false;
    }
}

abstract class CLConstantMemberRefInfo extends CLCPInfo {

    public int classIndex;

    public int nameAndTypeIndex;

    protected CLConstantMemberRefInfo(int classIndex, int nameAndTypeIndex,
                                      short tag) {
        super.tag = tag;
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        out.writeShort(classIndex);
        out.writeShort(nameAndTypeIndex);
    }
    public boolean equals(Object obj) {
        if (obj instanceof CLConstantMemberRefInfo) {
            CLConstantMemberRefInfo c = (CLConstantMemberRefInfo) obj;
            if ((c.tag == tag) && (c.classIndex == classIndex)
                    && (c.nameAndTypeIndex == nameAndTypeIndex)) {
                return true;
            }
        }
        return false;
    }

}

class CLConstantFieldRefInfo extends CLConstantMemberRefInfo {

    public CLConstantFieldRefInfo(int classIndex, int nameAndTypeIndex) {
        super(classIndex, nameAndTypeIndex, CONSTANT_Fieldref);
    }
}

class CLConstantMethodRefInfo extends CLConstantMemberRefInfo {

    public CLConstantMethodRefInfo(int classIndex, int nameAndTypeIndex) {
        super(classIndex, nameAndTypeIndex, CONSTANT_Methodref);
    }
}

class CLConstantInterfaceMethodRefInfo extends CLConstantMemberRefInfo {

    public CLConstantInterfaceMethodRefInfo(int classIndex, int nameAndTypeIndex) {
        super(classIndex, nameAndTypeIndex, CONSTANT_InterfaceMethodref);
    }
}

class CLConstantStringInfo extends CLCPInfo {

    public int stringIndex;

    public CLConstantStringInfo(int stringIndex) {
        super.tag = CONSTANT_String;
        this.stringIndex = stringIndex;
    }

    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        out.writeShort(stringIndex);
    }

    public boolean equals(Object obj) {
        if (obj instanceof CLConstantStringInfo) {
            CLConstantStringInfo c = (CLConstantStringInfo) obj;
            if (c.stringIndex == stringIndex) {
                return true;
            }
        }
        return false;
    }
}

class CLConstantIntegerInfo extends CLCPInfo {

    public int i;

    public CLConstantIntegerInfo(int i) {
        super.tag = CONSTANT_Integer;
        this.i = i;
    }

    public short[] bytes() {
        short[] s = new short[4];
        short mask = 0xFF;
        int k = i;
        for (int j = 0; j < 4; j++) {
            s[3 - j] = (short) (k & mask);
            k >>>= 8;
        }
        return s;
    }

    public void write(CLOutputStream out) throws IOException {
        super.write(out);

        // out is cast to DataOutputStream to resolve the
        // writeInt()
        // ambiguity
        ((DataOutputStream) out).writeInt(i);
    }

    public boolean equals(Object obj) {
        if (obj instanceof CLConstantIntegerInfo) {
            CLConstantIntegerInfo c = (CLConstantIntegerInfo) obj;
            if (c.i == i) {
                return true;
            }
        }
        return false;
    }
}

class CLConstantFloatInfo extends CLCPInfo {

    public float f;

    public CLConstantFloatInfo(float f) {
        super.tag = CONSTANT_Float;
        this.f = f;
    }

    public short[] bytes() {
        short[] s = new short[4];
        short mask = 0xFF;
        int i = Float.floatToIntBits(f);
        for (int j = 0; j < 4; j++) {
            s[3 - j] = (short) (i & mask);
            i >>>= 8;
        }
        return s;
    }

    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        out.writeFloat(f);
    }

    public boolean equals(Object obj) {
        if (obj instanceof CLConstantFloatInfo) {
            CLConstantFloatInfo c = (CLConstantFloatInfo) obj;
            if (c.f == f) {
                return true;
            }
        }
        return false;
    }
}


class CLConstantLongInfo extends CLCPInfo {

    public long l;

    private short[] bytes() {
        short[] s = new short[8];
        short mask = 0xFF;
        long k = l;
        for (int j = 0; j < 8; j++) {
            s[7 - j] = (short) (k & mask);
            k >>>= 8;
        }
        return s;
    }

    public CLConstantLongInfo(long l) {
        super.tag = CONSTANT_Long;
        this.l = l;
    }

    public short[] lowBytes() {
        short[] s = bytes();
        short[] l = new short[4];
        l[0] = s[4];
        l[1] = s[5];
        l[2] = s[6];
        l[3] = s[7];
        return l;
    }

    public short[] highBytes() {
        short[] s = bytes();
        short[] h = new short[4];
        h[0] = s[0];
        h[1] = s[1];
        h[2] = s[2];
        h[3] = s[3];
        return h;
    }

    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        out.writeLong(l);
    }

    public boolean equals(Object obj) {
        if (obj instanceof CLConstantLongInfo) {
            CLConstantLongInfo c = (CLConstantLongInfo) obj;
            if (c.l == l) {
                return true;
            }
        }
        return false;
    }
}

class CLConstantDoubleInfo extends CLCPInfo {

    public double d;

    private short[] bytes() {
        short[] s = new short[8];
        short mask = 0xFF;
        long l = Double.doubleToLongBits(d);
        for (int j = 0; j < 8; j++) {
            s[7 - j] = (short) (l & mask);
            l >>>= 8;
        }
        return s;
    }

    public CLConstantDoubleInfo(double d) {
        super.tag = CONSTANT_Double;
        this.d = d;
    }

    public short[] lowBytes() {
        short[] s = bytes();
        short[] l = new short[4];
        l[0] = s[4];
        l[1] = s[5];
        l[2] = s[6];
        l[3] = s[7];
        return l;
    }

    public short[] highBytes() {
        short[] s = bytes();
        short[] h = new short[4];
        h[0] = s[0];
        h[1] = s[1];
        h[2] = s[2];
        h[3] = s[3];
        return h;
    }


    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        out.writeDouble(d);
    }


    public boolean equals(Object obj) {
        if (obj instanceof CLConstantDoubleInfo) {
            CLConstantDoubleInfo c = (CLConstantDoubleInfo) obj;
            if (c.d == d) {
                return true;
            }
        }
        return false;
    }
}

class CLConstantNameAndTypeInfo extends CLCPInfo {

    public int nameIndex;

    public int descriptorIndex;

    public CLConstantNameAndTypeInfo(int nameIndex, int descriptorIndex) {
        super.tag = CONSTANT_NameAndType;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
    }

    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        out.writeShort(nameIndex);
        out.writeShort(descriptorIndex);
    }

    public boolean equals(Object obj) {
        if (obj instanceof CLConstantNameAndTypeInfo) {
            CLConstantNameAndTypeInfo c = (CLConstantNameAndTypeInfo) obj;
            if ((c.nameIndex == nameIndex)
                    && (c.descriptorIndex == descriptorIndex)) {
                return true;
            }
        }
        return false;
    }
}

class CLConstantUtf8Info extends CLCPInfo {

    public byte[] b;

    public CLConstantUtf8Info(byte[] b) {
        super.tag = CONSTANT_Utf8;
        this.b = b;
    }

    public int length() {
        return b.length;
    }


    public void write(CLOutputStream out) throws IOException {
        super.write(out);
        out.writeUTF(new String(b));
    }
    public boolean equals(Object obj) {
        if (obj instanceof CLConstantUtf8Info) {
            CLConstantUtf8Info c = (CLConstantUtf8Info) obj;
            if ((new String(b)).equals(new String(c.b))) {
                return true;
            }
        }
        return false;
    }
}
