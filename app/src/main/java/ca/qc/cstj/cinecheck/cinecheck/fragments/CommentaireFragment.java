package ca.qc.cstj.cinecheck.cinecheck.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import ca.qc.cstj.cinecheck.cinecheck.R;
import ca.qc.cstj.cinecheck.cinecheck.models.Commentaire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CommentaireFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_URL = "url";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private String mUrl;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CommentaireFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CommentaireFragment newInstance(String url) {
        CommentaireFragment fragment = new CommentaireFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, 1);
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mUrl = getArguments().getString(ARG_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_commentaire_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            final Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            Ion.with(context)
                    .load(mUrl.concat("/commentaires/"))
                    .asJsonArray()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonArray>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonArray> result) {
                            Log.d("Commentaire GET", "Reached");
                            Log.d("Commentaire GET", "Headers : (".concat(String.valueOf(result.getHeaders().code())).concat(") ").concat(result.getHeaders().message()));
                            if (result.getException() != null) {
                                Log.e("Exception sent", result.getException().getMessage());
                            }
                            List<Commentaire> results = new ArrayList<Commentaire>();
                            if (result.getHeaders().code() == 200) {
                                results = createCommentaireList(result.getResult());
                            } else if (result.getHeaders().code() >= 500 && result.getHeaders().code() < 510) {
                                JsonArray err = new JsonArray();
                                try {
                                    err = result.getResult();
                                    String dMessage = err.get(0).getAsJsonObject().get("developperMessage").getAsJsonObject().get("code").getAsString();
                                    String code = err.get(0).getAsJsonObject().get("status").getAsString();
                                    Log.e("Error ".concat(code), dMessage);
                                } catch (NullPointerException ne) {
                                    JsonObject jo = new JsonObject();
                                    jo.addProperty("message", result.getHeaders().message());
                                    jo.addProperty("status", result.getHeaders().code());
                                    err.set(0, jo);
                                }
                                results = createCommentaireError(err);
                            } else {
                                JsonObject err = result.getResult().get(0).getAsJsonObject();
                                Log.e("Commentaire GET", "Got Error : ".concat(err.get("status").getAsString()).concat(" - ").concat(err.get("message").getAsString()));
                            }
                            CommentaireRecyclerViewAdapter commAdapter = new CommentaireRecyclerViewAdapter(results, mListener);
                            recyclerView.setAdapter(commAdapter);
                        }
                    });
        }
        return view;
    }

    private List<Commentaire> createCommentaireList(JsonArray results) {
        List<Commentaire> comms = new ArrayList<>();
        for (JsonElement comm : results) {
            comms.add(new Commentaire(comm.getAsJsonObject()));
        }
        return comms;
    }

    private List<Commentaire> createCommentaireError(JsonArray err) {
        JsonObject jo = new JsonObject();
        JsonObject error = err.get(0).getAsJsonObject();
        jo.addProperty("auteur", error.getAsJsonPrimitive("status").getAsString());
        jo.addProperty("texte", error.getAsJsonPrimitive("message").getAsString());

        List<Commentaire> retour = new ArrayList<>();
        retour.add(new Commentaire(jo));

        return retour;
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Commentaire item);
    }
}
