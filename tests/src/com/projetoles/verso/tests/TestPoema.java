package com.projetoles.verso.tests;

import java.util.Calendar;

import com.projetoles.model.Poesia;

import junit.framework.TestCase;

public class TestPoema extends TestCase {
	protected Poesia poema;
	@Override
	public void setUp() {
		poema = new Poesia();
	}
	 
	public void testaCriarPoema() {
		poema = new Poesia("José", "eu", "Carlos Drummond de Andrade", "E agora, José?A festa acabou,a luz apagou,o povo sumiu,a noite esfriou,e agora, José?e agora, você?você que é sem nome,que zomba dos outros,você que faz versos,que ama protesta,e agora, José?Está sem mulher,está sem discurso,está sem carinho,já não pode beber,já não pode fumar,cuspir já não pode,a noite esfriou,o dia não veio,o bonde não veio,o riso não veio,não veio a utopia e tudo acabou e tudo fugiu e tudo mofou, e agora, José?E agora, José?Sua doce palavra,seu instante de febre,sua gula e jejum,sua biblioteca,sua lavra de ouro,seu terno de vidro, sua incoerência,seu ódio - e agora?Com a chave na mão quer abrir a porta,não existe porta;quer morrer no mar,mas o mar secou;quer ir para Minas,Minas não há mais.José, e agora?", Calendar.getInstance(), "drummond,josé");
		
		assertEquals("José", poema.getTitulo());
		assertEquals("Carlos Drummond de Andrade", poema.getAutor());
		assertEquals("E agora, José?A festa acabou,a luz apagou,o povo sumiu,a noite esfriou,e agora, José?e agora, você?você que é sem nome,que zomba dos outros,você que faz versos,que ama protesta,e agora, José?Está sem mulher,está sem discurso,está sem carinho,já não pode beber,já não pode fumar,cuspir já não pode,a noite esfriou,o dia não veio,o bonde não veio,o riso não veio,não veio a utopia e tudo acabou e tudo fugiu e tudo mofou, e agora, José?E agora, José?Sua doce palavra,seu instante de febre,sua gula e jejum,sua biblioteca,sua lavra de ouro,seu terno de vidro, sua incoerência,seu ódio - e agora?Com a chave na mão quer abrir a porta,não existe porta;quer morrer no mar,mas o mar secou;quer ir para Minas,Minas não há mais.José, e agora?", poema.getPoesia());
		assertEquals("drummond,josé", poema.getTags());
		assertEquals("eu", poema.getPostador());
	}
	
}