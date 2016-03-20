/* Licensed under Apache-2.0 */
package com.infinitelatency.Eldrich;

import java.io.Reader;
import java.util.HashMap;

import javax.script.*;

public class JavaEngine extends AbstractScriptEngine implements Compilable, Invocable, AutoCloseable {

    private final JavaEngineFactory jef;
    private final Bindings binds = createBindings();
    private final HashMap<Function, Class<?>> functions = new HashMap<>();

    public JavaEngine(JavaEngineFactory jef) {
        this.jef = jef;
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public CompiledScript compile(String s) throws ScriptException {
        return null;
    }

    @Override
    public CompiledScript compile(Reader reader) throws ScriptException {
        return null;
    }

    @Override
    public Object invokeMethod(Object o, String s, Object... objects) throws ScriptException, NoSuchMethodException {
        return null;
    }

    @Override
    public Object invokeFunction(String s, Object... objects) throws ScriptException, NoSuchMethodException {
        return null;
    }

    @Override
    public <T> T getInterface(Class<T> aClass) {
        return null;
    }

    @Override
    public <T> T getInterface(Object o, Class<T> aClass) {
        return null;
    }

    /* TODO: A series of evals should carry values
    JavaEngine t = new JavaEngine();
    t.eval("int a = 10;");
    t.eval("System.out.println("a"));
    //Should print 10
     */

    @Override
    public Object eval(String s, ScriptContext scriptContext) throws ScriptException {
        return compile(s).eval(scriptContext);
    }

    @Override
    public Object eval(Reader reader, ScriptContext scriptContext) throws ScriptException {
        return compile(reader).eval(scriptContext);
    }

    @Override
    public Bindings createBindings() {
        return new SimpleBindings();
    }

    @Override
    public ScriptEngineFactory getFactory() {
        return jef;
    }
}
