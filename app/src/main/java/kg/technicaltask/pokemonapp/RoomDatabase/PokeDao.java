package kg.technicaltask.pokemonapp.RoomDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import kg.technicaltask.pokemonapp.model.Pokemon;

@Dao
public interface PokeDao {

    @Insert
    void insertPokemon(Pokemon... pokemon);

    @Query("SELECT * FROM Pokemon")
    List<Pokemon> getAllPokemon();

    @Query("DELETE FROM Pokemon")
    void clearDatabase();
}
