/* Licensed under Apache-2.0 */
package com.infinitelatency.Eldrich;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

class JavaEngineFactory implements ScriptEngineFactory {
  private static final JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
  private static final char[] symbols;
  private static final Random random = new Random();

  static {
    StringBuilder tmp = new StringBuilder();
    for (char ch = 'A'; ch <= 'Z'; ch++)
      tmp.append(ch);

    for (char ch = 'a'; ch <= 'z'; ch++)
      tmp.append(ch);

    symbols = tmp.toString().toCharArray();
  }

  @Override
  public String getEngineName() {
    return "Eldrich";
  }

  @Override
  public String getEngineVersion() {
    return Collections.max(javac.getSourceVersions()).toString();
  }

  @Override
  public List<String> getExtensions() {
    return Collections.unmodifiableList(Arrays.asList("java"));
  }

  @Override
  public List<String> getMimeTypes() {
    return Collections.unmodifiableList(
        Arrays.asList("text/java", "application/java", "text/x-java", "application/x-java"));
  }

  @Override
  public List<String> getNames() {
    return Collections.unmodifiableList(Arrays.asList("java", "JavaScripting"));
  }

  @Override
  public String getLanguageName() {
    return "java";
  }

  @Override
  public String getLanguageVersion() {
    return Collections.max(javac.getSourceVersions()).toString();
  }

  @Override
  public Object getParameter(String key) {
    switch (key) {
      case (ScriptEngine.ENGINE):
        return getEngineName();
      case (ScriptEngine.ENGINE_VERSION):
        return getEngineVersion();
      case (ScriptEngine.NAME):
        return getEngineName();
      case (ScriptEngine.LANGUAGE):
        return getLanguageName();
      case (ScriptEngine.LANGUAGE_VERSION):
        return getLanguageVersion();
      case ("THREADING"):
        return "MULTITHREADED";
      default:
        return null;
    }
  }

  //Based off the getMethodCallSyntax in the Jython project
  @Override
  public String getMethodCallSyntax(String s, String s1, String... strings) {
    StringBuilder buffer = new StringBuilder();
    buffer.append(String.format("%s.%s(", s, s1));
    int i = strings.length;
    for (String arg : strings) {
      buffer.append(arg);
      if (i-- > 0) {
        buffer.append(", ");
      }
    }
    buffer.append(");");
    return buffer.toString();
  }

  @Override
  public String getOutputStatement(String s) {
    return String.format("System.out.println(\"%s\");", s);
  }

  @Override
  public String getProgram(String... strings) {
    StringBuilder sb = new StringBuilder();
    String classname = generateClassname();
    sb.append("public class ");
    sb.append(classname);
    sb.append(" implements Runnable {\n");
    sb.append("   public void run() {\n");
    for (String s : strings) {
      sb.append(String.format("      %s\n", s));
    }
    sb.append("   }\n");
    sb.append("}");
    return sb.toString();
  }

  private String generateClassname() {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < 20; i++) {
      sb.append(symbols[random.nextInt(symbols.length)]);
    }
    return sb.toString();
  }

  @Override
  public ScriptEngine getScriptEngine() {
    return new JavaEngine(this);
  }
}
