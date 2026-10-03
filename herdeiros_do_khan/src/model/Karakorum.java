package model;

class Karakorum extends Parada {

	Karakorum(String id) {
		super(id, true, null);
	}
	
	@Override
	boolean podeReceberJogador() {
	    return true;
	}
	
	@Override
	boolean addYurt(Jogador jogador) {
		return false;
	}

}
