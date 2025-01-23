package ex5.tests;

import ex5.parsing.AssignmentParser;
import ex5.scope_managing.SymbolTable;
import ex5.scope_managing.SymbolTableException;
import ex5.util.Constants.VariableType;
import ex5.util.Constants.AssignmentStatus;

public class AssignmentParserTest {

    static String[] validAssignments = {
            "x = 5;",                     // Assign integer value
            "y = 3.14;",                  // Assign double value
            "text = \"Hello\";",          // Assign string value
            "flag = true;",               // Assign boolean value
            "letter = 'A';",              // Assign char value
            "x = x;",                     // Assign variable to itself
            "y = x;",                     // Assign int variable to double (valid widening conversion)
            "flag = flag;"                // Assign boolean variable to itself
    };

    static String[] invalidAssignments = {
            "z = 5;",                     // Variable 'z' is not declared
            "x = \"text\";",              // Type mismatch: int variable assigned string
            "y = true;",                  // Type mismatch: double variable assigned boolean
            "text = 5;",                  // Type mismatch: string variable assigned int
            "flag = 'A';",                // Type mismatch: boolean variable assigned char
            "letter = 3.14;",             // Type mismatch: char variable assigned double
            "x = z;",                     // Variable 'z' is not declared
            "y = letter;"                 // Type mismatch: double variable assigned char
    };

    public static void runTests() {
        System.out.println("Running tests for AssignmentParser...");
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.addScope();
        AssignmentParser assignmentParser = new AssignmentParser(symbolTable);

        // Add variables to the symbol table for valid assignments
        try {
            symbolTable.addVarToScope("x", VariableType.INT, AssignmentStatus.DECLARED, false);
            symbolTable.addVarToScope("y", VariableType.DOUBLE, AssignmentStatus.DECLARED, false);
            symbolTable.addVarToScope("text", VariableType.STRING, AssignmentStatus.DECLARED, false);
            symbolTable.addVarToScope("flag", VariableType.BOOLEAN, AssignmentStatus.DECLARED, false);
            symbolTable.addVarToScope("letter", VariableType.CHAR, AssignmentStatus.DECLARED, false);
        } catch (SymbolTableException e) {
            System.out.println("Error setting up symbol table: " + e.getMessage());
        }

        int passed = 0;
        int failed = 0;

        // Test valid assignments
        for (String assignment : validAssignments) {
            try {
                assignmentParser.parse(assignment);
                passed++;
            } catch (Exception e) {
                System.out.println("Test failed: " + assignment + " should be valid.");
                System.out.println("Error: " + e.getMessage());
                failed++;
            }
        }

        // Test invalid assignments
        for (String assignment : invalidAssignments) {
            try {
                assignmentParser.parse(assignment);
                System.out.println("Test failed: " + assignment + " should be invalid.");
                failed++;
            } catch (Exception e) {
                passed++;
            }
        }

        // Print summary
        System.out.println("Assignment Parser Test Results:");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed + "\n");
    }
}
