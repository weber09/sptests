package algol;

/* algol.AlgolParser.java */
/* Generated By:JavaCC: Do not edit this line. algol.AlgolParser.java */
class AlgolParser implements AlgolParserConstants {
/*         public static void main(String args[]) {
    algol.AlgolParser parser;
        int version = 0;
        int rebuild = 0;
        int commit = 9;
    if (args.length == 0) {
      System.out.println("Portugues Estruturado Parser Version " + version + "." + rebuild + "." + commit + ":  Reading from standard input . . .");
      parser = new algol.AlgolParser(System.in);
    } else if (args.length == 1) {
      System.out.println("Portugues Estruturado Parser Version " + version + "." + rebuild + "." + commit + ":  Reading from file " + args[0] + " . . .");
      try {
        parser = new algol.AlgolParser(new java.io.FileInputStream(args[0]));
      } catch (java.io.FileNotFoundException e) {
        System.out.println("Portugues Estruturado Parser Version " + version + "." + rebuild + "." + commit + ":  File " + args[0] + " not found.");
        return;
      }
    } else {
      System.out.println("Portugues Estruturado Parser Version " + version + "." + rebuild + "." + commit + ":  Usage is one of:");
      System.out.println("         java algol.AlgolParser < inputfile");
      System.out.println("OR");
      System.out.println("         java algol.AlgolParser inputfile");
      return;
    }
    try {
      parser.Specification();
      System.out.println("Portugues Estruturado Parser Version " + version + "." + rebuild + "." + commit + ":  Algol file parsed successfully.");
    } catch (algol.ParseException e) {
      System.out.println("Portugues Estruturado Parser Version " + version + "." + rebuild + "." + commit + ":  Encountered errors during parse.");
          System.out.println(e.getMessage());
    }
  }*/

  final public
  void Compile() throws ParseException{
    Specification();
  }

/*PROGRAM SPECIFICATION*/
  static final public 
 void Specification() throws ParseException {
    jj_consume_token(ALGORITMO);
    jj_consume_token(STRING_LITERAL);
    VariableInitializer();
    jj_consume_token(INICIO);
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case ESCREVA:
      case ESCREVAL:
      case LEIA:
      case LEIAL:
      case SE:
      case REPITA:
      case ENQUANTO:
      case PARA:
      case PARAR:
      case ESCOLHA:
      case CONTINUAR:
      case IDENTIFIER:{
        ;
        break;
        }
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      Statement();
    }
    jj_consume_token(FIMALGORITMO);
  }

/*PROGRAM SPECIFICATION*/

/*VARIABLES DECLARATION BLOCK*/
  static final public 
void VariableInitializer() throws ParseException {
    jj_consume_token(VAR);
    VariableDeclarations();
  }

  static final public void VariableDeclarations() throws ParseException {
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case IDENTIFIER:{
        ;
        break;
        }
      default:
        jj_la1[1] = jj_gen;
        break label_2;
      }
      VariableDeclaratorId();
      jj_consume_token(91);
      Type();
    }
  }

  static final public void VariableDeclaratorId() throws ParseException {
    NameList();
  }

  static final public void NameList() throws ParseException {
    Name();
    label_3:
    while (true) {
      if (jj_2_1(2)) {
        ;
      } else {
        break label_3;
      }
      jj_consume_token(COMMA);
      Name();
    }
  }

  static final public void Name() throws ParseException {
    jj_consume_token(IDENTIFIER);
  }

  static final public void Type() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case LOGICO:{
      jj_consume_token(LOGICO);
      break;
      }
    case INTEIRO:{
      jj_consume_token(INTEIRO);
      break;
      }
    case DECIMAL:{
      jj_consume_token(DECIMAL);
      break;
      }
    case LITERAL:{
      jj_consume_token(LITERAL);
      break;
      }
    default:
      jj_la1[2] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

/*VARIABLES DECLARATION BLOCK*/

/*STATEMENTS*/
  static final public 
void Statement() throws ParseException {
    if (jj_2_2(2)) {
      StatementExpression();
    } else {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case ESCOLHA:{
        SwitchStatement();
        break;
        }
      case SE:{
        IfStatement();
        break;
        }
      case ENQUANTO:{
        WhileStatement();
        break;
        }
      case REPITA:{
        DoStatement();
        break;
        }
      case PARA:{
        ForStatement();
        break;
        }
      case PARAR:{
        BreakStatement();
        break;
        }
      case CONTINUAR:{
        ContinueStatement();
        break;
        }
      case ESCREVA:
      case ESCREVAL:{
        WriteStatement();
        break;
        }
      case LEIA:
      case LEIAL:{
        ReadStatement();
        break;
        }
      default:
        jj_la1[3] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  static final public void StatementExpression() throws ParseException {
    Name();
    AssignmentOperator();
    Expression();
  }

  static final public void AssignmentOperator() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ASSIGN:{
      jj_consume_token(ASSIGN);
      break;
      }
    case STARASSIGN:{
      jj_consume_token(STARASSIGN);
      break;
      }
    case SLASHASSIGN:{
      jj_consume_token(SLASHASSIGN);
      break;
      }
    case REMASSIGN:{
      jj_consume_token(REMASSIGN);
      break;
      }
    case PLUSASSIGN:{
      jj_consume_token(PLUSASSIGN);
      break;
      }
    case MINUSASSIGN:{
      jj_consume_token(MINUSASSIGN);
      break;
      }
    case ANDASSIGN:{
      jj_consume_token(ANDASSIGN);
      break;
      }
    case XORASSIGN:{
      jj_consume_token(XORASSIGN);
      break;
      }
    case ORASSIGN:{
      jj_consume_token(ORASSIGN);
      break;
      }
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void Expression() throws ParseException {
    ConditionalOrExpression();
  }

  static final public void ConditionalOrExpression() throws ParseException {
    ConditionalAndExpression();
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case SC_OR:{
        ;
        break;
        }
      default:
        jj_la1[5] = jj_gen;
        break label_4;
      }
      jj_consume_token(SC_OR);
      ConditionalAndExpression();
    }
  }

  static final public void ConditionalAndExpression() throws ParseException {
    EqualityExpression();
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case SC_AND:{
        ;
        break;
        }
      default:
        jj_la1[6] = jj_gen;
        break label_5;
      }
      jj_consume_token(SC_AND);
      EqualityExpression();
    }
  }

  static final public void EqualityExpression() throws ParseException {
    RelationalExpression();
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case EQ:
      case 92:{
        ;
        break;
        }
      default:
        jj_la1[7] = jj_gen;
        break label_6;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case EQ:{
        jj_consume_token(EQ);
        break;
        }
      case 92:{
        jj_consume_token(92);
        break;
        }
      default:
        jj_la1[8] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      RelationalExpression();
    }
  }

  static final public void RelationalExpression() throws ParseException {
    AdditiveExpression();
    label_7:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case GT:
      case LT:
      case LE:
      case GE:{
        ;
        break;
        }
      default:
        jj_la1[9] = jj_gen;
        break label_7;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case LT:{
        jj_consume_token(LT);
        break;
        }
      case GT:{
        jj_consume_token(GT);
        break;
        }
      case LE:{
        jj_consume_token(LE);
        break;
        }
      case GE:{
        jj_consume_token(GE);
        break;
        }
      default:
        jj_la1[10] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      AdditiveExpression();
    }
  }

  static final public void AdditiveExpression() throws ParseException {
    MultiplicativeExpression();
    label_8:
    while (true) {
      if (jj_2_3(2)) {
        ;
      } else {
        break label_8;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case PLUS:{
        jj_consume_token(PLUS);
        break;
        }
      case MINUS:{
        jj_consume_token(MINUS);
        break;
        }
      default:
        jj_la1[11] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      MultiplicativeExpression();
    }
  }

  static final public void MultiplicativeExpression() throws ParseException {
    PowerExpression();
    label_9:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case STAR:
      case SLASH:
      case CSLASH:
      case REM:{
        ;
        break;
        }
      default:
        jj_la1[12] = jj_gen;
        break label_9;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case STAR:{
        jj_consume_token(STAR);
        break;
        }
      case SLASH:{
        jj_consume_token(SLASH);
        break;
        }
      case REM:{
        jj_consume_token(REM);
        break;
        }
      case CSLASH:{
        jj_consume_token(CSLASH);
        break;
        }
      default:
        jj_la1[13] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      PowerExpression();
    }
  }

  static final public void PowerExpression() throws ParseException {
    UnaryExpression();
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case XOR:{
      jj_consume_token(XOR);
      PowerExpression();
      break;
      }
    default:
      jj_la1[14] = jj_gen;
      ;
    }
  }

  static final public void UnaryExpression() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case PLUS:
    case MINUS:{
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case PLUS:{
        jj_consume_token(PLUS);
        break;
        }
      case MINUS:{
        jj_consume_token(MINUS);
        break;
        }
      default:
        jj_la1[15] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      UnaryExpression();
      break;
      }
    case VERDADEIRO:
    case FALSO:
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
    case CHARACTER_LITERAL:
    case STRING_LITERAL:
    case LPAREN:
    case IDENTIFIER:{
      PrimaryExpression();
      break;
      }
    default:
      jj_la1[16] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void PrimaryExpression() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case IDENTIFIER:{
      Name();
      break;
      }
    case LPAREN:{
      jj_consume_token(LPAREN);
      Expression();
      jj_consume_token(RPAREN);
      break;
      }
    case VERDADEIRO:
    case FALSO:
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
    case CHARACTER_LITERAL:
    case STRING_LITERAL:{
      Literal();
      break;
      }
    default:
      jj_la1[17] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void Literal() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case INTEGER_LITERAL:{
      jj_consume_token(INTEGER_LITERAL);
      break;
      }
    case FLOATING_POINT_LITERAL:{
      jj_consume_token(FLOATING_POINT_LITERAL);
      break;
      }
    case CHARACTER_LITERAL:{
      jj_consume_token(CHARACTER_LITERAL);
      break;
      }
    case STRING_LITERAL:{
      jj_consume_token(STRING_LITERAL);
      break;
      }
    case VERDADEIRO:
    case FALSO:{
      BooleanLiteral();
      break;
      }
    default:
      jj_la1[18] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void BooleanLiteral() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case VERDADEIRO:{
      jj_consume_token(VERDADEIRO);
      break;
      }
    case FALSO:{
      jj_consume_token(FALSO);
      break;
      }
    default:
      jj_la1[19] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void SwitchStatement() throws ParseException {
    jj_consume_token(ESCOLHA);
    Expression();
    label_10:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case CASO:
      case PADRAO:{
        ;
        break;
        }
      default:
        jj_la1[20] = jj_gen;
        break label_10;
      }
      SwitchLabel();
      label_11:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case ESCREVA:
        case ESCREVAL:
        case LEIA:
        case LEIAL:
        case SE:
        case REPITA:
        case ENQUANTO:
        case PARA:
        case PARAR:
        case ESCOLHA:
        case CONTINUAR:
        case IDENTIFIER:{
          ;
          break;
          }
        default:
          jj_la1[21] = jj_gen;
          break label_11;
        }
        Statement();
      }
    }
    jj_consume_token(FIMESCOLHA);
  }

  static final public void SwitchLabel() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case CASO:{
      jj_consume_token(CASO);
      Expression();
      jj_consume_token(91);
      break;
      }
    case PADRAO:{
      jj_consume_token(PADRAO);
      jj_consume_token(91);
      break;
      }
    default:
      jj_la1[22] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void IfStatement() throws ParseException {
    jj_consume_token(SE);
    Expression();
    jj_consume_token(ENTAO);
    label_12:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case ESCREVA:
      case ESCREVAL:
      case LEIA:
      case LEIAL:
      case SE:
      case REPITA:
      case ENQUANTO:
      case PARA:
      case PARAR:
      case ESCOLHA:
      case CONTINUAR:
      case IDENTIFIER:{
        ;
        break;
        }
      default:
        jj_la1[23] = jj_gen;
        break label_12;
      }
      Statement();
    }
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case SENAO:{
      jj_consume_token(SENAO);
      label_13:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case ESCREVA:
        case ESCREVAL:
        case LEIA:
        case LEIAL:
        case SE:
        case REPITA:
        case ENQUANTO:
        case PARA:
        case PARAR:
        case ESCOLHA:
        case CONTINUAR:
        case IDENTIFIER:{
          ;
          break;
          }
        default:
          jj_la1[24] = jj_gen;
          break label_13;
        }
        Statement();
      }
      break;
      }
    default:
      jj_la1[25] = jj_gen;
      ;
    }
    jj_consume_token(FIMSE);
  }

  static final public void WhileStatement() throws ParseException {
    jj_consume_token(ENQUANTO);
    Expression();
    jj_consume_token(FACA);
    label_14:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case ESCREVA:
      case ESCREVAL:
      case LEIA:
      case LEIAL:
      case SE:
      case REPITA:
      case ENQUANTO:
      case PARA:
      case PARAR:
      case ESCOLHA:
      case CONTINUAR:
      case IDENTIFIER:{
        ;
        break;
        }
      default:
        jj_la1[26] = jj_gen;
        break label_14;
      }
      Statement();
    }
    jj_consume_token(FIMENQUANTO);
  }

  static final public void DoStatement() throws ParseException {
    jj_consume_token(REPITA);
    label_15:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case ESCREVA:
      case ESCREVAL:
      case LEIA:
      case LEIAL:
      case SE:
      case REPITA:
      case ENQUANTO:
      case PARA:
      case PARAR:
      case ESCOLHA:
      case CONTINUAR:
      case IDENTIFIER:{
        ;
        break;
        }
      default:
        jj_la1[27] = jj_gen;
        break label_15;
      }
      Statement();
    }
    jj_consume_token(ATE);
    Expression();
  }

  static final public void ForStatement() throws ParseException {
    jj_consume_token(PARA);
    jj_consume_token(IDENTIFIER);
    jj_consume_token(DE);
    Expression();
    jj_consume_token(ATE);
    Expression();
    jj_consume_token(FACA);
    label_16:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case ESCREVA:
      case ESCREVAL:
      case LEIA:
      case LEIAL:
      case SE:
      case REPITA:
      case ENQUANTO:
      case PARA:
      case PARAR:
      case ESCOLHA:
      case CONTINUAR:
      case IDENTIFIER:{
        ;
        break;
        }
      default:
        jj_la1[28] = jj_gen;
        break label_16;
      }
      Statement();
    }
    jj_consume_token(FIMPARA);
  }

  static final public void BreakStatement() throws ParseException {
    jj_consume_token(PARAR);
  }

  static final public void ContinueStatement() throws ParseException {
    jj_consume_token(CONTINUAR);
  }

  static final public void ReturnStatement() throws ParseException {
    jj_consume_token(RETORNA);
    if (jj_2_4(2)) {
      Expression();
    } else {
      ;
    }
  }

  static final public void WriteStatement() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ESCREVA:{
      jj_consume_token(ESCREVA);
      break;
      }
    case ESCREVAL:{
      jj_consume_token(ESCREVAL);
      break;
      }
    default:
      jj_la1[29] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    jj_consume_token(LPAREN);
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case VERDADEIRO:
    case FALSO:
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
    case CHARACTER_LITERAL:
    case STRING_LITERAL:
    case IDENTIFIER:{
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case IDENTIFIER:{
        Name();
        break;
        }
      case VERDADEIRO:
      case FALSO:
      case INTEGER_LITERAL:
      case FLOATING_POINT_LITERAL:
      case CHARACTER_LITERAL:
      case STRING_LITERAL:{
        Literal();
        break;
        }
      default:
        jj_la1[30] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      label_17:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case COMMA:{
          ;
          break;
          }
        default:
          jj_la1[31] = jj_gen;
          break label_17;
        }
        jj_consume_token(COMMA);
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case IDENTIFIER:{
          Name();
          break;
          }
        case VERDADEIRO:
        case FALSO:
        case INTEGER_LITERAL:
        case FLOATING_POINT_LITERAL:
        case CHARACTER_LITERAL:
        case STRING_LITERAL:{
          Literal();
          break;
          }
        default:
          jj_la1[32] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
      break;
      }
    default:
      jj_la1[33] = jj_gen;
      ;
    }
    jj_consume_token(RPAREN);
  }

  static final public void ReadStatement() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case LEIA:{
      jj_consume_token(LEIA);
      break;
      }
    case LEIAL:{
      jj_consume_token(LEIAL);
      break;
      }
    default:
      jj_la1[34] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    jj_consume_token(LPAREN);
    NameList();
    jj_consume_token(RPAREN);
  }

  static private boolean jj_2_1(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  static private boolean jj_2_2(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  static private boolean jj_2_3(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(2, xla); }
  }

  static private boolean jj_2_4(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_4(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(3, xla); }
  }

  static private boolean jj_3R_28()
 {
    if (jj_3R_31()) return true;
    return false;
  }

  static private boolean jj_3R_27()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(73)) {
    jj_scanpos = xsp;
    if (jj_scan_token(74)) return true;
    }
    if (jj_3R_24()) return true;
    return false;
  }

  static private boolean jj_3R_24()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_27()) {
    jj_scanpos = xsp;
    if (jj_3R_28()) return true;
    }
    return false;
  }

  static private boolean jj_3R_22()
 {
    if (jj_3R_24()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_42()) jj_scanpos = xsp;
    return false;
  }

  static private boolean jj_3R_37()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(65)) {
    jj_scanpos = xsp;
    if (jj_scan_token(64)) {
    jj_scanpos = xsp;
    if (jj_scan_token(67)) {
    jj_scanpos = xsp;
    if (jj_scan_token(68)) return true;
    }
    }
    }
    return false;
  }

  static private boolean jj_3R_33()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(66)) {
    jj_scanpos = xsp;
    if (jj_scan_token(92)) return true;
    }
    return false;
  }

  static private boolean jj_3_1()
 {
    if (jj_scan_token(COMMA)) return true;
    if (jj_scan_token(88)) return true;
    return false;
  }

  static private boolean jj_3R_19()
 {
    if (jj_3R_22()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_40()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  static private boolean jj_3R_26()
 {
    if (jj_scan_token(SC_OR)) return true;
    return false;
  }

  static private boolean jj_3R_30()
 {
    if (jj_scan_token(SC_AND)) return true;
    return false;
  }

  static private boolean jj_3R_36()
 {
    if (jj_3R_19()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_3()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  static private boolean jj_3R_32()
 {
    if (jj_3R_36()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_37()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  static private boolean jj_3R_29()
 {
    if (jj_3R_32()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_33()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  static private boolean jj_3R_25()
 {
    if (jj_3R_29()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_30()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  static private boolean jj_3R_23()
 {
    if (jj_3R_25()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_26()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  static private boolean jj_3R_20()
 {
    if (jj_3R_23()) return true;
    return false;
  }

  static private boolean jj_3R_21()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(63)) {
    jj_scanpos = xsp;
    if (jj_scan_token(82)) {
    jj_scanpos = xsp;
    if (jj_scan_token(83)) {
    jj_scanpos = xsp;
    if (jj_scan_token(87)) {
    jj_scanpos = xsp;
    if (jj_scan_token(80)) {
    jj_scanpos = xsp;
    if (jj_scan_token(81)) {
    jj_scanpos = xsp;
    if (jj_scan_token(84)) {
    jj_scanpos = xsp;
    if (jj_scan_token(86)) {
    jj_scanpos = xsp;
    if (jj_scan_token(85)) return true;
    }
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  static private boolean jj_3R_18()
 {
    if (jj_scan_token(88)) return true;
    if (jj_3R_21()) return true;
    return false;
  }

  static private boolean jj_3R_41()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(23)) {
    jj_scanpos = xsp;
    if (jj_scan_token(24)) return true;
    }
    return false;
  }

  static private boolean jj_3R_39()
 {
    if (jj_3R_41()) return true;
    return false;
  }

  static private boolean jj_3R_42()
 {
    if (jj_scan_token(XOR)) return true;
    return false;
  }

  static private boolean jj_3R_35()
 {
    if (jj_3R_38()) return true;
    return false;
  }

  static private boolean jj_3R_38()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(50)) {
    jj_scanpos = xsp;
    if (jj_scan_token(51)) {
    jj_scanpos = xsp;
    if (jj_scan_token(53)) {
    jj_scanpos = xsp;
    if (jj_scan_token(54)) {
    jj_scanpos = xsp;
    if (jj_3R_39()) return true;
    }
    }
    }
    }
    return false;
  }

  static private boolean jj_3R_34()
 {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_20()) return true;
    return false;
  }

  static private boolean jj_3R_31()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(88)) {
    jj_scanpos = xsp;
    if (jj_3R_34()) {
    jj_scanpos = xsp;
    if (jj_3R_35()) return true;
    }
    }
    return false;
  }

  static private boolean jj_3_4()
 {
    if (jj_3R_20()) return true;
    return false;
  }

  static private boolean jj_3_3()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(73)) {
    jj_scanpos = xsp;
    if (jj_scan_token(74)) return true;
    }
    if (jj_3R_19()) return true;
    return false;
  }

  static private boolean jj_3R_40()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(75)) {
    jj_scanpos = xsp;
    if (jj_scan_token(76)) {
    jj_scanpos = xsp;
    if (jj_scan_token(79)) {
    jj_scanpos = xsp;
    if (jj_scan_token(77)) return true;
    }
    }
    }
    return false;
  }

  static private boolean jj_3_2()
 {
    if (jj_3R_18()) return true;
    return false;
  }

  static private boolean jj_initialized_once = false;
  /** Generated algol.Token Manager. */
  static public AlgolParserTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /** Current token. */
  static public Token token;
  /** Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private Token jj_scanpos, jj_lastpos;
  static private int jj_la;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[35];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static private int[] jj_la1_2;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
      jj_la1_init_2();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x3e000000,0x0,0xe0000,0x3e000000,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x1800000,0x1800000,0x1800000,0x1800000,0x0,0x3e000000,0x0,0x3e000000,0x3e000000,0x0,0x3e000000,0x3e000000,0x3e000000,0x6000000,0x1800000,0x0,0x1800000,0x1800000,0x18000000,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x1284a,0x0,0x8000,0x1284a,0x80000000,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0xec0000,0xec0000,0x6c0000,0x0,0x1400,0x1284a,0x1400,0x1284a,0x1284a,0x1,0x1284a,0x1284a,0x1284a,0x0,0x6c0000,0x20000000,0x6c0000,0x6c0000,0x0,};
   }
   private static void jj_la1_init_2() {
      jj_la1_2 = new int[] {0x1000000,0x1000000,0x0,0x0,0xff0000,0x20,0x40,0x10000004,0x10000004,0x1b,0x1b,0x600,0xb800,0xb800,0x4000,0x600,0x1000600,0x1000000,0x0,0x0,0x0,0x1000000,0x0,0x1000000,0x1000000,0x0,0x1000000,0x1000000,0x1000000,0x0,0x1000000,0x0,0x1000000,0x1000000,0x0,};
   }
  static final private JJCalls[] jj_2_rtns = new JJCalls[4];
  static private boolean jj_rescan = false;
  static private int jj_gc = 0;

  /** Constructor with InputStream. */
  public AlgolParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public AlgolParser(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new AlgolParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 35; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 35; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public AlgolParser(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new AlgolParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 35; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 35; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated algol.Token Manager. */
  public AlgolParser(AlgolParserTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 35; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(AlgolParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 35; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  static private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  @SuppressWarnings("serial")
  static private final class LookaheadSuccess extends java.lang.Error { }
  static final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  static private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next algol.Token. */
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific algol.Token. */
  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static private int jj_ntk_f() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  static private int[] jj_expentry;
  static private int jj_kind = -1;
  static private int[] jj_lasttokens = new int[100];
  static private int jj_endpos;

  static private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              continue jj_entries_loop;
            }
          }
          jj_expentries.add(jj_expentry);
          break jj_entries_loop;
        }
      }
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate algol.ParseException. */
  static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[93];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 35; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
          if ((jj_la1_2[i] & (1<<j)) != 0) {
            la1tokens[64+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 93; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  static final public void enable_tracing() {
  }

  /** Disable tracing. */
  static final public void disable_tracing() {
  }

  static private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 4; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
            case 2: jj_3_3(); break;
            case 3: jj_3_4(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  static private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}