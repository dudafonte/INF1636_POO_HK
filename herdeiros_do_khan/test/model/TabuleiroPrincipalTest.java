package model;

import java.util.Map;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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
		Jogador jogador1 = new Jogador(Cor.AZUL, Herdeiro.CHAGATAI, new TabuleiroJogador(Herdeiro.CHAGATAI, Map.of()), tabuleiro.getParada("idA"));
		jogador1.getTurno().adicionarMovimentos(1);
		
		List<Parada> caminho = new ArrayList<>();
		caminho.add(tabuleiro.getParada("idB"));
		
		boolean mover = tabuleiro.mover(jogador1, caminho);
		
		assertTrue("Movimento para parada adjacente deveria ser permitido", mover);
		assertEquals("Jogador deveria estar na parada B após mover", tabuleiro.getParada("idB"), jogador1.getParadaAtual());
	}
	
	@Test
	public void mover_usandoYurtParaPular() {
		Jogador jogador1 = new Jogador(Cor.AZUL, Herdeiro.CHAGATAI, new TabuleiroJogador(Herdeiro.CHAGATAI, Map.of()), tabuleiro.getParada("idA"));
		jogador1.getTurno().adicionarMovimentos(1);
		
		List<Parada> caminho = new ArrayList<>();
		caminho.add(tabuleiro.getParada("idB"));
		caminho.add(tabuleiro.getParada("idE"));
		
		tabuleiro.getParada("idB").addYurt(jogador1);
		
		boolean mover = tabuleiro.mover(jogador1, caminho);
		
		assertTrue("Movimento para deveria ser permitido", mover);
		assertEquals("Jogador deveria estar na parada E", tabuleiro.getParada("idE"), jogador1.getParadaAtual());
	}
	
	@Test(expected = IllegalStateException.class)
	public void altaniNaoComecaEmParadaSimples() {
	    new Jogador(Cor.AMARELO, Herdeiro.ALTANI,
	        new TabuleiroJogador(Herdeiro.ALTANI, Map.of()), tabuleiro.getParada("idA"));
	}

	@Test
	public void altaniComecaComDoisPeoesEmKarakorum() {
	    Karakorum k = new Karakorum("K");
	    Jogador j = new Jogador(Cor.AMARELO, Herdeiro.ALTANI,
	        new TabuleiroJogador(Herdeiro.ALTANI, Map.of()), k);
	    assertEquals(2, j.quantidadePeoes());
	    assertEquals(2, k.getQtdJogadores());
	}

}
