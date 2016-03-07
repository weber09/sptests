/**
 * Created by Gabriel on 06/03/2016.
 */
package algol;

import java.io.IOException;
import java.util.ArrayList;

public class CLConstantPool {

    private int cpIndex;

    private ArrayList<CLCPInfo> cpItems;

    public CLConstantPool() {
        cpIndex = 1;
        cpItems = new ArrayList<CLCPInfo>();
    }

    private int findOrAdd(CLCPInfo cpInfo) {
        int index = find(cpInfo);
        if (index == -1) {
            index = addCPItem(cpInfo);
        }
        return index;
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
}
