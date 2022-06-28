package kg.technicaltask.pokemonapp.model;

import com.google.gson.annotations.SerializedName;

public class OfficialArtWork {
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @SerializedName("front_default")
    private String imageUrl;
}
