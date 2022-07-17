package com.example.how_vi.colecao;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.how_vi.R;
import com.example.how_vi.Usuario.Usuario;
import com.example.how_vi.database.DatabaseHelper;
import com.example.how_vi.discos.Disco;

import java.util.ArrayList;


public class AdicionarFragment extends Fragment {
    DatabaseHelper dbHelper;
    Spinner spDisco;
    ArrayList<String> listaDisco;
    ArrayList<Integer> listaDiscoId;

    public AdicionarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.colecao_fragment_adicionar, container, false);

        spDisco = v.findViewById(R.id.spinner_disco_colecao);
        listaDisco = new ArrayList<String>();
        listaDiscoId = new ArrayList<Integer>();
        dbHelper = new DatabaseHelper(getActivity());
        dbHelper.getDiscosForSpinner(listaDiscoId, listaDisco);
        listaDisco.add("Selecione um disco");

        ArrayAdapter<String> spDiscoArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listaDisco){
            @Override
            public int getCount() {
                return listaDisco.size()-1;
            }
        };
        spDisco.setAdapter(spDiscoArrayAdapter);
        spDisco.setSelection(listaDisco.size()-1);

        Button btnSalvar = v.findViewById(R.id.button_salvar_colecao);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionar();
            }
        });


        // Inflate the layout for this fragment
        return v;
    }

    public void adicionar(){
        int idUsuario = ((Usuario) getActivity().getApplication()).getId();
        String nomeDisco = spDisco.getSelectedItem().toString();
        int idDisco = listaDiscoId.get(listaDisco.indexOf(nomeDisco));
        if (dbHelper.isDiscoNaColecao(idDisco,idUsuario)){
            Toast.makeText(getActivity(), nomeDisco +" já está na sua coleção", Toast.LENGTH_LONG).show();
            return;
        }
        dbHelper.insertInColecao(idUsuario, idDisco);
        Toast.makeText(getActivity(), nomeDisco +" salvo na sua coleção", Toast.LENGTH_LONG).show();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_colecao, new ListarFragment()).commit();

    }
}