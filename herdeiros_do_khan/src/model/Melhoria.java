package model;

class Melhoria {
	private final TipoMelhoria tipo;
	private final TipoColuna cor;
	private boolean bloqueadaNesseTurno;
	private final TipoTesouro tesouroEspecifico;
	
	// melhorias comuns
	Melhoria(TipoMelhoria tipo, TipoColuna cor) {
		if (tipo == TipoMelhoria.TESOURO_VIRTUAL) {
			throw new IllegalArgumentException("tesouros virtuais requerem a definição do tipo de tesouro.");
		}
		this.tipo = tipo;
		this.cor = cor;
		this.bloqueadaNesseTurno = true;
		this.tesouroEspecifico = null;
	}
	
	// só para tesouro_virtual
	Melhoria(TipoMelhoria tipo, TipoColuna cor, TipoTesouro tesouro) {
		if (tipo != TipoMelhoria.TESOURO_VIRTUAL || tesouro == null) {
			throw new IllegalArgumentException("só TESOURO_VIRTUAL, com tipo de tesouro definido.");
		}
		this.tipo = tipo;
		this.cor = cor;
		this.bloqueadaNesseTurno = true;
		this.tesouroEspecifico = tesouro;
	}
	
	TipoMelhoria getTipo() {
		return tipo;
	}
	
	TipoColuna getCor() {
		return cor;
	}
	
	boolean isDisponivel() {
		return !bloqueadaNesseTurno;
	}
	
	void liberarParaProximoTurno() {
		this.bloqueadaNesseTurno = false;
	}
	
	TipoTesouro getTesouroEspecifico() {
		return tesouroEspecifico;
	}
}

