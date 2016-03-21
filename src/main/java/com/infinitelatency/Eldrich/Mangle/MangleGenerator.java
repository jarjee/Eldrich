/* Licensed under Apache-2.0 */
package com.infinitelatency.Eldrich.Mangle;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

public class MangleGenerator {
  final private MangleParser mp;

  private PackageDeclaration pakage = new PackageDeclaration();
  private List<ImportDeclaration> imports;
  private List<TypeDeclaration> types;

  MangleGenerator(String program) throws MangleException {
    mp = new MangleParser(program);

    if (mp.status()) {
      imports = mp.imports();


    } else {
      InputStream stream = new ByteArrayInputStream(program.getBytes(StandardCharsets.UTF_8));
      try {
        CompilationUnit cu = JavaParser.parse(stream);
        this.pakage = cu.getPackage();
        this.imports = cu.getImports();
        this.types = cu.getTypes();
      } catch (ParseException e) {
        throw new MangleException("We parsed this successfully previously, so this is unexpected");
      }
    }
  }

  CompilationUnit generate() {
    return new CompilationUnit(pakage, imports, types);
  }
}
