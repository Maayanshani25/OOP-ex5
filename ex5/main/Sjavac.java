package ex5.main;

import ex5.FileReader;
import ex5.Validator;
import ex5.parsing.ParserException;
import ex5.parsing.MethodReader;
import ex5.scope_managing.ScopeManager;
import ex5.scope_managing.SymbolTable;
import ex5.tests.DeclarationParserTest;
import ex5.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sjavac {
    private final List<String> preprocessedLines;
    private Map<String, ArrayList<Constants.VariableType>> methodsMap;
    private final SymbolTable symbolTable;
    // TODO: needed? or Validator methods are static?
    private Validator validator;
    private ScopeManager scopeManager;

    public Sjavac(String filePath) {
        // Read and pre-process the file:
        List<String> allLines = FileReader.readLines(filePath);
        preprocessedLines = FileReader.preProcessLines(allLines);
        methodsMap = new HashMap<>();
        symbolTable = new SymbolTable();
        validator = new Validator(symbolTable);
        scopeManager = new ScopeManager();
    }

    public boolean run() {
        try {
            methodsMap = MethodReader.readMethods(preprocessedLines);
        }
        // TODO: check how to catch exceptions
        catch (ParserException e) {
//            System.err(e.getMessage());
        }

        // TODO: wrong implement, needed to be changed after implementing Validator
        // For every line, check if valid
        for (String line : preprocessedLines) {
            if (!Validator.isValidLine(line)) {
                return false;
            }
        }
        // Check there are no un-closed scopes:
        if (!scopeManager.isScopeDequeEmpty()) {
            return false;
        }

        return true;
    }

//    // TODO: make sure handling IOExceptions correctly
//    public static void main(String[] args) throws IOException {
//        // TODO: if the args num is invalid, filename is wrong, or file is not sJava - throw IOException
//        //  (for now, being catch in the FileReader)
//        if (args.length != 1) {
//            throw new IOException();
//        }
//        String source_file_name = args[0];
//        Sjavac sjavac = new Sjavac(source_file_name);
//
//
////        // TODO: if the code is legal:
////        System.out.println(0);
////        // TODO: if the code is illegal:
////        System.out.println(1);
////        System.err(Constants.ILLEGAL_CODE_MESSAGE);
////        // TODO: in case of IO errors:
////        System.out.println(2);
////        System.err(Constants.IO_ERROR_MESSAGE);
//    }

    // main function for tests
    public static void main(String[] args) {
        System.out.println("Running all tests...\n");

        // Call runTests for each test class
//        SymbolTableTest.runTests();
        DeclarationParserTest.runTests();

        System.out.println("All tests completed.");
    }

}
