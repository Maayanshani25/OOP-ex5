maayanshani,rotem.garti
319111886,207869520

## Regular Expressions Used in the Code

---

### 1. VARIABLE_NAME_REGEX

Regular Expression:
(?!true$)(?!false$)([a-zA-Z]|_[a-zA-Z0-9])[a-zA-Z0-9_]*

Explanation:
This regex defines the structure of a valid variable name.
- (?!true$)(?!false$): Negative lookahead assertions ensure the variable name is not "true" or "false".
  This prevents the use of boolean literals as variable names.
- ([a-zA-Z]|_[a-zA-Z0-9]): Matches a valid starting character for a variable name:
    - [a-zA-Z]: Allows variable names to start with a letter.
    - |: OR operator.
    - _[a-zA-Z0-9]: Allows variable names to start with an underscore followed by an alphanumeric character.
- [a-zA-Z0-9_]*: Matches the rest of the variable name, allowing any combination of alphanumeric characters
  and underscores.

Purpose:
This ensures variable names:
- Follow valid naming conventions.
- Avoid conflicts with reserved boolean literals (true, false).
- Allow underscores and alphanumeric characters.

---

### 2. singleDeclaration

Regular Expression:
(" + VARIABLE_NAME_REGEX + ")\\s*(=\\s*(" + typeValueRegex + "|" + VARIABLE_NAME_REGEX + "))?

Explanation:
This regex defines the structure of a valid single variable declaration:
- (" + VARIABLE_NAME_REGEX + "): Matches the variable name using the rules defined in VARIABLE_NAME_REGEX.
    - Group 1 captures the variable name.
- \\s*: Matches optional whitespace after the variable name.
- (=\\s*(" + typeValueRegex + "|" + VARIABLE_NAME_REGEX + "))?: Matches an optional assignment:
    - =: The assignment operator.
    - \\s*: Matches optional whitespace around the assignment operator.
    - (" + typeValueRegex + "|" + VARIABLE_NAME_REGEX + "): Matches the assigned value:
        - typeValueRegex: Valid literal values for the type (int, double, Sring, char, boolean).
        - VARIABLE_NAME_REGEX: Another valid variable name being assigned.
    - Group 3 captures the value being assigned.
    - ?: Makes the assignment optional, allowing for declarations without initialization (e.g., int x;).

Purpose:
This regex validates and parses single variable declarations, ensuring:
- Variable names are valid.
- Assignments, if present, use either a valid literal value or a valid variable name.
- Formatting is flexible with optional whitespace.

---

These regular expressions form the backbone of variable validation and declaration parsing in the program,
ensuring adherence to strict syntax rules while maintaining flexibility in formatting.
