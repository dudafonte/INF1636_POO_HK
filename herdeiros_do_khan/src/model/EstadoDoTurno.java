package model;

import java.util.HashMap; 
import java.util.HashSet;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

class EstadoDoTurno {
    private int movimentosRestantes;
    private int acoesTributoRestantes;
    private boolean khanPendente; 
    private int melhoriasGratuitasPendentes;
    private boolean colunaAtivada;
    
    private final Map<Cidade, Integer> tesourosTomadosPorCidade = new HashMap<>();
    private final Map<TipoTributo, Integer> tributosVirtuais = new EnumMap<>(TipoTributo.class);
    private final Map<TipoTesouro, Integer> tesourosVirtuais = new EnumMap<>(TipoTesouro.class);
    
    private int peaoAtivo = 0;
    private final Map<Integer, Set<Parada>> paradasVisitadasPorPeao = new HashMap<>();
    private Integer peaoDasAcoesExtras = null;
    
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
    
    // tributos_virtuais
    
    int tributosVirtuaisDe(TipoTributo tipo) {
    	return tributosVirtuais.getOrDefault(tipo, 0);
    }
    
    void adicionarTributoVirtual(TipoTributo tipo, int qtd) {
    	if (qtd < 0) throw new IllegalArgumentException("quantidade negativa");
    	tributosVirtuais.merge(tipo, qtd, Integer::sum);
    }
    
    void consumirTributoVirtual(TipoTributo tipo, int qtd) {
    	if (qtd < 0) throw new IllegalArgumentException("qunatidade negativa");
    	if (tributosVirtuaisDe(tipo) < qtd) 
    		throw new IllegalStateException("tipo virtual insuficiente" + tipo);
    	tributosVirtuais.merge(tipo, -qtd, Integer::sum);
    }
    
    // tesouros_virtuais
    
    int tesourosVirtuaisDe(TipoTesouro tipo) {
    	return tesourosVirtuais.getOrDefault(tipo, 0);
    }
    
    void adicionarTesouroVirtual(TipoTesouro tipo) {
    	tesourosVirtuais.merge(tipo, 1, Integer::sum);
    }
    
    void consumirTesouroVirtual(TipoTesouro tipo) {
    	if (tesourosVirtuaisDe(tipo) <= 0) 
    		throw new IllegalStateException("tesouro virtual insuficiente: " + tipo);
    	tesourosVirtuais.merge(tipo, -1, Integer::sum);
    }
    
    // khan_pendente
    
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
    
    // visitas
    
    void registrarVisita(int peao, Parada p) { 
        paradasVisitadasPorPeao.computeIfAbsent(peao, k-> new HashSet<>()).add(p); 
    }
    
    void registrarVisita(Parada p) {
        registrarVisita(peaoAtivo, p);
    }

    boolean visitou(Parada p) {
        return paradasVisitadasPorPeao.getOrDefault(peaoAtivo, Set.of()).contains(p);
    }
    
    boolean peaoPodeFazerAcaoExtra() {
    	return peaoDasAcoesExtras == null || peaoDasAcoesExtras == peaoAtivo;
    }
    
    void registrarAcaoExtra() {
    	if (!peaoPodeFazerAcaoExtra())
    		throw new IllegalStateException("só um peão pode fazer ações extras nesse turno.");
    	peaoDasAcoesExtras = peaoAtivo;
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
    	return colunaAtivada && !khanPendente && melhoriasGratuitasPendentes == 0;
    }
    
    boolean isColunaAtivada() {
    	return colunaAtivada;
    }
    
    void registrarColunaAtivada() {
    	colunaAtivada = true;
    }
    
    // peao
    int getPeaoAtivo() { return peaoAtivo; }
    
    void selecionarPeao(int indice) { this.peaoAtivo = indice; }
   
}