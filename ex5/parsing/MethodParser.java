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

public class MethodParser implements Parser {

    private final SymbolTable symbolTable;
    private final ScopeManager scopeManager;
    private final Map<String, ArrayList<VariableType>> methods;

    public MethodParser(SymbolTable symbolTable, ScopeManager scopeManager,
                        Map<String, ArrayList<VariableType>> methods) {
        this.symbolTable = symbolTable;
        this.scopeManager = scopeManager;
        this.methods = methods;
    }

    @Override
    public void parse(String line) throws ParserException, SymbolTableException {
        String currentLine = line.trim();

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
        } else {
            throw new ParserException(METHOD_GENERAL_SYNTAX_ERROR);
        }
    }

    private void parseMethodDeclaration(Matcher matcher, String line) throws ParserException, SymbolTableException {
        // Checks valid regex
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
            String[] parametersAndTypes = parametersString.split("\\s*,\\s*"); // TODO: check
            if (!(parametersAndTypes.length==1 && parametersAndTypes[0].equals(""))) {
                for (String parameter : parametersAndTypes) {
                    Variable variable = parseParameter(parameter);
                    if (variable != null) {
                        String parameterName = variable.getName();
                        // add var to symboltable
                        symbolTable.addVarToScope(parameterName, variable);
                    }
                }
            }

            // Check that ends with "return;" and "{} - alredy in MethodReader

            // if passes all test, add vars and open new scope
            if (!(parametersAndTypes.length==1 && parametersAndTypes[0].equals(""))) {
                for (String parameter : parametersAndTypes) {
                    Variable variable = parseParameter(parameter);
                    if (variable == null) {
                        String parameterName = variable.getName();
                        symbolTable.addVarToScope(parameterName, variable);
                    }
                }
            }
        } else {
            throw new ParserException(METHOD_DECLARE_SYNTAX_ERROR);
        }

    }

    // todo: what is this method?
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
                    VariableType constantType = ConstantParameter(parameterName);
                    if (constantType != null) {
                        if (!isTypeCompatible(constantType, expectedType)) {
                            throw new ParserException(METHOD_INVALID_PARAMETERS_ERROR);
                        }
                        continue;
                    }

                    // Check valid name and if exists:
                    // TODO: must be declared or assigned? not in the forum, needs to check school solution
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

        } else {
            throw new ParserException(METHOD_CALL_SYNTAX_ERROR);
        }
    }

    public static void addToMethodMap(String line, Map<String, ArrayList<Constants.VariableType>> methods)
            throws ParserException {
        String currentLine = line.trim(); // Trim leading and trailing whitespace
        Pattern declarePattern = Pattern.compile(METHOD_DECLARE_REGEX);
        Matcher declareMatcher = declarePattern.matcher(currentLine);

        if (declareMatcher.matches()) {
            String methodName = declareMatcher.group(1); // TODO: check
            //  Check method_name isn't already exists
            if (methods.containsKey(methodName)) {
                throw new ParserException(METHOD_NAME_ALREADY_EXIST_ERROR);
            }
            // Check valid parameters and create type list for the method
            String parametersString = declareMatcher.group(2); // TODO: check
            String[] parameters = parametersString.split(",");
            ArrayList<VariableType> parameterTypes = new ArrayList<>();
            if (!parametersString.isEmpty()) {
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

    private boolean isTypeCompatible(VariableType actual, VariableType expected) {
        if (actual == expected) return true;
        if (expected == VariableType.DOUBLE && actual == VariableType.INT) return true;
        if (expected == VariableType.BOOLEAN && (actual == VariableType.INT || actual == VariableType.DOUBLE))
            return true;
        return false;
    }

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

    private static boolean isValidName(String name) {
        Pattern namePattern = Pattern.compile(VARIABLE_NAME_REGEX);
        Matcher nameMatcher = namePattern.matcher(name);
        if (!nameMatcher.matches()) {
            return false;
        }
        return true;
    }

    private static Variable parseParameter(String parameter) throws ParserException {
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
        } else {
            throw new ParserException(METHOD_INVALID_PARAMETERS_ERROR);
        }
        return null;
    }

    private static VariableType ConstantParameter(String parameter) {
        parameter = parameter.trim();
        Pattern intPattern = Pattern.compile(INT_VALUE_REGEX);
        Matcher intMatcher = intPattern.matcher(parameter);
        Pattern doublePattern = Pattern.compile(DOUBLE_VALUE_REGEX);
        Matcher doubleMatcher = doublePattern.matcher(parameter);
        Pattern boolPattern = Pattern.compile(BOOLEAN_CONSTANT_REGEX);
        Matcher boolMatcher = boolPattern.matcher(parameter);
        Pattern charPattern = Pattern.compile(CHAR_VALUE_REGEX);
        Matcher charMatcher = charPattern.matcher(parameter);
        Pattern stringPattern = Pattern.compile(STRING_VALUE_REGEX);
        Matcher stringMatcher = stringPattern.matcher(parameter);

        if (intMatcher.matches()) {
            return VariableType.INT;
        } else if (doubleMatcher.matches()) {
            return VariableType.DOUBLE;
        } else if (boolMatcher.matches()) {
            return VariableType.BOOLEAN;
        } else if (charMatcher.matches()) {
            return VariableType.CHAR;
        } else if (stringMatcher.matches()) {
            return VariableType.STRING;
        } else {
            return null;
        }

    }

}
