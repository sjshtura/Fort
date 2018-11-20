package model;

import javafx.collections.ModifiableObservableListBase;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

public class Playlist extends ModifiableObservableListBase<model.Song> implements interfaces.Playlist {
    private ArrayList<Song> list = new ArrayList<>();

    @Override
    public boolean addSong(Song s) throws RemoteException {
        return list.add(s);
    }

    @Override
    public boolean deleteSong(Song s) throws RemoteException {
        return list.remove(s);
    }

    @Override
    public boolean deleteSongByID(long id) throws RemoteException {
        return list.remove(id);
    }

    @Override
    public void setList(ArrayList<Song> s) throws RemoteException {
        list = s;

    }

    @Override
    public ArrayList<Song> getList() throws RemoteException {
        return list;
    }

    @Override
    public void clearPlaylist() throws RemoteException {
        list.clear();

    }

    @Override
    public int sizeOfPlaylist() throws RemoteException {
        return list.size();
    }

    @Override
    public Song findSongByPath(String name) throws RemoteException {
        return null;
    }

    @Override
    public Song findSongByID(long id) throws RemoteException {
        return null;
    }

    @Override
    public Iterator<Song> iterator() {
        return null;
    }

    @Override
    public Song get(int index) {
        return list.get(index);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    protected void doAdd(int index, Song element) {

    }

    @Override
    protected Song doSet(int index, Song element) {
        return null;
    }

    @Override
    protected Song doRemove(int index) {
        return list.remove(index);
    }

    public boolean contains(Song song){
        for(Song s : list) {
            if(s == song) { return true; }
        }
        return false;
    }
}
