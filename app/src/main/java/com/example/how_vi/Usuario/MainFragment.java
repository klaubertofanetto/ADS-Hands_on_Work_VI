package com.example.how_vi.Usuario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.how_vi.R;
import com.example.how_vi.bandas.ListarFragment;

public class MainFragment extends Fragment {

    public MainFragment() {
        // Required empty public constructor
    }

   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.usuario_fragment_main, container, false);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_usuario, new EditarFragment()).commit();
        // Inflate the layout for this fragment
        return v;
    }
}