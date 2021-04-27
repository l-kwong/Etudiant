package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EtudiantController implements Initializable{

	@FXML
	private TableColumn<Etudiant, String> prenomColumn;

	@FXML
	private TextField txtPrenom;

	@FXML
	private TableView<Etudiant> etudiantsTable;

	@FXML
	private Button btnClear;

	@FXML
	private TextField txtAge;

	@FXML
	private Button btnEffacer;

	@FXML
	private TableColumn<Etudiant, String> deptColumn;

	@FXML
	private TableColumn<Etudiant, Double> ageColumn;

	@FXML
	private ComboBox<String> cboDept;

	@FXML
	private Button btnModifier;

	@FXML
	private TableColumn<Etudiant, String> nomColumn;

	@FXML
	private Button btnAjouter;

	@FXML
	private TextField txtNom;


	// liste pour les départements - cette liste permettra de donner les valeurs du comboBox
	private ObservableList<String> list=(ObservableList<String>) FXCollections.observableArrayList("Sciences","Droit","Medecine");

	// Placer les étudiants dans une observable list
	public ObservableList<Etudiant> etudiantData=FXCollections.observableArrayList();

	// Créer une méthode pour accéder à la liste des étudiants
	public ObservableList<Etudiant> getetudiantData()
	{
		return etudiantData;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		cboDept.setItems(list);
		// attribuer les valuers aux colonnes du tableView
		prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
		nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
		deptColumn.setCellValueFactory(new PropertyValueFactory<>("departement"));
		ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
		etudiantsTable.setItems(etudiantData);

		btnModifier.setDisable(true);
		btnEffacer.setDisable(true);
		btnClear.setDisable(true);

		showEtudiants(null);
		// Mettre à jour l'affiche d'un étudiant sélectionné
		etudiantsTable.getSelectionModel().selectedItemProperty(). addListener((
				observable, oldValue, newValue)-> showEtudiants(newValue));

	}
	
	@FXML
	public void verfiNum()	// méthode pour trouver des input non numéiques
	{
		txtAge.textProperty().addListener((observable,oldValue,newValue)->
		{
			if(!newValue.matches("^[0-9](\\.[0-9]+)?$"))
			{
				txtAge.setText(newValue.replaceAll("[^\\d*\\.]",""));	// si le input est non numérique, ça le remplace
			}
		});
	}

	// Ajouter étudiant
	@FXML
	void ajouter()
	{
		// Vérifier si un champ n'est pas vide
		if(noEmptyInput())
		{
			Etudiant tmp=new Etudiant();
			tmp=new Etudiant();
			tmp.setNom(txtNom.getText());
			tmp.setPrenom(txtPrenom.getText());
			tmp.setAge(Double.parseDouble(txtAge.getText()));
			tmp.setDepartement(cboDept.getValue());
				etudiantData.add(tmp);
				clearFields();
		}
	}

	// Effacer le contenu des champs
	@FXML
	void clearFields()
	{
		cboDept.setValue(null);
		txtNom.setText("");
		txtPrenom.setText("");
		txtAge.setText("");
	}
	
	

	// Afficher les étudiants
	public void showEtudiants(Etudiant etudiant)
	{
		if(etudiant !=null)
		{
			cboDept.setValue(etudiant.getDepartement());
			txtNom.setText(etudiant.getNom());
			txtPrenom.setText(etudiant.getPrenom());
			txtAge.setText(Double.toString(etudiant.getAge()));
			btnModifier.setDisable(false);
			btnEffacer.setDisable(false);
			btnClear.setDisable(false);
		}
		else
		{
			clearFields();
		}
	}
	
	// Mise à jour d'un étudiant
	@FXML
	public void updateEtudiant()
	{
		Etudiant etudiant=etudiantsTable.getSelectionModel().getSelectedItem();
		
		etudiant.setNom(txtNom.getText());
		etudiant.setPrenom(txtPrenom.getText());
		etudiant.setAge(Double.parseDouble(txtAge.getText()));
		etudiant.setDepartement(cboDept.getValue());
		etudiantsTable.refresh();
	}
	
	// Effacer un étudiant
	@FXML
	public void deleteEtudiant()
	{
		int selectedIndex = etudiantsTable.getSelectionModel().getSelectedIndex();
		if(selectedIndex >=0)
		{
			Alert alert=new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Effecer");
			alert.setContentText("confirmer la suppression!");
			Optional<ButtonType> result=alert.showAndWait();
			if(result.get()==ButtonType.OK)
				etudiantsTable.getItems().remove(selectedIndex);
		}
	}
	
	// vérifier champs vides
	private boolean noEmptyInput()
	{
		String errorMessage="";
		if(txtNom.getText()==null||txtNom.getText().length()==0)
			{
				errorMessage+="Le champ nom ne doit pas être vide! \n";
			}
		if(txtPrenom.getText()==null||txtPrenom.getText().length()==0)
			{
				errorMessage+="Le champ prénom ne doit pas être vide! \n";
			}
		if(txtAge.getText()==null||txtAge.getText().length()==0)
			{
				errorMessage+="Le champ age ne doit pas être vide! \n";
			}
		if(cboDept.getValue()==null)
			{
				errorMessage+="Le champ département ne doit pas être vide! \n";
			}
		if(errorMessage.length()==0)
			{
				return true;
			}
		else 
			{
			Alert alert=new Alert(AlertType.ERROR);
			alert.setTitle("Champs manquants");
			alert.setContentText("Compléter les champs manquants");
			alert.setContentText(errorMessage);
			alert.showAndWait();
			return false;
			}
	}
	
	// sauvegarde de données
	// Recupérer le chemin (path) des fichiers si ça existe
	public File getEtudiantFilePath()
	{
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String filePath = prefs.get("filePath", null);
		
		if(filePath != null)
		{
			return new File(filePath);
		}
			else
		{
			return null;
		}
	}
	
	// Attribuer un chemin de fichier
	public void setEtudiantFilePath(File file)
	{
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		if(file != null)
		{
			prefs.put("filePath", file.getPath());
		}
			else
		{
			prefs.remove("filePath");
		}
	}
	
	// Prendre les données de type XML et les convertir en données de type javafx
	public void loadEtudiantDataFromFile(File file)
	{
		try {
			JAXBContext context = JAXBContext.newInstance(EtudiantListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();
			
			EtudiantListWrapper wrapper = (EtudiantListWrapper) um.unmarshal(file);
			etudiantData.clear();
			etudiantData.addAll(wrapper.getEtudiants());
			setEtudiantFilePath(file);
			// Donner le titre du fichier sauvegardé
			Stage pStage=(Stage) etudiantsTable.getScene().getWindow();
			pStage.setTitle(file.getName());
			
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreurr");
			alert.setHeaderText("les données n'ont pas été trouvées");
			alert.setContentText("Les données ne pouvaient pas être trouvées dans le fichier : \n" +file.getPath());
			alert.showAndWait();
		}
	}
	
	// Prendre les données de type JavaFx et les convertir en type XML
	public void saveEtudiantDataToFile(File file)
	{
		try {
			JAXBContext context = JAXBContext.newInstance(EtudiantListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			EtudiantListWrapper wrapper = new EtudiantListWrapper();
			wrapper.setEtudiants(etudiantData);
			
			m.marshal(wrapper, file);
			
			// Sauvegarder dans le registre
			setEtudiantFilePath(file);
			// Donner le titre du fichier sauvegardé
			Stage pStage=(Stage) etudiantsTable.getScene().getWindow();
			pStage.setTitle(file.getName());
	
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur");
			alert.setHeaderText("Données non sauvegardées");
			alert.setContentText("Les données ne pouvaient pas être sauvegardées dans le fichier : \n" +file.getPath());
			alert.showAndWait();
		}
	}
	
	// Commencer un nouveau
	@FXML
	private void handleNew()
	{
		getetudiantData().clear();
		setEtudiantFilePath(null);
	}
	
	// Le FileChooser permet à l'usager de choisir le fichier à ouvrir
	@FXML
	private void handleOpen() 
	{
		FileChooser fileChooser = new FileChooser();
		
		// Permettre un filtre sur l'extension du fichier à chercher
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"XML files(*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		
		// montrer le dialogue
		File file = fileChooser.showOpenDialog(null);
		
		if(file != null) {
			loadEtudiantDataFromFile(file);
		}
				
	}
	
	

	// Sauvegarder le ficher corrrespondant à l'étudiant actif
	// S'il n'y a pas de fichier, le menu sauvegarder sous va s'afficher
	@FXML
	private void handleSave()
	{
		File etudiantFile = getEtudiantFilePath();
		if (etudiantFile != null) {
			saveEtudiantDataToFile(etudiantFile);
		} else {
			handleSaveAs();
		}
	}
	
	// Ouvrir le FileChooser pour trouver le chemin
	@FXML
	private void handleSaveAs() 
	{
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"XML files(*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		
		// Sauvegarde
		File file = fileChooser.showSaveDialog(null);
		
		if (file != null) {
			// Vérification de l'extension
			if (!file.getPath().endsWith(".xml")) {
				file = new File(file.getPath() + ".xml");
			}
			saveEtudiantDataToFile(file);
		} 
		
	}
	
	// Afficher les statistiques
	@FXML
	void handleStats()
	{
		try {
			FXMLLoader loader=new FXMLLoader(Main.class.getResource("AgeStat.FXML"));
			 AnchorPane pane=loader.load();
			 Scene scene=new Scene(pane);
			 AgeStat agestat=loader.getController();
			 agestat.SetStats(etudiantData);
			 Stage stage=new Stage();
			 stage.setScene(scene);
			 stage.setTitle("Statistique");
			 stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
