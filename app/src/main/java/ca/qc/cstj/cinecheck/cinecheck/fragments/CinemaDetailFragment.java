package ca.qc.cstj.cinecheck.cinecheck.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.ArrayList;

import ca.qc.cstj.cinecheck.cinecheck.R;
import ca.qc.cstj.cinecheck.cinecheck.helpers.Services;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CinemaDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CinemaDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CinemaDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String URL = "url";

    // TODO: Rename and change types of parameters
    private String mUrl;

    private OnFragmentInteractionListener mListener;

    public CinemaDetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CinemaDetailFragment newInstance(String uuid) {
        CinemaDetailFragment fragment = new CinemaDetailFragment();
        Bundle args = new Bundle();
        args.putString(URL, uuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_cinema_detail, container, false);

        Ion.with(getContext())
                .load(mUrl)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (result.getHeaders().code() == 200) {
                            TextView textView = (TextView) view.findViewById(R.id.cindet_nom);
                            textView.setText(result.getResult().getAsJsonPrimitive("nom").getAsString());
                            textView = (TextView) view.findViewById(R.id.cindet_adresse);
                            textView.setText(result.getResult().getAsJsonPrimitive("adresse").getAsString().concat(", "));
                            textView = (TextView) view.findViewById(R.id.cindet_cp);
                            textView.setText(result.getResult().getAsJsonPrimitive("codePostal").getAsString());
                            textView = (TextView) view.findViewById(R.id.cindet_ville);
                            textView.setText(result.getResult().getAsJsonPrimitive("ville").getAsString());
                            textView = (TextView) view.findViewById(R.id.cindet_telephone);
                            textView.setText(result.getResult().getAsJsonPrimitive("telephone").getAsString());


                        } else if (result.getHeaders().code() >= 500 && result.getHeaders().code() < 510) {
                            JsonObject err = result.getResult().getAsJsonObject();
                            String dMessage = err.get("developperMessage").getAsJsonObject().get("code").getAsString();
                            String code = err.get("status").getAsString();
                            Log.e("Error ".concat(code), dMessage);
                        } else {
                            JsonObject err = result.getResult().getAsJsonObject();
                            Log.e("Cinema GET", "Got Error : ".concat(err.get("status").getAsString()).concat(" - ").concat(err.get("message").getAsString()));
                        }
                    }
                });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
