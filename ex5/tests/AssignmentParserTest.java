package ex5.tests;

import ex5.parsing.AssignmentParser;
import ex5.scope_managing.SymbolTable;
import ex5.scope_managing.SymbolTableException;
import ex5.util.Constants.AssignmentStatus;
import ex5.util.Constants.VariableType;

public class AssignmentParserTest {

    static String[] validAssignments = {
            "x = 5;",                     // Assign integer value
            "y = 3.14;",                  // Assign double value
            "text = \"Hello World!\";",   // Assign string value
            "flag = true;",               // Assign boolean value
            "letter = 'Z';",              // Assign char value
            "x = x;",                     // Assign variable to itself
            "y = x;",                     // Assign int variable to double (widening conversion)
            "flag = flag;",               // Assign boolean variable to itself
            "flag = 3.14;",               // assign boolean variable to boolean
            "text = text;",               // Assign string variable to itself
            "letter = 'A';",              // Reassign to char variable
            "y = finalX;",                // Assign final int to double
            "flag = 1;",        // Valid boolean assignment from int (1 = true)
    };


    static String[] invalidAssignments = {
            "z = 10;",                    // Variable 'z' is not declared
            "x = \"string\";",            // Type mismatch: int variable assigned string
            "y = true;",                  // Type mismatch: double variable assigned boolean
            "text = 42;",                 // Type mismatch: string variable assigned int
            "letter = \"A\";",            // Type mismatch: char variable assigned string
            "x = finalY;",                // Type mismatch: assigning double to int
            "x = flag;",                  // Type mismatch: boolean variable assigned to int
            "y = letter;",                // Type mismatch: double variable assigned char
            "letter = letter + 1;",       // Invalid char operation
            "flag = finalX + 1;",         // Invalid boolean operation with final variable
            "text = x;",                  // Type mismatch: string variable assigned int
            "finalX = x + 1;"             // Invalid reassignment of final variable
    };

    static String[] invalidFinalAssignments = {
            "finalX = 10;",               // Final variable cannot be reassigned
            "finalY = 3.14;",             // Final variable cannot be reassigned
            "finalText = \"World\";",     // Final variable cannot be reassigned
            "finalFlag = false;",         // Final variable cannot be reassigned
            "finalLetter = 'B';",         // Final variable cannot be reassigned
            "finalText = finalX;",        // Type mismatch: final int to string
            "finalX = finalX + 1;"        // Reassigning a final variable with itself
    };

    public static void runTests() {
        System.out.println("Running Assignment Parser Tests...");
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.addScope();
        AssignmentParser assignmentParser = new AssignmentParser(symbolTable);

        // Setup variables in symbol table
        try {
            // Add non-final variables
            symbolTable.addVarToScope("x", VariableType.INT, AssignmentStatus.DECLARED, false);
            symbolTable.addVarToScope("y", VariableType.DOUBLE, AssignmentStatus.DECLARED, false);
            symbolTable.addVarToScope("text", VariableType.STRING, AssignmentStatus.DECLARED, false);
            symbolTable.addVarToScope("flag", VariableType.BOOLEAN, AssignmentStatus.DECLARED, false);
            symbolTable.addVarToScope("letter", VariableType.CHAR, AssignmentStatus.DECLARED, false);

            // Add final variables
            symbolTable.addVarToScope("finalX", VariableType.INT, AssignmentStatus.ASSIGNED_THIS_SCOPE, true);
            symbolTable.addVarToScope("finalY", VariableType.DOUBLE, AssignmentStatus.ASSIGNED_THIS_SCOPE, true);
            symbolTable.addVarToScope("finalText", VariableType.STRING, AssignmentStatus.ASSIGNED_THIS_SCOPE, true);
            symbolTable.addVarToScope("finalFlag", VariableType.BOOLEAN, AssignmentStatus.ASSIGNED_THIS_SCOPE, true);
            symbolTable.addVarToScope("finalLetter", VariableType.CHAR, AssignmentStatus.ASSIGNED_THIS_SCOPE, true);
        } catch (SymbolTableException e) {
            System.out.println("Error setting up symbol table: " + e.getMessage());
        }

        int passed = 0, failed = 0;

        // Test valid assignments
        for (String assignment : validAssignments) {
            try {
                assignmentParser.parse(assignment);
//                System.out.println("Test passed: " + assignment + " is valid.");
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
//                System.out.println("Test passed: " + assignment + " is invalid.");
                passed++;
            }
        }

        // Test invalid final assignments
        for (String assignment : invalidFinalAssignments) {
            try {
                assignmentParser.parse(assignment);
                System.out.println("Test failed: " + assignment + " should be invalid (final variable).");
                failed++;
            } catch (Exception e) {
//                System.out.println("Test passed: " + assignment + " is invalid (final variable).");
                passed++;
            }
        }

        // Print results
        System.out.println("\nAssignment Parser Test Results:");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed + "\n");
    }


}
