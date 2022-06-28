package kg.technicaltask.pokemonapp.model;

import com.google.gson.annotations.SerializedName;

public class Stats {
    @SerializedName("base_stat")
    private int baseStat;
    private Stat stat;

    public int getBaseStat() {
        return baseStat;
    }

    public void setBaseStat(int baseStat) {
        this.baseStat = baseStat;
    }

    public Stat getStat() {
        return stat;
    }

    public void setStat(Stat stat) {
        this.stat = stat;
    }



}
