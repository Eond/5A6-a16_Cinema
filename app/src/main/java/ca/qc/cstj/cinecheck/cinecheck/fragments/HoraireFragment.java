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
import ca.qc.cstj.cinecheck.cinecheck.helpers.Services;
import ca.qc.cstj.cinecheck.cinecheck.models.Horaire;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class HoraireFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_INSTANCE_PARENT = "instance-parent";
    private static final String ARG_URLS = "urls";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private String mInstanceParent = "";
    private List<String> mUrls = new ArrayList<String>();
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HoraireFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static HoraireFragment newInstanceF(int columnCount, ArrayList<String> urls) {
        HoraireFragment fragment = new HoraireFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_INSTANCE_PARENT, "film");
        args.putStringArrayList(ARG_URLS, urls);
        fragment.setArguments(args);
        return fragment;
    }

    public static HoraireFragment newInstanceC(int columnCount, ArrayList<String> urls) {
        HoraireFragment fragment = new HoraireFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_INSTANCE_PARENT, "cinema");
        args.putStringArrayList(ARG_URLS, urls);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mInstanceParent = getArguments().getString(ARG_INSTANCE_PARENT);
            mUrls = getArguments().getStringArrayList(ARG_URLS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horaire_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            final Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            final List<Horaire> horaires = new ArrayList<>();
            for(String url : mUrls) {
                Ion.with(context)
                        .load(url.concat("?fields=duree,nom,titre,imageUrl&expand=true"))
                        .asJsonObject()
                        .withResponse()
                        .setCallback(new FutureCallback<Response<JsonObject>>() {
                            @Override
                            public void onCompleted(Exception e, Response<JsonObject> result) {
                                Log.d("Horaires GET", "Headers : (".concat(String.valueOf(result.getHeaders().code())).concat(") ").concat(result.getHeaders().message()));
                                if (result.getException() != null) {
                                    Log.e("Exception sent", result.getException().getMessage());
                                }
                                if (result.getHeaders().code() == 200) {
                                    horaires.add(new Horaire(result.getResult()));
                                } else if (result.getHeaders().code() >= 500 && result.getHeaders().code() < 510) {
                                    JsonObject err = new JsonObject();
                                    try {
                                        err = result.getResult();
                                        String dMessage = err.get("developperMessage").getAsJsonObject().get("code").getAsString();
                                        String code = err.get("status").getAsString();
                                        Log.e("Error ".concat(code), dMessage);
                                    } catch (NullPointerException ne) {
                                        err.addProperty("message", result.getHeaders().message());
                                        err.addProperty("status", result.getHeaders().code());
                                    }
                                    horaires.add(createHoraireError(err));
                                } else {
                                    JsonObject err = result.getResult();
                                    Log.e("Horaires GET", "Got Error : ".concat(err.get("status").getAsString()).concat(" - ").concat(err.get("message").getAsString()));
                                }
                            }
                        });
            }
            HoraireRecyclerViewAdapter horaireAdapter = new HoraireRecyclerViewAdapter(horaires, mListener, mInstanceParent);
            recyclerView.setAdapter(horaireAdapter);
        }
        return view;
    }

    private Horaire createHoraireError(JsonObject error) {
        JsonObject retrun = new JsonObject();
        retrun.addProperty("urlc", "");
        retrun.addProperty("urlf", "");
        retrun.addProperty("dateHeure", "");

        JsonObject sjof = new JsonObject();
        sjof.addProperty("url", "");
        sjof.addProperty("titre", error.getAsJsonPrimitive("status").getAsString().concat(" - ").concat(error.getAsJsonPrimitive("message").getAsString()));
        sjof.addProperty("imageUrl", "");
        sjof.addProperty("duree", 0);

        JsonObject sjoc = new JsonObject();
        sjof.addProperty("url", "");
        sjof.addProperty("nom", error.getAsJsonPrimitive("status").getAsString().concat(" - ").concat(error.getAsJsonPrimitive("message").getAsString()));

        retrun.add("film", sjof);
        retrun.add("cinema", sjoc);

        return new Horaire(retrun);
    }

    /*private List<Horaire> createHoraireList(JsonArray result) {
        List<Horaire> horaires = new ArrayList<>();

        for(JsonElement element : result) {
            horaires.add(new Horaire(element.getAsJsonObject()));
        }

        return horaires;
    }*/


    @Override
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
    }

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
        void onListFragmentInteraction(Horaire item);
    }
}
