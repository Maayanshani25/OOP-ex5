//package ex5.tests;
//
//import ex5.scope_managing.SymbolTable;
//import ex5.util.Constants;
//import ex5.scope_managing.SymbolTableException;
//
//public class SymbolTableTest {
//
//    public static void runTests() {
//        System.out.println("Running SymbolTable tests...");
//
//        testAddScope();
//        testAddVariableToScope();
//        testDuplicateVariableError();
//
//        System.out.println("All SymbolTable tests completed.\n");
//    }
//
//    private static void testAddScope() {
//        try {
//            SymbolTable symbolTable = new SymbolTable();
//            symbolTable.addScope();
//            System.out.println("testAddScope passed");
//        } catch (Exception e) {
//            System.out.println("testAddScope failed: " + e.getMessage());
//        }
//    }
//
//    private static void testAddVariableToScope() {
//        try {
//            SymbolTable symbolTable = new SymbolTable();
//            symbolTable.addScope();
//            symbolTable.addVarToScope("x", Constants.VariableType.INT);
//            System.out.println("testAddVariableToScope passed");
//        } catch (Exception e) {
//            System.out.println("testAddVariableToScope failed: " + e.getMessage());
//        }
//    }
//
//    private static void testDuplicateVariableError() {
//        try {
//            SymbolTable symbolTable = new SymbolTable();
//            symbolTable.addScope();
//            symbolTable.addVarToScope("x", Constants.VariableType.INT);
//            symbolTable.addVarToScope("x", Constants.VariableType.INT); // Should fail
//            System.out.println("testDuplicateVariableError failed (no exception thrown)");
//        } catch (SymbolTableException e) {
//            System.out.println("testDuplicateVariableError passed: " + e.getMessage());
//        } catch (Exception e) {
//            System.out.println("testDuplicateVariableError failed: Unexpected exception: " + e.getMessage());
//        }
//    }
//}
