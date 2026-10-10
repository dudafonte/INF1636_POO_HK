package model;

import static org.junit.Assert.*;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;


public class TabuleiroJogadorTest {
	private TabuleiroJogador chagatai;
	
	@Before 
	public void setUp() {
		chagatai = criarTabuleiro(Herdeiro.CHAGATAI);
	}

	// helpers 
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
	
	// compra uma melhoria em cada coluna colorida "linha completa"
	
	private void comprarLinha(TabuleiroJogador t, EstadoDoTurno turno) {
		comprar(t, TipoMelhoria.MOVIMENTOS_EXTRA, TipoColuna.AMARELA, turno);
		comprar(t, TipoMelhoria.MOVIMENTOS_EXTRA, TipoColuna.VERDE, turno);
		comprar(t, TipoMelhoria.MOVIMENTOS_EXTRA, TipoColuna.VERMELHA, turno);
	}
	
	// ---------- ativação de colunas ----------
	
	@Test 
	public void ativarColuna_colocarFichaEDeixaColunaIndisponivel() {
		EstadoDoTurno turno = new EstadoDoTurno();
		chagatai.ativarColuna(TipoColuna.AMARELA, turno);
		
		assertFalse(chagatai.getColuna(TipoColuna.AMARELA).isDisponivel());
		assertEquals(3, chagatai.getPecasAtivacaoDisponiveis());
		
	}
	
	@Test(expected = IllegalStateException.class)
	public void ativarColuna_colunaJaUsadaLancaExcecao() {
		chagatai.ativarColuna(TipoColuna.AMARELA, new EstadoDoTurno());
		
		// turno seguinte: a ficha continua amarela
		chagatai.ativarColuna(TipoColuna.AMARELA, new EstadoDoTurno());
		
	}
	
	@Test(expected = IllegalStateException.class) 
	public void ativarColuna_soUmaColunaPorTurno() {
		EstadoDoTurno turno = new EstadoDoTurno();
		chagatai.ativarColuna(TipoColuna.AMARELA, turno);
		chagatai.ativarColuna(TipoColuna.VERDE, turno);
	}
	
	@Test 
	public void ativarColuna_somaAcoesBaseDaColuna() {
		EstadoDoTurno turno = new EstadoDoTurno();
		
		chagatai.ativarColuna(TipoColuna.VERMELHA, turno);
		assertEquals(2, turno.getMovimentosRestantes());
		assertEquals(2, turno.getAcoesTributoRestantes());
		assertFalse(turno.isKhanPendente());
	
	}
	
	@Test
	public void ativarColuna_comIconeKhanMarcaKhanPendente() {
		EstadoDoTurno turno = new EstadoDoTurno();
		chagatai.ativarColuna(TipoColuna.VERDE, turno);
		
		assertTrue(turno.isKhanPendente());
		assertFalse(turno.podeFinalizarTurno());
	}
	
	@Test
	public void colunaBranca_recuperaTodasAsFichasIncluisiveAUsada() {
		chagatai.ativarColuna(TipoColuna.AMARELA, new EstadoDoTurno());
		chagatai.ativarColuna(TipoColuna.VERMELHA, new EstadoDoTurno());
		chagatai.ativarColuna(TipoColuna.VERDE, new EstadoDoTurno());
		assertEquals(1, chagatai.getPecasAtivacaoDisponiveis());
		chagatai.ativarColuna(TipoColuna.BRANCA, new EstadoDoTurno());
		assertEquals(4, chagatai.getPecasAtivacaoDisponiveis());
		for (ColunaDeAcao c: chagatai.getColunas()) {
			assertTrue("coluna " + c.getCor() + " deveria estar disponível", c.isDisponivel());
		}
	}
	
	// ---------- melhorias: bloqueio no turno da compra ----------
	
	@Test 
	public void melhoriaComprada_naoValeNoMesmoTurno_eValeDepoisDeLiberada() {
		comprar(chagatai, TipoMelhoria.MOVIMENTOS_EXTRA, TipoColuna.AMARELA, new EstadoDoTurno());
		
		// melhoria ainda bloqueada: só 3 movimentos base 
		EstadoDoTurno turnoDaCompra = new EstadoDoTurno();
		chagatai.ativarColuna(TipoColuna.AMARELA, turnoDaCompra);
		assertEquals(3, turnoDaCompra.getMovimentosRestantes());
		// próximo turno: libera e recupera a ficha
		chagatai.recuperarTodasFichas();
		chagatai.liberarMelhoriasInicioTurno();
		EstadoDoTurno turnoSeguinte = new EstadoDoTurno();
		chagatai.ativarColuna(TipoColuna.AMARELA, turnoSeguinte);
		
		assertEquals(5, turnoSeguinte.getMovimentosRestantes());
	}
	
	@Test
	public void melhoriaDeTributoExtra_somaUmaAcaoDeTributoDepoisDeLiberada() {
		comprar(chagatai, TipoMelhoria.TRIBUTO_EXTRA, TipoColuna.VERMELHA, new EstadoDoTurno());
		chagatai.liberarMelhoriasInicioTurno();
		
		EstadoDoTurno turno = new EstadoDoTurno();
		chagatai.ativarColuna(TipoColuna.VERMELHA, turno);
		assertEquals(3, turno.getAcoesTributoRestantes());
	}
	
	// ---------- melhorias: restrições de coluna ----------
	
	@Test(expected = IllegalStateException.class) 
	public void melhoria_corIncompativelComAColunaLancaExcecao() {
		chagatai.adicionarMelhoriaComprada(melhoria(TipoMelhoria.MOVIMENTOS_EXTRA, TipoColuna.VERDE), TipoColuna.AMARELA, new EstadoDoTurno());
	}
	
	@Test(expected = IllegalStateException.class) 
	public void melhoria_CompradaNaColunaBrancaLancaExcecao() {
		chagatai.adicionarMelhoriaComprada(melhoria(TipoMelhoria.MOVIMENTOS_EXTRA, TipoColuna.BRANCA), TipoColuna.BRANCA, new EstadoDoTurno());
	}
	
	@Test 
	public void colunaCheia_naoAceitaQuintaMelhoria() {
		EstadoDoTurno turno = new EstadoDoTurno();
		for (int i =0; i < 4; i++) {
			comprar(chagatai, TipoMelhoria.MOVIMENTOS_EXTRA, TipoColuna.AMARELA, turno);
		}
		assertEquals(4, chagatai.getColuna(TipoColuna.AMARELA).quantidadeMelhorias());
		
		try {
			comprar(chagatai, TipoMelhoria.MOVIMENTOS_EXTRA, TipoColuna.AMARELA, turno);
			fail("a 5ª melhoria deveria ser recusada.");
		}
		catch (IllegalStateException e) {
			assertEquals(4, chagatai.getColuna(TipoColuna.AMARELA).quantidadeMelhorias());
		}
	}
	
	// ---------- linha completa e melhoria grátis ----------
	
	@Test 
	public void linhaIncompleta_naoDaMelhoriaGratis() {
		EstadoDoTurno turno = new EstadoDoTurno();
		comprar(chagatai, TipoMelhoria.MOVIMENTOS_EXTRA, TipoColuna.AMARELA, turno);
		comprar(chagatai, TipoMelhoria.MOVIMENTOS_EXTRA, TipoColuna.VERDE, turno);
		assertEquals(0, turno.getMelhoriasGratuitasPendentes());
	}
	
	@Test 
	public void linhaCompleta_daUmaMelhoriaGratisPendente() {
		EstadoDoTurno turno = new EstadoDoTurno();
		comprarLinha(chagatai, turno);
		
		assertEquals(1, turno.getMelhoriasGratuitasPendentes());
		assertEquals(0, chagatai.getColuna(TipoColuna.BRANCA).quantidadeMelhorias());
		
	}
	
	@Test
	public void segundaLinhaCompleta_daOutraMelhoriaGratis() {
		EstadoDoTurno turno = new EstadoDoTurno();
		comprarLinha(chagatai, turno);
		chagatai.resgatarMelhoriaGratuita(melhoria(TipoMelhoria.TRIBUTO_EXTRA, TipoColuna.BRANCA), turno);
		assertEquals(0, turno.getMelhoriasGratuitasPendentes());
		comprarLinha(chagatai, turno);
		assertEquals(1, turno.getMelhoriasGratuitasPendentes());
	}
	
	@Test
	public void resgatarMelhoriaGratuita_adicionaNaBrancaEConsomePendente() {
		EstadoDoTurno turno = new EstadoDoTurno();
		comprarLinha(chagatai, turno);
		chagatai.resgatarMelhoriaGratuita(melhoria(TipoMelhoria.TRIBUTO_EXTRA, TipoColuna.BRANCA), turno);
		assertEquals(1, chagatai.getColuna(TipoColuna.BRANCA).quantidadeMelhorias());
		assertEquals(0, turno.getMelhoriasGratuitasPendentes());
	}
	
	@Test(expected = IllegalStateException.class)
	public void resgatarMelhoriaGratuita_semPendenciaLancaExcecao() {
		chagatai.resgatarMelhoriaGratuita(melhoria(TipoMelhoria.TRIBUTO_EXTRA, TipoColuna.BRANCA), new EstadoDoTurno());
	}
	
	// ---------- habilidade do Jochi ----------
	
	@Test 
	public void jochi_comecaComEspacoDaColunaBrancaOcupado() {
		TabuleiroJogador jochi = criarTabuleiro(Herdeiro.JOCHI);
		EstadoDoTurno turno = new EstadoDoTurno();
		comprarLinha(jochi, turno);
		assertEquals(0, turno.getMelhoriasGratuitasPendentes());
	}
	
	@Test 
	public void jochi_segundaLinhaCompletaDaMelhoriaGratis() {
		TabuleiroJogador jochi = criarTabuleiro(Herdeiro.JOCHI);
		EstadoDoTurno turno = new EstadoDoTurno();
		comprarLinha(jochi, turno);
		comprarLinha(jochi, turno);
		assertEquals(1, turno.getMelhoriasGratuitasPendentes());
	}
	
	
	
}


