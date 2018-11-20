package controller;

import javafx.collections.MapChangeListener;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Model;
import model.Song;
import view.View;
import java.io.File;
import java.rmi.RemoteException;
import java.util.List;

public class Controller {

    private Model model;
    private View view;
    private final FileChooser fileChooser = new FileChooser();
    private Stage stage;
    private MediaPlayer mediaPlayer;

    public void link(Model model, View view) {
        this.model = model;
        this.view = view;
        this.view.setController(this);
    }

    public void addSongToLibrary(){
        List<File> list =
                fileChooser.showOpenMultipleDialog(stage);
        if (list != null) {
            for (File file : list) {
                try {
                    model.getLibrary().addSong(new Song(file.getPath()));
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            refreshLibrary();
        }
    }

    public void loadFolder(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File folder = directoryChooser.showDialog(stage);
        if(folder !=null) {
            addFilesFromFolder(folder);
            refreshLibrary();
        }
    }

    private void addFilesFromFolder (File folder) {
        File[] files = folder.listFiles();
        for(File file : files) {
            if(file.isDirectory()) {
                addFilesFromFolder(file);
            }
            else {
                if(file.getPath().matches(".+((aac)|(flac)|(mp3)|(ogg)|(rm)|(wav)|(wma))")) {
                    try {
                        model.getLibrary().addSong(new Song(file.getPath()));
                    }
                    catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void setStage(Stage stage){ this.stage = stage; }

    public void setFileConfig() {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Audio Files", "*.aac", "*.flac", "*.mp3", "*.ogg", "*.rm", "*.wav", "*.wma"),
                new FileChooser.ExtensionFilter("Advanced Audio Coding", " *.aac"),
                new FileChooser.ExtensionFilter("Free Lossless Audio Codec", "*.flac"),
                new FileChooser.ExtensionFilter("MP3", "*.mp3"),
                new FileChooser.ExtensionFilter("Ogg Vorbis", "*.ogg"),
                new FileChooser.ExtensionFilter("Real Media", "*.rm"),
                new FileChooser.ExtensionFilter("Wave", "*.wav"),
                new FileChooser.ExtensionFilter("Windows Media Audio", "*.wma")
                );
    }

    private void refreshLibrary() {
        try {
            view.setLibrary(model.getLibrary());
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        view.getLibrary().refresh();
    }

    private void refreshPlaylist() {
        try {
            view.setPlaylist(model.getPlaylist());
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        view.getPlaylist().refresh();
    }

    public void addToPlaylist(Song song) {
        try {
            if(model.getPlaylist().size() != 0) {
                if (checkInPlaylist(song)) {
                    view.getPlaylist().getSelectionModel().select(song);
                    return;
                }
            }
            song.setMedia(true);
            if (song.getAlbum().isEmpty() ) {
                song.getMedia().getMetadata().addListener((MapChangeListener<String, Object>) ch -> {
                    if (ch.wasAdded()) {
                        setMetadata(song, ch.getKey(), ch.getValueAdded());
                    }
                });
            }
            model.getPlaylist().addSong(song);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        refreshPlaylist();
        refreshLibrary();
    }

    public void addAllToPlaylist() {
        try {
            for(Song song : model.getLibrary().getList()) {
                if(!checkInPlaylist(song)) {
                    addToPlaylist(song);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        refreshPlaylist();
        refreshLibrary();
    }

    private boolean checkInPlaylist(Song song) {
        try {
            if (model.getPlaylist().contains(song)) {
                return true;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setMetadata (Song song, String key, Object value) {
        if (key.equals("album")) {
            song.setAlbum(value.toString());
        }
        else if (key.equals("title")) {
            song.setTitle(value.toString());
        }
        else if (key.equals("artist")) {
            song.setInterpret(value.toString());
        }
        refreshPlaylist();
    }

    public void playSong(Song song)  {
        try {
            if(mediaPlayer != null && view.getPlaylist().getSelectionModel().getSelectedItem() != null) {
                mediaPlayer.dispose();
                mediaPlayer = new MediaPlayer(song.getMedia());
                System.out.println("hallo");
                System.out.println(song.getMedia().getMetadata().get("titel"));
                mediaPlayer.play();
            } else if(view.getPlaylist().getSelectionModel().getSelectedItem() != null) {
                mediaPlayer = new MediaPlayer(song.getMedia());
                mediaPlayer.setVolume(view.getVolume());
                mediaPlayer.play();
            }
            if(mediaPlayer != null) {
                setCurrentPlayTime();
                mediaPlayer.setOnEndOfMedia(() -> skipSong());
                if (view.getLibrary().getSelectionModel().getSelectedItem() != null) {
                    song.setMedia(true);
                    if (song.getAlbum().isEmpty()) {
                        song.getMedia().getMetadata().addListener(new MapChangeListener<String, Object>() {
                            @Override
                            public void onChanged(Change<? extends String, ? extends Object> ch) {
                                if (ch.wasAdded()) {
                                    setMetadata(song, ch.getKey(), ch.getValueAdded());
                                }
                            }
                        });
                    }
                    model.getPlaylist().addSong(song);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        refreshPlaylist();
        refreshLibrary();
    }

    public void pauseSong() {
        if(mediaPlayer != null) {
            if(mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.play();
            }
        }
    }

    public void stopSong() {
        if(mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void skipSong() {
        try {
            if (view.getPlaylist().getSelectionModel().getSelectedItem() != null) {
                if(view.getPlaylist().getSelectionModel().getSelectedIndex() == model.getPlaylist().size()-1) {
                    view.getPlaylist().getSelectionModel().selectFirst();
                } else {
                    view.getPlaylist().getSelectionModel().selectNext();
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        playSong(view.getPlaylist().getSelectionModel().getSelectedItem());
    }

    public void readMetadata(Song song) {
        view.setTitleTextField(song.getTitle());
        view.setArtistTextField(song.getInterpret());
        view.setAlbumTextField(song.getAlbum());
    }

    public void writeMetadata() {
        if(view.getPlaylist().getSelectionModel().getSelectedItem() != null) {
            view.getPlaylist().getSelectionModel().getSelectedItem().setTitle(view.getTitleTextField());
            view.getPlaylist().getSelectionModel().getSelectedItem().setAlbum(view.getAlbumTextField());
            view.getPlaylist().getSelectionModel().getSelectedItem().setInterpret(view.getArtistTextField());
        }
    }

    public void setCurrentPlayTime() {
        mediaPlayer.currentTimeProperty().addListener(observable -> {
            int seconds = (int)mediaPlayer.getMedia().getDuration().toSeconds()-(int)mediaPlayer.getCurrentTime().toSeconds();
            String output = String.format("%2d",(int)(seconds/60))+":"+String.format("%02d",seconds%60);
            view.setPlayTime(output);
        });
    }

    public void removeFromPlaylist(int index) {
        try {
            if(view.getPlaylist().getSelectionModel().getSelectedItem()!= null) {
                model.getPlaylist().get(index).setMedia(false);
                model.getPlaylist().remove(index);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setVolume(double value) {
       if(mediaPlayer != null) {
           mediaPlayer.setVolume(value);
       }
    }

    public void loadMusicFolder() {
        String os = System.getProperties().getProperty("os.name");
        if ( os.contains("Windows")) {
            addFilesFromFolder(new File(System.getProperties().getProperty("user.home") + "\\Music"));
        }
        else {
            addFilesFromFolder(new File(System.getProperties().getProperty("user.home") + "/Musik"));
        }
        refreshLibrary();
    }
}
