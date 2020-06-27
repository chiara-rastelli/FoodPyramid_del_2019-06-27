package it.polito.tdp.crimes.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	EventsDao db;
	SimpleWeightedGraph<String, DefaultWeightedEdge> graph;
	List<Adiacenza> listaAdiacenzeGrafo;
	Double pesoMedio;
	
	List<String> percorsoMigliore;
	Double pesoMassimo;
	
	public Model() {
		this.db = new EventsDao();
	}
	
	public Double getPesoMedio() {
		return this.pesoMedio;
	}
	
	public List<String> getCategories(){
		return this.db.listAllCategories();
	}
	
	public List<LocalDate> getAllDate(){
		List<LocalDate> result = new ArrayList<>();
		for (LocalDate temp : this.db.listAllDays())
			result.add(temp);
		Collections.sort(result);
		return result;
	}

	public void creaGrafo(String categoria, LocalDate data) {
		int anno = data.getYear();
		int mese = data.getMonthValue();
		int giorno = data.getDayOfMonth();
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.graph, this.db.listAllEventsByCategoryAndDate(anno, mese, giorno, categoria));
		System.out.println("Grafo creato con "+this.graph.vertexSet().size()+" vertici!\n");
		this.listaAdiacenzeGrafo = new ArrayList<>(this.db.listAllAdiacenza(anno, mese, giorno, categoria));
		Double pesoTot = 0.0;
		for (Adiacenza a: this.listaAdiacenzeGrafo) {
			if (!this.graph.containsEdge(a.id1, a.id2) && !a.id1.equals(a.id2))
				Graphs.addEdge(this.graph, a.getId1(), a.getId2(), a.getPeso());
			pesoTot += a.getPeso();
		}
		this.pesoMedio = pesoTot/this.listaAdiacenzeGrafo.size();
		System.out.println("Grafo creato con "+this.graph.edgeSet().size()+" archi!\n");
	}
	
	public List<String> getPercorsoMigliore(){
		return this.percorsoMigliore;
	}
	
	public Double getPesoMassimo() {
		return this.pesoMassimo;
	}
	
	public List<Adiacenza> getAllAdiacenze(){
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		for (Adiacenza a : this.listaAdiacenzeGrafo)
			if (a.getPeso() < this.pesoMedio)
				result.add(a);
		Collections.sort(result);
		return result;
	}

	public void cercaPercorso(String id1, String id2) {
		this.percorsoMigliore = new ArrayList<String>();
		this.pesoMassimo = Double.MIN_VALUE;
		List<String> parziale = new ArrayList<>();
		parziale.add(id1);
		this.ricorri(parziale, 0.0,id2);
	}

	private void ricorri(List<String> parziale, Double pesoParziale, String id2) {
		
		//caso terminale: sono arrivata a destinazione
		if (parziale.get(parziale.size()-1).compareTo(id2)==0) {
			if (pesoParziale > this.pesoMassimo) {
				this.pesoMassimo = pesoParziale;
				this.percorsoMigliore = new ArrayList<>(parziale);
			}
			return;
		}
		
		for (String s : Graphs.neighborListOf(this.graph, parziale.get(parziale.size()-1))) {
			if (!parziale.contains(s)) {
				DefaultWeightedEdge eTemp = this.graph.getEdge(parziale.get(parziale.size()-1), s);
				Double peso = this.graph.getEdgeWeight(eTemp);
				parziale.add(s);
				this.ricorri(parziale, pesoParziale+peso, id2);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}
}
