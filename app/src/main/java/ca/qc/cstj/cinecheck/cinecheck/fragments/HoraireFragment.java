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
    private static final String ARG_URL = "url";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private String mInstanceParent = "";
    private String mUrl;
    private List<Horaire> horaires = new ArrayList<>();
    private Boolean loadError = false;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HoraireFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static HoraireFragment newInstanceF(int columnCount, String url) {
        HoraireFragment fragment = new HoraireFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_INSTANCE_PARENT, "film");
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        Log.d("Horaires Instance", "Parent : Film");
        return fragment;
    }

    public static HoraireFragment newInstanceC(int columnCount, String url) {
        HoraireFragment fragment = new HoraireFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_INSTANCE_PARENT, "cinema");
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        Log.d("Horaires Instance", "Parent : Cinema");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Horaire OnCreate", "Reached.");

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mInstanceParent = getArguments().getString(ARG_INSTANCE_PARENT);
            mUrl = getArguments().getString(ARG_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Horaire OnCreateView", "Reached");
        View view = inflater.inflate(R.layout.fragment_horaire_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Log.d("Horaire OnCreateView", "START");
            final Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            Log.d("Horaire OnCreateView", "URL : ".concat(mUrl));
            Ion.with(context)
                    .load(mUrl.concat("/horaires/?fields=duree,nom,titre,imageUrl&expand=true"))
                    .asJsonArray()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonArray>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonArray> result) {
                            Log.d("Horaire GET", "Reached");
                            Log.d("Horaire GET", "Headers : (".concat(String.valueOf(result.getHeaders().code())).concat(") ").concat(result.getHeaders().message()));
                            if (result.getException() != null) {
                                Log.e("Exception sent", result.getException().getMessage());
                            }
                            List<Horaire> results = new ArrayList<Horaire>();
                            if (result.getHeaders().code() == 200) {
                                results = createHoraireList(result.getResult());
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
                                results = createHoraireError(err);
                            } else {
                                JsonObject err = result.getResult().get(0).getAsJsonObject();
                                Log.e("Horaire GET", "Got Error : ".concat(err.get("status").getAsString()).concat(" - ").concat(err.get("message").getAsString()));
                            }
                            HoraireRecyclerViewAdapter horaireAdapter = new HoraireRecyclerViewAdapter(results, mListener, mInstanceParent);
                            recyclerView.setAdapter(horaireAdapter);
                        }
                    });
        }
        return view;
    }

    private List<Horaire> createHoraireList(JsonArray results) {
        List<Horaire> horaires = new ArrayList<>();

        for(JsonElement element : results) {
            horaires.add(new Horaire(element.getAsJsonObject()));
        }

        return horaires;
    }

    private List<Horaire> createHoraireError(JsonArray errorA) {
        JsonObject jo = new JsonObject();
        JsonObject error = errorA.get(0).getAsJsonObject();
        jo.addProperty("urlc", "");
        jo.addProperty("urlf", "");
        jo.addProperty("dateHeure", "");

        JsonObject sjof = new JsonObject();
        sjof.addProperty("url", "");
        sjof.addProperty("titre", error.getAsJsonPrimitive("status").getAsString().concat(" - ").concat(error.getAsJsonPrimitive("message").getAsString()));
        sjof.addProperty("imageUrl", "");
        sjof.addProperty("duree", 0);

        JsonObject sjoc = new JsonObject();
        sjof.addProperty("url", "");
        sjof.addProperty("nom", error.getAsJsonPrimitive("status").getAsString().concat(" - ").concat(error.getAsJsonPrimitive("message").getAsString()));

        jo.add("film", sjof);
        jo.add("cinema", sjoc);

        List<Horaire> lh = new ArrayList<>();
        lh.add(new Horaire(jo));

        return lh;
    }
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

    /*private List<Horaire> createHoraireList(JsonArray result) {
        List<Horaire> horaires = new ArrayList<>();

        for(JsonElement element : result) {
            horaires.add(new Horaire(element.getAsJsonObject()));
        }

        return horaires;
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
        void onListFragmentInteraction(Horaire item);
    }
}
