**1.0.8** (_2010-01-17_)
  * Fixed issues:
    * [Issue 17](https://code.google.com/p/javaparser/issues/detail?id=17): A refactor suggestion for AnnotationExpr and its subclasses
    * [Issue 21](https://code.google.com/p/javaparser/issues/detail?id=21): Java 5 JavaParser compiled JARs
    * [Issue 22](https://code.google.com/p/javaparser/issues/detail?id=22): Please use java.lang.reflect.Modifier constants in japa.parser.ast.body.ModifierSet
    * [Issue 27](https://code.google.com/p/javaparser/issues/detail?id=27): Implement the "equal" method
    * [Issue 30](https://code.google.com/p/javaparser/issues/detail?id=30): equals and hashCode methods

**1.0.7** (_2009-04-12_)
  * [Issue 19](https://code.google.com/p/javaparser/issues/detail?id=19) fixed:
  * Tests changed to run with junit 4

**1.0.6** (_2009-01-11_)
  * [Issue 11](https://code.google.com/p/javaparser/issues/detail?id=11) fixed: changed method `get/setPakage` to `get/setPackage` in the class `CompilationUnit`
  * Created new visitor adapter to help AST modification: `ModifierVisitorAdapter`
  * Changed visitor adapters to `abstract`

**1.0.5** (_2008-10-26_)
  * Created simplified constructors in the nodes of the AST (without positional arguments)
  * Created `ASTHelper` class with some helpful methods (more methods are still needed) (See UsingThisParser for an example)

**1.0.4** (_2008-10-07_)
  * Moved to javacc 4.1. ([Issue 10](https://code.google.com/p/javaparser/issues/detail?id=10))
  * The java\_1\_5.jj can be build alone without compilation errors

**1.0.3** (_2008-09-06_)
  * Removed `SuperMemberAccessExpr` class, it was no longer used
  * Removed the methods `get/setTypeArgs()` from `ArrayCreationExpr`, this node shouldn't have these methods.
  * Fixed the bug with start/end position of the nodes `IntegerLiteralMinValueExpr` and `LongLiteralMinValueExpr`
  * The methods `get/setAnnotations()` from all `BodyDeclaration` subclasses were pushed down to `BodyDeclaration` class

**1.0.2** (_2008-07-20_)
  * Issue fixed: [Issue 1](https://code.google.com/p/javaparser/issues/detail?id=1): Add support for editing AST nodes or create new ones

**1.0.1** (_2008-07-01_)
  * Issue fixed: [Issue 5](https://code.google.com/p/javaparser/issues/detail?id=5): end line and end column equal to begin line and begin column

**1.0.0** (_2008-06-25_)
  * Changed version numbering, starting version 1.0.0
  * Javadoc done for packages:
    * japa.parser
    * japa.parser.ast
  * Corrected bug when parsing in multithread:
    * `JavaParser.setCacheParser(false)` must be called before to use the parser concurrent

**2008-06-19**
  * No code changes, added binary distribution to download page

**2008-06-11**
  * Issue fixed: [Issue 2](https://code.google.com/p/javaparser/issues/detail?id=2): NPE in `VoidVisitorAdapter`

**2008-06-09**
  * Added Adapters for de visitors
    * `japa.parser.ast.visitor.GenericVisitorAdapter`
    * `japa.parser.ast.visitor.VoidVisitorAdapter`

**2008-05-28**
  * This project now is published at Google Code:
    * http://code.google.com/p/javaparser/

**2008-05-25**
  * Added support for comments and javadoc to the tree.
    * Javadocs are stored directly to members (`BodyDeclaration` and all deriveds (classes, methods, fields, etc.)), accessible by the method getJavadoc()
    * All comments are stored in the `CompilationUnit`, accessible by the method getComments()

**2008-04-01**
  * Changed all nodes public attributes to be private and created getters to access them
  * Changed the methods of the Node getLine e getColumn to getBeginLine and getBeginColumn
  * Added the methods getEndLine and getEndColumn to the Node class (works only in the BlockNode)

**2007-12-22**
  * Corrected `ConditionalExpression` bug

**2007-10-21**
  * Added LGPL License

**2007-10-21**
  * Bugs corrected:
    * Created `PackageDeclaration` member of `CompilationUnit` to add suport for annotations in the package declaration
  * Parameterized anonymous constructor invocation
  * Explicit constructor invotation Type Arguments
  * ctrl+z ("\u001A") ar end of compilation unit

**2007-10-09**
  * `EnumConstantDeclaration` annotation support corrected
  * Parssing Java Unicode escape characters suport added

**2007-10-03**
  * Bug corrected: `MotifComboPopup.this.super()` statement was generating parser error

**2007-10-01**
  * Bug corrected: Casting signed primitive values
    * double d = (double) -1;
> > > ^

**2007-08-06**
  * Bug with the ingle line comments in the final of the unit corrected

**2007-07-31**
  * Fixed the bug with the following expression:
    * Class c = (int.class);

**2007-06-26**
  * Bug fixes from Leon Poyyayil work
    * suport for hex floating point
    * unicode digits in indentifier
    * `MemberValueArrayInitializer`


**2007-03-09**
  * Long and Integer literal MIN\_VALUE bug

**2007-02-24**
  * '\0' bug fixed

**2007-02-01**
  * Many bug fixes
  * Added line/column to nodes