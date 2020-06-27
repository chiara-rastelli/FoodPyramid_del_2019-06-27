package it.polito.tdp.crimes.model;

import java.time.LocalDate;

public class TestModel {

	public static void main(String[] args) {
		
		Model m = new Model();
	//	for (LocalDate l : m.getAllDate())
	//		System.out.println(l+"\n");
		
		m.creaGrafo("drug-alcohol", LocalDate.of(2014, 4, 15));
		
	}

}
