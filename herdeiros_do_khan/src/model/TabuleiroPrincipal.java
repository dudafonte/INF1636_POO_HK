package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

class TabuleiroPrincipal {
	private final Map<String, Parada> paradas = new HashMap<>();
	private final List<Regiao> regioes =new ArrayList<>();
	private final Queue<Cidade> filaCidades = new LinkedList<>();
	private final Karakorum karakorum = new Karakorum("Karakorum");
	
	TabuleiroPrincipal() {
		inicializarMapa();
	}
	
	private void inicializarMapa() {
		Regiao regiao = new Regiao(NomeRegiao.CHINA);
		regioes.add(regiao);
		
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
		return List.copyOf(regioes);
	}
	
	boolean mover(Jogador jogador, Parada destino) {
		Parada paradaAtual = jogador.getParadaAtual();
		
		if (paradaAtual.eAdjacenteDe(destino) && destino.podeReceberJogador()) {
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
