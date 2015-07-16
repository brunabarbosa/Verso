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
		poema = new Poesia("Jos�", "eu", "Carlos Drummond de Andrade", "E agora, Jos�?A festa acabou,a luz apagou,o povo sumiu,a noite esfriou,e agora, Jos�?e agora, voc�?voc� que � sem nome,que zomba dos outros,voc� que faz versos,que ama protesta,e agora, Jos�?Est� sem mulher,est� sem discurso,est� sem carinho,j� n�o pode beber,j� n�o pode fumar,cuspir j� n�o pode,a noite esfriou,o dia n�o veio,o bonde n�o veio,o riso n�o veio,n�o veio a utopia e tudo acabou e tudo fugiu e tudo mofou, e agora, Jos�?E agora, Jos�?Sua doce palavra,seu instante de febre,sua gula e jejum,sua biblioteca,sua lavra de ouro,seu terno de vidro, sua incoer�ncia,seu �dio - e agora?Com a chave na m�o quer abrir a porta,n�o existe porta;quer morrer no mar,mas o mar secou;quer ir para Minas,Minas n�o h� mais.Jos�, e agora?", Calendar.getInstance(), "drummond,jos�");
		
		assertEquals("Jos�", poema.getTitulo());
		assertEquals("Carlos Drummond de Andrade", poema.getAutor());
		assertEquals("E agora, Jos�?A festa acabou,a luz apagou,o povo sumiu,a noite esfriou,e agora, Jos�?e agora, voc�?voc� que � sem nome,que zomba dos outros,voc� que faz versos,que ama protesta,e agora, Jos�?Est� sem mulher,est� sem discurso,est� sem carinho,j� n�o pode beber,j� n�o pode fumar,cuspir j� n�o pode,a noite esfriou,o dia n�o veio,o bonde n�o veio,o riso n�o veio,n�o veio a utopia e tudo acabou e tudo fugiu e tudo mofou, e agora, Jos�?E agora, Jos�?Sua doce palavra,seu instante de febre,sua gula e jejum,sua biblioteca,sua lavra de ouro,seu terno de vidro, sua incoer�ncia,seu �dio - e agora?Com a chave na m�o quer abrir a porta,n�o existe porta;quer morrer no mar,mas o mar secou;quer ir para Minas,Minas n�o h� mais.Jos�, e agora?", poema.getPoesia());
		assertEquals("drummond,jos�", poema.getTags());
		assertEquals("eu", poema.getPostador());
	}
	
}