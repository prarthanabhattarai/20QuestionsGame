import ListsAndStacks.BinTree;

/**
 * CommutativeExpressionTester.java
 **/

/**
 * Testing expression i/o.
 **/
public class CommutativeExpressionTester
{

  /**
   * Read in xml file in args[0], print tree, then write back to file at args[1].
   **/
  public static void main( String[] args )
  {
    BinTree exprTree = CommutativeExpressionReader.readCommutativeExpr( "countries.xml" );
    System.out.println(exprTree.inorderString());
    CommutativeExpressionWriter.writeCommutativeExpr( exprTree, "arithExpr_copy.xml" );
    
    BinTree exprTree1 = CommutativeExpressionReader.readCommutativeExpr( "arithExpr_copy.xml" );
    System.out.println(exprTree1.inorderString());
  }
}


