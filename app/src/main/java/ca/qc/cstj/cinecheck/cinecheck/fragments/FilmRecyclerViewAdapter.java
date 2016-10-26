package ca.qc.cstj.cinecheck.cinecheck.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ca.qc.cstj.cinecheck.cinecheck.R;
import ca.qc.cstj.cinecheck.cinecheck.fragments.FilmFragment.OnListFragmentInteractionListener;
import ca.qc.cstj.cinecheck.cinecheck.models.Film;

import java.util.List;

public class FilmRecyclerViewAdapter extends RecyclerView.Adapter<FilmRecyclerViewAdapter.ViewHolder> {

    private final List<Film> mValues;
    private final OnListFragmentInteractionListener mListener;

    public FilmRecyclerViewAdapter(List<Film> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_film_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //holder.imgFilm.setText(mValues.get(position).imgFilm);
        holder.lblTitreFilm.setText(mValues.get(position).getTitre());

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
