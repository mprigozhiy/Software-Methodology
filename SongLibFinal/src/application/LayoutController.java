//Mikhail Prigozhiy and Michael Calabrese

package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.StringTokenizer;

import com.sun.glass.events.MouseEvent;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Pair;

public class LayoutController extends Application implements Initializable {

	@FXML
	ListView<song> library;
	@FXML
	Button addButton;
	@FXML
	Button editButton;
	@FXML
	Button deleteButton;
	@FXML
	TextField artistText;
	@FXML
	TextField songText;
	@FXML
	TextField albumText;
	@FXML
	TextField yearText;
	@FXML
	Text artistLabel;
	@FXML
	Text songLabel;
	@FXML
	Text albumLabel;
	@FXML
	Text yearLabel;
	@FXML
	HBox bottom;

	ObservableList<song> data = FXCollections.observableArrayList(
			//new song("Test", "Am", "I", "2012"),new song("Test", "bye", "I", "2012"),new song("Test", "he", "I", "2012")
			);

	//ObservableList<String> songName = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		library.setCellFactory(new Callback<ListView<song>, ListCell<song>>(){


			@Override
			public ListCell<song> call(ListView<song> arg0) {
				ListCell<song> cell = new ListCell<song>(){


					@Override
					protected void updateItem(song t, boolean bln) {
						super.updateItem(t, bln);
						if (t != null) {
							setText(t.getSong());
						} else {
							setText("");
						}
					}
				};

				cell.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2 && (!cell.isEmpty())) {
						showItem();
					}});

				return cell;
			}
		});

		try {
			getData();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		library.setItems(data);
		
		if(data.size() > 0){
			library.getSelectionModel().select(0);
			library.scrollTo(0);
			artistLabel.setText("Artist: " + library.getSelectionModel().getSelectedItem().getArtist());
			
			songLabel.setText("Song: " + library.getSelectionModel().getSelectedItem().getSong());
			
			if(library.getSelectionModel().getSelectedItem().getAlbum().compareTo("") == 0){
				albumLabel.setText("Album: n/a");
			} else {
			albumLabel.setText("Album: " + library.getSelectionModel().getSelectedItem().getAlbum());
			}
			
			if(library.getSelectionModel().getSelectedItem().getYear().compareTo("") == 0){
				yearLabel.setText("Year: n/a");
			} else {
				yearLabel.setText("Year: " + library.getSelectionModel().getSelectedItem().getYear());
			}
		}
		
		if(library.getSelectionModel().getSelectedIndex() < 0){
			deleteButton.setDisable(true);
			editButton.setDisable(true);
		}


		setEvents();


	}


	private void getData() throws FileNotFoundException{
		File file = new File("songs.txt");
		if (!file.exists()){
			return;
		}
		Scanner ptr = new Scanner(file);
		String currLine;
		String Artist;
		String Song;
		String Album;
		String Year;

		while(ptr.hasNextLine()){
			currLine = ptr.nextLine();
			StringTokenizer st = new StringTokenizer(currLine, ",");
			while(st.hasMoreTokens()){
				Artist = st.nextToken().trim();
				Song = st.nextToken().trim();
				Album = st.nextToken().trim();
				Year = st.nextToken().trim();
				data.add(new song(Artist, Song, Album, Year));
			}
		}

	}
	private void setEvents(){
		
		library.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
				
			if(newValue != null){
				artistLabel.setText("Artist: " + newValue.getArtist());
				
				songLabel.setText("Song: " + newValue.getSong());
				
				if(newValue.getAlbum().compareTo("") == 0){
					albumLabel.setText("Album: n/a");
				} else {
				albumLabel.setText("Album: " + newValue.getAlbum());
				}
				
				if(newValue.getYear().compareTo("") == 0){
					yearLabel.setText("Year: n/a");
				} else {
					yearLabel.setText("Year: " + newValue.getYear());
				}
			
			} else {
				artistLabel.setText("Artist: n/a");
				songLabel.setText("Song: n/a");
				albumLabel.setText("Album: n/a");
				yearLabel.setText("Year: n/a");
			}
		});

		
		addButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				String newSong = songText.getText().trim();
				String newArtist = artistText.getText().trim();
				String newAlbum = albumText.getText().trim();
				String newYear = yearText.getText().trim();
				
				
				song newSongObject = new song(newArtist, newSong, newAlbum, newYear);
					
					if(newArtist.trim().compareTo("") == 0 || newSong.trim().compareTo("") == 0){
						blankField();
					} else {
				
					if(addConfirmation(newSongObject)){
				
					int result = addOrdered(newSongObject);
					
					if (result == -1) {
						duplicate(newSongObject);
					} else if (result == 1){
						blankField();
					}
					
					if(data.size() == 1){
						library.getSelectionModel().select(0);
						library.scrollTo(0);
					}
					}
					}
					
				}});

	
		yearText.textProperty().addListener(
				(observable, oldValue, newValue) -> {
					checkYear(oldValue, newValue, yearText);
				});
		
		bottom.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent key) {
				if(key.getCode() == KeyCode.ENTER){
					//System.out.println("Enter key");
					if(artistText.isFocused() || songText.isFocused() || albumText.isFocused() || yearText.isFocused()){
						song input = new song(artistText.getText().trim(), songText.getText().trim(), albumText.getText().trim(), yearText.getText().trim());
						if(input.getArtist().trim().compareTo("") == 0 || input.getSong().trim().compareTo("") == 0){
							blankField();
						} else {
						if(addConfirmation(input)){
						int result = addOrdered(input);
						//System.out.println("attempting to enter");
						if(result == -1){
							duplicate(input);
						}}
					}
				}
				
					
				if(data.size() == 1){
					library.getSelectionModel().select(0);
					library.scrollTo(0);
				}
				
				library.requestFocus();
				
			}
			
		}});
		
		library.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent key) {
				
				if(key.getCode() == KeyCode.DELETE && data.size() > 0){
					boolean delete = deleteConfirmation(library.getSelectionModel().getSelectedItem());
					if(delete){
					int index = library.getSelectionModel().getSelectedIndex();
					data.remove(index);
					library.getSelectionModel().select(index);
					library.scrollTo(index);
					library.setItems(data);
					if(data.size() == 0){
						deleteButton.setDisable(true);
						editButton.setDisable(true);
					}
				} }
				
			}}

				);

		deleteButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				int index = library.getSelectionModel().getSelectedIndex();
				boolean delete = deleteConfirmation(library.getSelectionModel().getSelectedItem());
				if(data.size() > 0 && delete) {
					data.remove(index);
					library.setItems(data);
					library.getSelectionModel().select(index);
					library.scrollTo(index);
					if(data.size() == 0){
						deleteButton.setDisable(true);
						editButton.setDisable(true);
					}
				}

			}

		});
		
		editButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				song editTarget = library.getSelectionModel().getSelectedItem();
				
				Dialog<song> dialog = new Dialog<>();
				ButtonType edit = new ButtonType("Update", ButtonData.OK_DONE);
				dialog.getDialogPane().getButtonTypes().addAll(edit, ButtonType.CANCEL);
				
				dialog.setTitle("Update Properties");
				
				TextField Artist = new TextField(editTarget.getArtist());
				Artist.setPromptText("Required Field");
				TextField Song = new TextField(editTarget.getSong());
				Song.setPromptText("Required Field");
				TextField Album = new TextField(editTarget.getAlbum());
				Album.setPromptText("Optional Field");
				TextField Year = new TextField(editTarget.getYear());
				Year.setPromptText("Optional Field");
				Year.textProperty().addListener(
						(observable, oldValue, newValue) -> {
							checkYear(oldValue, newValue, Year);
						});
				
				
				
				GridPane contents = new GridPane();
				contents.add(new Label("Artist: "), 0, 0);
				contents.add(Artist, 1, 0);
				contents.add(new Label("Song: "), 0, 1);
				contents.add(Song, 1, 1);
				contents.add(new Label("Album: "), 0, 2);
				contents.add(Album, 1, 2);
				contents.add(new Label("Year: "), 0, 3);
				contents.add(Year, 1, 3);
				
				Platform.runLater(() -> library.requestFocus());
				
				
				dialog.setResultConverter(dialogButton -> {
				    if (dialogButton == edit) {
				        return new song(Artist.getText(), Song.getText(), Album.getText(), Year.getText());
				    }
				    return null;
				});
				
				
				dialog.getDialogPane().setContent(contents);
				Optional<song> result = dialog.showAndWait();

			
					
				
				result.ifPresent(song -> 
					{		
						if(editConfirmation(editTarget, result.get())){
							data.remove(editTarget);
							if(addOrdered(song) == -1){
							addOrdered(editTarget);
							Alert alert = new Alert(AlertType.ERROR);
							alert.setHeaderText("No two songs with the same name can have the same artist.");
							alert.setTitle("Duplicate Error");
							alert.setContentText("You're attempting to edit \"" + editTarget.getSong() + "\" by " + "\"" + 
									editTarget.getArtist() + "\" to \"" + song.getSong() + "\" by " + "\"" + 
									song.getArtist() + "\" which already exists in your library so no changes were made.");
							alert.showAndWait();
							
							
						} else if(addOrdered(song) == 1){
							addOrdered(editTarget);	
							blankField();
					}
						
						}});
			}

			private void checkYear(String oldValue, String newValue, TextField field) {
				
					
				try {
			        Integer.parseInt(newValue);
			        
			    } catch (NumberFormatException nfe) {
			    	
			    	if(field.getText().length() > 1){
			    		field.setText(newValue.substring(0, newValue.length()-1));
			    	} else {
			    		field.setText("");
			    	}}
				
				if(newValue.length() > 4){
					field.setText(oldValue);
				}  
				
			}
			
		});



	}

	private void checkYear(String oldValue, String newValue, TextField field) {
		try {
	        Integer.parseInt(newValue);
	        
	    } catch (NumberFormatException nfe) {
	    	
	    	if(field.getText().length() > 1){
	    		field.setText(newValue.substring(0, newValue.length()-1));
	    	} else {
	    		field.setText("");
	    	}}
		
		if(newValue.length() > 4){
			field.setText(oldValue);
		}
		
	}


	private void setAlerts(Stage primaryStage){

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){

			@Override
			public void handle(WindowEvent event) {

				//System.out.println("closing");

				try {
					PrintWriter fw = new PrintWriter("songs.txt");
					BufferedWriter bw = new BufferedWriter(fw);
					int index;
					for(index = 0; index < data.size(); index++){

						//System.out.println(data.get(index).getSong());
						song currSong = data.get(index);
						String toFile = " " + currSong.getArtist() + ", " + currSong.getSong() + ", " + currSong.getAlbum() + ", " + currSong.getYear();
						bw.write(toFile);
						bw.newLine();

					}

					bw.close();
					//System.out.println("Save Complete!");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}



			}


		});


		if(data.size() > 1){
			library.getSelectionModel().select(0);
			library.scrollTo(0);
		}



		library.getSelectionModel().selectedItemProperty().addListener(
				(obs, oldVal, newVal) ->
				enableDelete()); 
	}

	public void enableDelete(){
		deleteButton.setDisable(false);
		editButton.setDisable(false);
		
	}


	private void showItem() {
		if(data.size() > 0){

			Alert alert = new Alert(AlertType.INFORMATION);
			//alert.initOwner(primaryStage);
			alert.setTitle("List Item");
			alert.setHeaderText(
					"Selected song properties");
			song selected = library.getSelectionModel().getSelectedItem();
			String content;
			
			if(selected.getAlbum().compareTo("") == 0 && selected.getYear().compareTo("") == 0){
				content = "\nArtist: " + selected.getArtist() + "\nSong Title: " + selected.getSong() +
						"\nAlbum: n/a \nYear: n/a";

			} else if (selected.getAlbum().compareTo("") == 0){
				content = "\nArtist: " + selected.getArtist() + "\nSong Title: " + selected.getSong() +
						"\nAlbum: n/a"  + "\nYear: " + selected.getYear();

			} else if (selected.getYear().compareTo("") == 0) {
				content = "\nArtist: " + selected.getArtist() + "\nSong Title: " + selected.getSong() +
						"\nAlbum: " + selected.getAlbum() + "\nYear: n/a";

			} else {
				content = "\nArtist: " + selected.getArtist() + "\nSong Title: " + selected.getSong() +
						"\nAlbum: " + selected.getAlbum() + "\nYear: " + selected.getYear();

			}

			alert.setContentText(content);
			alert.showAndWait();
		} 
	}
	
	private void blankField(){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText("Missing Artist/Song names");
		alert.setTitle("Missing Fields");
		alert.setContentText("You must fill out both the Artsit and Song fields in order to add a song.");
		alert.showAndWait();
	}
	
	private int addOrdered(song song){
		song.setArtist(song.getArtist().trim());
		song.setSong(song.getSong().trim());
		song.setAlbum(song.getAlbum().trim());
		song.setYear(song.getYear().trim());
		
		if(song.getSong().equals("") || song.getArtist().equals("")){

			
			return 1;
		} else {
		
		if(data.size() == 1){
			if(song.getSong().toLowerCase().compareTo(data.get(0).getSong().toLowerCase()) == 0){
				if(song.getArtist().toLowerCase().compareTo(data.get(0).getArtist().toLowerCase()) == 0){
					//System.out.println("Same song+artist already exist.");
					return -1;
				}
			} else if(song.getSong().toLowerCase().compareTo(data.get(0).getSong().toLowerCase()) < 0){
				data.add(0, song);
				library.getSelectionModel().select(0);
				library.scrollTo(0);
				artistText.clear();
				songText.clear();
				albumText.clear();
				yearText.clear();
				return 0;
			}
		}
		
		for(int i = 0; i < data.size(); i++){ 
			if(song.getSong().toLowerCase().compareTo(data.get(i).getSong().toLowerCase()) == 0){
				if(song.getArtist().toLowerCase().compareTo(data.get(i).getArtist().toLowerCase()) == 0){
					//System.out.println("Same song+artist already exist.");
					return -1;
				}
			}}
		
			
			for(int i = 0; i < data.size(); i++){
				if(song.getSong().toLowerCase().compareTo(data.get(i).getSong().toLowerCase()) < 0 && song.getSong().toLowerCase().compareTo(data.get(i+1).getSong().toLowerCase()) < 0){
					data.add(i, song);
					library.getSelectionModel().select(i);
					library.scrollTo(i);
					break;
				} else if (i == (data.size() - 2)){ //comparing to the second to last object
					if (song.getSong().toLowerCase().compareTo(data.get(i+1).getSong().toLowerCase()) < 0){
						data.add(i+1, song);
						library.getSelectionModel().select(i+1);
						library.scrollTo(i+1);
						break;
					} else {
						data.add(song);
						library.getSelectionModel().select(data.size()-1);
						library.scrollTo(data.size()-1);
						break;
					}}
			}
			//data.add(new song(artistText.getText(), songText.getText(), albumText.getText(), yearText.getText()));
			if(data.size() == 0){
				data.add(song);
				library.getSelectionModel().select(0);
				library.scrollTo(0);
			} else if(data.size() == 1){
				if(song.getSong().toLowerCase().compareTo(data.get(0).getSong().toLowerCase()) < 0){
					data.add(0, song);
					library.getSelectionModel().select(0);
					library.scrollTo(0);
				} else {
					data.add(song);
					library.getSelectionModel().select(1);
					library.scrollTo(1);
				}}}
		
		artistText.clear();
		songText.clear();
		albumText.clear();
		yearText.clear();
		
		return 0;
	}
	
	private void duplicate(song newSongObject){
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("Duplicate");
		alert.setHeaderText("This song already exists in your library.");
		alert.setContentText("\"" + newSongObject.getSong() + "\" by \"" +
		newSongObject.getArtist() + "\" already exists in your library."
				);
		alert.showAndWait();
	}
	
	private boolean addConfirmation(song target){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setContentText("Are you sure you want to add: \"" + target.getSong() + "\" by \"" + target.getArtist()
			+ "\"?");
		alert.setTitle("Add Confirmation");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK){
			return true;
		} else {
			return false;
		}
	}
	
	private boolean deleteConfirmation(song target){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setContentText("Are you sure you want to delete: \"" + target.getSong() + "\" by \"" + target.getArtist()
			+ "\"?");
		alert.setTitle("Delete Confirmation");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK){
			return true;
		} else {
			return false;
		}
	}
	
	
	
	private boolean editConfirmation(song oldVal, song newVal){
		
		if(oldVal.getArtist().compareTo(newVal.getArtist().trim()) == 0 && 
				oldVal.getSong().compareTo(newVal.getSong().trim()) == 0 &&
						oldVal.getAlbum().compareTo(newVal.getAlbum().trim()) == 0 &&
								oldVal.getYear().compareTo(newVal.getYear().trim()) == 0) {
				return false;
		}
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setContentText("Are you sure you want to make these changes?");
		alert.setTitle("Edit Confirmation");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK){
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		setAlerts(primaryStage);
	}

}
