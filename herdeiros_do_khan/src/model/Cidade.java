package model;

import java.util.ArrayList;
import java.util.List;

class Cidade {
	private final NomeCidade nome;
	private final Regiao regiao;
	private final List<TipoTesouro> tesouros = new ArrayList<>();
	private Jogador yurtConquista = null; 
	
	Cidade(NomeCidade nome, Regiao regiao) {
		this.nome = nome;
		this.regiao = regiao;
	}

	NomeCidade getNome() {
		return nome;
	}

	Regiao getRegiao() {
		return regiao;
	}
	
	Jogador getYurtConquista() {
		return yurtConquista;
	}

	List<TipoTesouro> getTesouros() {
		return List.copyOf(tesouros);
	}
	
	boolean addTesouro(TipoTesouro tesouro) {
		return tesouros.add(tesouro);
	}
	
	void removeTesouro(TipoTesouro tesouro) {
	    if (!tesouros.remove(tesouro)) {
	        throw new IllegalStateException("Cidade não possui o tesouro: " + tesouro);
	    }
	}
	
	boolean temTesouro() {
		return !tesouros.isEmpty();
	}
	
	void addYurtConquista(Jogador jogador) {
		if (jogador == null) throw new IllegalArgumentException("jogador não pode ser nulo");
		if (yurtConquista != null) throw new IllegalStateException("cidade já foi conquistada");
		
		yurtConquista = jogador;
		
	}

}
