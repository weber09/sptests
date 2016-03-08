/**
 * Created by Gabriel on 06/03/2016.
 */
package algol;

import java.io.IOException;
import java.util.ArrayList;

public class CLConstantPool {

    private int cpIndex;

    private ArrayList<CLCPInfo> cpItems;

    private int findOrAdd(CLCPInfo cpInfo) {
        int index = find(cpInfo);
        if (index == -1) {
            index = addCPItem(cpInfo);
        }
        return index;
    }

    public CLConstantPool() {
        cpIndex = 1;
        cpItems = new ArrayList<CLCPInfo>();
    }

    public int size() {
        return cpItems.size();
    }

    public int find(CLCPInfo cpInfo) {
        int index = cpItems.indexOf(cpInfo);
        if (index != -1) {
            CLCPInfo c = cpItems.get(index);
            index = c.cpIndex;
        }
        return index;
    }

    public CLCPInfo cpItem(int i) {
        if (((i - 1) < 0) || ((i - 1) >= cpItems.size())) {
            return null;
        }
        return cpItems.get(i - 1);
    }

    public int addCPItem(CLCPInfo cpInfo) {
        int i = cpIndex++;
        cpInfo.cpIndex = i;
        cpItems.add(cpInfo);

        if ((cpInfo instanceof CLConstantLongInfo)
                || (cpInfo instanceof CLConstantDoubleInfo)) {
            cpIndex++;
            cpItems.add(null);
        }
        return i;
    }

    public void write(CLOutputStream out) throws IOException {
        for (int i = 0; i < cpItems.size(); i++) {
            CLCPInfo cpInfo = cpItems.get(i);
            if (cpInfo != null) {
                cpInfo.write(out);
            }
        }
    }

    public int constantClassInfo(String s) {
        CLCPInfo c = new CLConstantClassInfo(constantUtf8Info(s));
        return findOrAdd(c);
    }

    public int constantFieldRefInfo(String className, String name, String type) {
        CLCPInfo c = new CLConstantFieldRefInfo(constantClassInfo(className),
                constantNameAndTypeInfo(name, type));
        return findOrAdd(c);
    }

    public int constantMethodRefInfo(String className, String name, String type) {
        CLCPInfo c = new CLConstantMethodRefInfo(constantClassInfo(className),
                constantNameAndTypeInfo(name, type));
        return findOrAdd(c);
    }

    public int constantInterfaceMethodRefInfo(String className, String name,
                                              String type) {
        CLCPInfo c = new CLConstantInterfaceMethodRefInfo(
                constantClassInfo(className), constantNameAndTypeInfo(name,
                type));
        return findOrAdd(c);
    }

    public int constantStringInfo(String s) {
        CLCPInfo c = new CLConstantStringInfo(constantUtf8Info(s));
        return findOrAdd(c);
    }

    public int constantIntegerInfo(int i) {
        CLCPInfo c = new CLConstantIntegerInfo(i);
        return findOrAdd(c);
    }

    public int constantFloatInfo(float f) {
        CLCPInfo c = new CLConstantFloatInfo(f);
        return findOrAdd(c);
    }

    public int constantLongInfo(long l) {
        CLCPInfo c = new CLConstantLongInfo(l);
        return findOrAdd(c);
    }

    public int constantDoubleInfo(double d) {
        CLCPInfo c = new CLConstantDoubleInfo(d);
        return findOrAdd(c);
    }

    public int constantNameAndTypeInfo(String name, String type) {
        CLCPInfo c = new CLConstantNameAndTypeInfo(constantUtf8Info(name),
                constantUtf8Info(type));
        return findOrAdd(c);
    }

    public int constantUtf8Info(String s) {
        CLCPInfo c = new CLConstantUtf8Info(s.getBytes());
        return findOrAdd(c);
    }

}
