package kg.technicaltask.pokemonapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class PokemonCardActivity extends AppCompatActivity {

    TextView mName, mHp, mTypeHeightWeight, mAttackWeight;
    ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_card);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        setTitle("Pokemon Card");

        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        mName = findViewById(R.id.textview_pokemon_name);
        mHp = findViewById(R.id.textview_pokemon_HP);
        mTypeHeightWeight = findViewById(R.id.textview_pokemon_type_height_weight);
        mAttackWeight = findViewById(R.id.textview_pokemon_attack_def);

        mImage = findViewById(R.id.imageview_pokemon);

        mName.setText(getIntent().getStringExtra("PokemonName"));
        mHp.setText((getIntent().getIntExtra("PokemonHP", 0)) + " HP");
        mTypeHeightWeight.setText("Type(s): " + (getIntent().getStringExtra("PokemonType")) + "\n"
                + "Height - " + (getIntent().getIntExtra("PokemonHeight", 0)) + " "
                + "Weight - " + getIntent().getIntExtra("PokemonWeight", 0));
        mAttackWeight.setText("Attack - " + (getIntent().getIntExtra("PokemonAttack", 0)) + " "
                + "Defense - " + (getIntent().getIntExtra("PokemonDefense", 0)));

        Picasso.get().load(getIntent().getStringExtra("PokemonImage")).into(mImage);
    }
}