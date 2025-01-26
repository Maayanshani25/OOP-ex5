package ex5.parsing;

import ex5.Variable;
import ex5.scope_managing.ScopeManager;
import ex5.scope_managing.SymbolTable;
import ex5.scope_managing.SymbolTableException;
import ex5.util.Constants;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ex5.util.Constants.*;


/**
 * The MethodParser class is responsible for parsing and validating method declarations and calls.
 * It ensures that methods adhere to the syntax rules and interact correctly with the symbol
 * table and scope manager.
 */
public class MethodParser implements Parser {

    private final SymbolTable symbolTable;
    private final ScopeManager scopeManager;
    private final Map<String, ArrayList<VariableType>> methods;

    /**
     * Constructor for MethodParser.
     *
     * @param symbolTable  The SymbolTable instance for managing variable scopes.
     * @param scopeManager The ScopeManager instance for managing nested scopes.
     * @param methods      A map of method names to their respective parameter types.
     */
    public MethodParser(SymbolTable symbolTable, ScopeManager scopeManager,
                        Map<String, ArrayList<VariableType>> methods) {
        this.symbolTable = symbolTable;
        this.scopeManager = scopeManager;
        this.methods = methods;
    }

    /**
     * Parses a line of code to determine if it is a method declaration or call, delegating as needed.
     *
     * @param line The line of code to parse.
     * @throws ParserException      If the syntax of the line is invalid.
     * @throws SymbolTableException If there are issues with variable declarations or scopes.
     */
    @Override
    public void parse(String line) throws ParserException, SymbolTableException {
        String currentLine = line.trim();

        Pattern callPattern = Pattern.compile(METHOD_CALL_REGEX);
        Matcher callMatcher = callPattern.matcher(currentLine);
        Pattern declarePattern = Pattern.compile(METHOD_DECLARE_REGEX);
        Matcher declareMatcher = declarePattern.matcher(currentLine);

        if (callMatcher.matches()) {
            parseMethodCall(callMatcher, line);
        } else if (declareMatcher.matches()) {
            parseMethodDeclaration(declareMatcher, line);
        } else {
            throw new ParserException(METHOD_GENERAL_SYNTAX_ERROR);
        }
    }

    /**
     * Parses and validates a method declaration.
     *
     * @param matcher The Matcher object for the method declaration regex.
     * @param line    The line of code containing the method declaration.
     * @throws ParserException      If the method declaration is invalid.
     * @throws SymbolTableException If there are issues with variable declarations or scopes.
     */
    private void parseMethodDeclaration(Matcher matcher, String line)
            throws ParserException, SymbolTableException {
        scopeManager.enterNewScope(ScopeKind.METHOD);

        if (matcher.matches()) {
            String methodName = matcher.group(1);
            String parametersString = matcher.group(2);

            // Check if not in other method scope
            if (scopeManager.getMethodsCounter() > 1) {
                throw new ParserException(METHOD_DECLARE_IN_METHOD_SCOPE_ERROR);
            }

            // Check if method name is valid
            Pattern namePattern = Pattern.compile(METHOD_NAME_REGEX);
            Matcher nameMatcher = namePattern.matcher(methodName);
            if (!nameMatcher.matches()) {
                throw new ParserException(INVALID_METHOD_NAME_ERROR);
            }

            // Check if method name already exists - already been checked in MethodReader
            // Check parameters name are valid:
            String[] parametersAndTypes = parametersString.trim().split("\\s*,\\s*");
            if (!(parametersAndTypes.length == 1 && parametersAndTypes[0].equals(""))) {
                for (String parameter : parametersAndTypes) {
                    Variable variable = parseParameter(parameter);
                    if (variable != null) {
                        String parameterName = variable.getName();
                        // add var to symboltable
                        symbolTable.addVarToScope(parameterName, variable);
                    } else {
                        throw new ParserException(METHOD_INVALID_PARAMETERS_ERROR);
                    }
                }
            }
            // Check that ends with "return;" and "{} - alredy in MethodReader

        } else {
            throw new ParserException(METHOD_DECLARE_SYNTAX_ERROR);
        }
    }

    /**
     * Parses and validates a method call.
     *
     * @param matcher The Matcher object for the method call regex.
     * @param line    The line of code containing the method call.
     * @throws ParserException      If the method call is invalid.
     * @throws SymbolTableException If there are issues with variable declarations or scopes.
     */
    private void parseMethodCall(Matcher matcher, String line) throws ParserException, SymbolTableException {
        if (matcher.matches()) {
            String methodName = matcher.group(1);
            String parametersString = matcher.group(2);

            // Check is in another method scope:
            if (scopeManager.getMethodsCounter() == 0) {
                throw new ParserException(METHOD_CALL_OUT_OF_METHOD_SCOPE);
            }

            // Check method_name exists:
            if (!methods.containsKey(methodName)) {
                throw new ParserException(METHOD_NAME_DOESNT_EXIST);
            }

            // Check parameters have valid name and requested type, and exist:
            String[] parameters = parametersString.split("\\s*,\\s*");
            // Check num of parameters ais valid:
            if (!(parametersString.isEmpty() && methods.get(methodName).isEmpty())) {
                if (parameters.length != methods.get(methodName).size()) {
                    throw new ParserException(METHOD_INVALID_PARAMETERS_ERROR);
                }
                for (int i = 0; i < parameters.length; i++) {
                    String parameterName = parameters[i].trim();
                    VariableType expectedType = methods.get(methodName).get(i);

                    // Check if constant
                    VariableType constantType = Variable.ConstantParameter(parameterName);
                    if (constantType != null) {
                        if (!Variable.isTypeCompatible(constantType, expectedType)) {
                            throw new ParserException(METHOD_INVALID_PARAMETERS_ERROR);
                        }
                        continue;
                    }

                    // Check valid name and if exists and was assigned:
                    if (!symbolTable.isVariableAssigned(parameterName)) {
                        throw new ParserException(METHOD_INVALID_PARAMETERS_ERROR);
                    }
                    // Check valid type:
                    VariableType type = symbolTable.getVarType(parameterName);
                    if (!Variable.isTypeCompatible(type, expectedType)) {
                        throw new ParserException(METHOD_INVALID_PARAMETERS_ERROR);
                    }
                }
            }

        } else {
            throw new ParserException(METHOD_CALL_SYNTAX_ERROR);
        }
    }

    /**
     * Adds a method declaration to the method map, validating its syntax and parameters.
     *
     * @param line    The line of code containing the method declaration.
     * @param methods The map of methods to update.
     * @throws ParserException If the method declaration is invalid.
     */
    public static void addToMethodMap(String line, Map<String, ArrayList<Constants.VariableType>> methods)
            throws ParserException {
        String currentLine = line.trim();
        Pattern declarePattern = Pattern.compile(METHOD_DECLARE_REGEX);
        Matcher declareMatcher = declarePattern.matcher(currentLine);

        if (declareMatcher.matches()) {
            String methodName = declareMatcher.group(1);
            //  Check method_name isn't already exists
            if (methods.containsKey(methodName)) {
                throw new ParserException(METHOD_NAME_ALREADY_EXIST_ERROR);
            }
            // Check valid parameters and create type list for the method
            String parametersString = declareMatcher.group(2);
            String[] parameters = parametersString.trim().split(",");
            ArrayList<VariableType> parameterTypes = new ArrayList<>();
            if (!(parameters.length == 1 && parameters[0].equals(""))) {
                for (String parameter : parameters) {
                    Variable variable = parseParameter(parameter);
                    if (variable != null) {
                        parameterTypes.add(variable.getType());
                    }
                }
            }
            methods.put(methodName, parameterTypes);
        }
    }

    /**
     * Parses a parameter declaration and returns a Variable object.
     *
     * @param parameter The parameter declaration string.
     * @return A Variable object representing the parameter.
     * @throws ParserException If the parameter declaration is invalid.
     */
    private static Variable parseParameter(String parameter) throws ParserException {
        String[] parameterStrings = parameter.trim().split("\\s+");
        if (parameterStrings.length == 3) {
            if (parameterStrings[0].equals(FINAL) &&
                    isValidName(parameterStrings[2])) {
                String parameterName = parameterStrings[2];
                VariableType parameterType = parseType(parameterStrings[1]);
                return new Variable(parameterName, parameterType, AssignmentStatus.ASSIGNED, true);
            } else {
                throw new ParserException(METHOD_INVALID_PARAMETERS_ERROR);
            }

        } else if (parameterStrings.length == 2) {
            if (isValidName(parameterStrings[1])) {
                String parameterName = parameterStrings[1];
                VariableType parameterType = parseType(parameterStrings[0]);
                return new Variable(parameterName, parameterType, AssignmentStatus.ASSIGNED, false);
            } else {
                throw new ParserException(METHOD_INVALID_PARAMETERS_ERROR);
            }
        } else {
            throw new ParserException(METHOD_INVALID_PARAMETERS_ERROR);
        }
    }

    /**
     * Validates a parameter name against the variable naming rules.
     *
     * @param name The parameter name to validate.
     * @return true if the name is valid, false otherwise.
     */
    private static boolean isValidName(String name) {
        Pattern namePattern = Pattern.compile(VARIABLE_NAME_REGEX);
        Matcher nameMatcher = namePattern.matcher(name);
        return nameMatcher.matches();
    }

    /**
     * Parses the type of a parameter and returns its VariableType.
     *
     * @param parameterType The type string of the parameter.
     * @return The corresponding VariableType.
     * @throws ParserException If the type is invalid.
     */
    private static VariableType parseType(String parameterType) throws ParserException {
        switch (parameterType) {
            case INT:
                return VariableType.INT;
            case DOUBLE:
                return VariableType.DOUBLE;
            case STRING:
                return VariableType.STRING;
            case CHAR:
                return VariableType.CHAR;
            case BOOLEAN:
                return VariableType.BOOLEAN;
            default:
                throw new ParserException(INVALID_TYPE_ERROR + parameterType);
        }
    }
}
