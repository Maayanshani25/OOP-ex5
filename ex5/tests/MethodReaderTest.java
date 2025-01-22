package ex5.tests;

import ex5.parsing.MethodReader;
import ex5.parsing.MethodParser;
import ex5.parsing.ParserException;
import ex5.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodReaderTest {

    public static void main(String[] args) {
        testValidMethodDeclaration();
        testInvalidMethodDeclaration();
        testMissingReturnLine();
        testNestedBrackets();
    }

    private static void testValidMethodDeclaration() {
        try {
            List<String> lines = new ArrayList<>();
            lines.add("void validMethod(int a, double b) {");
            lines.add("    int x = 10;");
            lines.add("    return;");
            lines.add("}");

            Map<String, ArrayList<Constants.VariableType>> methods = MethodReader.readMethods(lines);
            if (methods.containsKey("validMethod") &&
                    methods.get("validMethod").size() == 2 &&
                    methods.get("validMethod").get(0) == Constants.VariableType.INT &&
                    methods.get("validMethod").get(1) == Constants.VariableType.DOUBLE) {
                System.out.println("testValidMethodDeclaration: PASS");
            } else {
                System.out.println("testValidMethodDeclaration: FAIL");
            }
        } catch (Exception e) {
            System.out.println("testValidMethodDeclaration: FAIL - Unexpected exception: " + e.getMessage());
        }
    }

    private static void testInvalidMethodDeclaration() {
        try {
            List<String> lines = new ArrayList<>();
            lines.add("void invalidMethod(int a string b) {");
            lines.add("    return;");
            lines.add("}");

            MethodReader.readMethods(lines);
            System.out.println("testInvalidMethodDeclaration: FAIL - Expected ParserException was not thrown.");
        } catch (ParserException e) {
            System.out.println("testInvalidMethodDeclaration: PASS - Caught expected exception: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("testInvalidMethodDeclaration: FAIL - Unexpected exception: " + e.getMessage());
        }
    }

    private static void testMissingReturnLine() {
        try {
            List<String> lines = new ArrayList<>();
            lines.add("void noReturnMethod(int a) {");
            lines.add("    int x = 20;");
            lines.add("}");

            MethodReader.readMethods(lines);
            System.out.println("testMissingReturnLine: FAIL - Expected ParserException was not thrown.");
        } catch (ParserException e) {
            System.out.println("testMissingReturnLine: PASS - Caught expected exception: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("testMissingReturnLine: FAIL - Unexpected exception: " + e.getMessage());
        }
    }

    private static void testNestedBrackets() {
        try {
            List<String> lines = new ArrayList<>();
            lines.add("void nestedBracketsMethod(int a) {");
            lines.add("    if (true) {");
            lines.add("        while (false) {");
            lines.add("            // some logic");
            lines.add("        }");
            lines.add("    }");
            lines.add("    return;");
            lines.add("}");

            MethodReader.readMethods(lines);
            System.out.println("testNestedBrackets: PASS");
        } catch (Exception e) {
            System.out.println("testNestedBrackets: FAIL - Unexpected exception: " + e.getMessage());
        }
    }
}
