package model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TabuleiroPrincipalTest {
	
	private TabuleiroPrincipal tabuleiro;

	@Before
	public void setUp() {
	    tabuleiro = new TabuleiroPrincipal();
	}

	@Test
	public void mover_paraParadaAdjacente() {
		Jogador jogador1 = new Jogador(Cor.AZUL, Herdeiro.ALTANI, new TabuleiroJogador(), tabuleiro.getParada("idA"));
		
		boolean mover = tabuleiro.mover(jogador1, tabuleiro.getParada("idB"));
		
		assertTrue("Movimento para parada adjacente deveria ser permitido", mover);
		assertEquals("Jogador deveria estar na parada B após mover", tabuleiro.getParada("idB"), jogador1.getParadaAtual());
	}

}
