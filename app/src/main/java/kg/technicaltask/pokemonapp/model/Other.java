package kg.technicaltask.pokemonapp.model;

import com.google.gson.annotations.SerializedName;

public class Other {
    public OfficialArtWork getOfficialArtWork() {
        return officialArtWork;
    }

    public void setOfficialArtWork(OfficialArtWork officialArtWork) {
        this.officialArtWork = officialArtWork;
    }

    @SerializedName("official-artwork")
    private OfficialArtWork officialArtWork;
}
