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
		
		assertTrue("Movimento deveria ser permitido", mover);
		assertEquals("Jogador deveria estar na parada E", tabuleiro.getParada("idE"), jogador1.getParadaAtual());
	}
	
	@Test(expected = IllegalStateException.class)
	public void altaniNaoComecaEmParadaSimples() {
	    new Jogador(Cor.VERDE, Herdeiro.ALTANI,
	        new TabuleiroJogador(Herdeiro.ALTANI, Map.of()), tabuleiro.getParada("idA"));
	}

	@Test
	public void altaniComecaComDoisPeoesEmKarakorum() {
	    Karakorum k = new Karakorum("K");
	    Jogador j = new Jogador(Cor.VERDE, Herdeiro.ALTANI,
	        new TabuleiroJogador(Herdeiro.ALTANI, Map.of()), k);
	    assertEquals(2, j.quantidadePeoes());
	    assertEquals(2, k.getQtdJogadores());
	}
	
	@Test
	public void atacarCidade() {
		Jogador jogador1 = new Jogador(Cor.AZUL, Herdeiro.CHAGATAI, new TabuleiroJogador(Herdeiro.CHAGATAI, Map.of()), tabuleiro.getParada("idA"));
		jogador1.adicionarTributo(TipoTributo.ESPADA, 2);
		
		Cidade cidade = tabuleiro.getCidadesAtivas().get(0);
		TipoTesouro tesouro = cidade.getTesouros().get(0);
		int qtdTesourosAntes = cidade.getTesouros().size();
		
		boolean ataque = tabuleiro.atacarCidade(jogador1, cidade, tesouro);
		int qtdTesourosDepois = cidade.getTesouros().size();
		
		assertTrue("Ataque deveria ser permitido", ataque);
		assertEquals(qtdTesourosAntes-1, qtdTesourosDepois);
	}
	
	@Test
	public void atacarEConquistarCidade() {
		Jogador jogador1 = new Jogador(Cor.AZUL, Herdeiro.CHAGATAI, new TabuleiroJogador(Herdeiro.CHAGATAI, Map.of()), tabuleiro.getParada("idA"));
		jogador1.adicionarTributo(TipoTributo.ESPADA, 20);
		
		Cidade cidade = tabuleiro.getCidadesAtivas().get(0);
		
		for (int i = 0; i < 4; i++) {
			tabuleiro.atacarCidade(jogador1, cidade, cidade.getTesouros().get(0));
		}
		
		Cidade cidadeNova = tabuleiro.getCidadesAtivas().get(0);
		
		assertEquals("Cidade tem yurt do jogador", jogador1, cidade.getYurtConquista());
		assertTrue("Cidade foi conquistada", tabuleiro.getCidadesConquistadas().contains(cidade));
		assertFalse("Cidade não está mais nas cidades ativas", tabuleiro.getCidadesAtivas().contains(cidade));
		assertEquals("Cidade nova deveria ser a que estava na fila", NomeCidade.KAESONG, cidadeNova.getNome());
		assertEquals("Cidade nova deveria ter 4 tesouros", 4, cidadeNova.getTesouros().size());
	}
	
	@Test
	public void pegarTributo() {
		Jogador jogador1 = new Jogador(Cor.AZUL, Herdeiro.CHAGATAI, new TabuleiroJogador(Herdeiro.CHAGATAI, Map.of()), tabuleiro.getParada("idA"));
		jogador1.getTurno().registrarVisita(tabuleiro.getParada("idB"));
		int qtdAntes = jogador1.quantidadeDe(TipoTributo.ESPADA);
		
		Provincia provincia = tabuleiro.getParada("idB").getProvinciasAdjacentes().get(0);
		
		boolean tributo = tabuleiro.pegarTributo(jogador1, provincia, TipoTributo.ESPADA);
		int qtdDepois = jogador1.quantidadeDe(TipoTributo.ESPADA);
		
		assertTrue("Tributo foi recolhido", tributo);
		assertEquals("Provincia perdeu o tributo", 0, provincia.getQtdTributo());
		assertEquals("Jogador ganhou tributo", qtdAntes+1, qtdDepois);
	}

}
