package com.infinitelatency.Eldrich.Mangle;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.rodionmoiseev.c10n.C10N;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MangleParserTests {
    /**
     * Packages are flat out forbidden in the mangle parser.
     * The justification is that packages only make sense with a valid JAVA
     * file. Using one with a irregular structure / multiple make no sense, so
     * we explicitly forbid them altogether.
     */

    @Test
    public void noPackages(){
        String s = "package com.test.packagename;\n" +
                "int a = 10;";
        try {
            MangleParser mp = new MangleParser(s);
        } catch (MangleException e) {
            MangleParserMsg msg = C10N.get(MangleParserMsg.class);
            assertEquals(e.getMessage(), msg.noPackages());
        }
    }


    /**
     * A valid program should not be mangled by the parser.
     */
    @Test
    public void validProgramNoMangle() throws ParseException, MangleException {
        String sample = "class D {\n" +
                "               private int value;\n" +
                "               private int test = 10;"+
                "                       public D() {\n" +
                "                          value = 5;\n" +
                "                       }\n" +
                "                       public void test(int derp) {\n" +
                "                          System.out.println(value + test + derp);\n" +
                "                       }\n" +
                "                    }";

        MangleParser mp = new MangleParser(sample);
        assertTrue("We should not mangle a valid Java file", !mp.status());
    }

    @Test
    public void parseStatements() throws ParseException, MangleException {
        String statements = "a[0] = 1;" +
                "b = 10;" +
                "c = 'a';" +
                "{ d = 5; }";

        MangleParser mp = new MangleParser(statements);
        List<Statement> parsedStatements = mp.statements();

        List<NodeTuple> expressionStatements = new ArrayList<>();
        expressionStatements.add(new NodeTuple("a[0] = 1;", ExpressionStmt.class));
        expressionStatements.add(new NodeTuple("b = 10;", ExpressionStmt.class));
        expressionStatements.add(new NodeTuple("c = 'a';", ExpressionStmt.class));
        expressionStatements.add(new NodeTuple("{\n    d = 5;\n}", BlockStmt.class));

        assertTrue(parsedStatements.size() == expressionStatements.size());
        for (int i = 0; i < parsedStatements.size(); i++){
            assertEquals(parsedStatements.get(i).getClass(), expressionStatements.get(i).type);
            assertEquals(parsedStatements.get(i).toString(), expressionStatements.get(i).contents);
        }
    }
}
