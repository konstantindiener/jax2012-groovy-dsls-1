package de.coinor.groovydsl;

import static org.junit.Assert.assertEquals;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.junit.Test;

public class JavaDslTestCase {

	@Test
	public void testNettoeinkommen() throws CompilationFailedException, IOException {
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("monatlichesBruttoeinkommen", 2300.0);
		p.put("Steuern", 210.0);
		p.put("Sozialabgaben", 400.0);
		p.put("sonstigeBelastungen", 160.0);
		
		Object result = runDsl("nettoEinkommen.formel", p);
		
		assertEquals(Double.valueOf(1530.0), result);
	}
	
	@Test
	public void testDelta1() throws CompilationFailedException, IOException {
		// Der aufruf der automatisch erstellten Setter kann in Eclipse zu Compiler-Fehlern führen,
		// die aber im Maven Build nicht auftreten.
		
		ImmobilieObject im = new ImmobilieObject();
		im.setMonatlicheMiete(430.0);
		im.setSelbstgenutzt(true);
		
		AntragstellerObject an = new AntragstellerObject();
		an.setNettoEinkommen(1530.0);

		Map<String, Object> p = new HashMap<String, Object>();
		p.put("Immobilie", im);
		p.put("Antragsteller", an);
		p.put("Kreditrate", 270.0);
		
		Object result = runDsl("delta.formel", p);
		
		assertEquals(Double.valueOf(1260.0), result);
	}
	
	@Test
	public void testDelta2() throws CompilationFailedException, IOException {
		// Der aufruf der automatisch erstellten Setter kann in Eclipse zu Compiler-Fehlern führen,
		// die aber im Maven Build nicht auftreten.
		
		ImmobilieObject im = new ImmobilieObject();
		im.setMonatlicheMiete(430.0);
		im.setSelbstgenutzt(false);
		
		AntragstellerObject an = new AntragstellerObject();
		an.setNettoEinkommen(1530.0);

		Map<String, Object> p = new HashMap<String, Object>();
		p.put("Immobilie", im);
		p.put("Antragsteller", an);
		p.put("Kreditrate", 270.0);
		
		Object result = runDsl("delta.formel", p);
		
		assertEquals(Double.valueOf(1690.0), result);
	}
	

	Object runDsl(String name, Map<String, Object> properties) throws CompilationFailedException, IOException {
		Binding b = new Binding();
		for (Entry<String, Object> entry : properties.entrySet()) {
			b.setVariable(entry.getKey(), entry.getValue());
		}
		
		CompilerConfiguration conf = new CompilerConfiguration();
		conf.setScriptBaseClass(DslDefinition.class.getName());
		GroovyShell gs = new GroovyShell(b, conf);
		
		Script s = gs.parse(new File(name));
		
		return s.run();
	}
}
