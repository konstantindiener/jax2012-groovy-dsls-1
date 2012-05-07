package de.coinor.groovydsl;

import static org.junit.Assert.assertEquals;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.junit.Test;

public class JavaDslTestCase {

	@Test
	public void testNettoEinkommen() throws CompilationFailedException, IOException {
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("monatlichesBruttoeinkommen", 2300.0);
		p.put("Steuern", 210.0);
		p.put("Sozialabgaben", 400.0);
		p.put("sonstigeBelastungen", 160.0);
		
		Object result = runDsl("nettoEinkommen.formel", p);
		
		assertEquals(1530.0, result);
	}
	
/*	@Test
	public void testDelta1() {
		def result = runDsl("delta.formel", [
			Immobilie: new ImmobilieObject(
				selbgenutzt: true,
				monatlicheMiete: 430.0),
			Antragsteller: new AntragstellerObject(
				nettoEinkommen: 1530.0),
			Kreditrate: 270.0])
		
		assert result == 1260.0
	}
	
	@Test
	public void testDelta2() {
		def result = runDsl("delta.formel", [
			Immobilie: new ImmobilieObject(
				selbgenutzt: false,
				monatlicheMiete: 430.0),
			Antragsteller: new AntragstellerObject(
				nettoEinkommen: 1530.0),
			Kreditrate: 270.0])
		
		assert result == 1690.0
	}*/

	private Object runDsl(String scriptName, Map<String, Object> p)
			throws CompilationFailedException, IOException {
		
		Binding b = new Binding(p);
		
		CompilerConfiguration cs = new CompilerConfiguration();
		cs.setScriptBaseClass(DslDefinition.class.getName());
		
		GroovyShell gs = new GroovyShell(b, cs);
		Script s = gs.parse(new File(scriptName));
		
		return s.run();
	}
}
