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
        int atual = tributosVirtuais.getOrDefault(tipo, 0);
        tributosVirtuais.put(tipo, atual + qtd);
    }
    
    void consumirTributoVirtual(TipoTributo tipo, int qtd) {
        if (qtd < 0) throw new IllegalArgumentException("quantidade negativa");
        if (tributosVirtuaisDe(tipo) < qtd)
            throw new IllegalStateException("tributo virtual insuficiente: " + tipo);
        int atual = tributosVirtuais.getOrDefault(tipo, 0);
        tributosVirtuais.put(tipo, atual - qtd);
    }
    
    // tesouros_virtuais
    
    int tesourosVirtuaisDe(TipoTesouro tipo) {
    	return tesourosVirtuais.getOrDefault(tipo, 0);
    }
    
    void adicionarTesouroVirtual(TipoTesouro tipo) {
        int atual = tesourosVirtuais.getOrDefault(tipo, 0);
        tesourosVirtuais.put(tipo, atual + 1);
    }
    
    void consumirTesouroVirtual(TipoTesouro tipo) {
        if (tesourosVirtuaisDe(tipo) <= 0)
            throw new IllegalStateException("tesouro virtual insuficiente: " + tipo);
        int atual = tesourosVirtuais.getOrDefault(tipo, 0);
        tesourosVirtuais.put(tipo, atual - 1);
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
        Set<Parada> visitadas = paradasVisitadasPorPeao.get(peao);
        if (visitadas == null) {
            visitadas = new HashSet<>();
            paradasVisitadasPorPeao.put(peao, visitadas);
        }
        visitadas.add(p);
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
    	int atual = tesourosTomadosPorCidade.getOrDefault(c, 0);
    	tesourosTomadosPorCidade.put(c, atual + 1);
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