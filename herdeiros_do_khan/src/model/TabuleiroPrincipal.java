package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

class TabuleiroPrincipal {
	private final Map<String, Parada> paradas = new HashMap<>();
	private final List<Regiao> regioes =new ArrayList<>();
	private final List<TipoTesouro> poolTesouros = new ArrayList<>();
	private final Queue<Cidade> filaCidades = new LinkedList<>();
	private final List<Cidade> cidadesAtivas = new ArrayList<>();
	private final List<Cidade> cidadesConquistadas = new ArrayList<>();
	private final Karakorum karakorum = new Karakorum("Karakorum");
	
	TabuleiroPrincipal() {
		inicializarPoolTesouros();
		inicializarMapa();
	}
	
	private void inicializarMapa() {
		Regiao regiao = new Regiao(NomeRegiao.CHINA);
		regioes.add(regiao);
		
		Cidade cidadeAtiva = new Cidade(NomeCidade.PEQUIM, regiao);
		List<TipoTesouro> tesouros = sacarTesouros(4);
		for (TipoTesouro tesouro : tesouros) {
			cidadeAtiva.addTesouro(tesouro);
		}
		cidadesAtivas.add(cidadeAtiva);
		
		
		Cidade cidadeFila = new Cidade(NomeCidade.KAESONG, regiao);
		filaCidades.add(cidadeFila);
		
		
		Parada paradaA = new Parada("idA", false, regiao);
		Parada paradaB = new Parada("idB", false, regiao);
		Parada paradaC = new Parada("idC", true, regiao);
		Parada paradaD = new Parada("idD", false, regiao);
		Parada paradaE = new Parada("idE", false, regiao);
		
		paradas.put("idA", paradaA);
		paradas.put("idB", paradaB);
		paradas.put("idC", paradaC);
		paradas.put("idD", paradaD);
		paradas.put("idE", paradaE);
		
		paradaA.conectarCom(paradaB);
		paradaA.conectarCom(paradaC);
		paradaB.conectarCom(paradaE);
		
		paradaA.addCidadeAdjacente(cidadeAtiva);
		

	}
	
	private static <T> void embaralhar(List<T> lista) {
	    Collections.shuffle(lista);
	}
	
	private void inicializarPoolTesouros() {
	    for (TipoTesouro tipo : TipoTesouro.values()) {
	        for (int i = 0; i < 8; i++) {
	            poolTesouros.add(tipo);
	        }
	    }
	    embaralhar(poolTesouros);
	}
	

	Parada getParada(String id) {
		return paradas.get(id);
	}

	List<Regiao> getRegioes() {
		return List.copyOf(regioes);
	}
	
	boolean mover(Jogador jogador, List<Parada> caminho) {
		if (jogador == null) throw new IllegalArgumentException("jogador não pode ser nulo");
		if (caminho == null || caminho.isEmpty()) return false;
		if (!validarCaminho(jogador, caminho)) {
			return false;
		}
		executarCaminho(jogador, caminho);
		return true;
	}
	
	private boolean validarCaminho(Jogador jogador, List<Parada> caminho) {
		int movimentos = 0;
		Parada paradaAnterior = jogador.getParadaAtual();
		
		for (int i = 0; i < caminho.size(); i++) {
			if (!caminho.get(i).eAdjacenteDeParada(paradaAnterior)) { return false; }
			if (i != caminho.size()-1) {
				if (!caminho.get(i).temYurtDoJogador(jogador)) { movimentos++; }
			}
			else {
				if (!caminho.get(i).podeReceberJogador()) { return false; }
				movimentos++;
			}
			paradaAnterior = caminho.get(i);
		}
		return movimentos <= jogador.getTurno().getMovimentosRestantes();
	}
	
	private void executarCaminho(Jogador jogador, List<Parada> caminho) {
		for (int i = 0; i < caminho.size(); i++) {
			if (i == caminho.size()-1) {
				jogador.getTurno().consumirMovimento();
				jogador.getTurno().registrarVisita(caminho.get(i));
			}
			else {
				if (!caminho.get(i).temYurtDoJogador(jogador)) {
					jogador.getTurno().registrarVisita(caminho.get(i));
					jogador.getTurno().consumirMovimento();
				}
			}
		}
		jogador.setParadaAtual(caminho.getLast());
	}
	
	boolean pegarTributo(Jogador jogador, Provincia provincia, TipoTributo tributo) {
		if (jogador == null) throw new IllegalArgumentException("jogador não pode ser nulo");
		if (provincia == null) throw new IllegalArgumentException("provincia não pode ser nula");
		
		boolean paradaAdjacente = false;
		for (Parada parada : provincia.getParadasAdjacentes()) {
			if (jogador.getTurno().visitou(parada)) {
				paradaAdjacente = true;
				break;
			}
		}
		
		if (paradaAdjacente && provincia.getTributo() == tributo) {
			if (provincia.diminuiQtdTributo()) {
				jogador.adicionarTributo(tributo, 1);
				return true;
			}
		}
		return false;
	}
	
	boolean construirYurt(Jogador jogador, Parada parada) {
		if (jogador == null) throw new IllegalArgumentException("jogador não pode ser nulo");
		if (parada == null) throw new IllegalArgumentException("parada não pode ser nula");
		
		if (jogador.getTurno().visitou(parada)) {
			return parada.addYurt(jogador);
		}
		return false;
	}
	
	boolean atacarCidade(Jogador jogador, Cidade cidade, TipoTesouro tesouro) {
		if (jogador == null) throw new IllegalArgumentException("jogador não pode ser nulo");
		if (cidade == null) throw new IllegalArgumentException("cidade não pode ser nula");
		
		int custo = jogador.getTurno().custoProximoTesouro(cidade);
		
		if (jogador.getParadaAtual().eAdjacenteDeCidade(cidade) && cidade.getTesouros().contains(tesouro) 
				&& jogador.podeGastarComVirtuais(TipoTributo.ESPADA, custo)) {
			jogador.gastarComVirtuais(TipoTributo.ESPADA, custo);
			cidade.removeTesouro(tesouro);
			jogador.adicionarTesouro(tesouro);
			jogador.getTurno().registrarTesouroTomado(cidade);
			
			if (!cidade.temTesouro()) {
				conquistarCidade(jogador, cidade);
			}
			return true;
		}
		return false;
	}
	
	private void conquistarCidade(Jogador jogador, Cidade cidade) {
		cidadesAtivas.remove(cidade);
		cidadesConquistadas.add(cidade);
		cidade.addYurtConquista(jogador);
		
		Cidade cidadeNova = filaCidades.poll();
		if (cidadeNova != null) {
			List<TipoTesouro> tesouros = sacarTesouros(4);
			for (TipoTesouro tesouro : tesouros) {
				cidadeNova.addTesouro(tesouro);
			}
			cidadesAtivas.add(cidadeNova);
		}
	}
	
	private List<TipoTesouro> sacarTesouros(int qtd) {
	    List<TipoTesouro> sacados = new ArrayList<>();
	    for (int i = 0; i < qtd && !poolTesouros.isEmpty(); i++) {
	        sacados.add(poolTesouros.remove(poolTesouros.size() - 1));
	    }
	    return sacados;
	}
	
	boolean usarKhanProvincia(Jogador jogador, Provincia provincia) {
		// TODO
		return false;
	}
	
	boolean usarKhanMelhorias(Jogador jogador) {
		// TODO
		return false;
	}
	
	boolean comprarMelhoria(Jogador jogador, Melhoria melhoria) {
		// TODO
		return false;
	}
	
	boolean comprarMelhoria(Jogador jogador) {
		// TODO
		return false;
	}
	
	boolean entregarTesouros(Jogador jogador, List<TipoTesouro> tesouros) {
		// TODO
		return false;
	}
	
	boolean cumprirDemanda(Jogador jogador, CartaConselheiroAberto carta) {
		// TODO
		return false;
	}
	
	boolean ganharVotos(Jogador jogador, int qtd) {
		// TODO
		return false;
	}

}
