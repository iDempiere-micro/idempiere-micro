/*
 *   Copyright 2005 The Apache Software Foundation
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package org.adempiere.osgi;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.osgi.framework.FrameworkUtil;

import java.io.Reader;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

/**
 * Imported from Apache Felix project.
 * Original file:
 * http://svn.apache.org/repos/asf/felix/trunk/mishell/src/main/java/org/apache/felix/mishell/OSGiScriptEngine.java
 *
 */


public class OSGiScriptEngine implements ScriptEngine{
	@SuppressFBWarnings("UUF_UNUSED_FIELD")
	private ScriptEngine engine;
	@SuppressFBWarnings("UUF_UNUSED_FIELD")
	private OSGiScriptEngineFactory factory;
	public OSGiScriptEngine(ScriptEngine engine, OSGiScriptEngineFactory factory){
		this.engine=engine;
		this.factory=factory;
	}
	public Bindings createBindings() {
		return engine.createBindings();
	}
	public Object eval(Reader reader, Bindings n) throws ScriptException {
		return engine.eval(reader, n);
	}
	public Object eval(Reader reader, ScriptContext context) throws ScriptException {
		return engine.eval(reader, context);
	}
	public Object eval(Reader reader) throws ScriptException {
		return engine.eval(reader);
	}
	public Object eval(String script, Bindings n) throws ScriptException {
		return engine.eval(script, n);
	}
	public Object eval(String script, ScriptContext context) throws ScriptException {
		return engine.eval(script, context);
	}
	public Object eval(String script) throws ScriptException {
		return engine.eval(script);
	}
	public Object get(String key) {
		return engine.get(key);
	}
	public Bindings getBindings(int scope) {
		return engine.getBindings(scope);
	}
	public ScriptContext getContext() {
		return engine.getContext();
	}
	public ScriptEngineFactory getFactory() {
		return factory;
	}
	public void put(String key, Object value) {
		engine.put(key, value);
	}
	public void setBindings(Bindings bindings, int scope) {
		engine.setBindings(bindings, scope);
	}
	public void setContext(ScriptContext context) {
		engine.setContext(context);
	}

	/** Get script engine, checking classpath first, and then osgi plugins
	 *
	 * @param engineName
	 * @return ScriptEngine found, or null
	 */
	public static ScriptEngine getScriptEngine(String engineName)
	{
		OSGiScriptEngineManager osgiFactory = new OSGiScriptEngineManager( FrameworkUtil.getBundle(OSGiScriptEngine.class).getBundleContext());
		ScriptEngine engine = osgiFactory.getEngineByName(engineName);

		return engine;
	}
}
