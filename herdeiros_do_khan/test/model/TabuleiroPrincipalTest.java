package model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class TabuleiroPrincipalTest {

	private static final int DEFAULT_TIMEOUT = 2000;

	private TabuleiroPrincipal tabuleiro;

	@Before
	public void setUp() {
		tabuleiro = new TabuleiroPrincipal();
	}

	// ---------- helpers ----------

	private Jogador criarJogadorChagatai(String idParada) {
		return new Jogador(Cor.AZUL, Herdeiro.CHAGATAI,new TabuleiroJogador(Herdeiro.CHAGATAI, Map.of()),tabuleiro.getParada(idParada));
	}

	private List<Parada> caminho(String... idsParadas) {
		List<Parada> caminho = new ArrayList<>();
		for (String id : idsParadas) {
			caminho.add(tabuleiro.getParada(id));
		}
		return caminho;
	}

	private void atacarCidadeVariasVezes(Jogador jogador, Cidade cidade, int vezes) {
		for (int i = 0; i < vezes; i++) {
			tabuleiro.atacarCidade(jogador, cidade, cidade.getTesouros().get(0));
		}
	}

	// ---------- movimentação ----------

	@Test(timeout = DEFAULT_TIMEOUT)
	public void mover_comUmMovimentoParaParadaAdjacente_jogadorChegaNaParadaB() {
		Jogador jogador = criarJogadorChagatai("idA");
		jogador.getTurno().adicionarMovimentos(1);

		boolean mover = tabuleiro.mover(jogador, caminho("idB"));

		assertTrue("Movimento para parada adjacente deveria ser permitido", mover);
		assertEquals("Jogador deveria estar na parada B após mover",tabuleiro.getParada("idB"), jogador.getParadaAtual());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void mover_usandoYurtDoJogadorParaPular_jogadorChegaNaParadaE() {
		Jogador jogador = criarJogadorChagatai("idA");
		jogador.getTurno().adicionarMovimentos(1);
		tabuleiro.getParada("idB").addYurt(jogador);

		boolean mover = tabuleiro.mover(jogador, caminho("idB", "idE"));

		assertTrue("Movimento pulando pela yurt deveria ser permitido", mover);
		assertEquals("Jogador deveria estar na parada E após pular pela yurt",tabuleiro.getParada("idE"), jogador.getParadaAtual());
	}

	// ---------- habilidade do Altani ----------

	@Test(expected = IllegalStateException.class, timeout = DEFAULT_TIMEOUT)
	public void altani_comecandoEmParadaSimples_lancaExcecao() {
		new Jogador(Cor.VERDE, Herdeiro.ALTANI,new TabuleiroJogador(Herdeiro.ALTANI, Map.of()),tabuleiro.getParada("idA"));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void altani_comecandoEmKarakorum_temDoisPeoesNaKarakorum() {
		Karakorum karakorum = new Karakorum("K");
		Jogador altani = new Jogador(Cor.VERDE, Herdeiro.ALTANI,new TabuleiroJogador(Herdeiro.ALTANI, Map.of()),karakorum);

		assertEquals("Quantidade de peões do jogador Altani",2, altani.quantidadePeoes());
		assertEquals("Quantidade de peões em Karakorum",2, karakorum.getQtdJogadores());
	}

	// ---------- ataque a cidades ----------

	@Test(timeout = DEFAULT_TIMEOUT)
	public void atacarCidade_comEspadasSuficientes_removeUmTesouroDaCidade() {
		Jogador jogador = criarJogadorChagatai("idA");
		jogador.adicionarTributo(TipoTributo.ESPADA, 2);
		Cidade cidade = tabuleiro.getCidadesAtivas().get(0);
		TipoTesouro tesouro = cidade.getTesouros().get(0);
		int qtdTesourosAntes = cidade.getTesouros().size();

		boolean ataque = tabuleiro.atacarCidade(jogador, cidade, tesouro);
		int qtdTesourosDepois = cidade.getTesouros().size();

		assertTrue("Ataque deveria ser permitido", ataque);
		assertEquals("Quantidade de tesouros da cidade após o ataque",qtdTesourosAntes - 1, qtdTesourosDepois);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void atacarCidade_ateEsgotarOsTesouros_conquistaACidadeERepoeDaFila() {
		Jogador jogador = criarJogadorChagatai("idA");
		jogador.adicionarTributo(TipoTributo.ESPADA, 20);
		Cidade cidade = tabuleiro.getCidadesAtivas().get(0);

		atacarCidadeVariasVezes(jogador, cidade, 4);
		Cidade cidadeNova = tabuleiro.getCidadesAtivas().get(0);

		assertEquals("Cidade deveria ter a yurt de conquista do jogador",jogador, cidade.getYurtConquista());
		assertTrue("Cidade deveria estar entre as conquistadas",tabuleiro.getCidadesConquistadas().contains(cidade));
		assertFalse("Cidade não deveria mais estar entre as ativas",tabuleiro.getCidadesAtivas().contains(cidade));
		assertEquals("Cidade nova deveria ser a que estava na fila",NomeCidade.KAESONG, cidadeNova.getNome());
		assertEquals("Cidade nova deveria ter 4 tesouros",4, cidadeNova.getTesouros().size());
	}

	// ---------- tributo ----------

	@Test(timeout = DEFAULT_TIMEOUT)
	public void pegarTributo_depoisDeVisitarAParada_jogadorGanhaOTributo() {
		Jogador jogador = criarJogadorChagatai("idA");
		jogador.getTurno().registrarVisita(tabuleiro.getParada("idB"));
		int qtdAntes = jogador.quantidadeDe(TipoTributo.ESPADA);
		Provincia provincia = tabuleiro.getParada("idB").getProvinciasAdjacentes().get(0);

		boolean tributo = tabuleiro.pegarTributo(jogador, provincia, TipoTributo.ESPADA);
		int qtdDepois = jogador.quantidadeDe(TipoTributo.ESPADA);

		assertTrue("Tributo deveria ter sido recolhido", tributo);
		assertEquals("Província deveria ter perdido o tributo",0, provincia.getQtdTributo());
		assertEquals("Quantidade de espadas do jogador após pegar o tributo",qtdAntes + 1, qtdDepois);
	}
	
	
}


