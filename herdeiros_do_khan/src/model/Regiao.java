package model;

import java.util.ArrayList;
import java.util.List;

class Regiao {
	private NomeRegiao nome;
	private List<Cidade> cidades; 
	private List<Parada> paradas;
	
	Regiao(NomeRegiao nome) {
		this.nome = nome;
		this.cidades = new ArrayList<>();
	    this.paradas = new ArrayList<>();
	}

	NomeRegiao getNome() {
		return nome;
	}

	List<Cidade> getCidades() {
		return cidades;
	}

	List<Parada> getParadas() {
		return paradas;
	}
	
	boolean addCidade(Cidade cidade) {
		// TODO
		return false;
	}
	
	boolean addParada(Parada parada) {
		// TODO
		return false;
	}

}
