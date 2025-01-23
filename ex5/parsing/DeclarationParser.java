package ex5.parsing;

import ex5.scope_managing.SymbolTable;
import ex5.scope_managing.SymbolTableException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ex5.util.Constants.*;


public class DeclarationParser implements Parser {

    private final SymbolTable symbolTable;
    private String currentLine;

    /**
     * Constructor for VariablesParser.
     * @param symbolTable The SymbolTable instance used for managing variable scopes.
     */
    public DeclarationParser(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    /**
     * Parses a line of variable declarations and adds valid variables to the symbol table.
     * @param line The line of code to parse.
     * @throws ParserException If the syntax or semantics of the line are invalid.
     */

    @Override
    public void parse(String line) throws ParserException, SymbolTableException {
        currentLine = line.trim(); // Trim leading and trailing whitespace

        // Compile the regex
        Pattern pattern = Pattern.compile(TYPE_REGEX);
        Matcher matcher = pattern.matcher(currentLine);

        // Check if the line starts with one of the keywords
        if (matcher.find()) {
            boolean isFinal = matcher.group(1) != null; // Check if 'final' is present
            String keyword = matcher.group(2); // Get the type keyword

            // Perform specific actions based on the keyword
            switch (keyword) {
                case INT:
                    parseType(INT_VALUE_REGEX, VariableType.INT, INT, isFinal);
                    break;
                case DOUBLE:
                    parseType(DOUBLE_VALUE_REGEX, VariableType.DOUBLE, DOUBLE, isFinal);
                    break;
                case STRING:
                    parseType(STRING_VALUE_REGEX, VariableType.STRING, STRING, isFinal);
                    break;
                case CHAR:
                    parseType(CHAR_VALUE_REGEX, VariableType.CHAR, CHAR, isFinal);
                    break;
                case BOOLEAN:
                    parseType(BOOLEAN_VALUE_REGEX, VariableType.BOOLEAN, BOOLEAN, isFinal);
                    break;
                default:
                    throw new ParserException(INVALID_TYPE_ERROR + keyword);
            }
        } else {
            throw new ParserException(WRONG_DECLARATION_FORMAT);
        }
    }


    private void parseType(String typeValueRegex, VariableType variableType, String type, boolean isFinal)
            throws ParserException, SymbolTableException {
        final String singleDeclaration = "(" + VARIABLE_NAME_REGEX + ")(\\s*=\\s*(" + typeValueRegex + "))?";
        final String multipleDeclarations = singleDeclaration + "(\\s*,\\s*" + singleDeclaration + ")*";
        final String correctLineRegex = FINAL_START_REGEX + type + "\\s+" + multipleDeclarations + "\\s*;$";

        Pattern pattern = Pattern.compile(correctLineRegex);
        Matcher matcher = pattern.matcher(currentLine);

        if (matcher.matches()) {
            // Remove 'final' if present
            String declarationsPart = currentLine.replaceFirst(FINAL_START_REGEX, "").trim();
            declarationsPart = declarationsPart.substring(type.length()).trim();
            declarationsPart = declarationsPart.substring(0, declarationsPart.length() - 1).trim();
            String[] declarations = declarationsPart.split(COMMA_SEPARATION_REGEX);

            for (String declaration : declarations) {
                declaration = declaration.trim();
                Pattern singlePattern = Pattern.compile(singleDeclaration);
                Matcher singleMatcher = singlePattern.matcher(declaration);

                if (singleMatcher.matches()) {
                    String variableName = singleMatcher.group(1);
                    AssignmentStatus assignmentStatus = getAssignmentStatus(isFinal, singleMatcher, variableName);

                    // Add the variable to the symbol table
                    symbolTable.addVarToScope(variableName, variableType, assignmentStatus, isFinal);
                } else {
                    throw new ParserException(INVALID_VARIABLE_DECLARATION + declaration);
                }
            }
        } else {
            throw new ParserException(PARSER_WRONG_LINE_FORMAT+ currentLine);
        }
    }

    private static AssignmentStatus getAssignmentStatus(boolean isFinal, Matcher singleMatcher, String variableName) throws ParserException {
        String value = singleMatcher.group(3); // Group 3 contains just the value
        boolean hasValue = value != null && !value.trim().isEmpty();

        // If 'final', enforce assignment
        if (isFinal && !hasValue) {
            throw new ParserException(String.format(FINAL_VARIABLE_ASSIGNMENT_ERROR, variableName));
        }

        AssignmentStatus assignmentStatus = hasValue ? AssignmentStatus.ASSIGNED : AssignmentStatus.DECLARED;
        return assignmentStatus;
    }


}
