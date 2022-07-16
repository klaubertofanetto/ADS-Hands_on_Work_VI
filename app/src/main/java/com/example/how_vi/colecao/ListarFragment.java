package com.example.how_vi.colecao;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.how_vi.R;
import com.example.how_vi.Usuario.Usuario;
import com.example.how_vi.database.DatabaseHelper;
import com.example.how_vi.discos.EditarFragment;

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
        View v = inflater.inflate(R.layout.colecao_fragment_listar, container, false);

        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        ListView lv = v.findViewById(R.id.listview_minha_colecao);
        int idUsuario = ((Usuario) getActivity().getApplication()).getId();
        dbHelper.getAllDiscosFromColecao(getActivity(), lv, idUsuario);

        Button btnAdicionar = v.findViewById(R.id.adicionar_a_colecao);
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_colecao, new AdicionarFragment()).commit();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvId = view.findViewById(R.id.tvListarColecaoId);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Tem certeza que deseja excluir?");
                builder.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Disco excluído da coleção", Toast.LENGTH_LONG).show();
                        dbHelper.deleteFromColecao(tvId.getText().toString());
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_colecao, new ListarFragment()).commit();

                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // não faz nada
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        // Inflate the layout for this fragment
        return v;
    }
}