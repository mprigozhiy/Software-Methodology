//Mikhail Prigozhiy and Michael Calabrese

package application;
 
import javafx.beans.property.SimpleStringProperty;
 
public class song {
   private final SimpleStringProperty artist;
   private final SimpleStringProperty song;
   private final SimpleStringProperty album;
   private final SimpleStringProperty year;

    public song(String artist, String song, String album, String year) {
        this.artist = new SimpleStringProperty(artist);
        this.song = new SimpleStringProperty(song);
        this.album = new SimpleStringProperty(album);
        this.year = new SimpleStringProperty(year);
    }
    
    
    public String getArtist() {
        return artist.get();
    }
 
    public void setArtist(String artistName) {
        artist.set(artistName);
    }
        
    public String getSong() {
        return song.get();
    }
    
    public void setSong(String songName) {
        song.set(songName);
    }
    
    public String getAlbum() {
        return album.get();
    }
    
    public void setAlbum(String albumName) {
        album.set(albumName);
    }
    
    public String getYear() {
    	return year.get();
    }
    
    public void setYear(String year) {
    	this.year.set(year);
    }
}