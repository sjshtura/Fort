package model;

import java.rmi.RemoteException;

public class Model {

    private Playlist library = new Playlist();
    private Playlist playlist = new Playlist();

    public Playlist getLibrary () throws RemoteException { return library; }
    public Playlist getPlaylist () throws RemoteException { return playlist; }
}
