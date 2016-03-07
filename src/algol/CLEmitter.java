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

public class CLEmitter {

    private String name;

    private CLFile clFile;

    private CLConstantPool constantPool;

    private ArrayList<CLFieldInfo> fields;

    private ArrayList<CLAttributeInfo> fAttributes;

    private ArrayList<CLMethodInfo> methods;

    private ArrayList<CLAttributeInfo> mAttributes;

    private ArrayList<CLAttributeInfo> attributes;

    private ArrayList<CLInstruction> mCode;

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
