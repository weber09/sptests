/**
 * Created by Gabriel on 06/03/2016.
 */

package algol;

import java.io.IOException;
import java.util.ArrayList;

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

}
