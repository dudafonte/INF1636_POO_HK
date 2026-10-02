package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class EstadoDoTurno {
	private int movimentosRestantes;
	private int acoesTributoRestantes;
	private boolean khanUsado;
	private int melhoriasCompradas;
	
	private final Set<Parada> paradasVisitadas = new HashSet<>();
	private final Map<Cidade, Integer> tesourosTomadosPorCidade = new HashMap<>();
	
	int getMovimentosRestantes() { return movimentosRestantes; }
	void adicionarMovimentos(int n) {
		if (n < 0) throw new IllegalStateException("número negativo");
		movimentosRestantes += n;
	}
	void consumirMovimento() {
		if (movimentosRestantes <= 0) throw new IllegalStateException("sem movimentos");
		movimentosRestantes--;
	}
	
	int getAcoesTributoRestantes() { return acoesTributoRestantes; }
	void adicionarAcoesTributoRestantes(int n) { acoesTributoRestantes += n; }
	void consumirAcaoTributo() {
		if (acoesTributoRestantes <= 0) throw new IllegalStateException("sem ações de tributo");
		acoesTributoRestantes--;
	}
	
	boolean isKhanUsado() { return khanUsado; }
	void marcarKhanUsado() { khanUsado = true; }
	
	int getMelhoriasCompradas() { return melhoriasCompradas; }
	void registrarMelhoriaComprada() { melhoriasCompradas++; }
	
	void registrarVisita(Parada p) { paradasVisitadas.add(p); }
	boolean visitou(Parada p) { return paradasVisitadas.contains(p); }
	
	int custoProximoTesouro(Cidade c) {
		return tesourosTomadosPorCidade.getOrDefault(c, 0) == 0 ? 1 : 2;
	}
	
	void registrarTesouroTomado(Cidade c) {
		tesourosTomadosPorCidade.merge(c, 1, Integer::sum);
	}
	
}
