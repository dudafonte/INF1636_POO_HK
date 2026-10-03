package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


class TabuleiroJogador {
	private final Herdeiro herdeiro;
	private final List<ColunaDeAcao> colunas;
	private int pecasAtivacaoDisponiveis;
	private static final int TOTAL_PECAS_ATIVACAO = 4;
	
	TabuleiroJogador(Herdeiro herdeiro, Map<TipoColuna, Map<TipoAcao, Integer>> acoesBasePorColuna) {
		this.herdeiro = herdeiro;
		this.pecasAtivacaoDisponiveis = TOTAL_PECAS_ATIVACAO;
		this.colunas = new ArrayList<>();
		
		for (TipoColuna tipo : TipoColuna.values()) {
			Map<TipoAcao, Integer> acoesBase = acoesBasePorColuna.getOrDefault(tipo, Map.of());
			colunas.add(new ColunaDeAcao(tipo, acoesBase));
		}
		
		if (herdeiro == Herdeiro.JOCHI) {
			ColunaDeAcao colunaBranca = getColuna(TipoColuna.BRANCA);
			Melhoria espacoOcupadoJochi = new Melhoria(TipoMelhoria.HABILIDADE_NATIVA_JOCHI, TipoColuna.BRANCA);
			espacoOcupadoJochi.liberarParaProximoTurno();
			colunaBranca.adicionarMelhoriaGratuita(espacoOcupadoJochi);
		}
		
	}
	
	Herdeiro getHerdeiro() {
		return herdeiro;
	}
	
	List<ColunaDeAcao> getColunas() {
		return colunas;
	}
	
	int getPecasAtivacaoDisponiveis() {
		return pecasAtivacaoDisponiveis;
	}
	
	ColunaDeAcao getColuna(TipoColuna tipo) {
		return colunas.stream().filter(c -> c.getCor() == tipo).findFirst().orElseThrow(() -> new IllegalArgumentException("coluna inválida: " + tipo));
	}
	
	void ativarColuna(TipoColuna tipo, EstadoDoTurno turno) {
		ColunaDeAcao coluna = getColuna(tipo);
		if (!coluna.isDisponivel()) {
			throw new IllegalStateException("A coluna " + tipo + " já está com uma peça de ativação e indisponível.");
		}
		if (pecasAtivacaoDisponiveis <= 0) {
			throw new IllegalStateException("o jogador não possui peças de ativação disponíveis.");
		}
		coluna.colocarFicha();
		pecasAtivacaoDisponiveis--;
		
		if (coluna.getQuantidadeBase(TipoAcao.RECUPERAR_FICHAS) > 0 || tipo == TipoColuna.BRANCA) {
			recuperarTodasFichas();
		}
		processarEfeitosColuna(coluna, turno);
	}
	
	void recuperarTodasFichas() {
		for (ColunaDeAcao c : colunas) {
			c.recuperarFicha();
		}
		this.pecasAtivacaoDisponiveis = TOTAL_PECAS_ATIVACAO;
	}
	
	void adicionarMelhoriaComprada(Melhoria melhoria, TipoColuna colunaAlvoTipo, EstadoDoTurno turno) {
		ColunaDeAcao colunaAlvo = getColuna(colunaAlvoTipo);
		colunaAlvo.adicionarMelhoriaComprada(melhoria);
		verificarLinhaCompleta(turno);
	}
	
	void adicionarMelhoriaGratuita(Melhoria melhoria) {
		ColunaDeAcao colunaBranca = getColuna(TipoColuna.BRANCA);
		colunaBranca.adicionarMelhoriaGratuita(melhoria);
	}
	
	private void verificarLinhaCompleta(EstadoDoTurno turno) {
        int tamanhoMinimo = Math.min(
            Math.min(getColuna(TipoColuna.AMARELA).getMelhorias().size(), 
                     getColuna(TipoColuna.VERDE).getMelhorias().size()),
            getColuna(TipoColuna.VERMELHA).getMelhorias().size()
        );
        ColunaDeAcao colunaBranca = getColuna(TipoColuna.BRANCA);
        int quantidadeAtualBranca = colunaBranca.getMelhorias().size() + turno.getMelhoriasGratuitasPendentes();
        
        if (herdeiro == Herdeiro.JOCHI && tamanhoMinimo == 1 && quantidadeAtualBranca == 0) {
            return; 
        }
        
        if (quantidadeAtualBranca < tamanhoMinimo) {
            turno.adicionarMelhoriaGratuitaPendente();
        }
    }
	
	void liberarMelhoriasInicioTurno() {
		for (ColunaDeAcao c : colunas) {
			for (Melhoria m : c.getMelhorias()) {
				m.liberarParaProximoTurno();
			}
		}
	}
	
	private void processarEfeitosColuna(ColunaDeAcao coluna, EstadoDoTurno turno) {
		turno.adicionarMovimentos(coluna.getQuantidadeBase(TipoAcao.MOVER));
		turno.adicionarAcoesTributoRestantes(coluna.getQuantidadeBase(TipoAcao.PEGAR_TRIBUTO));
		if (coluna.possuiIconeKhan()) {
			turno.marcarKhanPendente(true);
		}
		for (Melhoria m : coluna.getMelhorias()) {
			if (!m.isDisponivel()) continue;
			switch (m.getTipo()) {
			case MOVIMENTOS_EXTRA:
				turno.adicionarMovimentos(2);
				break;
			case TRIBUTO_EXTRA:
				turno.adicionarAcoesTributoRestantes(1);
				break;
			default:
				break;
			}
		
		}
		
	}
	
	
}
