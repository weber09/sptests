package algol;

/**
 * Created by Gabriel on 07/03/2016.
 */

abstract class Member {

    public String name() {
        return member().getName();
    }

    public Type declaringType() {
        return Type.typeFor(member().getDeclaringClass());
    }

    public boolean isStatic() {
        return java.lang.reflect.Modifier.isStatic(member().getModifiers());
    }

    public boolean isPublic() {
        return java.lang.reflect.Modifier.isPublic(member().getModifiers());
    }

    public boolean isProtected() {
        return java.lang.reflect.Modifier.isProtected(member().getModifiers());
    }

    public boolean isPrivate() {
        return java.lang.reflect.Modifier.isPrivate(member().getModifiers());
    }

    public boolean isAbstract() {
        return java.lang.reflect.Modifier.isAbstract(member().getModifiers());
    }

    public boolean isFinal() {
        return java.lang.reflect.Modifier.isFinal(member().getModifiers());
    }

    protected abstract java.lang.reflect.Member member();

}

class Method extends Member {

    private java.lang.reflect.Method method;


    public Method(java.lang.reflect.Method method) {
        this.method = method;
    }

    public String toDescriptor() {
        String descriptor = "(";
        for (Class paramType : method.getParameterTypes()) {
            descriptor += Type.typeFor(paramType).toDescriptor();
        }
        descriptor += ")" + Type.typeFor(method.getReturnType()).toDescriptor();
        return descriptor;
    }

    public String toString() {
        String str = name() + "(";
        for (Class paramType : method.getParameterTypes()) {
            str += Type.typeFor(paramType).toString();
        }
        str += ")";
        return str;
    }

    public Type returnType() {
        return Type.typeFor(method.getReturnType());
    }

    public boolean equals(Method that) {
        return Type.argTypesMatch(this.method.getParameterTypes(), that.method
                .getParameterTypes());
    }

    protected java.lang.reflect.Member member() {
        return method;
    }

}

class Field extends Member {

    private java.lang.reflect.Field field;

    public Field(java.lang.reflect.Field field) {
        this.field = field;
    }

    public Type type() {
        return Type.typeFor(field.getType());
    }

    protected java.lang.reflect.Member member() {
        return field;
    }

}

class Constructor extends Member {

    java.lang.reflect.Constructor constructor;

    public Constructor(java.lang.reflect.Constructor constructor) {
        this.constructor = constructor;
    }

    public String toDescriptor() {
        String descriptor = "(";
        for (Class paramType : constructor.getParameterTypes()) {
            descriptor += Type.typeFor(paramType).toDescriptor();
        }
        descriptor += ")V";
        return descriptor;
    }
    public Type declaringType() {
        return Type.typeFor(constructor.getDeclaringClass());
    }

    protected java.lang.reflect.Member member() {
        return constructor;
    }

}
