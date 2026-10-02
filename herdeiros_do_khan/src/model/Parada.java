package model;

import java.util.ArrayList;
import java.util.List;

class Parada {
	private String id;
	private Regiao regiao;
	private boolean dupla;
	private List<Parada> paradasAdjacentes;
	private List<Provincia> provinciasAdjacentes;
	private List<Cidade> cidadesAdjacentes;
	private final List<Jogador> jogadores = new ArrayList<>();
	private List<Jogador> yurts;
	
	Parada(String id, boolean dupla, Regiao regiao) {
		this.id = id;
		this.dupla = dupla;
		this.regiao = regiao;
		this.paradasAdjacentes = new ArrayList<>();
		this.provinciasAdjacentes = new ArrayList<>();
		this.cidadesAdjacentes = new ArrayList<>();
		this.yurts = new ArrayList<>();
	}
	
	void addJogador(Jogador j) { if (!jogadores.contains(j)) jogadores.add(j); }
	void removeJogador(Jogador j) { jogadores.remove(j); }

	String getId() {
		return id;
	}

	Regiao getRegiao() {
		return regiao;
	}

	boolean isDupla() {
		return dupla;
	}

	List<Parada> getParadasAdjacentes() {
		return paradasAdjacentes;
	}

	List<Provincia> getProvinciasAdjacentes() {
		return provinciasAdjacentes;
	}

	List<Cidade> getCidadesAdjacentes() {
		return cidadesAdjacentes;
	}

	int getQtdJogadores() {
		return jogadores.size();
	}

	List<Jogador> getJogadores() { return List.copyOf(jogadores); }
	
	List<Jogador> getYurts() {
		return yurts;
	}
	
	boolean addParadaAdjacente(Parada parada) {
		// TODO
		return false;
	}
	
	boolean addProvinciaAdjacente(Provincia provincia) {
		// TODO
		return false;
	}
	
	boolean addCidadeAdjacente(Cidade cidade) {
		// TODO
		return false;
	}
	
	boolean addJogador() {
		// TODO
		return false;
	}
	
	boolean removeJogador() {
		// TODO
		return false;
	}
	
	boolean addYurt(Jogador jogador) {
		int capacidade = dupla ? 2 : 1;
		if (yurts.size() >= capacidade) return false;
		if (!jogador.colocarYurt()) return false;
		yurts.add(jogador);
		return true;
	}
	
	boolean temYurtDoJogador(Jogador jogador) {
		return yurts.contains(jogador);
	}

}
