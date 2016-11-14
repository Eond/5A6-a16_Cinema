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
import android.widget.FrameLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.HeadersResponse;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import ca.qc.cstj.cinecheck.cinecheck.R;
import ca.qc.cstj.cinecheck.cinecheck.helpers.Services;
import ca.qc.cstj.cinecheck.cinecheck.models.Cinema;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CinemaFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CinemaFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CinemaFragment newInstance(int columnCount) {
        CinemaFragment fragment = new CinemaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cinema_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            final Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            //recyclerView.setAdapter(new CinemaRecyclerViewAdapter(DummyContent.ITEMS, mListener));
            //TODO: Service Web Call
            Ion.with(context)
                    .load(Services.BASE_URL.concat("/cinemas/"))
                    .asJsonArray()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonArray>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonArray> result) {
                            Log.d("Cinemas GET", "Headers : (".concat(String.valueOf(result.getHeaders().code())).concat(") ").concat(result.getHeaders().message()));
                            if (result.getException() != null) {
                                Log.e("Exception sent",result.getException().getMessage());
                            }
                            if (result.getHeaders().code() == 200) {
                                CinemaRecyclerViewAdapter cinemaAdapter = new CinemaRecyclerViewAdapter(createCinemaList(result.getResult()), mListener);
                                recyclerView.setAdapter(cinemaAdapter);
                            } else if (result.getHeaders().code() >= 500 && result.getHeaders().code() < 510) {
                                JsonObject err = new JsonObject();
                                try {
                                    err = result.getResult().get(0).getAsJsonObject();
                                    String dMessage = err.get("developperMessage").getAsJsonObject().get("code").getAsString();
                                    String code = err.get("status").getAsString();
                                    Log.e("Error ".concat(code), dMessage);
                                } catch (NullPointerException ne) {
                                    err.addProperty("message", result.getHeaders().message());
                                    err.addProperty("status", result.getHeaders().code());
                                }
                                CinemaRecyclerViewAdapter cinemaAdapter = new CinemaRecyclerViewAdapter(createCinemaError(err), mListener);
                                recyclerView.setAdapter(cinemaAdapter);
                            } else {
                                JsonObject err = result.getResult().get(0).getAsJsonObject();
                                Log.e("Cinemas GET", "Got Error : ".concat(err.get("status").getAsString()).concat(" - ").concat(err.get("message").getAsString()));
                            }
                        }
                    });
        }
        return view;
    }

    private List<Cinema> createCinemaError(JsonObject error) {
        List<Cinema> retrun = new ArrayList<>();
        JsonObject jo = new JsonObject();
        jo.addProperty("nom", error.getAsJsonPrimitive("message").getAsString());
        jo.addProperty("adresse", error.getAsJsonPrimitive("status").getAsString());
        jo.addProperty("url", "");
        retrun.add(new Cinema(jo));
        return retrun;
    }

    private List<Cinema> createCinemaList(JsonArray result) {
        List<Cinema> cinemas = new ArrayList<>();

        for(JsonElement element : result) {
            cinemas.add(new Cinema(element.getAsJsonObject()));
        }

        return cinemas;
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
        void onListFragmentInteraction(Cinema item);
    }
}
