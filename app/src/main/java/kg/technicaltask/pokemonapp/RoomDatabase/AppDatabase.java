package kg.technicaltask.pokemonapp.RoomDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import kg.technicaltask.pokemonapp.model.Pokemon;


@Database(entities = {Pokemon.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PokeDao PokeDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDbInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "DB_POKEMON")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
