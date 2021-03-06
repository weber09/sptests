package algol;
/* Generated By:JavaCC: Do not edit this line. StructuredPortugueseParserConstants.java */

/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface AlgolParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int SINGLE_LINE_COMMENT = 9;
  /** RegularExpression Id. */
  int FORMAL_COMMENT = 10;
  /** RegularExpression Id. */
  int MULTI_LINE_COMMENT = 11;
  /** RegularExpression Id. */
  int ALGORITMO = 13;
  /** RegularExpression Id. */
  int VAR = 14;
  /** RegularExpression Id. */
  int INICIO = 15;
  /** RegularExpression Id. */
  int FIMALGORITMO = 16;
  /** RegularExpression Id. */
  int INTEIRO = 17;
  /** RegularExpression Id. */
  int LOGICO = 18;
  /** RegularExpression Id. */
  int LITERAL = 19;
  /** RegularExpression Id. */
  int VETOR = 20;
  /** RegularExpression Id. */
  int DE = 21;
  /** RegularExpression Id. */
  int VERDADEIRO = 22;
  /** RegularExpression Id. */
  int FALSO = 23;
  /** RegularExpression Id. */
  int ESCREVA = 24;
  /** RegularExpression Id. */
  int ESCREVAL = 25;
  /** RegularExpression Id. */
  int LEIA = 26;
  /** RegularExpression Id. */
  int LEIAL = 27;
  /** RegularExpression Id. */
  int SE = 28;
  /** RegularExpression Id. */
  int ENTAO = 29;
  /** RegularExpression Id. */
  int FIMSE = 30;
  /** RegularExpression Id. */
  int SENAO = 31;
  /** RegularExpression Id. */
  int REPITA = 32;
  /** RegularExpression Id. */
  int ATE = 33;
  /** RegularExpression Id. */
  int ENQUANTO = 34;
  /** RegularExpression Id. */
  int FACA = 35;
  /** RegularExpression Id. */
  int FIMENQUANTO = 36;
  /** RegularExpression Id. */
  int PARA = 37;
  /** RegularExpression Id. */
  int FIMPARA = 38;
  /** RegularExpression Id. */
  int NULL = 39;
  /** RegularExpression Id. */
  int VOID = 40;
  /** RegularExpression Id. */
  int CASO = 41;
  /** RegularExpression Id. */
  int PARAR = 42;
  /** RegularExpression Id. */
  int PADRAO = 43;
  /** RegularExpression Id. */
  int ESCOLHA = 44;
  /** RegularExpression Id. */
  int RETORNA = 45;
  /** RegularExpression Id. */
  int DECIMAL = 46;
  /** RegularExpression Id. */
  int CONTINUAR = 47;
  /** RegularExpression Id. */
  int FIMESCOLHA = 48;
  /** RegularExpression Id. */
  int INTEGER_LITERAL = 49;
  /** RegularExpression Id. */
  int FLOATING_POINT_LITERAL = 50;
  /** RegularExpression Id. */
  int EXPONENT = 51;
  /** RegularExpression Id. */
  int CHARACTER_LITERAL = 52;
  /** RegularExpression Id. */
  int STRING_LITERAL = 53;
  /** RegularExpression Id. */
  int LPAREN = 54;
  /** RegularExpression Id. */
  int RPAREN = 55;
  /** RegularExpression Id. */
  int LBRACE = 56;
  /** RegularExpression Id. */
  int RBRACE = 57;
  /** RegularExpression Id. */
  int LBRACKET = 58;
  /** RegularExpression Id. */
  int RBRACKET = 59;
  /** RegularExpression Id. */
  int COMMA = 60;
  /** RegularExpression Id. */
  int DOT = 61;
  /** RegularExpression Id. */
  int DOUBLEDOT = 62;
  /** RegularExpression Id. */
  int ASSIGN = 63;
  /** RegularExpression Id. */
  int GT = 64;
  /** RegularExpression Id. */
  int LT = 65;
  /** RegularExpression Id. */
  int EQ = 66;
  /** RegularExpression Id. */
  int LE = 67;
  /** RegularExpression Id. */
  int GE = 68;
  /** RegularExpression Id. */
  int SC_OR = 69;
  /** RegularExpression Id. */
  int SC_AND = 70;
  /** RegularExpression Id. */
  int INCR = 71;
  /** RegularExpression Id. */
  int DECR = 72;
  /** RegularExpression Id. */
  int PLUS = 73;
  /** RegularExpression Id. */
  int MINUS = 74;
  /** RegularExpression Id. */
  int STAR = 75;
  /** RegularExpression Id. */
  int SLASH = 76;
  /** RegularExpression Id. */
  int CSLASH = 77;
  /** RegularExpression Id. */
  int XOR = 78;
  /** RegularExpression Id. */

  int REM = 79;
  /** RegularExpression Id. */

  int PLUSASSIGN = 80;
  /** RegularExpression Id. */

  int MINUSASSIGN = 81;
  /** RegularExpression Id. */

  int STARASSIGN = 82;
  /** RegularExpression Id. */

  int SLASHASSIGN = 83;
  /** RegularExpression Id. */
  int ANDASSIGN = 84;
  /** RegularExpression Id. */

  int ORASSIGN = 85;
  /** RegularExpression Id. */

  int XORASSIGN = 86;
  /** RegularExpression Id. */

  int REMASSIGN = 87;
  /** RegularExpression Id. */
  int IDENTIFIER = 88;
  /** RegularExpression Id. */
  int LETTER = 89;
  /** RegularExpression Id. */
  int DIGIT = 90;

  /** Lexical state. */
  int DEFAULT = 0;
  /** Lexical state. */
  int IN_SINGLE_LINE_COMMENT = 1;
  /** Lexical state. */
  int IN_FORMAL_COMMENT = 2;
  /** Lexical state. */
  int IN_MULTI_LINE_COMMENT = 3;

  /** Literal token values. */
  String[] tokenImage = {
          "<EOF>",
          "\" \"",
          "\"\\t\"",
          "\"\\n\"",
          "\"\\r\"",
          "\"\\f\"",
          "\"//\"",
          "<token of kind 7>",
          "\"/*\"",
          "<SINGLE_LINE_COMMENT>",
          "\"*/\"",
          "\"*/\"",
          "<token of kind 12>",
          "\"algoritmo\"",
          "\"var\"",
          "\"inicio\"",
          "\"fimalgoritmo\"",
          "\"inteiro\"",
          "\"logico\"",
          "\"literal\"",
          "\"vetor\"",
          "\"de\"",
          "\"verdadeiro\"",
          "\"falso\"",
          "\"escreva\"",
          "\"escreval\"",
          "\"leia\"",
          "\"leial\"",
          "\"se\"",
          "\"entao\"",
          "\"fimse\"",
          "\"senao\"",
          "\"repita\"",
          "\"ate\"",
          "\"enquanto\"",
          "\"faca\"",
          "\"fimenquanto\"",
          "\"para\"",
          "\"fimpara\"",
          "\"null\"",
          "\"void\"",
          "\"caso\"",
          "\"parar\"",
          "\"padrao\"",
          "\"escolha\"",
          "\"retorna\"",
          "\"real\"",
          "\"continuar\"",
          "\"fimescolha\"",
          "<INTEGER_LITERAL>",
          "<FLOATING_POINT_LITERAL>",
          "<EXPONENT>",
          "<CHARACTER_LITERAL>",
          "<STRING_LITERAL>",
          "\"(\"",
          "\")\"",
          "\"{\"",
          "\"}\"",
          "\"[\"",
          "\"]\"",
          "\",\"",
          "\".\"",
          "\"..\"",
          "\"<-\"",
          "\">\"",
          "\"<\"",
          "\"=\"",
          "\"<=\"",
          "\">=\"",
          "\"ou\"",
          "\"e\"",
          "\"++\"",
          "\"--\"",
          "\"+\"",
          "\"-\"",
          "\"*\"",
          "\"/\"",
          "\"\\\\\"",
          "\"^\"",
          "\"%\"",
          "\"+<-\"",
          "\"-<-\"",
          "\"*<-\"",
          "\"/<-\"",
          "\"&<-\"",
          "\"|<-\"",
          "\"^<-\"",
          "\"%<-\"",
          "<IDENTIFIER>",
          "<LETTER>",
          "<DIGIT>",
          "\":\"",
          "\"!=\"",
          "\"passo\""
  };

}
