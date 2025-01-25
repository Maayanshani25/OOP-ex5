package ex5.main;

import ex5.FileReader;
import ex5.Validator;
import ex5.parsing.MethodReader;
import ex5.parsing.ParserException;
import ex5.scope_managing.ScopeManager;
import ex5.scope_managing.ScopeManagerException;
import ex5.scope_managing.SymbolTable;
import ex5.scope_managing.SymbolTableException;
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

    /**
     * Constructor for the Sjavac class.
     * Initializes the necessary components for validating the given sJava file.
     *
     * @param filePath The path to the sJava file to be validated.
     * @throws IOException If an error occurs while reading or preprocessing the file.
     */
    public Sjavac(String filePath) throws IOException {
        // Read and pre-process the file:
        List<String> allLines = FileReader.readLines(filePath);
        preprocessedLines = FileReader.preProcessLines(allLines);
        methodsMap = new HashMap<>();
        symbolTable = new SymbolTable();
        scopeManager = new ScopeManager(symbolTable);
    }

    /**
     * Runs the validation process on the sJava file.
     * Validates method declarations, scopes, and individual lines of code.
     *
     * @return true if the file is valid, false otherwise.
     * @throws IOException If an IO error occurs during file handling.
     */
    public boolean run() throws IOException {
        try {
            methodsMap = MethodReader.readMethods(preprocessedLines);
            scopeManager.enterNewScope(Constants.ScopeKind.GLOBAL);
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
        if (!scopeManager.areInGlobalScope()) {
            return false;
        }
        return true;
    }

    // TODO: make sure handling IOExceptions correctly


    /**
     * Entry point for the Sjavac program.
     * Validates the provided sJava file and prints the corresponding exit code.
     * Exit codes:
     * 0 - The file is valid.
     * 1 - The file has invalid syntax or semantics.
     * 2 - An IO error occurred (e.g., invalid file path or incorrect file format).
     *
     * @param args Command-line arguments. Expects a single argument: the file path.
     * @throws IOException If an IO error occurs during file handling.
     */
    public static void main(String[] args) throws IOException {
        // TODO: if the args num is invalid, filename is wrong, or file is not sJava - throw IOException

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
}
