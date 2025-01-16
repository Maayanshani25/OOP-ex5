package ex5.main;

import ex5.FileReader;
import ex5.SymbolTable;

import java.util.List;

// TODO: not sure needed - maybe could be in the main?
public class Analizer {
    List<String> preprocessedLines;
    SymbolTable symbolTable;

    public Analizer(String filePath) {
        List<String> allLines = FileReader.readLines(filePath);
        preprocessedLines = FileReader.preProcessLines(allLines);
        symbolTable = new SymbolTable();

        // TODO:

    }
}
