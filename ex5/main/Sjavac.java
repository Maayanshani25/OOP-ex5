package ex5.main;

import ex5.FileReader;
import ex5.Validator;
import ex5.scope_managing.ScopeManager;
import ex5.scope_managing.SymbolTable;

import java.io.IOException;
import java.util.List;

public class Sjavac {
    private final List<String> preprocessedLines;
    private final SymbolTable symbolTable;
    // TODO: needed? or Validator methods are static?
    private Validator validator;
    private ScopeManager scopeManager;

    public Sjavac(String filePath) {
        // Read and pre-process the file:
        List<String> allLines = FileReader.readLines(filePath);
        preprocessedLines = FileReader.preProcessLines(allLines);

        symbolTable = new SymbolTable();
        validator = new Validator(symbolTable);
        scopeManager = new ScopeManager();
    }

    public boolean run() {
        // TODO: i think this is wrong implement
        // For every line, check if valid
        for (String line : preprocessedLines) {
            if (!Validator.isValidLine(line)) {
                return false;
            }
        }
        // Check there are no un-closed scopes:
        if (!scopeManager.isScopeDequeEmpty()) {
            return false;
        }

        return true;
    }

    // TODO: make sure handling IOExceptions correctly
    public static void main(String[] args) throws IOException {
        // TODO: if the args num is invalid, filename is wrong, or file is not sJava - throw IOException
        //  (for now, being catch in the FileReader)
        if (args.length != 1) {
            throw new IOException();
        }
        String source_file_name = args[0];
        Sjavac sjavac = new Sjavac(source_file_name);


//        // TODO: if the code is legal:
//        System.out.println(0);
//        // TODO: if the code is illegal:
//        System.out.println(1);
//        System.err(Constants.ILLEGAL_CODE_MESSAGE);
//        // TODO: in case of IO errors:
//        System.out.println(2);
//        System.err(Constants.IO_ERROR_MESSAGE);
    }
}
