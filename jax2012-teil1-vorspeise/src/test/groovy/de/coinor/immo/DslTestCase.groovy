package de.coinor.immo

import org.junit.Test;

class DslTestCase extends GroovyTestCase {

	@Test
	public void testNettoeinkommen() {
		Binding b = new Binding()
		b.setVariable("monatlichesBruttoeinkommen", 2300.0)
		b.setVariable("Steuern", 210.0)
		b.setVariable("Sozialabgaben", 400.0)
		b.setVariable("sonstigeBelastungen", 160.0)
		GroovyShell gs = new GroovyShell(b)
		
		
		Script s = gs.parse(new File("monatlichesNettoeinkommen.formel"))
		def result = s.run();
		
		assert result == 1530.0;
	}
}
