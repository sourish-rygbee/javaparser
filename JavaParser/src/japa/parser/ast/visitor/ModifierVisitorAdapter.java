/*
 * Copyright (C) 2008 Júlio Vilmar Gesser.
 * 
 * This file is part of Java 1.5 parser and Abstract Syntax Tree.
 *
 * Java 1.5 parser and Abstract Syntax Tree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Java 1.5 parser and Abstract Syntax Tree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Java 1.5 parser and Abstract Syntax Tree.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 09/06/2008
 */
package japa.parser.ast.visitor;

import japa.parser.ast.BlockComment;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.LineComment;
import japa.parser.ast.Node;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.TypeParameter;
import japa.parser.ast.body.AnnotationDeclaration;
import japa.parser.ast.body.AnnotationMemberDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EmptyMemberDeclaration;
import japa.parser.ast.body.EmptyTypeDeclaration;
import japa.parser.ast.body.EnumConstantDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.InitializerDeclaration;
import japa.parser.ast.body.JavadocComment;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.ArrayAccessExpr;
import japa.parser.ast.expr.ArrayCreationExpr;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.BinaryExpr;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.CastExpr;
import japa.parser.ast.expr.CharLiteralExpr;
import japa.parser.ast.expr.ClassExpr;
import japa.parser.ast.expr.ConditionalExpr;
import japa.parser.ast.expr.DoubleLiteralExpr;
import japa.parser.ast.expr.EnclosedExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.InstanceOfExpr;
import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.expr.IntegerLiteralMinValueExpr;
import japa.parser.ast.expr.LongLiteralExpr;
import japa.parser.ast.expr.LongLiteralMinValueExpr;
import japa.parser.ast.expr.MarkerAnnotationExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.NullLiteralExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.expr.SingleMemberAnnotationExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.expr.SuperExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.expr.UnaryExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.AssertStmt;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.BreakStmt;
import japa.parser.ast.stmt.CatchClause;
import japa.parser.ast.stmt.ContinueStmt;
import japa.parser.ast.stmt.DoStmt;
import japa.parser.ast.stmt.EmptyStmt;
import japa.parser.ast.stmt.ExplicitConstructorInvocationStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.IfStmt;
import japa.parser.ast.stmt.LabeledStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.SwitchEntryStmt;
import japa.parser.ast.stmt.SwitchStmt;
import japa.parser.ast.stmt.SynchronizedStmt;
import japa.parser.ast.stmt.ThrowStmt;
import japa.parser.ast.stmt.TryStmt;
import japa.parser.ast.stmt.TypeDeclarationStmt;
import japa.parser.ast.stmt.WhileStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.type.VoidType;
import japa.parser.ast.type.WildcardType;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public class ModifierVisitorAdapter<A> implements GenericVisitor<Node, A> {

    private void removeNulls(List< ? > list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i) == null) {
                list.remove(i);
            }
        }
    }

    public Node visit(AnnotationDeclaration n, A arg) {
        if (n.getJavaDoc() != null) {
            n.setJavaDoc((JavadocComment) n.getJavaDoc().accept(this, arg));
        }
        List<AnnotationExpr> annotations = n.getAnnotations();
        if (annotations != null) {
            for (int i = 0; i < annotations.size(); i++) {
                annotations.set(i, (AnnotationExpr) annotations.get(i).accept(this, arg));
            }
            removeNulls(annotations);
        }
        List<BodyDeclaration> members = n.getMembers();
        if (members != null) {
            for (int i = 0; i < members.size(); i++) {
                members.set(i, (BodyDeclaration) members.get(i).accept(this, arg));
            }
            removeNulls(members);
        }
        return n;
    }

    public Node visit(AnnotationMemberDeclaration n, A arg) {
        if (n.getJavaDoc() != null) {
            n.setJavaDoc((JavadocComment) n.getJavaDoc().accept(this, arg));
        }
        List<AnnotationExpr> annotations = n.getAnnotations();
        if (annotations != null) {
            for (int i = 0; i < annotations.size(); i++) {
                annotations.set(i, (AnnotationExpr) annotations.get(i).accept(this, arg));
            }
            removeNulls(annotations);
        }
        n.setType((Type) n.getType().accept(this, arg));
        if (n.getDefaultValue() != null) {
            n.setDefaultValue((Expression) n.getDefaultValue().accept(this, arg));
        }
        return n;
    }

    public Node visit(ArrayAccessExpr n, A arg) {
        n.setName((Expression) n.getName().accept(this, arg));
        n.setIndex((Expression) n.getIndex().accept(this, arg));
        return n;
    }

    public Node visit(ArrayCreationExpr n, A arg) {
        n.setType((Type) n.getType().accept(this, arg));
        if (n.getDimensions() != null) {
            List<Expression> dimensions = n.getDimensions();
            if (dimensions != null) {
                for (int i = 0; i < dimensions.size(); i++) {
                    dimensions.set(i, (Expression) dimensions.get(i).accept(this, arg));
                }
                removeNulls(dimensions);
            }
        } else {
            n.setInitializer((ArrayInitializerExpr) n.getInitializer().accept(this, arg));
        }
        return n;
    }

    public Node visit(ArrayInitializerExpr n, A arg) {
        if (n.getValues() != null) {
            List<Expression> values = n.getValues();
            if (values != null) {
                for (int i = 0; i < values.size(); i++) {
                    values.set(i, (Expression) values.get(i).accept(this, arg));
                }
                removeNulls(values);
            }
        }
        return n;
    }

    public Node visit(AssertStmt n, A arg) {
        n.setCheck((Expression) n.getCheck().accept(this, arg));
        if (n.getMessage() != null) {
            n.setMessage((Expression) n.getMessage().accept(this, arg));
        }
        return n;
    }

    public Node visit(AssignExpr n, A arg) {
        n.setTarget((Expression) n.getTarget().accept(this, arg));
        n.setValue((Expression) n.getValue().accept(this, arg));
        return n;
    }

    public Node visit(BinaryExpr n, A arg) {
        n.setLeft((Expression) n.getLeft().accept(this, arg));
        n.setRight((Expression) n.getRight().accept(this, arg));
        return n;
    }

    public Node visit(BlockStmt n, A arg) {
        List<Statement> stmts = n.getStmts();
        if (stmts != null) {
            for (int i = 0; i < stmts.size(); i++) {
                stmts.set(i, (Statement) stmts.get(i).accept(this, arg));
            }
            removeNulls(stmts);
        }
        return n;
    }

    public Node visit(BooleanLiteralExpr n, A arg) {
        return n;
    }

    public Node visit(BreakStmt n, A arg) {
        return n;
    }

    public Node visit(CastExpr n, A arg) {
        n.setType((Type) n.getType().accept(this, arg));
        n.setExpr((Expression) n.getExpr().accept(this, arg));
        return n;
    }

    public Node visit(CatchClause n, A arg) {
        n.setExcept((Parameter) n.getExcept().accept(this, arg));
        n.setCatchBlock((BlockStmt) n.getCatchBlock().accept(this, arg));
        return n;

    }

    public Node visit(CharLiteralExpr n, A arg) {
        return n;
    }

    public Node visit(ClassExpr n, A arg) {
        n.setType((Type) n.getType().accept(this, arg));
        return n;
    }

    public Node visit(ClassOrInterfaceDeclaration n, A arg) {
        if (n.getJavaDoc() != null) {
            n.setJavaDoc((JavadocComment) n.getJavaDoc().accept(this, arg));
        }
        List<AnnotationExpr> annotations = n.getAnnotations();
        if (annotations != null) {
            for (int i = 0; i < annotations.size(); i++) {
                annotations.set(i, (AnnotationExpr) annotations.get(i).accept(this, arg));
            }
            removeNulls(annotations);
        }
        List<TypeParameter> typeParameters = n.getTypeParameters();
        if (typeParameters != null) {
            for (int i = 0; i < typeParameters.size(); i++) {
                typeParameters.set(i, (TypeParameter) typeParameters.get(i).accept(this, arg));
            }
            removeNulls(typeParameters);
        }
        List<ClassOrInterfaceType> extendz = n.getExtends();
        if (extendz != null) {
            for (int i = 0; i < extendz.size(); i++) {
                extendz.set(i, (ClassOrInterfaceType) extendz.get(i).accept(this, arg));
            }
            removeNulls(extendz);
        }
        List<ClassOrInterfaceType> implementz = n.getImplements();
        if (implementz != null) {
            for (int i = 0; i < implementz.size(); i++) {
                implementz.set(i, (ClassOrInterfaceType) implementz.get(i).accept(this, arg));
            }
            removeNulls(implementz);
        }
        List<BodyDeclaration> members = n.getMembers();
        if (members != null) {
            for (int i = 0; i < members.size(); i++) {
                members.set(i, (BodyDeclaration) members.get(i).accept(this, arg));
            }
            removeNulls(members);
        }
        return n;
    }

    public Node visit(ClassOrInterfaceType n, A arg) {
        if (n.getScope() != null) {
            n.setScope((ClassOrInterfaceType) n.getScope().accept(this, arg));
        }
        List<Type> typeArgs = n.getTypeArgs();
        if (typeArgs != null) {
            for (int i = 0; i < typeArgs.size(); i++) {
                typeArgs.set(i, (Type) typeArgs.get(i).accept(this, arg));
            }
            removeNulls(typeArgs);
        }
        return n;
    }

    public Node visit(CompilationUnit n, A arg) {
        if (n.getPackage() != null) {
            n.setPackage((PackageDeclaration) n.getPackage().accept(this, arg));
        }
        List<ImportDeclaration> imports = n.getImports();
        if (imports != null) {
            for (int i = 0; i < imports.size(); i++) {
                imports.set(i, (ImportDeclaration) imports.get(i).accept(this, arg));
            }
            removeNulls(imports);
        }
        List<TypeDeclaration> types = n.getTypes();
        if (types != null) {
            for (int i = 0; i < types.size(); i++) {
                types.set(i, (TypeDeclaration) types.get(i).accept(this, arg));
            }
            removeNulls(types);
        }
        return n;
    }

    public Node visit(ConditionalExpr n, A arg) {
        n.setCondition((Expression) n.getCondition().accept(this, arg));
        n.setThenExpr((Expression) n.getThenExpr().accept(this, arg));
        n.setElseExpr((Expression) n.getElseExpr().accept(this, arg));
        return n;
    }

    public Node visit(ConstructorDeclaration n, A arg) {
        if (n.getJavaDoc() != null) {
            n.setJavaDoc((JavadocComment) n.getJavaDoc().accept(this, arg));
        }
        List<AnnotationExpr> annotations = n.getAnnotations();
        if (annotations != null) {
            for (int i = 0; i < annotations.size(); i++) {
                annotations.set(i, (AnnotationExpr) annotations.get(i).accept(this, arg));
            }
            removeNulls(annotations);
        }
        List<TypeParameter> typeParameters = n.getTypeParameters();
        if (typeParameters != null) {
            for (int i = 0; i < typeParameters.size(); i++) {
                typeParameters.set(i, (TypeParameter) typeParameters.get(i).accept(this, arg));
            }
            removeNulls(typeParameters);
        }
        List<Parameter> parameters = n.getParameters();
        if (parameters != null) {
            for (int i = 0; i < parameters.size(); i++) {
                parameters.set(i, (Parameter) parameters.get(i).accept(this, arg));
            }
            removeNulls(parameters);
        }
        List<NameExpr> throwz = n.getThrows();
        if (throwz != null) {
            for (int i = 0; i < throwz.size(); i++) {
                throwz.set(i, (NameExpr) throwz.get(i).accept(this, arg));
            }
            removeNulls(throwz);
        }
        n.setBlock((BlockStmt) n.getBlock().accept(this, arg));
        return n;
    }

    public Node visit(ContinueStmt n, A arg) {
        return n;
    }

    public Node visit(DoStmt n, A arg) {
        n.setBody((Statement) n.getBody().accept(this, arg));
        n.setCondition((Expression) n.getCondition().accept(this, arg));
        return n;
    }

    public Node visit(DoubleLiteralExpr n, A arg) {
        return null;
    }

    public Node visit(EmptyMemberDeclaration n, A arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        return null;
    }

    public Node visit(EmptyStmt n, A arg) {
        return null;
    }

    public Node visit(EmptyTypeDeclaration n, A arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        return null;
    }

    public Node visit(EnclosedExpr n, A arg) {
        n.getInner().accept(this, arg);
        return null;
    }

    public Node visit(EnumConstantDeclaration n, A arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        if (n.getAnnotations() != null) {
            for (AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        if (n.getArgs() != null) {
            for (Expression e : n.getArgs()) {
                e.accept(this, arg);
            }
        }
        if (n.getClassBody() != null) {
            for (BodyDeclaration member : n.getClassBody()) {
                member.accept(this, arg);
            }
        }
        return null;
    }

    public Node visit(EnumDeclaration n, A arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        if (n.getAnnotations() != null) {
            for (AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        if (n.getImplements() != null) {
            for (ClassOrInterfaceType c : n.getImplements()) {
                c.accept(this, arg);
            }
        }
        if (n.getEntries() != null) {
            for (EnumConstantDeclaration e : n.getEntries()) {
                e.accept(this, arg);
            }
        }
        if (n.getMembers() != null) {
            for (BodyDeclaration member : n.getMembers()) {
                member.accept(this, arg);
            }
        }
        return null;
    }

    public Node visit(ExplicitConstructorInvocationStmt n, A arg) {
        if (!n.isThis()) {
            if (n.getExpr() != null) {
                n.getExpr().accept(this, arg);
            }
        }
        if (n.getTypeArgs() != null) {
            for (Type t : n.getTypeArgs()) {
                t.accept(this, arg);
            }
        }
        if (n.getArgs() != null) {
            for (Expression e : n.getArgs()) {
                e.accept(this, arg);
            }
        }
        return null;
    }

    public Node visit(ExpressionStmt n, A arg) {
        n.getExpression().accept(this, arg);
        return null;
    }

    public Node visit(FieldAccessExpr n, A arg) {
        n.getScope().accept(this, arg);
        return null;
    }

    public Node visit(FieldDeclaration n, A arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        if (n.getAnnotations() != null) {
            for (AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        n.getType().accept(this, arg);
        for (VariableDeclarator var : n.getVariables()) {
            var.accept(this, arg);
        }
        return null;
    }

    public Node visit(ForeachStmt n, A arg) {
        n.getVariable().accept(this, arg);
        n.getIterable().accept(this, arg);
        n.getBody().accept(this, arg);
        return null;
    }

    public Node visit(ForStmt n, A arg) {
        if (n.getInit() != null) {
            for (Expression e : n.getInit()) {
                e.accept(this, arg);
            }
        }
        if (n.getCompare() != null) {
            n.getCompare().accept(this, arg);
        }
        if (n.getUpdate() != null) {
            for (Expression e : n.getUpdate()) {
                e.accept(this, arg);
            }
        }
        n.getBody().accept(this, arg);
        return null;
    }

    public Node visit(IfStmt n, A arg) {
        n.getCondition().accept(this, arg);
        n.getThenStmt().accept(this, arg);
        if (n.getElseStmt() != null) {
            n.getElseStmt().accept(this, arg);
        }
        return null;
    }

    public Node visit(ImportDeclaration n, A arg) {
        n.getName().accept(this, arg);
        return null;
    }

    public Node visit(InitializerDeclaration n, A arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        n.getBlock().accept(this, arg);
        return null;
    }

    public Node visit(InstanceOfExpr n, A arg) {
        n.getExpr().accept(this, arg);
        n.getType().accept(this, arg);
        return null;
    }

    public Node visit(IntegerLiteralExpr n, A arg) {
        return null;
    }

    public Node visit(IntegerLiteralMinValueExpr n, A arg) {
        return null;
    }

    public Node visit(JavadocComment n, A arg) {
        return null;
    }

    public Node visit(LabeledStmt n, A arg) {
        n.getStmt().accept(this, arg);
        return null;
    }

    public Node visit(LongLiteralExpr n, A arg) {
        return null;
    }

    public Node visit(LongLiteralMinValueExpr n, A arg) {
        return null;
    }

    public Node visit(MarkerAnnotationExpr n, A arg) {
        n.getName().accept(this, arg);
        return null;
    }

    public Node visit(MemberValuePair n, A arg) {
        n.getValue().accept(this, arg);
        return null;
    }

    public Node visit(MethodCallExpr n, A arg) {
        if (n.getScope() != null) {
            n.getScope().accept(this, arg);
        }
        if (n.getTypeArgs() != null) {
            for (Type t : n.getTypeArgs()) {
                t.accept(this, arg);
            }
        }
        if (n.getArgs() != null) {
            for (Expression e : n.getArgs()) {
                e.accept(this, arg);
            }
        }
        return null;
    }

    public Node visit(MethodDeclaration n, A arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        if (n.getAnnotations() != null) {
            for (AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        if (n.getTypeParameters() != null) {
            for (TypeParameter t : n.getTypeParameters()) {
                t.accept(this, arg);
            }
        }
        n.getType().accept(this, arg);
        if (n.getParameters() != null) {
            for (Parameter p : n.getParameters()) {
                p.accept(this, arg);
            }
        }
        if (n.getThrows() != null) {
            for (NameExpr name : n.getThrows()) {
                name.accept(this, arg);
            }
        }
        if (n.getBody() != null) {
            n.getBody().accept(this, arg);
        }
        return null;
    }

    public Node visit(NameExpr n, A arg) {
        return null;
    }

    public Node visit(NormalAnnotationExpr n, A arg) {
        n.getName().accept(this, arg);
        if (n.getPairs() != null) {
            for (MemberValuePair m : n.getPairs()) {
                m.accept(this, arg);
            }
        }
        return null;
    }

    public Node visit(NullLiteralExpr n, A arg) {
        return null;
    }

    public Node visit(ObjectCreationExpr n, A arg) {
        if (n.getScope() != null) {
            n.getScope().accept(this, arg);
        }
        if (n.getTypeArgs() != null) {
            for (Type t : n.getTypeArgs()) {
                t.accept(this, arg);
            }
        }
        n.getType().accept(this, arg);
        if (n.getArgs() != null) {
            for (Expression e : n.getArgs()) {
                e.accept(this, arg);
            }
        }
        if (n.getAnonymousClassBody() != null) {
            for (BodyDeclaration member : n.getAnonymousClassBody()) {
                member.accept(this, arg);
            }
        }
        return null;
    }

    public Node visit(PackageDeclaration n, A arg) {
        if (n.getAnnotations() != null) {
            for (AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        n.getName().accept(this, arg);
        return null;
    }

    public Node visit(Parameter n, A arg) {
        if (n.getAnnotations() != null) {
            for (AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        n.getType().accept(this, arg);
        n.getId().accept(this, arg);
        return null;
    }

    public Node visit(PrimitiveType n, A arg) {
        return null;
    }

    public Node visit(QualifiedNameExpr n, A arg) {
        n.getQualifier().accept(this, arg);
        return null;
    }

    public Node visit(ReferenceType n, A arg) {
        n.getType().accept(this, arg);
        return null;
    }

    public Node visit(ReturnStmt n, A arg) {
        if (n.getExpr() != null) {
            n.getExpr().accept(this, arg);
        }
        return null;
    }

    public Node visit(SingleMemberAnnotationExpr n, A arg) {
        n.getName().accept(this, arg);
        n.getMemberValue().accept(this, arg);
        return null;
    }

    public Node visit(StringLiteralExpr n, A arg) {
        return null;
    }

    public Node visit(SuperExpr n, A arg) {
        if (n.getClassExpr() != null) {
            n.getClassExpr().accept(this, arg);
        }
        return null;
    }

    public Node visit(SwitchEntryStmt n, A arg) {
        if (n.getLabel() != null) {
            n.getLabel().accept(this, arg);
        }
        if (n.getStmts() != null) {
            for (Statement s : n.getStmts()) {
                s.accept(this, arg);
            }
        }
        return null;
    }

    public Node visit(SwitchStmt n, A arg) {
        n.getSelector().accept(this, arg);
        if (n.getEntries() != null) {
            for (SwitchEntryStmt e : n.getEntries()) {
                e.accept(this, arg);
            }
        }
        return null;

    }

    public Node visit(SynchronizedStmt n, A arg) {
        n.getExpr().accept(this, arg);
        n.getBlock().accept(this, arg);
        return null;
    }

    public Node visit(ThisExpr n, A arg) {
        if (n.getClassExpr() != null) {
            n.getClassExpr().accept(this, arg);
        }
        return null;
    }

    public Node visit(ThrowStmt n, A arg) {
        n.getExpr().accept(this, arg);
        return null;
    }

    public Node visit(TryStmt n, A arg) {
        n.getTryBlock().accept(this, arg);
        if (n.getCatchs() != null) {
            for (CatchClause c : n.getCatchs()) {
                c.accept(this, arg);
            }
        }
        if (n.getFinallyBlock() != null) {
            n.getFinallyBlock().accept(this, arg);
        }
        return null;
    }

    public Node visit(TypeDeclarationStmt n, A arg) {
        n.getTypeDeclaration().accept(this, arg);
        return null;
    }

    public Node visit(TypeParameter n, A arg) {
        if (n.getTypeBound() != null) {
            for (ClassOrInterfaceType c : n.getTypeBound()) {
                c.accept(this, arg);
            }
        }
        return null;
    }

    public Node visit(UnaryExpr n, A arg) {
        n.getExpr().accept(this, arg);
        return null;
    }

    public Node visit(VariableDeclarationExpr n, A arg) {
        if (n.getAnnotations() != null) {
            for (AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        n.getType().accept(this, arg);
        for (VariableDeclarator v : n.getVars()) {
            v.accept(this, arg);
        }
        return null;
    }

    public Node visit(VariableDeclarator n, A arg) {
        n.getId().accept(this, arg);
        if (n.getInit() != null) {
            n.getInit().accept(this, arg);
        }
        return null;
    }

    public Node visit(VariableDeclaratorId n, A arg) {
        return null;
    }

    public Node visit(VoidType n, A arg) {
        return null;
    }

    public Node visit(WhileStmt n, A arg) {
        n.getCondition().accept(this, arg);
        n.getBody().accept(this, arg);
        return null;
    }

    public Node visit(WildcardType n, A arg) {
        if (n.getExtends() != null) {
            n.getExtends().accept(this, arg);
        }
        if (n.getSuper() != null) {
            n.getSuper().accept(this, arg);
        }
        return null;
    }

    public Node visit(BlockComment n, A arg) {
        return null;
    }

    public Node visit(LineComment n, A arg) {
        return null;
    }

}
