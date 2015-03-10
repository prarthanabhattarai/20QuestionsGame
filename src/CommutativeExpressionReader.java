/**
 * CommutativeExpressionReader.java
 **/

// XML
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.FactoryConfigurationError;  
import javax.xml.parsers.ParserConfigurationException;
 


import org.xml.sax.SAXException;  
import org.xml.sax.SAXParseException;  
import org.w3c.dom.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMSource;  

//import from new package
import ListsAndStacks.*;

// io
import java.io.*;

/**
 * CommutativeExpressionReader reads xml files of expressions with
 * binary, commutative operators.
 */
public class CommutativeExpressionReader
{

   /**
    * Parses XML file.
    * @return expression BinTree corresponding to file.
    **/
   public static BinTree readCommutativeExpr( String file )
   {
     return readCommutativeExpr( new File( file ) );
   }

   /**
    * Parses XML file
    * @return expression BinTree corresponding to file.
    **/
   public static BinTree readCommutativeExpr( File file )
   {
      DocumentBuilderFactory factory =
         DocumentBuilderFactory.newInstance();
      
      try 
      {
         DocumentBuilder builder = factory.newDocumentBuilder();
         Document document = builder.parse( file );

         return parseExprTree( document );	    
         
      } 
      catch (SAXException sxe) 
      {
         // Error generated during parsing)
         Exception  x = sxe;
         if (sxe.getException() != null)
            x = sxe.getException();
         x.printStackTrace();         
      } 
      catch (ParserConfigurationException pce) 
      {
         // Parser with specified options can't be built
         pce.printStackTrace();
      }   
      catch (IOException ioe) 
      {
         // I/O error
         ioe.printStackTrace();
      }
      
      return null;
   }
   
   /**
    * Parses XML Document. 
    * @return parsed BinTree.
    **/
   private static BinTree parseExprTree( Document document )
   {
     BinTree tree = new DefaultBinTree();

     // parse root
     Element root = 
       (Element)document.getDocumentElement();

     tree.setRoot( parseExprNode( root ) );

     return tree;
   }
   
  /**
   * Parses expr element.
   * @return BinTreeNode represented by element.
   **/
  private static BinTreeNode parseExprNode( Element element )
  {
    // base case: atom
    if ( element.getAttribute( "type" ).equals( "atom" ) )
    {
      // must have exactly one non-text child
      // get children
      NodeList children = element.getChildNodes();      

      for ( int i = 0; i < children.getLength(); i++ )
      {
        // if not text node
        if ( children.item(i) instanceof Element )
        {
          Element atom = (Element)children.item(i);
      
          // get attribute by name
          return new DefaultBinTreeNode( atom.getAttribute( "value" ) );    
        }
      }
      
      // error if gets to here
      return null;
    }
    // recursive case
    else
    {
      // get children
      NodeList children = element.getChildNodes();      

      // iterate through, looking for operator and two operands
      // NOTE: operand order does not matter because operators are
      // commutative
      String commutativeOp = "";
      BinTreeNode operand1 = null;
      BinTreeNode operand2 = null;
      Element currentElt;

      for ( int i = 0; i < children.getLength(); i++ )
      {
        // if not text node
        if ( children.item(i) instanceof Element )
        {
          currentElt = (Element)children.item(i);

          // test tag name 
          // if operator
          if ( currentElt.getTagName().equals( "operator" ) )
            commutativeOp = currentElt.getAttribute( "value" );
          // otherwise, should be expression
          else if ( currentElt.getTagName().equals( "expr" ) )
          {
            // store in operand1, if nothing there yet
            if ( operand1 == null )
              operand1 = parseExprNode( currentElt );
            // otherwise, put in operand2
            else
              operand2 = parseExprNode( currentElt );
          }
        }
      }

      // create node
      BinTreeNode exprNode = new DefaultBinTreeNode( commutativeOp );
      // set left and right children; arbitrary order
      exprNode.setLeftChild( operand1 );
      exprNode.setRightChild( operand2 );
      
      return exprNode;
    }
  }

}
