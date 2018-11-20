package interfaces;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface Playlist extends Remote, Serializable, Iterable<model.Song>{
    boolean addSong(model.Song s) throws RemoteException;
    boolean deleteSong(model.Song s) throws RemoteException;
    boolean deleteSongByID(long id) throws RemoteException;
    void setList(ArrayList<model.Song> s) throws RemoteException;
    ArrayList<model.Song> getList() throws RemoteException;
    void clearPlaylist() throws RemoteException;
    int sizeOfPlaylist() throws RemoteException;
    Song findSongByPath(String name) throws RemoteException;
    Song findSongByID(long id) throws RemoteException;
}
