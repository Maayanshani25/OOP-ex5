package ex5.tests;

/** todo: 1. seperate declaration and assignment classes
 *        2. all in regex
 */

import ex5.parsing.DeclarationParser;
import ex5.scope_managing.SymbolTable;

import static ex5.util.Constants.VARIABLE_NAME_REGEX;

public class VariablesParserTest {

    static String[] validIntDeclarations = {
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

    static String[] invalidIntDeclarations = {
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

    static String[] validDoubleDeclarations = {
            "double pi = 3.14;",                  // Single variable with initialization
            "double e = 2.718, g = 9.81;",        // Multiple variables with initialization
            "double _underscore;",                // Variable name starting with an underscore
            "double _a, b_c, _123;",              // Variables with underscores and digits
            "double x = +1.0, y = 0.0;",          // Positive and zero values
            "double d1, d2 = 0.001, d3 = 42.0;",  // Variables with digits in names
            "double m1 = -5.5, n2 = +10.5;",      // Negative and positive signs
            "double a = 0.0, b = 0.0, c = 0.0;"   // Multiple variables, all initialized to zero
    };

    static String[] invalidDoubleDeclarations = {
            "double 1.5;",                       // Variable name cannot start with a digit
            "double 2.1, 3.3;",                  // Variable names cannot be numbers
            "double a,, b;",                     // Double commas
            "double a =;",                       // Assignment without a value
            "double a = 3.14.15;",               // Invalid value (double decimal points)
            "double a = 'c';",                   // Incorrect type initialization
            "double a = true;",                  // Incorrect type initialization
            "double @invalid;",                  // Invalid character in variable name
            "double ,a;",                        // Comma without variable
            "double a"                          // Missing semicolon
    };

    static String[] validStringDeclarations = {
            "String name = \"Alice\";",                   // Single variable with initialization
            "String greeting = \"Hello World!\";",       // Single variable with special characters
            "String empty = \"\";",                       // Single variable initialized as empty
            "String a, b = \"Hi\", c;",                   // Mixed initialization
            "String _underscore;",                        // Variable name starting with an underscore
            "String x, y = \"value\", z = \"test\";",     // Multiple variables with initialization
            "String m1, n2, o3 = \"alpha\";",             // Variables with alphanumeric names
            "String aaaa = \"This is a test\", bbbbb = \"Another test\", cccc;", // Complex initialization
            "String hashtag = \"i%#\";",                // variable from the table in ex5
    };

    static String[] invalidStringDeclarations = {
            "String 123name;",                            // Variable name cannot start with a digit
            "String name = Alice;",                       // Missing double quotes around value
            "String a = \"Unclosed string;",              // Unclosed string literal
            "String a,, b;",                              // Double commas
            "String ;",                                   // Missing variable name
            "String a b;",                                // Missing comma between variables
            "String a = \"value\", b,;",                  // Trailing comma
            "String a = 123;",                            // Incorrect type initialization
            "String @invalid;",                           // Invalid character in variable name
            "String ,a;",                                 // Comma without variable name
            "String hashtag = \"i%#\"",                  // No semicolon
    };



    public static void runTests() {
        System.out.println("Running VariablesParser tests...");
        testVariableNames();
        System.out.println("All VariablesParser tests completed.\n");
        System.out.println("Running DeclarationParser tests...");
        System.out.println("Running Int Declaration tests...");
        testIntDeclaration();
        System.out.println("Running Double Declaration tests...");
        testDoubleDeclaration();
        System.out.println("Running String Declaration tests...");
        testStringDeclaration();
        System.out.println("All DeclarationParser tests completed.\n");
    }

    static void testIntDeclaration() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.addScope();
        DeclarationParser declarationParser = new DeclarationParser(symbolTable);


        int passed = 0;
        int failed = 0;

        // Test valid declarations
        for (String declaration : validIntDeclarations) {
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
        for (String declaration : invalidIntDeclarations) {
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

    static void testDoubleDeclaration() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.addScope(); // Add a scope to the symbol table
        DeclarationParser declarationParser = new DeclarationParser(symbolTable);

        int passed = 0;
        int failed = 0;

        // Test valid declarations
        for (String declaration : validDoubleDeclarations) {
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
        for (String declaration : invalidDoubleDeclarations) {
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

    static void testStringDeclaration() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.addScope(); // Add a scope to the symbol table
        DeclarationParser declarationParser = new DeclarationParser(symbolTable);

        int passed = 0;
        int failed = 0;

        // Test valid declarations
        for (String declaration : validStringDeclarations) {
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
        for (String declaration : invalidStringDeclarations) {
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
        // Define valid and invalid variable names
        String[] validNames = {"g2", "b_3", "_a", "_0", "a_"};
        String[] invalidNames = {"2g", "_", "2__", "54_a", "__", "___b"};

        System.out.println("Running Variable Name Tests...");

        int passed = 0;
        int failed = 0;

        // Test valid names
        for (String name : validNames) {
            if (name.matches(VARIABLE_NAME_REGEX)) {
//                System.out.println("✔ Test passed: \"" + name + "\" is valid.");
                passed++;
            } else {
                System.out.println("✘ Test failed: \"" + name + "\" should be valid.");
                failed++;
            }
        }

        // Test invalid names
        for (String name : invalidNames) {
            if (!name.matches(VARIABLE_NAME_REGEX)) {
//                System.out.println("✔ Test passed: \"" + name + "\" is invalid.");
                passed++;
            } else {
                System.out.println("✘ Test failed: \"" + name + "\" should be invalid.");
                failed++;
            }
        }

        // Print summary
        System.out.println("\nTest Results:");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
    }

}

