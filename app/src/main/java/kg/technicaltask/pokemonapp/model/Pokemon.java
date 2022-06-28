package kg.technicaltask.pokemonapp.model;

import android.util.Log;
import android.util.StatsLog;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Transaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

@Entity
public class Pokemon {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uniq_id")
    public int uId;
    @ColumnInfo(name = "pokemon_id")
    private int id;
    @ColumnInfo(name = "pokemon_name")
    private String name;
    @ColumnInfo(name = "pokemon_type")
    private String type = "";
    @ColumnInfo(name = "pokemon_height")
    private int height;
    @ColumnInfo(name = "pokemon_weight")
    private int weight;
    @ColumnInfo(name = "pokemon_attack")
    private int attack;
    @ColumnInfo(name = "pokemon_defense")
    private int defense;
    @ColumnInfo(name = "pokemon_hp")
    private int hp;
    @ColumnInfo(name = "pokemon_order")
    private int order;
    @Ignore
    private Sprites sprites;
    @ColumnInfo(name = "pokemon_url")
    private String url;
    @Ignore
    private ArrayList<Stats> stats;
    @Ignore
    private ArrayList<PokeTypes> types;

    public Pokemon() {}

    public Pokemon(Pokemon pokemon) {

        setId(pokemon.getId());
        setName(pokemon.getName());
        setHeight(pokemon.getHeight());
        setWeight(pokemon.getWeight());
        setOrder(pokemon.getOrder());
        setHp(pokemon.getStats().get(0).getBaseStat());      //I checked all pokemon objects. There is order everywhere.
        setAttack(pokemon.getStats().get(1).getBaseStat());  //Element 0 in the array of stats objects is health, element 1 is attack
        setDefense(pokemon.getStats().get(2).getBaseStat()); //element 2 is defense
        for (int i = 0; i < pokemon.getTypes().size(); i++){
            pokemon.setType(pokemon.getTypes().get(i).getType().getName());
        }
        setUrl(pokemon.getSprites().getOther().getOfficialArtWork().getImageUrl());
    }

    public Pokemon(JSONObject pokemon) {
        try {
            id = pokemon.getInt("id");
            name = pokemon.getString("name");

            height = pokemon.getInt("height");
            weight = pokemon.getInt("weight");

            hp = pokemon.getJSONArray("stats").getJSONObject(0).getInt("base_stat");
            attack = pokemon.getJSONArray("stats").getJSONObject(1).getInt("base_stat");
            defense = pokemon.getJSONArray("stats").getJSONObject(2).getInt("base_stat");

            order = pokemon.getInt("order");

            url = pokemon.getJSONObject("sprites").getJSONObject("other").getJSONObject("official-artwork").getString("front_default");

            for (int i = 0; i < pokemon.getJSONArray("types").length(); i++) {
                type += pokemon.getJSONArray("types").getJSONObject(i).getJSONObject("type").getString("name") + " ";
            }

        } catch (JSONException e) {
            Log.e("Pokemon e: ", e.toString());
            e.printStackTrace();
        }
    }

    public static Comparator<Pokemon> PokemonBestAttack = new Comparator<Pokemon>() {
        @Override
        public int compare(Pokemon p1, Pokemon p2) {
            return p2.getAttack() - p1.getAttack();
        }
    };

    public static Comparator<Pokemon> PokemonBestDefense = new Comparator<Pokemon>() {
        @Override
        public int compare(Pokemon p1, Pokemon p2) {
            return p2.getDefense() - p1.getDefense();
        }
    };

    public static Comparator<Pokemon> PokemonBestHP = new Comparator<Pokemon>() {
        @Override
        public int compare(Pokemon p1, Pokemon p2) {
            return p2.getHp() - p1.getHp();
        }
    };

    public static Comparator<Pokemon> PokemonBestAttackAndHP = new Comparator<Pokemon>() {
        @Override
        public int compare(Pokemon p1, Pokemon p2) {
            return (p2.getHp() + p2.getAttack()) - (p1.getHp() + p1.getAttack());
        }
    };

    public static Comparator<Pokemon> PokemonBestAttackAndDefense = new Comparator<Pokemon>() {
        @Override
        public int compare(Pokemon p1, Pokemon p2) {
            return (p2.getDefense() + p2.getAttack()) - (p1.getDefense() + p1.getAttack());
        }
    };


    public static Comparator<Pokemon> PokemonBestHPAndDefense = new Comparator<Pokemon>() {
        @Override
        public int compare(Pokemon p1, Pokemon p2) {
            return (p2.getDefense() + p2.getHp()) - (p1.getDefense() + p1.getHp());
        }
    };

    public static Comparator<Pokemon> PokemonBest = new Comparator<Pokemon>() {
        @Override
        public int compare(Pokemon p1, Pokemon p2) {
            return (p2.getDefense() + p2.getAttack() + p2.getHp()) - (p1.getDefense() + p1.getAttack() + p1.getHp());
        }
    };

    public static Comparator<Pokemon> PokemonOrder = new Comparator<Pokemon>() {
        @Override
        public int compare(Pokemon p1, Pokemon p2) {
            return p1.getOrder() - p2.getOrder();
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type += type + " ";
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Stats> getStats() {
        return stats;
    }

    public void setStats(ArrayList<Stats> stats) {
        this.stats = stats;
    }

    public ArrayList<PokeTypes> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<PokeTypes> types) {
        this.types = types;
    }


    @Override
    public String toString() {
        return "Pokemon{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", attack=" + attack +
                ", defense=" + defense +
                ", hp=" + hp +
                ", order=" + order +
                ", sprites=" + sprites +
                ", url='" + url + '\'' +
                ", stats=" + stats +
                ", types=" + types +
                '}';
    }

}
