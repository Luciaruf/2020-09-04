package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	ImdbDAO dao;
	Graph<Movie, DefaultWeightedEdge> graph;
	
	public Model() {
		super();
		this.dao = new ImdbDAO();
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	}
	
	public Graph creaGrafo(double rank) {
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.graph, this.dao.listAllMoviesWithRank());
		
		
		for(Movie m1 : this.graph.vertexSet()) {
			for(Movie m2 : this.graph.vertexSet()) {
				if(!m1.equals(m2)) {
					if(m1.getRank()>= rank && m2.getRank()>=rank) {
						
						List<Integer> attori1 = this.dao.listaAttoriPerFilmId(m1.getId());
						List<Integer> attori2 = this.dao.listaAttoriPerFilmId(m2.getId());
						
						List<Integer> distinti = new ArrayList<>();
						int almeno = 0;
						
						for(Integer i1 : attori1) {
							for(Integer i2 : attori2) {
								if(i1.compareTo(i2)==0) {
									almeno++;
								}
							}
						}
						
						for(Integer int1 : attori1) {
							for(Integer int2 : attori2) {
								if(almeno != 0 && int1.compareTo(int2)==0) {
									distinti.add(int1);
								}
							}
						}
						
						if(distinti.size()!=0) {
							Graphs.addEdge(this.graph, m1, m2, distinti.size());
						}
						
					}
					
				}
			}
		}
		
		for(DefaultWeightedEdge e : this.graph.edgeSet()) {
			System.out.println(this.graph.getEdgeWeight(e));
		}
		
		return this.graph;
	}
	
	public Movie getMassimoMovie(){
		
		int best = 0;
		Movie m = null;
		
		for(Movie mm : this.graph.vertexSet()) {
			int somma = 0;
			for(DefaultWeightedEdge e : this.graph.incomingEdgesOf(mm)) {
				somma += this.graph.getEdgeWeight(e);
			}

			if(somma>best) {
				best = somma;
				m=mm;
			}
		}
		
		return m;
	}
	
	public Integer getMassimoInt() {
		int best = 0;
		Movie m = null;
		
		for(Movie mm : this.graph.vertexSet()) {
			int somma = 0;
			for(DefaultWeightedEdge e : this.graph.incomingEdgesOf(mm)) {
				somma += this.graph.getEdgeWeight(e);
			}

			if(somma>best) {
				best = somma;
				m=mm;
			}
		}
		
		return best;
	}
	
	
}
