package com.infinitelatency.Eldrich.Mangle;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.rodionmoiseev.c10n.C10N;
import com.infinitelatency.Eldrich.Mangle.Text.MangleParserMsg;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MangleParser {
    private boolean mangled;
    private final List<ImportDeclaration> imports = new ArrayList<>();
    private final List<Statement> statements = new ArrayList<>(); //Order is important here
    private final List<BlockStmt> blocks = new ArrayList<>();
    private final List<MethodDeclaration> methods = new ArrayList<>();
    private final List<FieldDeclaration> fieldDeclarations = new ArrayList<>();
    private final List<ClassOrInterfaceDeclaration> structuralDecls = new ArrayList<>();

    private MangleParserMsg msg = C10N.get(MangleParserMsg.class);

    public MangleParser(String program) throws ParseException, MangleException {
        InputStream stream = new ByteArrayInputStream(program.getBytes(StandardCharsets.UTF_8));
        try {
            JavaParser.parse(stream);  //If we can parse the entire thing in one go, it's a valid JAVA file
            mangled = false;
        } catch (ParseException e) {
            //Not a valid Java file, so we need to mangle the parsing of the program
            mangled = true;
            try {
                retryParse(program);
            } catch (ParseException e1){
                if (e1.getMessage().contains("package")){
                    throw new MangleException(msg.noPackages());
                }

            }
        }
    }

    private void retryParse(String program) throws ParseException, MangleException {
        String current = program.trim();
        int line = 0, column = 0;
        while (!current.isEmpty()) {
            try {
                ImportDeclaration id = JavaParser.parseImport(current);
                imports.add(id);
                current = truncate(current, id.getEndLine() - line, id.getEndColumn());
                line = id.getEndLine();
                column = id.getEndColumn();
            } catch (ParseException e) {
                try {
                    //BodyDeclaration actually has quite a few parse options, so will need to be done properly
                    BodyDeclaration bd = JavaParser.parseBodyDeclaration(current);
                    if (bd instanceof ClassOrInterfaceDeclaration) {
                        structuralDecls.add((ClassOrInterfaceDeclaration) bd);
                    } else if (bd instanceof MethodDeclaration) {
                        methods.add((MethodDeclaration) bd);
                    } else if (bd instanceof FieldDeclaration) {
                        fieldDeclarations.add((FieldDeclaration) bd);
                    } else {
                        throw new MangleException(msg.absentBodyCase(line, column));
                    }
                    current = truncate(current, bd.getEndLine() - line, bd.getEndColumn());
                    line = bd.getEndLine();
                    column = bd.getEndColumn();
                } catch (ParseException e1) {
                    //Now we try to parse blocks
                    try {
                        BlockStmt bs = JavaParser.parseBlock(current);
                        blocks.add(bs);
                        current = truncate(current, bs.getEndLine() - line, bs.getEndColumn());
                        line = bs.getEndLine();
                        column = bs.getEndColumn();
                    } catch (ParseException e2) {
                        //Finally, we try to parse statements
                        Statement s = JavaParser.parseStatement(current);
                        statements.add(s);
                        current = truncate(current, s.getEndLine() - line, s.getEndColumn());
                        line = s.getEndLine();
                        column = s.getEndColumn();
                    }
                }
            }
        }
    }

    @NotNull
    private String truncate (@NotNull String s, final int lines, final int columns){
        return s.substring(charsToDrop(s, lines, columns));
    }

    @Contract(pure = true)
    private int charsToDrop (@NotNull String s, final int lines, final int columns){
        int result = 0;
        for (int i = 0; i < lines; i++){
            result = s.contains("\n") ? result != 0 ? s.indexOf("\n", result) : s.indexOf("\n") : 0;
        }
        return result + columns;
    }

    boolean status() {
        return mangled;
    }

}
