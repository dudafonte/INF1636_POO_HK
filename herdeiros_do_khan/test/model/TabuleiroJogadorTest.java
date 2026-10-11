package model;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class TabuleiroJogadorTest {

	private static final int DEFAULT_TIMEOUT = 2000;

	private TabuleiroJogador chagatai;

	@Before
	public void setUp() {
		chagatai = criarTabuleiro(Herdeiro.CHAGATAI);
	}

	// ---------- helpers ----------

	private TabuleiroJogador criarTabuleiro(Herdeiro h) {
		Map<TipoColuna, Map<TipoAcao, Integer>> base = Map.of(
			TipoColuna.AMARELA, Map.of(TipoAcao.MOVER, 3),
			TipoColuna.VERMELHA, Map.of(TipoAcao.MOVER, 2, TipoAcao.PEGAR_TRIBUTO, 2),
			TipoColuna.VERDE, Map.of(TipoAcao.USAR_KHAN, 1),
			TipoColuna.BRANCA, Map.of(TipoAcao.RECUPERAR_FICHAS, 1)
		);
		return new TabuleiroJogador(h, base);
	}

	private Melhoria melhoria(TipoMelhoria tipo, TipoColuna cor) {
		return new Melhoria(tipo, cor);
	}

	private void comprar(TabuleiroJogador t, TipoMelhoria tipo, TipoColuna cor, EstadoDoTurno turno) {
		t.adicionarMelhoriaComprada(melhoria(tipo, cor), cor, turno);
	}

	// compra uma melhoria em cada coluna colorida ("linha completa")
	private void comprarLinha(TabuleiroJogador t, EstadoDoTurno turno) {
		comprar(t, TipoMelhoria.MOVIMENTOS_EXTRA, TipoColuna.AMARELA, turno);
		comprar(t, TipoMelhoria.MOVIMENTOS_EXTRA, TipoColuna.VERDE, turno);
		comprar(t, TipoMelhoria.MOVIMENTOS_EXTRA, TipoColuna.VERMELHA, turno);
	}

	// ativa uma coluna simulando um turno novo
	private void ativarEmNovoTurno(TipoColuna tipo) {
		chagatai.ativarColuna(tipo, new EstadoDoTurno());
	}

	private void comprarMelhoriasNaAmarela(int quantidade) {
		EstadoDoTurno turno = new EstadoDoTurno();
		for (int i = 0; i < quantidade; i++) {
			comprar(chagatai, TipoMelhoria.MOVIMENTOS_EXTRA, TipoColuna.AMARELA, turno);
		}
	}

	// devolve os nomes das colunas indisponíveis ("" se todas estiverem disponíveis)
	private String colunasIndisponiveis() {
		StringBuilder nomes = new StringBuilder();
		for (ColunaDeAcao c : chagatai.getColunas()) {
			if (!c.isDisponivel()) {
				nomes.append(c.getCor()).append(" ");
			}
		}
		return nomes.toString().trim();
	}

	// ---------- ativação de colunas ----------

	@Test(expected = IllegalStateException.class, timeout = DEFAULT_TIMEOUT)
	public void ativarColuna_colunaJaUsadaLancaExcecao() {
		ativarEmNovoTurno(TipoColuna.AMARELA);
		ativarEmNovoTurno(TipoColuna.AMARELA);
	}

	@Test(expected = IllegalStateException.class, timeout = DEFAULT_TIMEOUT)
	public void ativarColuna_soUmaColunaPorTurno() {
		EstadoDoTurno turno = new EstadoDoTurno();
		chagatai.ativarColuna(TipoColuna.AMARELA, turno);
		chagatai.ativarColuna(TipoColuna.VERDE, turno);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void ativarColuna_comIconeKhanMarcaKhanPendente() {
		EstadoDoTurno turno = new EstadoDoTurno();
		chagatai.ativarColuna(TipoColuna.VERDE, turno);

		assertTrue("Khan deveria estar pendente após ativar a coluna verde", turno.isKhanPendente());
		assertFalse("Não deveria ser possível finalizar o turno com Khan pendente", turno.podeFinalizarTurno());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void colunaBranca_recuperaTodasAsFichasInclusiveAUsada() {
		ativarEmNovoTurno(TipoColuna.AMARELA);
		ativarEmNovoTurno(TipoColuna.VERMELHA);
		ativarEmNovoTurno(TipoColuna.VERDE);
		assertEquals("Peças de ativação disponíveis após usar três colunas",1, chagatai.getPecasAtivacaoDisponiveis());

		ativarEmNovoTurno(TipoColuna.BRANCA);

		assertEquals("Peças de ativação disponíveis após usar a coluna branca",4, chagatai.getPecasAtivacaoDisponiveis());
		assertEquals("Colunas indisponíveis após usar a coluna branca","", colunasIndisponiveis());
	}

	// ---------- melhorias: bloqueio no turno da compra ----------

	@Test(timeout = DEFAULT_TIMEOUT)
	public void melhoriaComprada_naoValeNoMesmoTurno_eValeDepoisDeLiberada() {
		comprar(chagatai, TipoMelhoria.MOVIMENTOS_EXTRA, TipoColuna.AMARELA, new EstadoDoTurno());

		// melhoria ainda bloqueada: só os 3 movimentos base
		EstadoDoTurno turnoDaCompra = new EstadoDoTurno();
		chagatai.ativarColuna(TipoColuna.AMARELA, turnoDaCompra);
		assertEquals("Movimentos no turno da compra (melhoria ainda bloqueada)",3, turnoDaCompra.getMovimentosRestantes());

		// próximo turno: recupera a ficha e libera a melhoria
		chagatai.recuperarTodasFichas();
		chagatai.liberarMelhoriasInicioTurno();
		EstadoDoTurno turnoSeguinte = new EstadoDoTurno();
		chagatai.ativarColuna(TipoColuna.AMARELA, turnoSeguinte);

		assertEquals("Movimentos no turno seguinte (melhoria liberada)",5, turnoSeguinte.getMovimentosRestantes());
	}

	// ---------- melhorias: restrições de coluna ----------

	@Test(expected = IllegalStateException.class, timeout = DEFAULT_TIMEOUT)
	public void melhoria_corIncompativelComAColunaLancaExcecao() {
		chagatai.adicionarMelhoriaComprada(melhoria(TipoMelhoria.MOVIMENTOS_EXTRA, TipoColuna.VERDE),TipoColuna.AMARELA, new EstadoDoTurno());
	}

	@Test(expected = IllegalStateException.class, timeout = DEFAULT_TIMEOUT)
	public void melhoria_compradaNaColunaBrancaLancaExcecao() {
		chagatai.adicionarMelhoriaComprada(melhoria(TipoMelhoria.MOVIMENTOS_EXTRA, TipoColuna.BRANCA),TipoColuna.BRANCA, new EstadoDoTurno());
	}

	@Test(expected = IllegalStateException.class, timeout = DEFAULT_TIMEOUT)
	public void colunaCheia_naoAceitaQuintaMelhoria() {
		comprarMelhoriasNaAmarela(5);
	}

	// ---------- linha completa e melhoria grátis ----------

	@Test(timeout = DEFAULT_TIMEOUT)
	public void linhaIncompleta_naoDaMelhoriaGratis() {
		EstadoDoTurno turno = new EstadoDoTurno();
		comprar(chagatai, TipoMelhoria.MOVIMENTOS_EXTRA, TipoColuna.AMARELA, turno);
		comprar(chagatai, TipoMelhoria.MOVIMENTOS_EXTRA, TipoColuna.VERDE, turno);

		assertEquals("Melhorias gratuitas pendentes com linha incompleta",0, turno.getMelhoriasGratuitasPendentes());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void linhaCompleta_daUmaMelhoriaGratisPendente() {
		EstadoDoTurno turno = new EstadoDoTurno();
		comprarLinha(chagatai, turno);

		assertEquals("Melhorias gratuitas pendentes após linha completa",1, turno.getMelhoriasGratuitasPendentes());
		assertEquals("Melhorias na coluna branca antes de resgatar a grátis",0, chagatai.getColuna(TipoColuna.BRANCA).quantidadeMelhorias());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void segundaLinhaCompleta_daOutraMelhoriaGratis() {
		EstadoDoTurno turno = new EstadoDoTurno();
		comprarLinha(chagatai, turno);
		chagatai.resgatarMelhoriaGratuita(melhoria(TipoMelhoria.TRIBUTO_EXTRA, TipoColuna.BRANCA), turno);
		assertEquals("Melhorias gratuitas pendentes após resgatar a primeira",0, turno.getMelhoriasGratuitasPendentes());

		comprarLinha(chagatai, turno);

		assertEquals("Melhorias gratuitas pendentes após a segunda linha completa",1, turno.getMelhoriasGratuitasPendentes());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void resgatarMelhoriaGratuita_adicionaNaBrancaEConsomePendente() {
		EstadoDoTurno turno = new EstadoDoTurno();
		comprarLinha(chagatai, turno);

		chagatai.resgatarMelhoriaGratuita(melhoria(TipoMelhoria.TRIBUTO_EXTRA, TipoColuna.BRANCA), turno);

		assertEquals("Melhorias na coluna branca após resgatar a grátis",1, chagatai.getColuna(TipoColuna.BRANCA).quantidadeMelhorias());
		assertEquals("Melhorias gratuitas pendentes após o resgate",0, turno.getMelhoriasGratuitasPendentes());
	}

	@Test(expected = IllegalStateException.class, timeout = DEFAULT_TIMEOUT)
	public void resgatarMelhoriaGratuita_semPendenciaLancaExcecao() {
		chagatai.resgatarMelhoriaGratuita(melhoria(TipoMelhoria.TRIBUTO_EXTRA, TipoColuna.BRANCA), new EstadoDoTurno());
	}

	// ---------- habilidade do Jochi ----------

	@Test(timeout = DEFAULT_TIMEOUT)
	public void jochi_comecaComEspacoDaColunaBrancaOcupado() {
		TabuleiroJogador jochi = criarTabuleiro(Herdeiro.JOCHI);
		EstadoDoTurno turno = new EstadoDoTurno();
		comprarLinha(jochi, turno);

		assertEquals("Melhorias gratuitas pendentes para Jochi na primeira linha completa",0, turno.getMelhoriasGratuitasPendentes());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void jochi_segundaLinhaCompletaDaMelhoriaGratis() {
		TabuleiroJogador jochi = criarTabuleiro(Herdeiro.JOCHI);
		EstadoDoTurno turno = new EstadoDoTurno();
		comprarLinha(jochi, turno);
		comprarLinha(jochi, turno);

		assertEquals("Melhorias gratuitas pendentes para Jochi na segunda linha completa",1, turno.getMelhoriasGratuitasPendentes());
	}
	
	
}


