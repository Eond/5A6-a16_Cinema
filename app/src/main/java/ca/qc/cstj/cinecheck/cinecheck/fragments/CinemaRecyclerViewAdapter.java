package ca.qc.cstj.cinecheck.cinecheck.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.qc.cstj.cinecheck.cinecheck.R;
import ca.qc.cstj.cinecheck.cinecheck.fragments.CinemaFragment.OnListFragmentInteractionListener;
import ca.qc.cstj.cinecheck.cinecheck.models.Cinema;

import java.util.List;

public class CinemaRecyclerViewAdapter extends RecyclerView.Adapter<CinemaRecyclerViewAdapter.ViewHolder> {

    private final List<Cinema> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public CinemaRecyclerViewAdapter(List<Cinema> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_cinema_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Cinema cinema = mValues.get(position);
        holder.mItem = cinema;

        holder.lblNomCinema.setText(cinema.getNom());
        holder.lblAdresse.setText(cinema.getAdresse());

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
        public final TextView lblNomCinema;
        public final TextView lblAdresse;
        public Cinema mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            lblNomCinema = (TextView) view.findViewById(R.id.lblNomCinema);
            lblAdresse = (TextView) view.findViewById(R.id.lblAdresse);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + lblNomCinema.getText() + "'";
        }
    }
}
