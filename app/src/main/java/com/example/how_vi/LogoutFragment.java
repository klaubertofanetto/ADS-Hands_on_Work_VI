package com.example.how_vi;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.how_vi.Usuario.Usuario;

public class LogoutFragment extends Fragment {


    public LogoutFragment() {
        // Required empty public constructor
    }

     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((Usuario) getActivity().getApplication()).clear();
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        startActivity(intent);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.logout_fragment_main, container, false);
    }
}