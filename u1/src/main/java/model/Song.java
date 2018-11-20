package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.media.Media;
import java.io.File;

public class Song implements interfaces.Song {
    private SimpleStringProperty path,album,interpret,title;
    private Long id;
    private Media media;

    public Song(String path) {
        this.path = new SimpleStringProperty(path);
        this.album = new SimpleStringProperty("");
        this.interpret = new SimpleStringProperty("");
        this.title = new SimpleStringProperty("");
    }

    @Override
    public String getAlbum() {
        return album.getValue();
    }

    @Override
    public void setAlbum(String album) { this.album.setValue(album); }

    @Override
    public String getInterpret() { return interpret.getValue(); }

    @Override
    public void setInterpret(String interpret) { this.interpret.setValue(interpret); }

    @Override
    public String getPath() {
        return path.getValue();
    }

    @Override
    public void setPath(String path) {
        this.path.setValue(path);
    }

    @Override
    public String getTitle() { return title.getValue(); }

    @Override
    public void setTitle(String title) {
        this.title.setValue(title);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id=id;
    }

    public void setMedia(boolean value) {
        if(value) {
            File file = new File(this.path.getValue());
            media = new Media(file.toURI().toString());
        }
        else {
            media = null;
        }
    }

    @Override
    public Media getMedia() {
        File file = new File(path.getValue());
        media = new Media(file.toURI().toString());
        return media;
    }

    @Override
    public ObservableValue<String> pathProperty() {
        return null;
    }

    @Override
    public ObservableValue<String> albumProperty() {
        return null;
    }

    @Override
    public ObservableValue<String> interpretProperty() {
        return null;
    }

    @Override
    public ObservableValue<String> titleProperty() {
        return null;
    }

    @Override
    public String toString() {
        if (title.getValue().isEmpty()) {
            String os = System.getProperties().getProperty("os.name");
            if (os.contains("Windows")) {
                return path.getValue().substring(path.getValue().lastIndexOf("\\") + 1, path.getValue().lastIndexOf("."));
            } else {
                return path.getValue().substring(path.getValue().lastIndexOf("/") + 1, path.getValue().lastIndexOf("."));
            }
        }
        else {
            return title.getValue();
        }
    }
}
