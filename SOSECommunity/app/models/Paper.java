package models;

/**
 * Created by sagejoyoox on 11/17/16.
 */
import java.util.List;

public class Paper {
    String title;
    List<String> authors;
    List<String> citations;
    String journal;
    String abs;
    List<String> keywords;
    String year;
    String volume;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void setCitations(List<String> citations) {
        this.citations = citations;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public void setAbs(String abs) {
        this.abs = abs;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public List<String> getCitations() {
        return citations;
    }

    public String getJournal() {
        return journal;
    }

    public String getAbs() {
        return abs;
    }

    public String getYear() {
        return year;
    }

    public String getVolume() {
        return volume;
    }

    public List<String> getKeywords() {
        return keywords;
    }

}
