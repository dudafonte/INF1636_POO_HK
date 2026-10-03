package model;

import java.util.ArrayList;
import java.util.List;

class Regiao {
	private final NomeRegiao nome;
	private final List<Cidade> cidades = new ArrayList<>(); 
	private final List<Parada> paradas = new ArrayList<>();
	
	Regiao(NomeRegiao nome) {
		this.nome = nome;
	}

	NomeRegiao getNome() {
		return nome;
	}

	List<Cidade> getCidades() {
		return List.copyOf(cidades);
	}

	List<Parada> getParadas() {
		return List.copyOf(paradas);
	}
	
	boolean addCidade(Cidade cidade) {
		if (!cidades.contains(cidade)) {
			return cidades.add(cidade);
		}
		return false;
	}
	
	boolean addParada(Parada parada) {
		if (!paradas.contains(parada)) {
			return paradas.add(parada);
		}
		return false;
	}

}
