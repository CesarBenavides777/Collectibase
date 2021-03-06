package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import model.Storage;
import model.VHSCollection;
import view.CollectApp;

/**
 * VHSController is responsible for connecting listeners in VHS.fxml
 * to variables in VHSCollection. VHSController is also responsible for 
 * updating the VHS.txt file to be used for VHS.fxml's TableView. 
 * 
 * @author Jesus Nieto
 *
 */
public class VHSController{
	public Button backButton;
	public Button updateButton;
	public Button insertButton;
	public Button deleteButton;
	@FXML
	public TextField titleField;
	@FXML
	public TextField genreField;
	@FXML
	public TextField formatField;
	@FXML
	public TextField yearField;
	@FXML
	public TextField directorField;
	@FXML
	public TextField specialEditionField;
	@FXML
	public TextField homeRecordingsField;
	@FXML
	public TextField multiProgramField;
	@FXML
	public TextField multiTapeField;
	@FXML
	public TextField sleeveTypeField;
	@FXML
	public TableView<VHSCollection> vhsTable;
	@FXML
	public TableColumn<VHSCollection, String> titleColumn;
	@FXML
	public TableColumn<VHSCollection, String> genreColumn;
	@FXML
	public TableColumn<VHSCollection, String> formatColumn;
	@FXML
	public TableColumn<VHSCollection, String> yearColumn;
	@FXML
	public TableColumn<VHSCollection, String> directorColumn;
	@FXML
	public TableColumn<VHSCollection, String> specialEditionColumn;
	@FXML
	public TableColumn<VHSCollection, String> homeRecordingsColumn;
	@FXML
	public TableColumn<VHSCollection, String> multiProgramColumn;
	@FXML
	public TableColumn<VHSCollection, String> multiTapeColumn;
	@FXML
	public TableColumn<VHSCollection, String> sleeveTypeColumn;

	/*************
	 * initialize initializes the cells in the TableView to their respective variables in
	 * the VHSCollection. initialize also pre-populates the TableView with the data in
	 * VHS.txt.
	 */
	public void initialize() {
		cellValueFactory();
		getVHSFromFile();

	}

	/*********
	 * backButtonHandle is responsible for returning to Collectibase's main screen and saves any new
	 * entered information to VHS.txt before exiting. 
	 */
	public void backButtonHandle() {
		CollectApp.stage.show();
		CollectController.childScene.hide();
		Storage.allVHS = vhsTable.getItems();
		try {
			writeVHSFile();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/********
	 * updateButtonHandle is responsible for updating the TableView and VHS.txt whenever pressed.
	 */
	public void updateButtonHandle() {
		Storage.allVHS = vhsTable.getItems();
		try {
			writeVHSFile();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/***********8
	 * insertButtonHandle is responsible for inserting any text in the text fields onto the TableView. 
	 * Empty fields are also accounted for. After the fields have been inserted they are cleared and VHS.txt is updated.
	 */
	public void insertButtonHandle() {
		VHSCollection vhsAdd = new VHSCollection();
		// error checks if text fields are empty
		if (titleField.getText().equals("")) {
			vhsAdd.setTitle("");
		} else {
			vhsAdd.setTitle(titleField.getText());
		}

		if (genreField.getText().equals("")) {
			vhsAdd.setGenre("");
		} else {
			vhsAdd.setGenre(genreField.getText());
		}

		if (formatField.getText().equals("")) {
			vhsAdd.setFormat("");
		} else {
			vhsAdd.setFormat(formatField.getText());
		}

		if (yearField.getText().equals("")) {
			vhsAdd.setYear("0");
		} else {
			vhsAdd.setYear(yearField.getText());
		}

		if (directorField.getText().equals("")) {
			vhsAdd.setDirector("");
		} else {
			vhsAdd.setDirector(directorField.getText());
		}

		if (specialEditionField.getText().equals("")) {
			vhsAdd.setSpecialEdition("");
		} else {
			vhsAdd.setSpecialEdition(specialEditionField.getText());
		}

		if (homeRecordingsField.getText().equals("")) {
			vhsAdd.setHomeRecording("");
		} else {
			vhsAdd.setHomeRecording(homeRecordingsField.getText());
		}

		if (multiProgramField.getText().equals("")) {
			vhsAdd.setMultiProgram("");
		} else {
			vhsAdd.setMultiProgram(multiProgramField.getText());
		}

		if (multiTapeField.getText().equals("")) {
			vhsAdd.setMultiTape("");
		} else {
			vhsAdd.setMultiTape(multiTapeField.getText());
		}

		if (sleeveTypeField.getText().equals("")) {
			vhsAdd.setSleeveType("");
		} else {
			vhsAdd.setSleeveType(sleeveTypeField.getText());
		}

		// clears the text field when inserting
		vhsTable.getItems().add(vhsAdd);
		titleField.clear();
		genreField.clear();
		formatField.clear();
		yearField.clear();
		directorField.clear();
		specialEditionField.clear();
		homeRecordingsField.clear();
		multiProgramField.clear();
		multiTapeField.clear();
		sleeveTypeField.clear();

		// writes to the file to keep table updated
		Storage.allVHS = vhsTable.getItems();
		try {
			writeVHSFile();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/***********
	 * deleteButtonHandle deletes a selected field or row from the TableView. VHS.txt is
	 * also updated by deleting the respective fields from the file. 
	 */
	public void deleteButtonHandle() {
		ObservableList<VHSCollection> vhsSelected;
		Storage.allVHS = vhsTable.getItems();
		vhsSelected = vhsTable.getSelectionModel().getSelectedItems();
		vhsSelected.forEach(Storage.allVHS::remove);
		Storage.allVHS = vhsTable.getItems();
		try {
			writeVHSFile();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*********
	 * cellValueFactory is responsible for connecting the variable columns with it's respective
	 * variable in VHSCollection. cellValueFactory also allows for a single cell to be edited when double clicked. 
	 */
	public void cellValueFactory() {
		titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
		genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
		formatColumn.setCellValueFactory(new PropertyValueFactory<>("format"));
		yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
		directorColumn.setCellValueFactory(new PropertyValueFactory<>("director"));
		specialEditionColumn.setCellValueFactory(new PropertyValueFactory<>("specialEdition"));
		homeRecordingsColumn.setCellValueFactory(new PropertyValueFactory<>("homeRecording"));
		multiProgramColumn.setCellValueFactory(new PropertyValueFactory<>("multiProgram"));
		multiTapeColumn.setCellValueFactory(new PropertyValueFactory<>("multiTape"));
		sleeveTypeColumn.setCellValueFactory(new PropertyValueFactory<>("sleeveType"));

		// the code below gives it the ability to edit info
		vhsTable.setEditable(true);
		titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		yearColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		genreColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		formatColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		directorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		specialEditionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		homeRecordingsColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		multiProgramColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		multiTapeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		sleeveTypeColumn.setCellFactory(TextFieldTableCell.forTableColumn());

	}

	/*******
	 * The methods below update a single variable of a collectible when it's cell
	 * is double clicked.
	 * @param edditedCell: the cell that was edited in VHS.fxml's TableView will be the 
	 * variable that is changed in VHSCollection. 
	 */
	public void changeTitleName(CellEditEvent<VHSCollection, String> edditedCell) {

		VHSCollection titleSelected = vhsTable.getSelectionModel().getSelectedItem();
		titleSelected.setTitle(edditedCell.getNewValue().toString());

	}

	public void changeGenre(CellEditEvent<VHSCollection, String> edditedCell1) {
		VHSCollection genreSelected = vhsTable.getSelectionModel().getSelectedItem();
		genreSelected.setGenre(edditedCell1.getNewValue().toString());
	}

	public void changeFormat(CellEditEvent<VHSCollection, String> edditedCell) {
		VHSCollection formatSelected = vhsTable.getSelectionModel().getSelectedItem();
		formatSelected.setFormat(edditedCell.getNewValue().toString());
	}

	public void changeYear(CellEditEvent<VHSCollection, String> edditedCell) {
		VHSCollection yearSelected = vhsTable.getSelectionModel().getSelectedItem();
		yearSelected.setYear(edditedCell.getNewValue().toString());
	}

	public void changeDirector(CellEditEvent<VHSCollection, String> edditedCell) {
		VHSCollection directorSelected = vhsTable.getSelectionModel().getSelectedItem();
		directorSelected.setDirector(edditedCell.getNewValue().toString());
	}

	public void changeSpecialEdition(CellEditEvent<VHSCollection, String> edditedCell) {
		VHSCollection specialSelected = vhsTable.getSelectionModel().getSelectedItem();
		specialSelected.setSpecialEdition(edditedCell.getNewValue().toString());
	}

	public void changeHomeRecordings(CellEditEvent<VHSCollection, String> edditedCell) {
		VHSCollection homeSelected = vhsTable.getSelectionModel().getSelectedItem();
		homeSelected.setHomeRecording(edditedCell.getNewValue().toString());
	}

	public void changeMultiProgram(CellEditEvent<VHSCollection, String> edditedCell) {
		VHSCollection multiProgramSelected = vhsTable.getSelectionModel().getSelectedItem();
		multiProgramSelected.setMultiProgram(edditedCell.getNewValue().toString());
	}

	public void changeMultiTape(CellEditEvent<VHSCollection, String> edditedCell) {
		VHSCollection multiTapeSelected = vhsTable.getSelectionModel().getSelectedItem();
		multiTapeSelected.setMultiTape(edditedCell.getNewValue().toString());
	}

	public void changeSleeveType(CellEditEvent<VHSCollection, String> edditedCell) {
		VHSCollection sleeveSelected = vhsTable.getSelectionModel().getSelectedItem();
		sleeveSelected.setSleeveType(edditedCell.getNewValue().toString());
	}

	/********
	 * getVHSFromFile updates VHS.fxml's TableView by reading the data in VHS.txt.
	 */
	private void getVHSFromFile() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("vhs.txt")));
			String line;
			String[] array;
			while ((line = br.readLine()) != null) {
				array = line.split("%");
				vhsTable.getItems().add(new VHSCollection(array[0], array[1], array[2], array[3],
						array[4], array[5], array[6], array[7], array[8], array[9]));
			}
			br.close();

		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}
	
	/**************
	 * writeVHSFile is responsible for writing to VHS.txt in order to update VHS.fxml's TableView.
	 * @throws Exception: In case that the collection has not been previously created, the vhs.txt file is not found. 
	 */
	public void writeVHSFile() throws Exception {
		Writer writer = null;
		try {
			File file = new File("vhs.txt");
			writer = new BufferedWriter(new FileWriter(file));
			for (VHSCollection vhs : Storage.allVHS) {
				String text = vhs.getTitle() + "%" + vhs.getGenre() + "%" + vhs.getFormat() + "%" + vhs.getYear() + "%"
						+ vhs.getDirector() + "%" + vhs.getSpecialEdition() + "%" + vhs.getHomeRecording() + "%"
						+ vhs.getMultiProgram() + "%" + vhs.getMultiTape() + "%" + vhs.getSleeveType() + "\n";
				writer.write(text);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			writer.flush();
			writer.close();

		}
	}
}
