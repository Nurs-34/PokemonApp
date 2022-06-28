package kg.technicaltask.pokemonapp.model;

import java.util.ArrayList;

public class PokemonList {


    private String previous;
    private String next;
    private ArrayList<Results> results;

    public String getPrev() {
        return previous;
    }

    public void setPrev(String prev) {
        this.previous = prev;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public ArrayList<Results> getResults() {
        return results;
    }

    public void setResults(ArrayList<Results> results) {
        this.results = results;
    }

}
