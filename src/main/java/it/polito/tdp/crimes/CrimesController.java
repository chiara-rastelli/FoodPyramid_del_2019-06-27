/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CrimesController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<LocalDate> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<Adiacenza> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	String categoria = this.boxCategoria.getValue();
    	LocalDate data = this.boxGiorno.getValue();
    	if (categoria == null || data == null) {
    		this.txtResult.setText("Devi selezionare sia una categoria di reato che un giorno per "
    				+ "potre creare il grafo!\n");
    		return;
    	}
    	this.model.creaGrafo(categoria, data);
    	this.txtResult.appendText("Peso medio degli archi del grafo = "+this.model.getPesoMedio()+"\n");
    	this.txtResult.appendText("Ecco gli archi con peso minore del peso medio degli archi del grafo:\n");
    	for (Adiacenza a: this.model.getAllAdiacenze())
    		this.txtResult.appendText(a.toString()+"\n");
    	this.boxArco.getItems().addAll(this.model.getAllAdiacenze());
    	this.btnPercorso.setDisable(false);
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	Adiacenza arco = this.boxArco.getValue();
    	if (arco == null) {
    		this.txtResult.setText("Devi prima selezionare un grafo per poter trovare il percorso!\n");
    		return;
    	}
    	txtResult.appendText("Calcola percorso tra "+arco.getId1()+" e "+arco.getId2()+"\n");
    	model.cercaPercorso(arco.getId1(), arco.getId2());
    	for (String s : this.model.getPercorsoMigliore())
    		this.txtResult.appendText(s+"\n");
    	this.txtResult.appendText("Peso percorso piu' lungo: "+this.model.getPesoMassimo()+"\n");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxCategoria.getItems().addAll(this.model.getCategories());
    	this.boxGiorno.getItems().addAll(this.model.getAllDate());
    	this.btnPercorso.setDisable(true);
    }
}
