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
	private int qtdJogadores;
	private List<Jogador> yurts;
	
	Parada(String id, boolean dupla, Regiao regiao) {
		this.id = id;
		this.dupla = dupla;
		this.regiao = regiao;
		this.paradasAdjacentes = new ArrayList<>();
		this.provinciasAdjacentes = new ArrayList<>();
		this.cidadesAdjacentes = new ArrayList<>();
		this.yurts = new ArrayList<>();
		this.qtdJogadores = 0;
	}

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
		return qtdJogadores;
	}

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
		// TODO
		return false;
	}
	
	boolean temYurtDoJogador(Jogador jogador) {
		// TODO
		return false;
	}
	
	boolean eAdjacenteDe(Parada parada) {
		return paradasAdjacentes.contains(parada);
	}

}
