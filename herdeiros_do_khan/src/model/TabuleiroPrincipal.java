package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

class TabuleiroPrincipal {
	private Map<String, Parada> paradas;
	private List<Regiao> regioes;
	private Queue<Cidade> filaCidades;
	private Karakorum karakorum;
	
	TabuleiroPrincipal() {
		this.paradas = new HashMap<>();
		this.regioes = new ArrayList<>();
		this.filaCidades = new LinkedList<>();
		this.karakorum = new Karakorum("Karakorum");
		inicializarMapa();
	}
	
	private void inicializarMapa() {
		Regiao regiao = new Regiao(NomeRegiao.CHINA);
		
		Parada paradaA = new Parada("idA", false, regiao);
		Parada paradaB = new Parada("idB", false, regiao);
		Parada paradaC = new Parada("idC", true, regiao);
		Parada paradaD = new Parada("idD", false, regiao);
		
		paradas.put("idA", paradaA);
		paradas.put("idB", paradaB);
		paradas.put("idC", paradaC);
		paradas.put("idD", paradaD);
		
		paradaA.addParadaAdjacente(paradaB);
		paradaA.addParadaAdjacente(paradaC);
		paradaB.addParadaAdjacente(paradaA);
		paradaC.addParadaAdjacente(paradaA);	
		
	}
	

	Parada getParada(String id) {
		return paradas.get(id);
	}

	List<Regiao> getRegioes() {
		return regioes;
	}
	
	boolean mover(Jogador jogador, Parada destino) {
		Parada paradaAtual = jogador.getParadaAtual();
		
		if (paradaAtual.eAdjacenteDe(destino) && destino.addJogador()) {
			paradaAtual.removeJogador();
			jogador.setParadaAtual(destino);
			return true;
		}
		
		return false;
	}
	
	boolean pegarTributo(Jogador jogador, Provincia provincia, TipoTributo tributo) {
		// TODO
		return false;
	}
	
	boolean construirYurt(Jogador jogador) {
		// TODO
		return false;
	}
	
	boolean atacarCidade(Jogador jogador, Cidade cidade, TipoTesouro tesouro) {
		// TODO
		return false;
	}
	
	boolean usarKhanProvincia(Jogador jogador, Provincia provincia) {
		// TODO
		return false;
	}
	
	boolean usarKhanMelhorias(Jogador jogador) {
		// TODO
		return false;
	}
	
	boolean comprarMelhoria(Jogador jogador, PecaMelhoria melhoria) {
		// TODO
		return false;
	}
	
	boolean comprarMelhoria(Jogador jogador) {
		// TODO
		return false;
	}
	
	boolean entregarTesouros(Jogador jogador, List<TipoTesouro> tesouros) {
		// TODO
		return false;
	}
	
	boolean cumprirDemanda(Jogador jogador, CartaConselheiroAberto carta) {
		// TODO
		return false;
	}
	
	boolean ganharVotos(Jogador jogador, int qtd) {
		// TODO
		return false;
	}

}
