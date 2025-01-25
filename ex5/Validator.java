package ex5;

import ex5.parsing.*;
import ex5.scope_managing.ScopeManager;
import ex5.scope_managing.ScopeManagerException;
import ex5.scope_managing.SymbolTable;
import ex5.scope_managing.SymbolTableException;
import ex5.util.Constants;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ex5.util.Constants.*;

/**
 * The Validator class is responsible for validating lines of code and directing them to the appropriate parsers.
 * It ensures that lines conform to the correct syntax and semantics, leveraging various parsers and a symbol table.
 */
public class Validator {

    private final AssignmentParser assignmentsParser;
    private final DeclarationParser declarationParser;
    private final IfAndWhileParser ifAndWhileParser;
    private final MethodParser methodParser;
    private final ScopeManager scopeManager;
    private final Map<String, ArrayList<VariableType>> methods;

    /**
     * Constructs a Validator instance with the provided symbol table, scope manager, and methods map.
     *
     * @param symbolTable   The symbol table used for variable and method scope management.
     * @param scopeManager  The scope manager to track and manage scopes.
     * @param methods       A map of method names to their parameter types.
     */
    public Validator(SymbolTable symbolTable,
                     ScopeManager scopeManager,
                     Map<String, ArrayList<VariableType>> methods) {
        this.scopeManager = scopeManager;
        this.methods = methods;
        this.assignmentsParser = new AssignmentParser(symbolTable);
        this.declarationParser = new DeclarationParser(symbolTable);
        this.ifAndWhileParser = new IfAndWhileParser(symbolTable, scopeManager);
        this.methodParser = new MethodParser(symbolTable, scopeManager, methods);
    }

    /**
     * Validates a line of code and delegates it to the appropriate parser.
     *
     * @param line The line of code to validate.
     * @return True if the line is valid, false otherwise.
     * @throws ParserException      If the syntax or semantics of the line are invalid.
     * @throws SymbolTableException If there are issues with the symbol table (e.g., scope management).
     * @throws ScopeManagerException If there are issues with scope management (e.g., invalid scope transitions).
     */
    public boolean isValidLine(String line) throws ParserException, SymbolTableException, ScopeManagerException {
        String trimmedLine = line.trim();

        // Check if the line ends with valid characters
        if (!trimmedLine.matches(LINE_END_REGEX)) {
            throw new ParserException(INVALID_ENDLINE_ERROR_MESSAGE + trimmedLine);
        }

        // Check for valid keywords or variable names
        Pattern keywordPattern = Pattern.compile(KEYWORDS_REGEX);
        Matcher keywordMatcher = keywordPattern.matcher(trimmedLine);

        if (keywordMatcher.find()) {
            String keyword = keywordMatcher.group();

            // Handle method syntax
            if (keyword.equals(VOID) || methods.containsKey(keyword)) {
                methodParser.parse(trimmedLine);
                return true;
            }

            // Handle "while" syntax
            if (keyword.equals(WHILE)) {
                ifAndWhileParser.parse(trimmedLine);
                return true;
            }

            // Handle "if" syntax
            if (keyword.equals(IF)) {
                ifAndWhileParser.parse(trimmedLine);
                return true;
            }

            // Handle variable declaration syntax
            if (keyword.matches(DECLARATION_REGEX)) {
                declarationParser.parse(trimmedLine);
                return true;
            }

            // Handle return statement
            if (keyword.equals(RETURN)) {
                Pattern returnPattern = Pattern.compile(RETURN_LINE);
                Matcher returnMatcher = returnPattern.matcher(trimmedLine);

                if (!returnMatcher.matches()) {
                    throw new ParserException(INVALID_RETURN_STATEMENT_SYNTAX);
                }
                if (scopeManager.getMethodsCounter() == 0) {
                    throw new ParserException(RETURN_OUT_OF_METHOD_SCOPE_ERROR);
                }
                return true;
            }

            // Handle assignment syntax
            if (keyword.matches(VARIABLE_NAME_REGEX)) {
                assignmentsParser.parse(trimmedLine);
                return true;
            }

            // Handle end of scope
            if (keyword.equals(END_OF_SCOPE)) {
                // check there is only one "{":
                if (trimmedLine.equals(END_OF_SCOPE)){
                    scopeManager.exitScope();
                    return true;
                }
            }
        }

        // If no valid keyword or pattern matched, throw an exception
        throw new ParserException(UNRECOGNIZED_INVALIE_LINE_MESSAGE + trimmedLine);
    }
}
