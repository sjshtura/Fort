package view;

import javafx.geometry.Pos;
import model.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import controller.Controller;

public class View extends BorderPane {
    private Controller controller;
    private ListView<Song> library, playlist;
    private TextField titleTextField;
    private TextField artistTextField;
    private TextField albumTextField;
    private Label playTime;
    private Slider volumeSlider;

    /**
     * this class creates the main UI
     */

    public View() {
        getStyleClass().add("borderpane");

        //Declaration and implementation for the top part
        HBox menuBar = new HBox(10);
        ChoiceBox choiceBox = new ChoiceBox(FXCollections.observableArrayList("Funktionalität erst später"));
        choiceBox.getSelectionModel().selectFirst();

        Button loadButton = new Button();
        loadButton.setId("loadButton");
        loadButton.getStyleClass().add("buttonSmall");

        Button saveButton = new Button();
        saveButton.setId("saveButton");
        saveButton.getStyleClass().add("buttonSmall");

        playTime = new Label("0:00");
        playTime.setPadding(new Insets(3,3,3,3));

        menuBar.getChildren().addAll(choiceBox, loadButton, saveButton, playTime);

        //Declaration and implementation for the left part
        library = new ListView<>();

        //Declaration and implementation for the center part
        HBox centerBox =  new HBox();
        VBox playlistControllerBox = new VBox();

        playlist = new ListView<>();
        playlist.getSelectionModel().selectedItemProperty().addListener(observable -> {
            if(playlist.getSelectionModel().getSelectedItem() != null) {
                controller.readMetadata(playlist.getSelectionModel().getSelectedItem());
            }
        });

        Button removeFromPlaylist = new Button();
        removeFromPlaylist.setId("deleteButton");
        removeFromPlaylist.setOnAction(event -> controller.removeFromPlaylist(playlist.getSelectionModel().getSelectedIndex()));

        Button toPlaylist = new Button();
        toPlaylist.setId("toPlaylistButton");
        toPlaylist.setOnAction(event -> controller.addToPlaylist(library.getSelectionModel().getSelectedItem()));

        playlistControllerBox.getChildren().addAll(removeFromPlaylist, toPlaylist);
        centerBox.getChildren().addAll(playlistControllerBox, playlist);

        //implementing the right side
        VBox rightVBox = new VBox();
        HBox controlHBox = new HBox(10);

        Label titleLabel = new Label("Title:");
        Label artistLabel = new Label("Artist:");
        Label albumLabel = new Label("Album:");

        titleTextField = new TextField();
        artistTextField = new TextField();
        albumTextField = new TextField();

        Button playButton = new Button();
        playButton.setId("playButton");
        playButton.setOnAction(event -> controller.playSong(playlist.getSelectionModel().getSelectedItem()));

        Button pauseButton = new Button();
        pauseButton.setId("pauseButton");
        pauseButton.setOnAction(event -> controller.pauseSong());

        Button stopButton = new Button();
        stopButton.setId("stopButton");
        stopButton.setOnAction(event -> controller.stopSong());

        Button skipButton = new Button();
        skipButton.setId("skipButton");
        skipButton.setOnAction(event -> controller.skipSong());

        Button commitButton = new Button();
        commitButton.setId("commitButton");
        commitButton.setOnAction(event -> controller.writeMetadata());

        HBox volumeBox = new HBox();

        Label volumeOutLabel = new Label();
        volumeOutLabel.setId("volumeOut");
        volumeOutLabel.getStyleClass().add("buttonSmall");

        Label volumeFullLabel = new Label();
        volumeFullLabel.setId("volumeFull");
        volumeFullLabel.getStyleClass().add("buttonSmall");

        volumeSlider = new Slider();
        volumeSlider.setMax(100);
        volumeSlider.setMin(0);
        volumeSlider.setValue(50);
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> controller.setVolume(volumeSlider.getValue() / 100));

        volumeBox.setAlignment(Pos.CENTER);
        volumeBox.getChildren().addAll(volumeOutLabel, volumeSlider, volumeFullLabel);

        controlHBox.setPadding(new Insets(5,5,5,0));
        controlHBox.getChildren().addAll(playButton, pauseButton, stopButton, skipButton, commitButton);
        rightVBox.getChildren().addAll(titleLabel, titleTextField, artistLabel, artistTextField, albumLabel,
                albumTextField, controlHBox, volumeBox);

        //implementing the bottom side
        HBox bottomHBox = new HBox();
        Button addAllButton = new Button();
        addAllButton.setId("allToPlaylistButton");
        addAllButton.setOnAction(event -> controller.addAllToPlaylist());

        Button addSongToLibraryButton = new Button();
        addSongToLibraryButton.setId("addFileButton");
        addSongToLibraryButton.setOnAction(event -> controller.addSongToLibrary());

        Button addFolderToLibraryButton = new Button();
        addFolderToLibraryButton.setId("addFolderButton");
        addFolderToLibraryButton.setOnAction(event -> controller.loadFolder());

        bottomHBox.setPadding(new Insets(5,5,5,5));
        bottomHBox.setSpacing(10);
        bottomHBox.getChildren().addAll(addSongToLibraryButton, addFolderToLibraryButton, addAllButton);

        //adding all elements to the BorderPane
        setTop(menuBar);
        setLeft(library);
        setRight(rightVBox);
        setCenter(centerBox);
        setBottom(bottomHBox);
    }

    public ListView<Song> getLibrary() { return library; }

    public void setLibrary (ObservableList<Song> items){
        library.setItems(null);
        library.setItems(items);
    }

    public ListView<Song> getPlaylist() { return playlist; }

    public void setPlaylist (ObservableList<Song> items){
        playlist.setItems(null);
        playlist.setItems(items);
    }

    public void setController(Controller contr) { controller = contr; }

    public void setPlayTime(String string) { this.playTime.setText(string); }

    public void setTitleTextField (String title) { titleTextField.setText(title); }

    public void setArtistTextField (String artist) { artistTextField.setText(artist); }

    public void setAlbumTextField (String album) { albumTextField.setText(album); }

    public String getTitleTextField() { return titleTextField.getText(); }

    public String getArtistTextField() { return artistTextField.getText(); }

    public String getAlbumTextField() { return albumTextField.getText(); }

    public double getVolume() { return volumeSlider.getValue() / 100; }
}