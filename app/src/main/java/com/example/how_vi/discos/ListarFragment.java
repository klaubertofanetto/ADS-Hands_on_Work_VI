package com.example.how_vi.discos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.how_vi.R;
import com.example.how_vi.database.DatabaseHelper;

public class ListarFragment extends Fragment {

    public ListarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.disco_fragment_listar, container, false);

        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        ListView lv = v.findViewById(R.id.listview_discos);
        dbHelper.getAllDiscos(getActivity(), lv);

        Button btnAdicionar = v.findViewById(R.id.adicionarDisco);
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_discos, new AdicionarFragment()).commit();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvId = view.findViewById(R.id.tvListarDiscoId);
                Bundle b = new Bundle();
                b.putInt("id", Integer.parseInt(tvId.getText().toString()));
                EditarFragment editar = new EditarFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                editar.setArguments(b);
                ft.replace(R.id.frame_discos, editar).commit();
            }
        });


        // Inflate the layout for this fragment
        return v;
    }
}