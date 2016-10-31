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
import ca.qc.cstj.cinecheck.cinecheck.fragments.FilmFragment.OnListFragmentInteractionListener;
import ca.qc.cstj.cinecheck.cinecheck.helpers.Services;
import ca.qc.cstj.cinecheck.cinecheck.models.Film;

import java.util.List;

public class FilmRecyclerViewAdapter extends RecyclerView.Adapter<FilmRecyclerViewAdapter.ViewHolder> {

    private final List<Film> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public FilmRecyclerViewAdapter(List<Film> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_film_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Film film = mValues.get(position);
        holder.mItem = film;
        //holder.imgFilm.setText(mValues.get(position).imgFilm);
        holder.lblTitreFilm.setText(mValues.get(position).getTitre());

        Ion.with(context)
                .load(Services.FILMS_IMG.concat(film.getImgUrl()).concat(".png"))
                .withBitmap()
                .placeholder(R.drawable.spinner)
                .error(R.drawable.error)
                .intoImageView(holder.imgFilm);

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
        public final ImageView imgFilm;
        public final TextView lblTitreFilm;
        public Film mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imgFilm = (ImageView) view.findViewById(R.id.imgFilm);
            lblTitreFilm = (TextView) view.findViewById(R.id.lblTitreFilm);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + lblTitreFilm.getText() + "'";
        }
    }
}
