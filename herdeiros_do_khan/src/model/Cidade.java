package model;

import java.util.ArrayList;
import java.util.List;

class Cidade {
	private NomeCidade nome;
	private Regiao regiao;
	private List<TipoTesouro> tesouros;
	
	Cidade(NomeCidade nome, Regiao regiao) {
		this.nome = nome;
		this.regiao = regiao;
		this.tesouros = new ArrayList<>();
	}

	NomeCidade getNome() {
		return nome;
	}

	Regiao getRegiao() {
		return regiao;
	}

	List<TipoTesouro> getTesouros() {
		return tesouros;
	}
	
	boolean addTesouro(TipoTesouro tesouro) {
		// TODO
		return false;
	}
	
	TipoTesouro removeTesouro(TipoTesouro tesouro ) {
		// TODO
		return tesouro;
	}
	
	boolean temTesouro() {
		// TODO
		return false;
	}

}
