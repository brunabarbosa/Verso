package com.projetoles.verso.tests;

import junit.framework.TestCase;

public class TestSuite extends TestCase {
 
	public static void main(String[] args) {
		TestCase ti = new TestExample() {
			@Override
			public void runTest() {
				testExample();
			}
		};
		ti.run();
	}
	
}