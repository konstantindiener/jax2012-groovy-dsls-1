package de.coinor.groovydsl

abstract class DslDefinition extends Script {

	def Wenn(Map m, boolean condition) {
		if (condition) {
			m.Dann
		}
		else {
			m.Sonst
		}
	}
}
