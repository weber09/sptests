package algol;

/**
 * Created by Gabriel on 06/03/2016.
 */
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Hashtable;


class Type {

    private Class<?> classRep;

    private static Hashtable<String, Type> types = new Hashtable<String, Type>();

    public final static Type INT = typeFor(int.class);

    public final static Type LONG = typeFor(long.class);

    public final static Type CHAR = typeFor(char.class);

    public final static Type BOOLEAN = typeFor(boolean.class);

    public final static Type DECIMAL = typeFor(double.class);

    public final static Type BOXED_INT = typeFor(java.lang.Integer.class);

    public final static Type BOXED_CHAR = typeFor(java.lang.Character.class);

    public final static Type BOXED_BOOLEAN = typeFor(java.lang.Boolean.class);

    public static Type STRING = typeFor(java.lang.String.class);

    public static Type OBJECT = typeFor(java.lang.Object.class);

    public final static Type VOID = typeFor(void.class);

    public final static Type NULLTYPE = new Type(java.lang.Object.class);

    public final static Type CONSTRUCTOR = new Type(null);

    public final static Type ANY = new Type(null);

    private Type(Class<?> classRep) {
        this.classRep = classRep;
    }

    protected Type() {
        super();
    }

    public static Type typeFor(Class<?> classRep) {
        if (types.get(descriptorFor(classRep)) == null) {
            types.put(descriptorFor(classRep), new Type(classRep));
        }
        return types.get(descriptorFor(classRep));
    }

    public Class<?> classRep() {
        return classRep;
    }

    public void setClassRep(Class<?> classRep) {
        this.classRep = classRep;
    }

    public boolean equals(Type that) {
        return this.toDescriptor().equals(that.toDescriptor());
    }

    public boolean isArray() {
        return classRep.isArray();
    }

    public Type componentType() {
        return typeFor(classRep.getComponentType());
    }

    public Type superClass() {
        return classRep == null || classRep.getSuperclass() == null ? null
                : typeFor(classRep.getSuperclass());
    }

    public boolean isPrimitive() {
        return classRep.isPrimitive();
    }

    public boolean isInterface() {
        return classRep.isInterface();
    }

    public boolean isReference() {
        return !isPrimitive();
    }

    public boolean isFinal() {
        return Modifier.isFinal(classRep.getModifiers());
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(classRep.getModifiers());
    }

    public boolean isJavaAssignableFrom(Type that) {
        return this.classRep.isAssignableFrom(that.classRep);
    }

    public ArrayList<Method> abstractMethods() {
        ArrayList<Method> inheritedAbstractMethods = superClass() == null ? new ArrayList<Method>()
                : superClass().abstractMethods();
        ArrayList<Method> abstractMethods = new ArrayList<Method>();
        ArrayList<Method> declaredConcreteMethods = declaredConcreteMethods();
        ArrayList<Method> declaredAbstractMethods = declaredAbstractMethods();
        abstractMethods.addAll(declaredAbstractMethods);
        for (Method method : inheritedAbstractMethods) {
            if (!declaredConcreteMethods.contains(method)
                    && !declaredAbstractMethods.contains(method)) {
                abstractMethods.add(method);
            }
        }
        return abstractMethods;
    }

    private ArrayList<Method> declaredAbstractMethods() {
        ArrayList<Method> declaredAbstractMethods = new ArrayList<Method>();
        for (java.lang.reflect.Method method : classRep.getDeclaredMethods()) {
            if (Modifier.isAbstract(method.getModifiers())) {
                declaredAbstractMethods.add(new Method(method));
            }
        }
        return declaredAbstractMethods;
    }

    private ArrayList<Method> declaredConcreteMethods() {
        ArrayList<Method> declaredConcreteMethods = new ArrayList<Method>();
        for (java.lang.reflect.Method method : classRep.getDeclaredMethods()) {
            if (!Modifier.isAbstract(method.getModifiers())) {
                declaredConcreteMethods.add(new Method(method));
            }
        }
        return declaredConcreteMethods;
    }

    public void mustMatchOneOf(int line, Type... expectedTypes) {
        if (this == Type.ANY)
            return;
        for (int i = 0; i < expectedTypes.length; i++) {
            if (matchesExpected(expectedTypes[i])) {
                return;
            }
        }
        SPAST.compilationUnit.reportSemanticError(line,
                "Type %s doesn't match any of the expected types %s", this,
                Arrays.toString(expectedTypes));
    }

    public void mustMatchExpected(int line, Type expectedType) {
        if (!matchesExpected(expectedType)) {
            SPAST.compilationUnit.reportSemanticError(line,
                    "Type %s doesn't match type %s", this, expectedType);
        }
    }

    public boolean matchesExpected(Type expected) {
        return this == Type.ANY || expected == Type.ANY
                || (this == Type.NULLTYPE && expected.isReference())
                || this.equals(expected);
    }

    public static boolean argTypesMatch(Class<?>[] argTypes1,
                                        Class<?>[] argTypes2) {
        if (argTypes1.length != argTypes2.length) {
            return false;
        }
        for (int i = 0; i < argTypes1.length; i++) {
            if (!Type.descriptorFor(argTypes1[i]).equals(
                    Type.descriptorFor(argTypes2[i]))) {
                return false;
            }
        }
        return true;
    }

    public String simpleName() {
        return classRep.getSimpleName();
    }

    public String toString() {
        return toJava(this.classRep);
    }

    public String toDescriptor() {
        return descriptorFor(classRep);
    }

    private static String descriptorFor(Class<?> cls) {
        return cls == null ? "V" : cls == void.class ? "V"
                : cls.isArray() ? "[" + descriptorFor(cls.getComponentType())
                : cls.isPrimitive() ? (cls == int.class ? "I"
                : cls == char.class ? "C"
                : cls == boolean.class ? "Z"
                : cls == long.class ? "L" : "?")
                : "L" + cls.getName().replace('.', '/') + ";";
    }

    public String jvmName() {
        return this.isArray() || this.isPrimitive() ? this.toDescriptor()
                : classRep.getName().replace('.', '/');
    }

    private static String toJava(Class classRep) {
        return classRep.isArray() ? toJava(classRep.getComponentType()) + "[]"
                : classRep.getName();
    }

    public String packageName() {
        String name = toString();
        return name.lastIndexOf('.') == -1 ? "" : name.substring(0, name
                .lastIndexOf('.') - 1);
    }

    public String argumentTypeForAppend() {
        return this == Type.STRING || this.isPrimitive() ? toDescriptor()
                : "Ljava/lang/Object;";
    }

    public Method methodFor(String name, Type[] argTypes) {
        Class[] classes = new Class[argTypes.length];
        for (int i = 0; i < argTypes.length; i++) {
            classes[i] = argTypes[i].classRep;
        }
        Class cls = classRep;

        // Search this class and all superclasses
        while (cls != null) {
            java.lang.reflect.Method[] methods = cls.getDeclaredMethods();
            for (java.lang.reflect.Method method : methods) {
                if (method.getName().equals(name)
                        && Type.argTypesMatch(classes, method
                        .getParameterTypes())) {
                    return new Method(method);
                }
            }
            cls = cls.getSuperclass();
        }
        return null;
    }

    public Constructor constructorFor(Type[] argTypes) {
        Class[] classes = new Class[argTypes.length];
        for (int i = 0; i < argTypes.length; i++) {
            classes[i] = argTypes[i].classRep;
        }

        // Search only this class (we don't inherit constructors)
        java.lang.reflect.Constructor[] constructors = classRep
                .getDeclaredConstructors();
        for (java.lang.reflect.Constructor constructor : constructors) {
            if (argTypesMatch(classes, constructor.getParameterTypes())) {
                return new Constructor(constructor);
            }
        }
        return null;
    }

    public Field fieldFor(String name) {
        Class<?> cls = classRep;
        while (cls != null) {
            java.lang.reflect.Field[] fields = cls.getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                if (field.getName().equals(name)) {
                    return new Field(field);
                }
            }
            cls = cls.getSuperclass();
        }
        return null;
    }

    public static String argTypesAsString(Type[] argTypes) {
        if (argTypes.length == 0) {
            return "()";
        } else {
            String str = "(" + argTypes[0].toString();
            for (int i = 1; i < argTypes.length; i++) {
                str += "," + argTypes[i];
            }
            str += ")";
            return str;
        }
    }

    public boolean checkAccess(int line, Member member) {
        if (!checkAccess(line, classRep, member.declaringType().classRep)) {
            return false;
        }

        // Secondly, the member must be either public,
        if (member.isPublic()) {
            return true;
        }
        java.lang.Package p1 = classRep.getPackage();
        java.lang.Package p2 = member.declaringType().classRep.getPackage();
        if ((p1 == null ? "" : p1.getName()).equals((p2 == null ? "" : p2
                .getName()))) {
            return true;
        }
        if (member.isProtected()) {
            if (classRep.getPackage().getName().equals(
                    member.declaringType().classRep.getPackage().getName())
                    || typeFor(member.getClass().getDeclaringClass())
                    .isJavaAssignableFrom(this)) {
                return true;
            } else {
                SPAST.compilationUnit.reportSemanticError(line,
                        "The protected member, " + member.name()
                                + ", is not accessible.");
                return false;
            }
        }
        if (member.isPrivate()) {
            if (descriptorFor(classRep).equals(
                    descriptorFor(member.member().getDeclaringClass()))) {
                return true;
            } else {
                SPAST.compilationUnit.reportSemanticError(line,
                        "The private member, " + member.name()
                                + ", is not accessible.");
                return false;
            }
        }

        // Otherwise, the member has default access
        if (packageName().equals(member.declaringType().packageName())) {
            return true;
        } else {
            SPAST.compilationUnit.reportSemanticError(line, "The member, "
                    + member.name()
                    + ", is not accessible because it's in a different "
                    + "package.");
            return false;
        }
    }

    public boolean checkAccess(int line, Type targetType) {
        if (targetType.isPrimitive()) {
            return true;
        }
        if (targetType.isArray()) {
            return this.checkAccess(line, targetType.componentType());
        }
        return checkAccess(line, classRep, targetType.classRep);
    }

    public static boolean checkAccess(int line, Class referencingType,
                                      Class type) {
        java.lang.Package p1 = referencingType.getPackage();
        java.lang.Package p2 = type.getPackage();
        if (Modifier.isPublic(type.getModifiers())
                || (p1 == null ? "" : p1.getName()).equals((p2 == null ? ""
                : p2.getName()))) {
            return true;
        } else {
            SPAST.compilationUnit.reportSemanticError(line, "The type, "
                    + type.getCanonicalName() + ", is not accessible from "
                    + referencingType.getCanonicalName());
            return false;
        }
    }

    public Type resolve(Context context) {
        return this;
    }

    public static String signatureFor(String name, Type[] argTypes) {
        String signature = name + "(";
        if (argTypes.length > 0) {
            signature += argTypes[0].toString();
            for (int i = 1; i < argTypes.length; i++) {
                signature += "," + argTypes[i].toString();
            }
        }
        signature += ")";
        return signature;
    }

}

class TypeName extends Type {

    private int line;

    private String name;

    public TypeName(int line, String name) {
        this.line = line;
        this.name = name;
    }

    public int line() {
        return line;
    }

    public String jvmName() {
        return name.replace('.', '/');
    }

    public String toDescriptor() {
        return "L" + jvmName() + ";";
    }

    public String toString() {
        return name;
    }

    public String simpleName() {
        return name.substring(name.lastIndexOf('.') + 1);
    }

    public Type resolve(Context context) {
        Type resolvedType = context.lookupType(name);
        if (resolvedType == null) {
            // Try loading a type with the give fullname
            try {
                resolvedType = typeFor(Class.forName(name));
                context.addType(line, resolvedType);
                // context.compilationUnitContext().addEntry(line,
                // resolvedType.toString(),
                // new TypeNameDefn(resolvedType));
            } catch (Exception e) {
                SPAST.compilationUnit.reportSemanticError(line,
                        "Unable to locate a type named %s", name);
                resolvedType = Type.ANY;
            }
        }
        if (resolvedType != Type.ANY) {
            Type referencingType = ((SPTypeDecl) (context.classContext
                    .definition())).thisType();
            Type.checkAccess(line, referencingType.classRep(), resolvedType
                    .classRep());
        }
        return resolvedType;
    }
}

class ArrayTypeName extends Type {

    private Type componentType;

    public ArrayTypeName(Type componentType) {
        this.componentType = componentType;
    }

    public Type componentType() {
        return componentType;
    }

    public String toDescriptor() {
        return "[" + componentType.toDescriptor();
    }

    public String toString() {
        return componentType.toString() + "[]";
    }

    public Type resolve(Context context) {
        componentType = componentType.resolve(context);

        Class classRep = Array.newInstance(componentType().classRep(), 0)
                .getClass();
        return Type.typeFor(classRep);
    }

}

