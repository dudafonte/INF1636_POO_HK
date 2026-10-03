package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class EstadoDoTurno {
    private int movimentosRestantes;
    private int acoesTributoRestantes;
    private boolean khanPendente; 
    private int melhoriasGratuitasPendentes;
    
    private final Set<Parada> paradasVisitadas = new HashSet<>();
    private final Map<Cidade, Integer> tesourosTomadosPorCidade = new HashMap<>();
    
    int getMovimentosRestantes() { 
        return movimentosRestantes; 
    }
    
    void adicionarMovimentos(int n) {
        if (n < 0) throw new IllegalArgumentException("número negativo de movimentos");
        movimentosRestantes += n;
    }
    
    void consumirMovimento() {
        if (movimentosRestantes <= 0) throw new IllegalStateException("sem movimentos restantes");
        movimentosRestantes--;
    }
    
    int getAcoesTributoRestantes() { 
        return acoesTributoRestantes; 
    }
    
    void adicionarAcoesTributoRestantes(int n) { 
        if (n < 0) throw new IllegalArgumentException("número negativo de ações de tributo");
        acoesTributoRestantes += n; 
    }
    
    void consumirAcaoTributo() {
        if (acoesTributoRestantes <= 0) throw new IllegalStateException("sem ações de tributo restantes");
        acoesTributoRestantes--;
    }
    
    boolean isKhanPendente() { 
        return khanPendente; 
    }
    
    void marcarKhanPendente(boolean pendente) { 
        this.khanPendente = pendente; 
    }
    
    int getMelhoriasGratuitasPendentes() { 
        return melhoriasGratuitasPendentes; 
    }
    
    void adicionarMelhoriaGratuitaPendente() { 
        melhoriasGratuitasPendentes++; 
    }
    
    void consumirMelhoriaGratuitaPendente() {
        if (melhoriasGratuitasPendentes <= 0) throw new IllegalStateException("sem melhorias gratuitas pendentes");
        melhoriasGratuitasPendentes--;
    }
    
    void registrarVisita(Parada p) { 
        paradasVisitadas.add(p); 
    }
    
    boolean visitou(Parada p) { 
        return paradasVisitadas.contains(p); 
    }
    
    int custoProximoTesouro(Cidade c) {
        return tesourosTomadosPorCidade.getOrDefault(c, 0) == 0 ? 1 : 2;
    }
    
    void registrarTesouroTomado(Cidade c) {
        tesourosTomadosPorCidade.merge(c, 1, Integer::sum);
    }
    
    void resolverKhan() {
    	this.khanPendente = false;
    }
    
    boolean podeFinalizarTurno() {
    	return !khanPendente;
    }
}