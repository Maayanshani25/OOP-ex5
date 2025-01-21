package ex5.parsing;

import ex5.scope_managing.ScopeManager;
import ex5.scope_managing.SymbolTable;
import ex5.scope_managing.SymbolTableException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ex5.util.Constants.*;

// TODO: who is responsible to check scope length? at least java.lang.Integer.MAX VALUE


public class IfAndWhileParser implements Parser {
    // TODO: move to Constants
    public static final String IF_CONDITION_REGEX = "if\\s*\\((.+)\\)\\s*\\{";
    public static final String WHILE_CONDITION_REGEX = "while\\s*\\((.+)\\)\\s*\\{";
    public static final String OR_CONDITION_REGEX = "(.+?)\\|\\|(.+)";
    public static final String AND_CONDITION_REGEX = "(.+?)&&(.+)"; //TODO

    public static final String INT_VALUE_REGEX = "[-+]?\\d+";
    public static final String DOUBLE_VALUE_REGEX = "[-+]?(\\d*\\.\\d+|\\d+\\.\\d*|\\d+)";


    private final SymbolTable symbolTable;
    private final ScopeManager scopeManager;

    /**
     * Constructor for VariablesParser.
     * @param symbolTable The SymbolTable instance used for managing variable scopes.
     */
    public IfAndWhileParser(SymbolTable symbolTable, ScopeManager scopeManager) {
        this.symbolTable = symbolTable;
        this.scopeManager = scopeManager;
    }


    @Override
    public void parse(String line) throws ParserException, SymbolTableException {
        String currentLine = line.trim(); // Trim leading and trailing whitespace

        // Check if in Method scope:
        if (scopeManager.getMethodsCounter() < 1) {
            throw new ParserException(OUT_OF_METHOD_SCOPE_ERROR_MESSAGE);
        }

        // Check valid regex:
        // Compile the regex
        Pattern ifPattern = Pattern.compile(IF_CONDITION_REGEX);
        Pattern whilePattern = Pattern.compile(WHILE_CONDITION_REGEX);
        Matcher ifMatcher = ifPattern.matcher(currentLine);
        Matcher whileMatcher = whilePattern.matcher(currentLine);

        if (!ifMatcher.matches() && !whileMatcher.matches()) {
            throw new ParserException(LOOP_OR_CONDITION_PARSER_EXCEPTION_MESSAGE);
        }

        // Check valid condition:
        String condition = ifMatcher.group(1);
        if (!parseFullCondition(condition)) {
            throw new ParserException(LOOP_OR_CONDITION_PARSER_EXCEPTION_MESSAGE);
        }

    }

    private boolean parseFullCondition(String condition) throws ParserException, SymbolTableException {
        // Check recursive OR\AND condition:
        Pattern orPattern = Pattern.compile(OR_CONDITION_REGEX);
        Matcher orMatcher = orPattern.matcher(condition);

        Pattern andPattern = Pattern.compile(AND_CONDITION_REGEX);
        Matcher andMatcher = andPattern.matcher(condition);
        if (recursiveCondition(orMatcher) || recursiveCondition(andMatcher)) {
                return true;
        }

        // TODO: delete
//        while (orMatcher.find()) {
//            String leftCondition = orMatcher.group(1).trim();
//            String rightCondition = orMatcher.group(2).trim();
//            if (!parseFullCondition(leftCondition) || !parseFullCondition(rightCondition)) {
//                throw new ParserException(LOOP_OR_CONDITION_PARSER_EXCEPTION_MESSAGE);
//            }
//            return true;
//        }
//
//        // Check recursive AND condition:
//        Pattern andPattern = Pattern.compile(AND_CONDITION_REGEX);
//        Matcher andMatcher = andPattern.matcher(condition);
//
//        while (andMatcher.find()) {
//            String leftCondition = andMatcher.group(1).trim();
//            String rightCondition = andMatcher.group(2).trim();
//            if (!parseFullCondition(leftCondition) || !parseFullCondition(rightCondition)) {
//                throw new ParserException(LOOP_OR_CONDITION_PARSER_EXCEPTION_MESSAGE);
//            }
//            return true;
//        }

        if (!parseSingleCondition(condition)) {
            return false;
        }

        return true;
    }

    private boolean recursiveCondition(Matcher matcher) throws ParserException, SymbolTableException {
        boolean foundCondition = false;

        while (matcher.find()) {
            foundCondition = true;
            String leftCondition = matcher.group(1).trim();
            String rightCondition = matcher.group(2).trim();

            if (!parseFullCondition(leftCondition) || !parseFullCondition(rightCondition)) {
                throw new ParserException(LOOP_OR_CONDITION_PARSER_EXCEPTION_MESSAGE);
            }
        }
        return foundCondition;
    }

    private boolean parseSingleCondition(String condition) throws SymbolTableException {
        String[] tokens = condition.split("\\s+");
        if (tokens.length != 1) {
            return false;
        }
        // Check if is true/false
        String token = tokens[0];
        if (token.equals(TRUE) || token.equals(FALSE)) {
            return true;
        }

        // Check if is initialized bool\int\double variable
        if (symbolTable.isVariableAssigned(token) && isValidType(token)) {
            return true;
        }

        // Check if double or int constant
        Pattern doublePattern = Pattern.compile(DOUBLE_VALUE_REGEX);
        Pattern intPattern = Pattern.compile(INT_VALUE_REGEX);
        Matcher doubleMatcher = doublePattern.matcher(condition);
        Matcher intMatcher = intPattern.matcher(condition);

        if (intMatcher.matches() || doubleMatcher.matches()) {
            return true;
        }

        return false;

    }


    private boolean isValidType(String token) {
        VariableType type = symbolTable.getVarType(token);
        return (type == VariableType.BOOLEAN || type == VariableType.DOUBLE || type == VariableType.INT);
    }

}
