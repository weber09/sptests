package algol;

/**
 * Created by Gabriel on 08/03/2016.
 */

import static algol.CLConstants.*;

abstract class SPLiteral extends SPExpression {

    String text;

    SPLiteral(int line, String text)
    {
        super(line);
        this.text = text;
    }
}

class SPLiteralInt extends SPLiteral {

    public SPLiteralInt(int line, String text) {
        super(line, text);
    }

    public SPExpression analyze(Context context) {
        type = Type.INT;
        return this;
    }

    public void codegen(CLEmitter output) {
        int i = Integer.parseInt(text);
        switch (i) {
            case 0:
                output.addNoArgInstruction(ICONST_0);
                break;
            case 1:
                output.addNoArgInstruction(ICONST_1);
                break;
            case 2:
                output.addNoArgInstruction(ICONST_2);
                break;
            case 3:
                output.addNoArgInstruction(ICONST_3);
                break;
            case 4:
                output.addNoArgInstruction(ICONST_4);
                break;
            case 5:
                output.addNoArgInstruction(ICONST_5);
                break;
            default:
                if (i >= 6 && i <= 127) {
                    output.addOneArgInstruction(BIPUSH, i);
                } else if (i >= 128 && i <= 32767) {
                    output.addOneArgInstruction(SIPUSH, i);
                } else {
                    output.addLDCInstruction(i);
                }
        }
    }
}

class SPLiteralDecimal extends SPLiteral{
    public SPLiteralDecimal(int line, String text) {
        super(line, text);
    }

    public SPExpression analyze(Context context) {
        type = Type.DECIMAL;
        return this;
    }

    public void codegen(CLEmitter output) {
        output.addLDCInstruction(Double.parseDouble(text));
    }

}

class SPLiteralTrue extends SPLiteral {

    public SPLiteralTrue(int line) {
        super(line, "");
    }

    public SPExpression analyze(Context context) {
        type = Type.BOOLEAN;
        return this;
    }

    public void codegen(CLEmitter output) {
        output.addNoArgInstruction(ICONST_1);
    }


    public void codegen(CLEmitter output, String targetLabel, boolean onTrue) {
        if (onTrue) {
            output.addBranchInstruction(GOTO, targetLabel);
        }
    }

}

class SPLiteralFalse extends SPLiteral {

    public SPLiteralFalse(int line) {
        super(line, "");
    }

    public SPExpression analyze(Context context) {
        type = Type.BOOLEAN;
        return this;
    }

    public void codegen(CLEmitter output) {
        output.addNoArgInstruction(ICONST_0);
    }


    public void codegen(CLEmitter output, String targetLabel, boolean onTrue) {
        if (onTrue) {
            output.addBranchInstruction(GOTO, targetLabel);
        }
    }

}

class SPLiteralString extends SPLiteral {

    public SPLiteralString(int line, String text) {
        super(line, text);
        this.text = text;
    }

    public SPExpression analyze(Context context) {
        type = Type.STRING;
        return this;
    }

    public void codegen(CLEmitter output) {
        String s = Util.unescape(text);

        String literal = s.substring(1, s.length() - 1);
        output.addLDCInstruction(literal);
    }
}


class Util {

    public static String escapeSpecialXMLChars(String s) {
        String escapedString = s.replaceAll("&", "&amp;");
        escapedString = escapedString.replaceAll("<", "&lt;");
        escapedString = escapedString.replaceAll(">", "&gt;");
        escapedString = escapedString.replaceAll("\"", "&quot;");
        escapedString = escapedString.replaceAll("'", "&#39;");
        return escapedString;
    }

    public static String unescape(String s) {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\') {
                i++;
                if (i >= s.length()) {
                    break;
                }
                c = s.charAt(i);
                switch (c) {
                    case 'b':
                        b.append('\b');
                        break;
                    case 't':
                        b.append('\t');
                        break;
                    case 'n':
                        b.append('\n');
                        break;
                    case 'f':
                        b.append('\f');
                        break;
                    case 'r':
                        b.append('\r');
                        break;
                    case '"':
                        b.append('"');
                        break;
                    case '\'':
                        b.append('\'');
                        break;
                    case '\\':
                        b.append('\\');
                        break;
                }
            } else {
                b.append(c);
            }
        }
        return b.toString();
    }

}

