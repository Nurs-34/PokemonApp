package kg.technicaltask.pokemonapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kg.technicaltask.pokemonapp.R;
import kg.technicaltask.pokemonapp.model.Pokemon;
import kg.technicaltask.pokemonapp.utils.PokemonClickListener;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.MyViewHolder> {

    ArrayList<Pokemon> mItems;
    Context mContext;
    LayoutInflater inflater;
    PokemonClickListener mClickListener;

    public PokemonAdapter(Context context, ArrayList<Pokemon> items) {
        inflater = LayoutInflater.from(context);
        this.mItems = items;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_pokemon, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Pokemon pokemon = mItems.get(position);

        holder.mName.setText(pokemon.getName());
        holder.mHp.setText("HP: " + pokemon.getHp());
        holder.mAttack.setText("Attack: " + pokemon.getAttack());
        holder.mDefense.setText("Defense: " + pokemon.getDefense());
        holder.mType.setText("Type(s): " + pokemon.getType());
        holder.mHeight.setText("Height: " + pokemon.getHeight());
        holder.mWeight.setText("Weight: " + pokemon.getWeight());

        Picasso.get().load(pokemon.getUrl()).into(holder.mImage);

        holder.mRootView.setOnClickListener(view -> mClickListener.onPokemonClick(position));

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setOnCLickListener(PokemonClickListener cLickListener){
        mClickListener = cLickListener;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final CardView mRootView;
        private final TextView mName;
        private final TextView mHp;
        private final TextView mAttack;
        private final TextView mDefense;
        private final TextView mType;
        private final TextView mHeight;
        private final TextView mWeight;
        private final ImageView mImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mRootView = itemView.findViewById(R.id.root_view_pokemon);
            mName = itemView.findViewById(R.id.textview_name);
            mHeight = itemView.findViewById(R.id.textview_height);
            mWeight = itemView.findViewById(R.id.textview_weight);
            mDefense = itemView.findViewById(R.id.textview_defense);
            mHp = itemView.findViewById(R.id.textview_hp);
            mAttack = itemView.findViewById(R.id.textview_attack);
            mType = itemView.findViewById(R.id.textview_type);
            mImage = itemView.findViewById(R.id.imageview);

        }
    }
}
