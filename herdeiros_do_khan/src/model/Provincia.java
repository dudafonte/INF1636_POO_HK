package model;

import java.util.ArrayList;
import java.util.List;

class Provincia {
	private final TipoTributo tributo;
	private int qtdTributo;
	private final boolean provinciaKhan;
	private final Regiao regiao;
	private final List<Parada> paradasAdjacentes = new ArrayList<>();
	
	Provincia(TipoTributo tributo, boolean provinciaKhan, Regiao regiao) {
		this.tributo = tributo;
		this.provinciaKhan = provinciaKhan;
		this.regiao = regiao;
		this.qtdTributo = 1;
	}

	TipoTributo getTributo() {
		return tributo;
	}

	int getQtdTributo() {
		return qtdTributo;
	}

	boolean isProvinciaKhan() {
		return provinciaKhan;
	}

	Regiao getRegiao() {
		return regiao;
	}
	
	boolean addParadaAdjacente(Parada parada) {
	    if (!paradasAdjacentes.contains(parada)) {
	        return paradasAdjacentes.add(parada);
	    }
	    return false;
	}
	
	List<Parada> getParadasAdjacentes() {
	    return List.copyOf(paradasAdjacentes);
	}
	
	boolean aumentaQtdTributo() {
		if (qtdTributo < 3) {
			qtdTributo++;
			return true;
		}
		return false;
	}
	
	boolean diminuiQtdTributo() {
		if (qtdTributo > 0) {
			qtdTributo--;
			return true;
		}
		return false;
	}

}
