package ex5.tests;

import ex5.parsing.MethodParser;
import ex5.parsing.ParserException;
import ex5.scope_managing.ScopeManager;
import ex5.scope_managing.SymbolTable;
import ex5.scope_managing.SymbolTableException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static ex5.util.Constants.ScopeKind;
import static ex5.util.Constants.VariableType;

public class MethodParserTest {
    public static void main(String[] args) {
        try {
            SymbolTable symbolTable = new SymbolTable();
            ScopeManager scopeManager = new ScopeManager(symbolTable);
            Map<String, ArrayList<VariableType>> methods = new HashMap<>();
            MethodParser parser = new MethodParser(symbolTable, scopeManager, methods);

            System.out.println("Running tests for MethodParser...");

            testMethodDeclaration(parser);
            testMethodCall(parser);

            System.out.println("All tests passed!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testMethodDeclaration(MethodParser parser)
            throws ParserException, SymbolTableException {
        SymbolTable symbolTable = new SymbolTable();
        ScopeManager scopeManager = new ScopeManager(symbolTable);
        Map<String, ArrayList<VariableType>> methods = new HashMap<>();

        // Test 1: Valid method declaration
        parser.addToMethodMap("void myMethod(int param1, double param2) {", methods);
        System.out.println("Test 1 Passed: Valid method declaration");

        // Test 2: Duplicate method name
        try {
            parser.addToMethodMap("void myMethod(int param1, double param2) {", methods);
            throw new AssertionError("Test 2 Failed: Duplicate method name not detected");
        } catch (ParserException e) {
            System.out.println("Test 2 Passed: Duplicate method name detected");
        }

//        // Test 3: Method declaration in another method scope
//        scopeManager.enterNewScope(ScopeKind.METHOD);
//        try {
//            parser.addToMethodMap("void innerMethod(int param) {");
//            throw new AssertionError("Test 3 Failed: Method declaration inside method not detected");
//        } catch (ParserException e) {
//            System.out.println("Test 3 Passed: Method declaration inside method scope detected");
//        }
//        try {
//            scopeManager.exitScope();
//        } catch (ScopeManagerException e) {
//            System.out.println("!Scope manager exception detected");
//        }

        // Test 4: Invalid parameter names
        try {
            parser.addToMethodMap("void badParams(int param1, double 123bad) {", methods);
            throw new AssertionError("Test 4 Failed: Invalid parameter names not detected");
        } catch (ParserException e) {
            System.out.println("Test 4 Passed: Invalid parameter names detected");
        }

//        // Test 5: Ensure ends with return and }
//        try {
//            parser.parse("void methodWithoutReturn(int param1) { int x = 1; }");
//            throw new AssertionError("Test 5 Failed: Missing return statement not detected");
//        } catch (ParserException e) {
//            System.out.println("Test 5 Passed: Missing return statement detected");
//        }
    }

    private static void testMethodCall(MethodParser parserr) throws ParserException, SymbolTableException {
        SymbolTable symbolTable = new SymbolTable();
        ScopeManager scopeManager = new ScopeManager(symbolTable);
        scopeManager.enterNewScope(ScopeKind.METHOD);
        Map<String, ArrayList<VariableType>> methods = new HashMap<>();
        MethodParser parser = new MethodParser(symbolTable, scopeManager, methods);

        // Add method without parameters to map for call testing
        ArrayList<VariableType> emptyParamTypes = new ArrayList<>();
        methods.put("testEmptyMethod", emptyParamTypes);


        // Test 5: empty parameters list
        try {
            parser.parse("testEmptyMethod(123, true);");
            throw new AssertionError("Test 5 Failed: Invalid num of parameters not detected");
        } catch (ParserException e) {
            System.out.println("Test 5 Passed: Invalid num of parameters detected");
        }

        // Test 5.5: empty parameters list
        parser.parse("testEmptyMethod();");
        System.out.println("Test 5.5 Passed: Valid empty method call");


        // Add method to method map for call testing
        ArrayList<VariableType> paramTypes = new ArrayList<>();
        paramTypes.add(VariableType.INT);
        paramTypes.add(VariableType.BOOLEAN);
        methods.put("testMethod", paramTypes);


        // Test 6: Valid method call
        parser.parse("testMethod(123, true);");
        System.out.println("Test 6 Passed: Valid method call");

        // Test 7: Invalid method name
        try {
            parser.parse("nonExistentMethod(123, true);");
            throw new AssertionError("Test 7 Failed: Invalid method name not detected");
        } catch (ParserException e) {
            System.out.println("Test 7 Passed: Invalid method name detected");
        }

        // Test 8: Parameter count mismatch
        try {
            parser.parse("testMethod(123);");
            throw new AssertionError("Test 8 Failed: Parameter count mismatch not detected");
        } catch (ParserException e) {
            System.out.println("Test 8 Passed: Parameter count mismatch detected");
        }

        // Test 9: Invalid parameter type
        try {
            parser.parse("testMethod(123, \"string\");");
            throw new AssertionError("Test 9 Failed: Invalid parameter type not detected");
        } catch (ParserException e) {
            System.out.println("Test 9 Passed: Invalid parameter type detected");
        }


    }
}
