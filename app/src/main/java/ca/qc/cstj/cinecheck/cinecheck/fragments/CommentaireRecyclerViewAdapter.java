package ca.qc.cstj.cinecheck.cinecheck.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.qc.cstj.cinecheck.cinecheck.R;
import ca.qc.cstj.cinecheck.cinecheck.fragments.CommentaireFragment.OnListFragmentInteractionListener;
import ca.qc.cstj.cinecheck.cinecheck.models.Commentaire;

import java.util.List;

public class CommentaireRecyclerViewAdapter extends RecyclerView.Adapter<CommentaireRecyclerViewAdapter.ViewHolder> {

    private final List<Commentaire> mValues;
    private final OnListFragmentInteractionListener mListener;

    public CommentaireRecyclerViewAdapter(List<Commentaire> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_commentaire_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mAuteur.setText(mValues.get(position).getAuteur());
        holder.mMessage.setText(mValues.get(position).getMessage());

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
        public final TextView mAuteur;
        public final TextView mMessage;
        public Commentaire mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mAuteur = (TextView) view.findViewById(R.id.comm_auteur);
            mMessage = (TextView) view.findViewById(R.id.comm_message);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mMessage.getText() + "'";
        }
    }
}
