package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

class ColunaDeAcao {
	private TipoColuna cor;
	private boolean contemFichaAtivacao;
	private List<Melhoria> melhoriasAdicionadas;
	private static final int MAX_MELHORIAS = 4;
	private final Map<TipoAcao, Integer> acoesBase = new EnumMap<TipoAcao, Integer>(TipoAcao.class);
	
	ColunaDeAcao(TipoColuna cor, Map<TipoAcao, Integer> acoesBase) {
		this.cor = cor;
		if (acoesBase != null) {
			this.acoesBase.putAll(acoesBase);
		}
		this.contemFichaAtivacao = false;
		this.melhoriasAdicionadas = new ArrayList<>();
	}
	
	boolean podeAdicionarMelhoriaComprada(Melhoria novaMelhoria) {
		if (this.cor == TipoColuna.BRANCA) {
			return false;
		}
		return this.cor == novaMelhoria.getCor() && melhoriasAdicionadas.size() < MAX_MELHORIAS;
	}
	
	boolean podeAdicionarMelhoriaGratuita(Melhoria novaMelhoria) {
		if (this.cor != TipoColuna.BRANCA) {
			return false;
		}
		return melhoriasAdicionadas.size() < MAX_MELHORIAS;
	}
	
	void adicionarMelhoriaComprada(Melhoria melhoria) {
		if (podeAdicionarMelhoriaComprada(melhoria)) {
			melhoriasAdicionadas.add(melhoria);
		}
		else {
			throw new IllegalStateException("coluna cheia, cor incompatível ou tentativa de usar coluna branca para compra..");
		}
	}
	
	void adicionarMelhoriaGratuita(Melhoria melhoria) {
		if (podeAdicionarMelhoriaGratuita(melhoria)) {
			melhoriasAdicionadas.add(melhoria);
		}
		else {
			throw new IllegalStateException("coluna cheia ou limite de melhorias atingido.");
		}
	}
	
	boolean isDisponivel() {
		return !contemFichaAtivacao;
	}
	
	TipoColuna getCor() {
		return cor;
	}

	void colocarFicha() {
		if (!isDisponivel()) {
			throw new IllegalStateException("coluna já ativada.");
		}
		this.contemFichaAtivacao = true;
	}
	
	void recuperarFicha() {
		this.contemFichaAtivacao = false;
	}
	
	int getQuantidadeBase(TipoAcao tipo) {
		return acoesBase.getOrDefault(tipo, 0);
	}
	
	boolean possuiIconeKhan() {
		return getQuantidadeBase(TipoAcao.USAR_KHAN) > 0;
	}
	
	List<Melhoria> getMelhorias() {
		return Collections.unmodifiableList(melhoriasAdicionadas);
	}
	
	int quantidadeMelhorias() {
		return melhoriasAdicionadas.size();
	}
	
	
}
