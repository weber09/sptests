package algol;

/**
 * Created by Gabriel on 08/03/2016.
 */
class SPFormalParameter extends SPAST {

        private String name;

        private Type type;

        public SPFormalParameter(int line, String name, Type type) {
            super(line);
            this.name = name;
            this.type = type;
        }

        public String name() {
            return name;
        }

        public Type type() {
            return type;
        }

        public Type setType(Type newType) {
            return type = newType;
        }

        public SPAST analyze(Context context) {
            return this;
        }

        public void codegen(CLEmitter output) {
        }

    }
