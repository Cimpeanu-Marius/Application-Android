package com.example.marius.myapplication;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;

/**
 * A simple {@link ListFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationsListFragment extends ListFragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationsListFragment newInstance(String param1, String param2) {
        LocationsListFragment fragment = new LocationsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public LocationsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View listFragmentView = inflater.inflate(R.layout.fragment_locations_list, container, false);

        final ListView listview = (ListView) listFragmentView.findViewById(android.R.id.list);

        Bundle locationsBundle = this.getArguments();
        ArrayList<String> locationsList = locationsBundle.getStringArrayList("key");
        // initialize and set the list adapter
        final ArrayList<String> list = new ArrayList<String>();
        for(int i=0;i<locationsList.size();i++) {
            list.add(locationsList.get(i));
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String positionString = (String) listview.getItemAtPosition(position);
                mListener.onItemSelectedInList(positionString);
            }
        });

        final ArrayAdapter adapter = new ArrayAdapter(this.getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
        listview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String positionString = (String) listview.getItemAtPosition(position);
                mListener.onItemSelectedInList(positionString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button goBackButton = (Button) listFragmentView.findViewById(R.id.goBackButton);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGoBackButton();
            }
        });
        return listFragmentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
        public void onItemSelectedInList(String string);
        public void onGoBackButton();
    }

}
