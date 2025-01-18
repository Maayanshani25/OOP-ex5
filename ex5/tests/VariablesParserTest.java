package ex5.tests;


import static ex5.util.Constants.VARIABLE_NAME_REGEX;

public class VariablesParserTest {


    public static void runTests() {
        System.out.println("Running VariablesParser tests...");
        testVariableNames();
        System.out.println("All VariablesParser tests completed.\n");
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

