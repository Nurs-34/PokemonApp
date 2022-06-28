package kg.technicaltask.pokemonapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import kg.technicaltask.pokemonapp.RoomDatabase.AppDatabase;
import kg.technicaltask.pokemonapp.adapters.PokemonAdapter;
import kg.technicaltask.pokemonapp.model.Pokemon;
import kg.technicaltask.pokemonapp.model.PokemonList;
import kg.technicaltask.pokemonapp.model.Results;
import kg.technicaltask.pokemonapp.utils.NetworkService;
import kg.technicaltask.pokemonapp.utils.PokemonClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Context mContext = this;

    ProgressBar mProgressBar;
    CheckBox mAttack, mDefense, mHP;
    ExtendedFloatingActionButton mButton;

    RecyclerView mRecyclerView;
    PokemonAdapter mAdapter;

    AppDatabase db;

    final int mLimit = 30;
    int mOffset = 0;
    String mPrevPage = "";
    String mNextPage = "";
    ArrayList<Results> mPokemonNamesAndUrlsList = new ArrayList<>();
    ArrayList<Pokemon> mPokemonList = new ArrayList<>();
    List<Pokemon> mPokemonListDB = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getDbInstance(this.getApplicationContext());

        initView();
        initRecyclerView();
        if (!isConnected()) {
            Toast.makeText(mContext, "No Internet Access", Toast.LENGTH_SHORT).show();
            readPokemonFromDatabase();
            mAdapter.notifyDataSetChanged();
        } else getPokemonNamesAndUrls(mLimit, mOffset);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    getPokemonNamesAndUrlsNextOrPrev(mNextPage);
                } else if (!recyclerView.canScrollVertically(-1) && dy < 0) {
                    getPokemonNamesAndUrlsNextOrPrev(mPrevPage);
                }
            }
        });

        mButton.setOnClickListener(view -> {
            Random r = new Random();
            mOffset = r.nextInt(1124) + 1;
            getPokemonNamesAndUrls(mLimit, mOffset);
        });

        mAdapter.setOnCLickListener(new PokemonClickListener() {
            @Override
            public void onPokemonClick(int position) {
                Intent intent = new Intent(mContext, PokemonCardActivity.class);
                intent.putExtra("PokemonName", mPokemonList.get(position).getName());
                intent.putExtra("PokemonHP", mPokemonList.get(position).getHp());
                intent.putExtra("PokemonType", mPokemonList.get(position).getType());
                intent.putExtra("PokemonHeight", mPokemonList.get(position).getHeight());
                intent.putExtra("PokemonWeight", mPokemonList.get(position).getWeight());
                intent.putExtra("PokemonAttack", mPokemonList.get(position).getAttack());
                intent.putExtra("PokemonDefense", mPokemonList.get(position).getDefense());
                intent.putExtra("PokemonImage", mPokemonList.get(position).getUrl());
                startActivity(intent);
            }
        });

        mAttack.setOnClickListener(view -> {
            if (mAttack.isChecked())
                Collections.sort(mPokemonList, Pokemon.PokemonBestAttack);
//            else if (mAttack.isChecked() && mDefense.isChecked())
//                Collections.sort(mPokemonList, Pokemon.PokemonBestAttackAndDefense);
//            else if (mAttack.isChecked() && mHP.isChecked())
//                Collections.sort(mPokemonList, Pokemon.PokemonBestAttackAndHP);
//            else if (mAttack.isChecked() && mDefense.isChecked() && mHP.isChecked())
//                Collections.sort(mPokemonList, Pokemon.PokemonBest);
            else
                Collections.sort(mPokemonList, Pokemon.PokemonOrder);

            mAdapter.notifyDataSetChanged();
        });

        mDefense.setOnClickListener(view -> {
            if (mDefense.isChecked())
                Collections.sort(mPokemonList, Pokemon.PokemonBestDefense);
//            else if (mDefense.isChecked() && mAttack.isChecked())
//                Collections.sort(mPokemonList, Pokemon.PokemonBestAttackAndDefense);
//            else if (mDefense.isChecked() && mHP.isChecked())
//                Collections.sort(mPokemonList, Pokemon.PokemonBestHPAndDefense);
//            else if (mDefense.isChecked() && mAttack.isChecked() && mHP.isChecked())
//                Collections.sort(mPokemonList, Pokemon.PokemonBest);
            else
                Collections.sort(mPokemonList, Pokemon.PokemonOrder);

            mAdapter.notifyDataSetChanged();
        });

        mHP.setOnClickListener(view -> {
            if (mHP.isChecked())
                    Collections.sort(mPokemonList, Pokemon.PokemonBestHP);
                else if (mHP.isChecked() && mAttack.isChecked())
                    Collections.sort(mPokemonList, Pokemon.PokemonBestAttackAndHP);
                else if (mHP.isChecked() && mDefense.isChecked())
                    Collections.sort(mPokemonList, Pokemon.PokemonBestHPAndDefense);
                else if (mHP.isChecked() && mAttack.isChecked() && mDefense.isChecked())
                    Collections.sort(mPokemonList, Pokemon.PokemonBest);
                else
                    Collections.sort(mPokemonList, Pokemon.PokemonOrder);

            mAdapter.notifyDataSetChanged();
        });

//         mAttack.setOnCheckedChangeListener(new CheckBoxChange());
//         mDefense.setOnCheckedChangeListener(new CheckBoxChange());
//         mHP.setOnCheckedChangeListener(new CheckBoxChange());

    }

    private void initView() {
        mProgressBar = findViewById(R.id.progress_bar);

        mAttack = findViewById(R.id.checkbox_attack);
        mDefense = findViewById(R.id.checkbox_defense);
        mHP = findViewById(R.id.checkbox_hp);

        mRecyclerView = findViewById(R.id.recyclerview);

        mButton = findViewById(R.id.extended_fab_refresh);
    }

    private void initRecyclerView() {
        mAdapter = new PokemonAdapter(mContext, mPokemonList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getPokemonNamesAndUrls(int limit, int offset) {

        NetworkService.getInstance().getApi().getPokemonList("https://pokeapi.co/api/v2/pokemon/", limit, offset).enqueue(new Callback<PokemonList>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<PokemonList> call, Response<PokemonList> response) {

                mPokemonNamesAndUrlsList.clear();
                mPokemonList.clear();
                mAdapter.notifyDataSetChanged();
                if (response.body() != null) {
                    mPrevPage = response.body().getPrev();
                }
                mNextPage = response.body().getNext();
                mPokemonNamesAndUrlsList = response.body().getResults();
                getPokemon();

            }

            @Override
            public void onFailure(Call<PokemonList> call, Throwable t) {
                Log.e("Failure", t.toString());
            }
        });
    }

    private void getPokemonNamesAndUrlsNextOrPrev(String url) {
        NetworkService.getInstance().getApi().getPrevNextPokemonList(url).enqueue(new Callback<PokemonList>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<PokemonList> call, Response<PokemonList> response) {

                mPokemonNamesAndUrlsList.clear();
                mPokemonList.clear();
                mAdapter.notifyDataSetChanged();
                if (response.body() != null) {
                    mPrevPage = response.body().getPrev();
                }
                mNextPage = response.body().getNext();
                mPokemonNamesAndUrlsList = response.body().getResults();

                getPokemon();
            }

            @Override
            public void onFailure(Call<PokemonList> call, Throwable t) {
                Log.e("Failure", t.toString());
            }
        });
    }

    private void getPokemon() {

        db.PokeDao().clearDatabase();

        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        for (int i = 0; i < mPokemonNamesAndUrlsList.size(); i++) {
            NetworkService.getInstance().getApi().getPokemonInfo(mPokemonNamesAndUrlsList.get(i).getUrl()).enqueue(new Callback<Pokemon>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {

//                Pokemon pokemon = new Pokemon(response.body());
                    assert response.body() != null;
                    Pokemon pokemon = new Pokemon();
                    pokemon.setId(response.body().getId());
                    pokemon.setName(response.body().getName());
                    pokemon.setHeight(response.body().getHeight());
                    pokemon.setWeight(response.body().getWeight());
                    pokemon.setOrder(response.body().getOrder());
                    pokemon.setHp(response.body().getStats().get(0).getBaseStat());      //I checked all pokemon objects. There is order everywhere.
                    pokemon.setAttack(response.body().getStats().get(1).getBaseStat());  //Element 0 in the array of stats objects is health, element 1 is attack
                    pokemon.setDefense(response.body().getStats().get(2).getBaseStat()); //Element 2 is defense
                    for (int i = 0; i < response.body().getTypes().size(); i++) {
                        pokemon.setType(response.body().getTypes().get(i).getType().getName());
                    }
                    pokemon.setUrl(response.body().getSprites().getOther().getOfficialArtWork().getImageUrl());
                    mPokemonList.add(pokemon);
                    Collections.sort(mPokemonList, Pokemon.PokemonOrder);
                    mProgressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();

                    db.PokeDao().insertPokemon(pokemon);
                }


                @Override
                public void onFailure(Call<Pokemon> call, Throwable t) {
                    Log.e("Failure", t.toString());
                }
            });
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void readPokemonFromDatabase() {
        mPokemonListDB = db.PokeDao().getAllPokemon();
        mPokemonList.clear();
        mPokemonList.addAll(mPokemonListDB);
        Collections.sort(mPokemonList, Pokemon.PokemonOrder);
        mAdapter.notifyDataSetChanged();
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    class CheckBoxChange implements CompoundButton.OnCheckedChangeListener {

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (mAttack.isChecked())
                Collections.sort(mPokemonList, Pokemon.PokemonBestAttack);
            else if (mAttack.isChecked() && mDefense.isChecked())
                Collections.sort(mPokemonList, Pokemon.PokemonBestAttackAndDefense);
            else if (mAttack.isChecked() && mHP.isChecked())
                Collections.sort(mPokemonList, Pokemon.PokemonBestAttackAndHP);
            else if (mAttack.isChecked() && mDefense.isChecked() && mHP.isChecked())
                Collections.sort(mPokemonList, Pokemon.PokemonBest);
            else
                Collections.sort(mPokemonList, Pokemon.PokemonOrder);

            if (mDefense.isChecked())
                Collections.sort(mPokemonList, Pokemon.PokemonBestDefense);
            else if (mDefense.isChecked() && mAttack.isChecked())
                Collections.sort(mPokemonList, Pokemon.PokemonBestAttackAndDefense);
            else if (mDefense.isChecked() && mHP.isChecked())
                Collections.sort(mPokemonList, Pokemon.PokemonBestHPAndDefense);
            else if (mDefense.isChecked() && mAttack.isChecked() && mHP.isChecked())
                Collections.sort(mPokemonList, Pokemon.PokemonBest);
            else
                Collections.sort(mPokemonList, Pokemon.PokemonOrder);

            if (mHP.isChecked())
                Collections.sort(mPokemonList, Pokemon.PokemonBestHP);
            else if (mHP.isChecked() && mAttack.isChecked())
                Collections.sort(mPokemonList, Pokemon.PokemonBestAttackAndHP);
            else if (mHP.isChecked() && mDefense.isChecked())
                Collections.sort(mPokemonList, Pokemon.PokemonBestHPAndDefense);
            else if (mHP.isChecked() && mAttack.isChecked() && mDefense.isChecked())
                Collections.sort(mPokemonList, Pokemon.PokemonBest);
            else
                Collections.sort(mPokemonList, Pokemon.PokemonOrder);

            mAdapter.notifyDataSetChanged();
        }
    }
}



