package ex5.tests;


import ex5.parsing.IfAndWhileParser;
import ex5.scope_managing.ScopeManager;
import ex5.scope_managing.SymbolTable;
import ex5.scope_managing.SymbolTableException;
import ex5.util.Constants;
import ex5.util.Constants.AssignmentStatus;
import ex5.util.Constants.VariableType;

public class WhileParserTest {
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable();
        ScopeManager scopeManager = new ScopeManager(symbolTable);
        IfAndWhileParser whileParser = new IfAndWhileParser(symbolTable, scopeManager);

        try {
            // Add initial scope
            scopeManager.enterNewScope(Constants.ScopeKind.METHOD);
            symbolTable.addScope();

            // Test 1: Valid condition using boolean literal
            String validTrueCondition = "if (true) {";
            System.out.println("Test 1: " + testWhileParser(validTrueCondition, whileParser));

            // Test 2: Valid condition using boolean variable
            symbolTable.addVarToScope("a", VariableType.BOOLEAN, AssignmentStatus.ASSIGNED, false);
            String validBooleanVariable = "if (a) {";
            System.out.println("Test 2: " + testWhileParser(validBooleanVariable, whileParser));

            // Test 3: Invalid condition with undeclared variable (should fail and catch exception)
            String invalidCondition = "if (c) {";
            try {
                whileParser.parse(invalidCondition);
                System.out.println("Test 3: Failed (Expected exception)");
            } catch (Exception e) {
                System.out.println("Test 3: Passed (Caught exception: " + e.getMessage() + ")");
            }

            // Test 4: Valid OR condition with boolean variables
            symbolTable.addVarToScope("b", VariableType.BOOLEAN, AssignmentStatus.ASSIGNED, false);
            String validOrCondition = "if (a || b) {";
            System.out.println("Test 4: " + testWhileParser(validOrCondition, whileParser));

            // Test 5: Valid complex OR/AND condition with more than three subconditions
            String validComplexCondition = "if (a || b && true) {";
            System.out.println("Test 5: " + testWhileParser(validComplexCondition, whileParser));

            // Test 6: Invalid syntax (missing parentheses)
            String invalidSyntax = "if a || b {";
            try {
                whileParser.parse(invalidSyntax);
                System.out.println("Test 6: Failed (Expected exception)");
            } catch (Exception e) {
                System.out.println("Test 6: Passed (Caught exception: " + e.getMessage() + ")");
            }

            // Test 7: Invalid syntax (extra characters)
            String invalidSyntaxExtraChars = "if (a || b) extra {";
            try {
                whileParser.parse(invalidSyntaxExtraChars);
                System.out.println("Test 7: Failed (Expected exception)");
            } catch (Exception e) {
                System.out.println("Test 7: Passed (Caught exception: " + e.getMessage() + ")");
            }

            // Test 8: Valid true condition in a deeper OR chain
            String validDeepTrueCondition = "if (true || a || b) {";
            System.out.println("Test 8: " + testWhileParser(validDeepTrueCondition, whileParser));

            // Test 9: Invalid condition with an uninitialized variable
            symbolTable.addVarToScope("d", VariableType.BOOLEAN, AssignmentStatus.DECLARED, false);
            String invalidUninitializedVariable = "if (d) {";
            try {
                whileParser.parse(invalidUninitializedVariable);
                System.out.println("Test 9: Failed (Expected exception)");
            } catch (Exception e) {
                System.out.println("Test 9: Passed (Caught exception: " + e.getMessage() + ")");
            }

            // Test 10: Valid nested AND/OR condition with both variables and literals
            String validNestedCondition = "if (a && b || true && a || a || b && false) {";
            System.out.println("Test 10: " + testWhileParser(validNestedCondition, whileParser));

            // Test 11: Invalid boolean comparison (a == b)
            String invalidComparison = "if (a == b) {";
            try {
                whileParser.parse(invalidComparison);
                System.out.println("Test 11: Failed (Expected exception)");
            } catch (Exception e) {
                System.out.println("Test 11: Passed (Caught exception: " + e.getMessage() + ")");
            }

            // Test 12: Invalid condition with unbalanced parentheses
            String invalidUnbalancedParentheses = "if ((a || b) {";
            try {
                whileParser.parse(invalidUnbalancedParentheses);
                System.out.println("Test 12: Failed (Expected exception)");
            } catch (Exception e) {
                System.out.println("Test 12: Passed (Caught exception: " + e.getMessage() + ")");
            }

            // Test 13: Valid condition with boolean literal "false"
            String validFalseCondition = "if (false) {";
            System.out.println("Test 13: " + testWhileParser(validFalseCondition, whileParser));

            // Test 14: Valid condition with both true and false in OR chain
            String validTrueFalseCondition = "if (true || false) {";
            System.out.println("Test 14: " + testWhileParser(validTrueFalseCondition, whileParser));

            // Test 15: Invalid syntax with incorrect boolean literal (TRUE instead of true)
            String invalidTrueLiteralCondition = "if (TRUE) {";
            try {
                whileParser.parse(invalidTrueLiteralCondition);
                System.out.println("Test 15: Failed (Expected exception)");
            } catch (Exception e) {
                System.out.println("Test 15: Passed (Caught exception: " + e.getMessage() + ")");
            }

        } catch (SymbolTableException e) {
            System.out.println("SymbolTable error: " + e.getMessage());
        }
    }

    private static String testWhileParser(String line, IfAndWhileParser whileParser) {
        try {
            whileParser.parse(line);
            return "Passed";
        } catch (Exception e) {
            return "Failed: " + e.getMessage();
        }
    }
}
