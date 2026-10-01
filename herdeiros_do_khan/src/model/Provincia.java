package model;

class Provincia {
	private TipoTributo tributo;
	private int quantidade;
	private boolean provinciaKhan;
	private Regiao regiao;
	
	Provincia(TipoTributo tributo, boolean provinciaKhan, Regiao regiao) {
		this.tributo = tributo;
		this.provinciaKhan = provinciaKhan;
		this.regiao = regiao;
		this.quantidade = 1;
	}

	TipoTributo getTributo() {
		return tributo;
	}

	int getQuantidade() {
		return quantidade;
	}

	boolean isProvinciaKhan() {
		return provinciaKhan;
	}

	Regiao getRegiao() {
		return regiao;
	}
	
	boolean aumentaQuantidade() {
		// TODO
		return false;
	}
	
	boolean diminuiQuantidade() {
		// TODO
		return false;
	}

}
