package com.example.marius.myapplication;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM21 = "param1";
    private static final String ARG_PARAM22 = "param2";

    private String mParam21;
    private String mParam22;


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM21, param1);
        args.putString(ARG_PARAM22, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam21 = getArguments().getString(ARG_PARAM21);
            mParam22 = getArguments().getString(ARG_PARAM22);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View registrationView = inflater.inflate(R.layout.fragment_registration, container, false);

        Button cancelLoginButton =(Button)registrationView.findViewById(R.id.CancelLogInButton);
        cancelLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mListener.OnCancelButtonPressedInRegistrationFragment(Uri.parse("CancelLogIn"));
            }
            
        });

        Button registrationLoginButton =(Button)registrationView.findViewById(R.id.LogInButtonInRegistrationFragment);
        registrationLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String registrationUsername = ((TextView)registrationView.findViewById(R.id.RegistrationFragmentUsername)).getText().toString();
                    String registrationPassword = ((TextView)registrationView.findViewById(R.id.RegistrationFragmentPassword)).getText().toString();
                    String retypedpassword = ((TextView)registrationView.findViewById(R.id.RegistrationFragmentRetypePassword)).getText().toString();
                    mListener.OnLogInButtonPressedInRegistrationFragment(registrationUsername, registrationPassword, retypedpassword);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        return registrationView;
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
        public void OnCancelButtonPressedInRegistrationFragment(Uri uri);
        public void OnLogInButtonPressedInRegistrationFragment(String username, String password, String retypedPassword) throws IOException;


    }

}
