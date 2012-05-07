package de.coinor.groovydsl

import org.codehaus.groovy.control.CompilerConfiguration;
import org.junit.Test;

class DslTestCase extends GroovyTestCase {

	@Test
	public void testNettoEinkommen() {
		def result = runDsl("nettoEinkommen.formel",
			[
				monatlichesBruttoeinkommen: 2300.0,
				Steuern: 210.0,
				Sozialabgaben: 400.0,
				sonstigeBelastungen: 160.0
				])
		
		assert result == 1530.0
	}
	
	@Test
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
	}

	def runDsl(String scriptName, def p) {
		Binding b = new Binding()
		p.each { key, value ->
			b.setVariable(key, value)
		}
		
		CompilerConfiguration cs = new CompilerConfiguration(
			scriptBaseClass: DslDefinition.class.name)
		
		GroovyShell gs = new GroovyShell(b, cs)
		Script s = gs.parse(new File(scriptName))
		
		def result = s.run()
	}
}
