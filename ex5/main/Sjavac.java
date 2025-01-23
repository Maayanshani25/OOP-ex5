package ex5.main;

import ex5.FileReader;
import ex5.Validator;
import ex5.parsing.ParserException;
import ex5.parsing.MethodReader;
import ex5.scope_managing.ScopeManager;
import ex5.scope_managing.ScopeManagerException;
import ex5.scope_managing.SymbolTable;
import ex5.scope_managing.SymbolTableException;
import ex5.tests.AssignmentParserTest;
import ex5.tests.DeclarationParserTest;
import ex5.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sjavac {
    private final List<String> preprocessedLines;
    private Map<String, ArrayList<Constants.VariableType>> methodsMap;
    private final SymbolTable symbolTable;
    private Validator validator;
    private ScopeManager scopeManager;

    // TODO: to make sure constructor can throw IOError
    public Sjavac(String filePath) throws IOException {
        // Read and pre-process the file:
        List<String> allLines = FileReader.readLines(filePath);
        preprocessedLines = FileReader.preProcessLines(allLines);
        methodsMap = new HashMap<>();
        symbolTable = new SymbolTable();
        scopeManager = new ScopeManager();

    }

    public boolean run() throws IOException{
        try {
            methodsMap = MethodReader.readMethods(preprocessedLines);
            validator = new Validator(symbolTable, scopeManager, methodsMap);

            // For every line, check if valid
            for (String line : preprocessedLines) {
                if (!validator.isValidLine(line)) { // Use the validator instance
                    return false;
                }
                symbolTable.updateVarsStatus();
            }
        } catch (ParserException | SymbolTableException | ScopeManagerException e) {
            System.err.println(e.getMessage()); // Log the exception (optional)
            return false; // Return false if any exception occurs
        }

        // Check there are no un-closed scopes:
        if (!scopeManager.isScopeDequeEmpty()) {
            return false;
        }
        return true;
    }

    // TODO: make sure handling IOExceptions correctly
    public static void main(String[] args) throws IOException {
        // TODO: if the args num is invalid, filename is wrong, or file is not sJava - throw IOException
        //  (for now, being catch in the FileReader)

        try {
            if (args.length != 1) {
                throw new IOException();
            }

            String source_file_name = args[0];
            Sjavac sjavac = new Sjavac(source_file_name);
            if (sjavac.run()) {
                // the file is good
                System.out.println(0);
            } else {
                // the format is not good
                System.out.println(1);
            }
        } catch (IOException e) {
            // IO error
            System.out.println(2);
        }
    }

    // main function for tests
//    public static void main(String[] args) {
//        System.out.println("Running all tests...\n");
//
//        // Call runTests for each test class
////        SymbolTableTest.runTests();
//        DeclarationParserTest.runTests();
//        AssignmentParserTest.runTests();
//
//        System.out.println("All tests completed.");
//    }

}
