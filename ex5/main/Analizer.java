package ex5.main;

import ex5.FileReader;
import ex5.scope_managing.SymbolTable;
import ex5.Validator;

import java.util.List;

// TODO: not sure needed - maybe could be in the main?
public class Analizer {
    List<String> preprocessedLines;
    SymbolTable symbolTable;
    Validator validator;

    public Analizer(String filePath) {
        // Read and pre-process the file:
        List<String> allLines = FileReader.readLines(filePath);
        preprocessedLines = FileReader.preProcessLines(allLines);

        symbolTable = new SymbolTable();
        validator = new Validator();
    }

    public boolean run() {
        // TODO: i think this is wrong implement
        // For every line, check if valid
        for (String line : preprocessedLines) {
            if (!Validator.isValidLine(line)) {
                return false;
            }
        }
        return true;
    }
}
