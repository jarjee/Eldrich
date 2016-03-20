package com.infinitelatency.Eldrich.Mangle;

import com.github.javaparser.ParseException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MangleParserTests {
    /**
     * Packages are flat out forbidden in the mangle parser.
     * The justification is that packages only make sense with a valid JAVA
     * file. Using one with a irregular strucutre / multiple make no sense, so
     * we explicitly forbid them altogether.
     */

    @Test
    public void noPackages(){
        String s = "package com.test.packagename;\n" +
                "int a = 10;";
        try {
            MangleParser mp = new MangleParser(s);
        } catch (ParseException e) {
            fail("We should only see a MangleException");
        } catch (MangleException e) {
            assertEquals(e.getMessage(), MangleParser.PACKAGE_ERR);
        }
    }
}
