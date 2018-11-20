package interfaces;

import javafx.beans.value.ObservableValue;
import javafx.scene.media.Media;

public interface Song {
    String getAlbum() ;

    void setAlbum(String album) ;

    String getInterpret() ;

    void setInterpret(String interpret) ;

    String getPath() ;

    void setPath(String path) ;

    String getTitle() ;
    
    void setTitle(String title) ;

    long getId();

    void setId(long id);

    Media getMedia();

    ObservableValue<String> pathProperty();
    ObservableValue<String> albumProperty();
    ObservableValue<String> interpretProperty();
    ObservableValue<String> titleProperty();

    String toString();
}
