package model;

import java.util.ArrayList;
import java.util.List;

class Parada {
	private final String id;
	private final Regiao regiao;
	private final boolean dupla;
	
	private final List<Parada> paradasAdjacentes = new ArrayList<>();
	private final List<Provincia> provinciasAdjacentes = new ArrayList<>();
	private final List<Cidade> cidadesAdjacentes = new ArrayList<>();
	private final List<Jogador> jogadores = new ArrayList<>();
	private final List<Jogador> yurts = new ArrayList<>();
	
	Parada(String id, boolean dupla, Regiao regiao) {
		this.id = id;
		this.dupla = dupla;
		this.regiao = regiao;
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
		return List.copyOf(paradasAdjacentes);
	}

	List<Provincia> getProvinciasAdjacentes() {
		return List.copyOf(provinciasAdjacentes);
	}

	List<Cidade> getCidadesAdjacentes() {
		return List.copyOf(cidadesAdjacentes);
	}

	int getQtdJogadores() {
		return jogadores.size();
	}

	List<Jogador> getJogadores() { 
		return List.copyOf(jogadores); 
	}
	
	List<Jogador> getYurts() {
		return List.copyOf(yurts);
	}
	
	
	boolean addParadaAdjacente(Parada parada) {
		if (!paradasAdjacentes.contains(parada)) {
			return paradasAdjacentes.add(parada);
		}
		return false;
	}
	
	boolean addProvinciaAdjacente(Provincia provincia) {
		if (!provinciasAdjacentes.contains(provincia)) {
			return provinciasAdjacentes.add(provincia);
		}
		return false;	
	}
	
	boolean addCidadeAdjacente(Cidade cidade) {
		if(!cidadesAdjacentes.contains(cidade)) {
			return cidadesAdjacentes.add(cidade);
		}
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
	
	boolean eAdjacenteDe(Parada parada) {
		return paradasAdjacentes.contains(parada);
	}
	
	boolean podeReceberJogador() {
	    int capacidade = dupla ? 2 : 1;
	    return jogadores.size() < capacidade;
	}

	boolean addJogador(Jogador j) {
	    if (!podeReceberJogador()) return false;
	    if (!jogadores.contains(j)) {
	        jogadores.add(j);
	        return true;
	    }
	    return false;
	}
	
	void removeJogador(Jogador j) { jogadores.remove(j); }

}
