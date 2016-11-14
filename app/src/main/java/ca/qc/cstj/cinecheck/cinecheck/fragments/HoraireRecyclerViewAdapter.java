package ca.qc.cstj.cinecheck.cinecheck.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import ca.qc.cstj.cinecheck.cinecheck.R;
import ca.qc.cstj.cinecheck.cinecheck.helpers.Services;
import ca.qc.cstj.cinecheck.cinecheck.models.Horaire;
import ca.qc.cstj.cinecheck.cinecheck.fragments.HoraireFragment.OnListFragmentInteractionListener;

import java.util.List;

public class HoraireRecyclerViewAdapter extends RecyclerView.Adapter<HoraireRecyclerViewAdapter.ViewHolder> {

    private final List<Horaire> mValues;
    private final HoraireFragment.OnListFragmentInteractionListener mListener;
    private final String mInstanceParent;
    private Context context;

    public HoraireRecyclerViewAdapter(List<Horaire> items, OnListFragmentInteractionListener listener, String instanceParent) {
        mValues = items;
        mListener = listener;
        mInstanceParent = instanceParent;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_horaire_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Horaire horaire = mValues.get(position);
        holder.mItem = horaire;
        holder.mCinema.setText(horaire.getCinema());
        holder.mCinema.setVisibility(View.INVISIBLE);
        holder.mFilm.setText(horaire.getFilm());
        holder.mFilm.setVisibility(View.INVISIBLE);
        Ion.with(context)
                .load(Services.FILMS_IMG.concat(horaire.getFilmImgUrl()).concat(".png"))
                .withBitmap()
                .placeholder(R.drawable.spinner)
                .error(R.drawable.error)
                .intoImageView(holder.mImgFilm);
        holder.mImgFilm.setVisibility(View.INVISIBLE);
        if (mInstanceParent == "film") {
            holder.mFilm.setVisibility(View.VISIBLE);
            holder.mImgFilm.setVisibility(View.VISIBLE);
        }
        else {
            holder.mCinema.setVisibility(View.VISIBLE);
        }
        holder.mDebut.setText(horaire.getDateHeure());
        holder.mDuree.setText(horaire.getDuree());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mCinema;
        public final ImageView mImgFilm;
        public final TextView mFilm;
        public final TextView mDebut;
        public final TextView mDuree;
        public Horaire mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCinema = (TextView) view.findViewById(R.id.hor_cinema);
            mImgFilm = (ImageView) view.findViewById(R.id.hor_film_img);
            mFilm = (TextView) view.findViewById(R.id.hor_film_titre);
            mDebut = (TextView) view.findViewById(R.id.hor_debut);
            mDuree = (TextView) view.findViewById(R.id.hor_duree);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCinema.getText() + "' & '" + mFilm.getText() + "' & '" + mDebut.getText() + "'";
        }
    }
}
