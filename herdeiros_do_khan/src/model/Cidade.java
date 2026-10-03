package model;

import java.util.ArrayList;
import java.util.List;

class Cidade {
	private final NomeCidade nome;
	private final Regiao regiao;
	private final List<TipoTesouro> tesouros = new ArrayList<>();
	
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

}
