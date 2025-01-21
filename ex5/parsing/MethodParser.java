package ex5.parsing;

import ex5.Validator;
import ex5.Variable;
import ex5.scope_managing.ScopeManager;
import ex5.scope_managing.SymbolTable;
import ex5.scope_managing.SymbolTableException;
import static ex5.util.Constants.*;


import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodParser implements Parser {
    public static final String METHOD_NAME_REGEX = "[a-zA-Z][a-zA-Z0-9_]*";
    public static final String METHOD_CALL_REGEX = "(" + METHOD_NAME_REGEX + ")\\s*\\(([^)]*)\\)\\s*;";
    public static final String METHOD_DECLARE_REGEX = "void\\s+(" + METHOD_NAME_REGEX + ")\\s*\\(([^)]*)\\)\\s*\\{";// TODO
    public static final String BOOLEAN_CONSTANT_REGEX = "(true|false)";


    private final SymbolTable symbolTable;
    private final ScopeManager scopeManager;
    // TODO: un-comment after the tests and remove setMethodsMap
    //    private final Map<String, ArrayList<VariableType>> methods;
    private Map<String, ArrayList<VariableType>> methods;

    public MethodParser(SymbolTable symbolTable, ScopeManager scopeManager,
                        Map<String, ArrayList<VariableType>> methods) {
        this.symbolTable = symbolTable;
        this.scopeManager = scopeManager;
        this.methods = methods;
    }

    @Override
    public void parse(String line) throws ParserException, SymbolTableException {
        String currentLine = line.trim(); // Trim leading and trailing whitespace

        Pattern callPattern = Pattern.compile(METHOD_CALL_REGEX);
        Matcher callMatcher = callPattern.matcher(currentLine);
        Pattern declarePattern = Pattern.compile(METHOD_DECLARE_REGEX);
        Matcher declareMatcher = declarePattern.matcher(currentLine);

        // If is a call regex:
        if (callMatcher.matches()) {
            parseMethodCall(callMatcher, line);
        }
        // If is a declaration regex:
        else if (declareMatcher.matches()) {
            parseMethodDeclaration(declareMatcher, line);
        }
        else {
            throw new ParserException(METHOD_GENERAL_SYNTAX_ERROR);
        }


    }

    private void parseMethodDeclaration(Matcher matcher, String line) throws ParserException, SymbolTableException {
        // TODO: conditions to check:
        //  is "void method_name (type parameter, type parameter...) {"
        //  is not in other method
        //  method_name isnt already exists (here or in second pre-process?)
        //  parameters name arent already exist
        //  ends with "return;" and then "}"

        // TODO: if legal, need to add:
        //  new scope
        //  parameters to scope vars

        // Checks valid regex
        if (matcher.matches()) {
            String methodName = matcher.group(1); // TODO: check
            String parametersString = matcher.group(2); // TODO: check

            // Check if not in other method scope
            if (scopeManager.getMethodsCounter() > 0) {
                throw new ParserException(METHOD_DECLARE_IN_METHOD_SCOPE_ERROR);
            }

            // Check if method name is valid
            Pattern namePattern = Pattern.compile(METHOD_NAME_REGEX);
            Matcher nameMatcher = namePattern.matcher(methodName);
            if (!nameMatcher.matches()) {
                throw new ParserException(INVALID_METHOD_NAME_ERROR);
            }

            // Check if method name already exists
            if (methods.containsKey(methodName)) {
                throw new ParserException(METHOD_NAME_ALREADY_EXIST_ERROR);
            }

            // Check parameters name are not already exist, and are valid:
            String[] parametersAndTypes = parametersString.split("\\s*,\\s*"); // TODO: check
            for (String parameter : parametersAndTypes) {
                Variable variable = parseParameter(parameter);
                if (variable != null) {
                    String parameterName = variable.getName();
                    if (symbolTable.isVariableDeclared(parameterName)) {
                        throw new ParserException(METHOD_PARAMETERS_ALREADY_EXIST_ERROR);
                    }
                }
            }

            // Check that ends with "return;" and "{}
            // TODO

            // if passes all test, add vars and open new scope
            scopeManager.enterNewScope(ScopeKind.METHOD);
            for (String parameter : parametersAndTypes) {
                Variable variable = parseParameter(parameter);
                if (variable == null) {
                    String parameterName = variable.getName();
                    symbolTable.addVarToScope(parameterName, variable);
                }
            }
        }

        else {
            throw new ParserException(METHOD_DECLARE_SYNTAX_ERROR);
        }

    }

    private void parseMethodCall(Matcher matcher, String line) throws ParserException, SymbolTableException {
        // TODO: conditions to check:
        //  is "method_name (var, var...);"
        //  is in other method
        //  method_name exists
        //  num of vars fit to the requiered
        //  vars exist/constants
        //  var types fit to the types requiered (bool can get int/double, double can get int)

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
            if (parameters.length != methods.get(methodName).size()) {
                throw new ParserException(METHOD_INVALID_PARAMETERS_ERROR);
            }
            for (int i = 0; i < parameters.length; i++) {
                String parameterName = parameters[i];
                VariableType expectedType = methods.get(methodName).get(i);

                // Check if constant
                VariableType constantType = ConstantParameter(parameterName);
                if (constantType != null) {
                    if (!isTypeCompatible(constantType, expectedType)) {
                        throw new ParserException(METHOD_INVALID_PARAMETERS_ERROR);
                    }
                    continue;
                }

                // Check valid name and if exists:
                // TODO: must be declared or assigned?
                if (!symbolTable.isVariableDeclared(parameterName)) {
                    throw new ParserException(METHOD_INVALID_PARAMETERS_ERROR);
                }
                // Check valid type:
                VariableType type = symbolTable.getVarType(parameterName);
                if (!isTypeCompatible(type, expectedType)) {
                    throw new ParserException(METHOD_INVALID_PARAMETERS_ERROR);
                }
            }

        }
        else {
            throw new ParserException(METHOD_CALL_SYNTAX_ERROR);
        }
    }

    public void addToMethodMap(String line) throws ParserException {
        String currentLine = line.trim(); // Trim leading and trailing whitespace
        Pattern declarePattern = Pattern.compile(METHOD_DECLARE_REGEX);
        Matcher declareMatcher = declarePattern.matcher(currentLine);

        if (declareMatcher.matches()) {
            String methodName = declareMatcher.group(1); // TODO: check
            //  Check method_name isn't already exists
            if (methods.get(methodName)!=null) {
                throw new ParserException(METHOD_NAME_ALREADY_EXIST_ERROR);
            }
            // Check valid parameters and create type list for the method
            String parametersString = declareMatcher.group(2); // TODO: check
            String[] parameters = parametersString.split(",");
            ArrayList<VariableType> parameterTypes = new ArrayList<>();
            for (String parameter : parameters) {
                Variable variable = parseParameter(parameter);
                if (variable != null) {
                    parameterTypes.add(variable.getType());
                }
            }
            methods.put(methodName, parameterTypes);
        }
    }

    private boolean isTypeCompatible(VariableType actual, VariableType expected) {
        if (actual == expected) return true;
        if (expected == VariableType.DOUBLE && actual == VariableType.INT) return true;
        if (expected == VariableType.BOOLEAN && (actual == VariableType.INT || actual == VariableType.DOUBLE)) return true;
        return false;
    }

    private VariableType parseType(String parameterType) throws ParserException {
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

    private boolean isValidName(String name) {
        Pattern namePattern = Pattern.compile(VARIABLE_NAME_REGEX);
        Matcher nameMatcher = namePattern.matcher(name);
        if (!nameMatcher.matches()) {
            return false;
        }
        return true;
    }

    private Variable parseParameter(String parameter) throws ParserException {
        String[] parameterStrings = parameter.trim().split("\\s+");
        // If 3 strings: Final, type, name
        if (parameterStrings.length == 3) {
            if (parameterStrings[0].equals(FINAL) &&
                    isValidName(parameterStrings[2])) {
                String parameterName = parameterStrings[2];
                VariableType parameterType = parseType(parameterStrings[1]);
                return new Variable(parameterName, parameterType, AssignmentStatus.ASSIGNED, true);
            }
        } else if (parameterStrings.length == 2) {
            if (isValidName(parameterStrings[1])) {
                String parameterName = parameterStrings[1];
                VariableType parameterType = parseType(parameterStrings[0]);
                return new Variable(parameterName, parameterType, AssignmentStatus.ASSIGNED, false);
            } else {
                throw new ParserException(METHOD_INVALID_PARAMETERS_ERROR);
            }
        }
        else {
            throw new ParserException(METHOD_INVALID_PARAMETERS_ERROR);
        }
        return null;
    }

    private VariableType ConstantParameter(String parameter) {
        Pattern intPattern = Pattern.compile(INT_VALUE_REGEX);
        Matcher intMatcher = intPattern.matcher(parameter);
        Pattern doublePattern = Pattern.compile(DOUBLE_VALUE_REGEX);
        Matcher doubleMatcher = doublePattern.matcher(parameter);
        Pattern boolPattern = Pattern.compile(BOOLEAN_CONSTANT_REGEX);
        Matcher boolMatcher = boolPattern.matcher(parameter);

        if (intMatcher.matches()) {
            return VariableType.INT;
        }
        else if (doubleMatcher.matches()) {
            return VariableType.DOUBLE;
        }
        else if (boolMatcher.matches()) {
            return VariableType.BOOLEAN;
        }
        else {
            return null;
        }

    }

    public void setMethodsMap(Map<String, ArrayList<VariableType>> methods) {
        this.methods = methods;
    }
}
