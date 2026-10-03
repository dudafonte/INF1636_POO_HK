package model;

import java.util.EnumMap; 
import java.util.Map;
import java.util.List;



class Jogador {
	static final int TOTAL_YURTS = 12;
	
	private final Cor cor;
	private final Herdeiro herdeiro;
	private final TabuleiroJogador tabuleiro;
	
	private final Map<TipoTributo, Integer> tributos = new EnumMap<>(TipoTributo.class);
	private final Map<TipoTesouro, Integer> tesouros = new EnumMap<>(TipoTesouro.class);
	
	private Parada paradaAtual;
	private int votos;
	private int yurtsColocados;
	private EstadoDoTurno turno = new EstadoDoTurno();
	
	private TipoConselheiroSecreto conselheiroSecreto;
	
	Jogador(Cor cor, Herdeiro herdeiro, TabuleiroJogador tabuleiro, Parada paradaInicial) {
		this.cor = cor;
		this.herdeiro = herdeiro;
		this.tabuleiro = tabuleiro;
		this.paradaAtual = paradaInicial;
		paradaInicial.addJogador(this);
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
	
	void gastarTributo(TipoTributo tipo, int qtd) {
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
	Parada getParadaAtual() { return paradaAtual; }
	
	void setParadaAtual(Parada nova) { 
		if (paradaAtual != null) paradaAtual.removeJogador(this);
		paradaAtual = nova;
		nova.addJogador(this);
	}
	
	void ganharVoto(int n) {
		if (n < 0) throw new IllegalArgumentException("número de votos negativo");
		votos+=n; 
	}
	
	int getVotos() { return votos; }
	
	void iniciarTurno() { turno = new EstadoDoTurno(); }
	
	EstadoDoTurno getTurno() { return turno; }
	
	Cor getCor() {return cor; }
	
	Herdeiro getHerdeiro() { return herdeiro; }
	
	TabuleiroJogador getTabuleiro() { return tabuleiro; }
	
}