package it.polito.tdp.crimes.model;

public class Adiacenza implements Comparable<Adiacenza>{

	String id1;
	String id2;
	Integer peso;
	
	public Adiacenza(String id1, String id2, Integer peso){
		super();
		this.id1 = id1;
		this.id2 = id2;
		this.peso = peso;
	}

	public String getId1() {
		return id1;
	}

	public void setId1(String id1) {
		this.id1 = id1;
	}

	public String getId2() {
		return id2;
	}

	public void setId2(String id2) {
		this.id2 = id2;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id1 == null) ? 0 : id1.hashCode());
		result = prime * result + ((id2 == null) ? 0 : id2.hashCode());
		result = prime * result + ((peso == null) ? 0 : peso.hashCode());
		return result;
	}

	@Override
	public int compareTo(Adiacenza o) {
		return this.peso.compareTo(o.peso);
	}

	@Override
	public String toString() {
		return "Arco tra " + id1 + " e " + id2 + " --> peso=" + peso;
	}

	
}
