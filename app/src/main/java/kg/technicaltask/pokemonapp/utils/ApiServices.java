package kg.technicaltask.pokemonapp.utils;

import org.json.JSONObject;

import java.util.List;

import kg.technicaltask.pokemonapp.model.Pokemon;
import kg.technicaltask.pokemonapp.model.PokemonList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiServices {

    @GET
    Call<PokemonList> getPokemonList(@Url String url, @Query("limit") int limit, @Query("offset") int offset);

    @GET
    Call<PokemonList> getPrevNextPokemonList(@Url String url);

    @GET
    Call<Pokemon> getPokemonInfo(@Url String url);

    @GET
    Call<String> getPokemonInfoJSON(@Url String url);
}
