package algol;

/**
 * Created by Gabriel on 06/03/2016.
 */
import java.io.IOException;
import java.util.ArrayList;

abstract class CLMemberInfo {

    public int accessFlags;

    public int nameIndex;

    public int descriptorIndex;

    public int attributesCount;

    public ArrayList<CLAttributeInfo> attributes;

    protected CLMemberInfo(int accessFlags, int nameIndex, int descriptorIndex,
                           int attributesCount, ArrayList<CLAttributeInfo> attributes) {
        this.accessFlags = accessFlags;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributesCount = attributesCount;
        this.attributes = attributes;
    }

    public void write(CLOutputStream out) throws IOException {
        out.writeShort(accessFlags);
        out.writeShort(nameIndex);
        out.writeShort(descriptorIndex);
        out.writeShort(attributesCount);
        for (int i = 0; i < attributes.size(); i++) {
            CLAttributeInfo attributeInfo = attributes.get(i);
            attributeInfo.write(out);
        }
    }
}

class CLFieldInfo extends CLMemberInfo {

    public CLFieldInfo(int accessFlags, int nameIndex, int descriptorIndex,
                       int attributesCount, ArrayList<CLAttributeInfo> attributes) {
        super(accessFlags, nameIndex, descriptorIndex, attributesCount,
                attributes);
    }
}

class CLMethodInfo extends CLMemberInfo {


    public CLMethodInfo(int accessFlags, int nameIndex, int descriptorIndex,
                        int attributesCount, ArrayList<CLAttributeInfo> attributes) {
        super(accessFlags, nameIndex, descriptorIndex, attributesCount,
                attributes);
    }

}
