package model;

import java.util.EnumMap;  
import java.util.ArrayList;
import java.util.Map;
import java.util.List;



class Jogador {
	static final int TOTAL_YURTS = 12;
	
	private final Cor cor;
	private final Herdeiro herdeiro;
	private final TabuleiroJogador tabuleiro;
	
	private final Map<TipoTributo, Integer> tributos = new EnumMap<>(TipoTributo.class);
	private final Map<TipoTesouro, Integer> tesouros = new EnumMap<>(TipoTesouro.class);
	
	private final List<Parada> paradas = new ArrayList<>();
	private int votos;
	private int yurtsColocados;
	private EstadoDoTurno turno = new EstadoDoTurno();
	
	private TipoConselheiroSecreto conselheiroSecreto;
	
	Jogador(Cor cor, Herdeiro herdeiro, TabuleiroJogador tabuleiro, Parada paradaInicial) {
		this.cor = cor;
		if (herdeiro != tabuleiro.getHerdeiro()) {
			throw new IllegalArgumentException("herdeiro do jogador diferente do herdeiro do tabuleiro.");
		}
		this.herdeiro = herdeiro;
		this.tabuleiro = tabuleiro;
		if (!paradaInicial.addJogador(this)) {
			throw new IllegalStateException("para incial sem espaço para o peão.");
		}
		paradas.add(paradaInicial);
		if (herdeiro == Herdeiro.ALTANI) {
			if (!paradaInicial.addJogador(this)) {
				throw new IllegalStateException("parada inicial sem espaço para o 2o. peão.");
			}
			paradas.add(paradaInicial);
		}
		for (TipoTributo t: TipoTributo.values()) tributos.put(t, 0);
		for (TipoTesouro t: TipoTesouro.values()) tesouros.put(t, 0);
		
	}
	
	// ---------- lógica do conselheiro secreto ----------
	void escolherConselheiroSecreto(List<TipoConselheiroSecreto> cartasDistribuidas, TipoConselheiroSecreto cartaEscolhida) {
		if (cartasDistribuidas == null || cartasDistribuidas.size() != 2) {
			throw new IllegalArgumentException("o jogador deve receber exatamente 2 cartas de conselheiro secreto na preparação.");
		}
		if (!cartasDistribuidas.contains(cartaEscolhida)) {
			throw new IllegalArgumentException("a carta escolhida deve estar entre as 2 opções recebidas.");
		}
		this.conselheiroSecreto = cartaEscolhida;
	}
	
	TipoConselheiroSecreto getConselheiroSecreto() {
		return conselheiroSecreto;
	}
	
	// ---------- tributos ----------
	int quantidadeDe(TipoTributo tipo) {
		return tributos.get(tipo);
	}
	
	void adicionarTributo(TipoTributo tipo, int qtd) {
		if (qtd < 0) throw new IllegalArgumentException("quantidade negativa");
		tributos.merge(tipo, qtd, Integer::sum);
	}
	
	boolean podeGastar(TipoTributo tipo, int qtd) {
		if (qtd < 0) throw new IllegalArgumentException("quantidade negativa");
		return tributos.get(tipo) >= qtd;
	}
	
	private void gastarTributo(TipoTributo tipo, int qtd) {
		if (qtd < 0) throw new IllegalArgumentException("quantidade negativa");
		if (!podeGastar(tipo, qtd))
			throw new IllegalStateException("tributo insuficiente: " + tipo);
		tributos.merge(tipo, -qtd, Integer::sum);
	}
	
	// ---------- tesouros ----------
	
	int quantidadeDe(TipoTesouro tipo) {
		return tesouros.get(tipo);
	}
	
	void adicionarTesouro(TipoTesouro tipo) {
		tesouros.merge(tipo, 1, Integer::sum);
	}
	
	void entregarTesouro(TipoTesouro tipo, int qtd) {
		if (qtd < 0) throw new IllegalArgumentException("quantidade negativa");
		if (tesouros.get(tipo) < qtd) 
			throw new IllegalStateException("tesouro insuficiente: " + tipo);
		tesouros.merge(tipo, -qtd, Integer::sum);
	}
	
	int tiposDeTesouroDiferentes() {
		return (int) tesouros.values().stream().filter(q -> q > 0).count();
	}
	
	// ---------- yurts ----------
	int yurtsRestantes() {
		return TOTAL_YURTS - yurtsColocados;
	}
	
	boolean podeColocarYurt() {
		return yurtsColocados < TOTAL_YURTS;
	}
	
	boolean colocarYurt() {
		if (!podeColocarYurt()) {
			return false;
		}
		yurtsColocados++;
		return true;
	}
	
	// ---------- posição, votos, turno ----------
	int quantidadePeoes() { return paradas.size(); }
	
	int getPeaoAtivo() { return turno.getPeaoAtivo(); }
	
	void selecionarPeao(int indice) {
		if (indice < 0 || indice >= paradas.size())
			throw new IllegalArgumentException("peão inválido: " + indice);
		turno.selecionarPeao(indice);
	}
	
	Parada getParadaAtual() { return paradas.get(turno.getPeaoAtivo()); }
	
	Parada getParada(int indice) { return paradas.get(indice); }
	
	void setParadaAtual(Parada nova) {
		if (!nova.podeReceberJogador()) {
			throw new IllegalStateException("a parada" + nova.getId() + " está ocupada.");
		}
		int peao = turno.getPeaoAtivo();
		paradas.get(peao).removeJogador(this);
		paradas.set(peao, nova);
		nova.addJogador(this);
		turno.registrarVisita(peao, nova);
	}
	
	void ganharVoto(int n) {
		if (n < 0) throw new IllegalArgumentException("número de votos negativo");
		votos+=n; 
	}
	
	int getVotos() { return votos; }
	
	void iniciarTurno() { 
		turno = new EstadoDoTurno();
		for (int i = 0; i < paradas.size(); i++) {
			turno.registrarVisita(i, paradas.get(i));
		}
	}
	
	boolean podeFazerAcaoExtra() { return turno.peaoPodeFazerAcaoExtra();}
	
	void registrarAcaoExtra() { turno.registrarAcaoExtra(); }
	
	EstadoDoTurno getTurno() { return turno; }
	
	Cor getCor() {return cor; }
	
	Herdeiro getHerdeiro() { return herdeiro; }
	
	TabuleiroJogador getTabuleiro() { return tabuleiro; }
	
	// virtuais
	
	boolean podeGastarComVirtuais(TipoTributo tipo, int qtd) {
		if (qtd < 0) throw new IllegalArgumentException("quantidade negativa.");
		return tributos.get(tipo) + turno.tributosVirtuaisDe(tipo) >= qtd;
	}
	
	void gastarComVirtuais(TipoTributo tipo, int qtd) {
		if (!podeGastarComVirtuais(tipo, qtd))
			throw new IllegalStateException("tributo insuficiente: " + tipo);
		int doVirtual = Math.min(qtd, turno.tributosVirtuaisDe(tipo));
		turno.consumirTributoVirtual(tipo, doVirtual);
		gastarTributo(tipo, qtd - doVirtual);
	}
	
	int quantidadeDisponivelDe(TipoTesouro tipo) {
		return tesouros.get(tipo) + turno.tesourosVirtuaisDe(tipo);
	}
	
	void entregarTesouroComVirtual(TipoTesouro tipo) {
		if (quantidadeDisponivelDe(tipo) <= 0)
			throw new IllegalStateException("tesouro insuficiente: " + tipo);
		if (turno.tesourosVirtuaisDe(tipo) > 0) {
			turno.consumirTesouroVirtual(tipo);
		}
		else {
			entregarTesouro(tipo, 1);
		}
	}
	
}