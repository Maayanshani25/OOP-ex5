package ex5.tests;

/** todo: 1. seperate declaration and assignment classes
 *        2. all in regex
 */

import ex5.parsing.DeclarationParser;
import ex5.scope_managing.SymbolTable;

import static ex5.util.Constants.VARIABLE_NAME_REGEX;

public class VariablesParserTest {


    public static void runTests() {
//        System.out.println("Running VariablesParser tests...");
//        testVariableNames();
//        System.out.println("All VariablesParser tests completed.\n");
        System.out.println("Running DeclarationParser tests...");
        testIntDeclaration();
        System.out.println("All DeclarationParser tests completed.\n");
    }

    static void testIntDeclaration() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.addScope();
        DeclarationParser declarationParser = new DeclarationParser(symbolTable);

        String[] validDeclarations = {
//                "int a;",                        // Single variable, no initialization
//                "int b, c, d;",                  // Multiple variables, no initialization
                "int e = 5;",                    // Single variable, with initialization
                "int f = 3, g = 4, h = 5;",      // Multiple variables, with initialization
                "int _underscore;",              // Variable name starting with an underscore
                "int _a, b_c, _123;",            // Variables with underscores and digits
//                "int a = -1;",                   // Single variable, negative integer
                "int x = +2, y = 0;",            // Single variable, positive and zero integers
//                "int a = 1234567890;",           // Large integer
                "int f1, g1 = 42, h2 = 100;",    // Variables with digits in names
//                "int a, b = 3, c;",              // Mixed initialization
                "int i, j = -10, k = +15;",      // Mixed signs in initialization
                "int m1, n2, o3 = 3, p4;",       // Variables with alphanumeric names
                "int a = 0, b = 0, c = 0;"       // Multiple variables, all initialized to zero
        };

        String[] invalidDeclarations = {
                "int 1;",                        // Variable name cannot start with a digit
                "int 2, 3, 4;",                  // Variable names cannot be numbers
                "int 5 = 5;",                    // Variable name cannot start with a digit
                "int 6 = 3, 7 = 4, 8 = 5;",      // Variable names cannot be numbers
                "int ;",                         // Missing variable name
                "int a,, b;",                    // Double commas
                "int a =;",                      // Assignment without a value
                "int a = 1.5;",                  // Non-integer value for an integer type
                "int a b;",                      // Missing comma between variables
                "int a = 1, , b = 2;",           // Double commas in the middle
                "int a = 1, b,;",                // Trailing comma
                "int a = 3 + 2;",                // Expressions are not allowed
                "int a = , b;",                  // Empty assignment
                "int a = 'c';",                  // Incorrect type initialization
                "int a = true;",                 // Incorrect type initialization
                "int a = \"hello\";",            // Incorrect type initialization
                "int @invalid;",                 // Invalid character in variable name
                "int a b = 5;",                  // Missing comma
                "int a = , b = 2;",              // Missing value for one variable
                "int a b;",                      // Missing comma between variable names
                "int ,;",                        // Comma without variables
                "int a,,b;"                      // Double commas between variables
        };


        int passed = 0;
        int failed = 0;

        // Test valid declarations
        for (String declaration : validDeclarations) {
            try {
                declarationParser.parse(declaration);
//                System.out.println("Test passed: " + declaration + " is valid.");
                passed++;
            } catch (Exception e) {
                System.out.println("Test failed: " + declaration + " should be valid.");
                System.out.println("Error: " + e.getMessage());
                failed++;
            }
        }

        // Test invalid declarations
        for (String declaration : invalidDeclarations) {
            try {
                declarationParser.parse(declaration);
                System.out.println("Test failed: " + declaration + " should be invalid.");
                failed++;
            } catch (Exception e) {
//                System.out.println("Test passed: " + declaration + " is invalid.");
                passed++;
            }
        }

        // Print summary
        System.out.println("\nTest Results:");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
    }


    static void testVariableNames() {

        String[] validNames = {"g2", "b_3", "_a", "_0", "a_"};
        String[] invalidNames = {"2g", "_", "2__", "54_a", "__", "___b"};

        System.out.println("Running Variable Name Tests...");

        // Test valid names
        for (String name : validNames) {
            if (!name.matches(VARIABLE_NAME_REGEX)) {
                System.out.println("Test failed: " + name + " should be valid.");
            } else {
                System.out.println("Test passed: " + name + " is valid.");
            }
        }

        // Test invalid names
        for (String name : invalidNames) {
            if (name.matches(VARIABLE_NAME_REGEX)) {
                System.out.println("Test failed: " + name + " should be invalid.");
            } else {
                System.out.println("Test passed: " + name + " is invalid.");
            }
        }

        System.out.println("All tests completed.");
    }
}

