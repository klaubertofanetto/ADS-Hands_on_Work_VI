package com.example.how_vi.bandas;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.how_vi.R;
import com.example.how_vi.database.DatabaseHelper;

public class EditarFragment extends Fragment {

    private EditText etNome;
    private DatabaseHelper dbHelper;

    public EditarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.banda_fragment_editar, container, false);
        etNome = v.findViewById(R.id.edit_text_nome_banda);
        Bundle b = getArguments();
        int id_banda = b.getInt("id");

        dbHelper = new DatabaseHelper(getActivity());
        Banda banda = dbHelper.getBandaById(id_banda);
        etNome.setText(banda.getNome());

        Button btnEditar = v.findViewById(R.id.button_editar_banda);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar(id_banda);
            }
        });

        Button btnExcluir = v.findViewById(R.id.button_excluir_banda);
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Tem certeza que deseja excluir "+banda.getNome()+"?");
                        builder.setMessage("Isto também irá excluir os discos dessa banda presentes na sua coleção e no catálogo de discos.");
                        builder.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                excluir(banda);
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

    public void editar (int id){
        String nomeBanda = etNome.getText().toString();
        if (nomeBanda.equals("")){
            Toast.makeText(getActivity(), "Por favor, informe o nome da banda", Toast.LENGTH_LONG).show();
            return;
        }
        dbHelper = new DatabaseHelper(getActivity());
        Banda banda = new Banda(id, nomeBanda);
        Toast.makeText(getActivity(), "Nome da banda atualizado para " + nomeBanda, Toast.LENGTH_LONG).show();
        dbHelper.updateBanda(banda);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_bandas, new ListarFragment()).commit();
    }

    public void excluir (Banda banda){
        String nomeBanda = banda.getNome();
        dbHelper = new DatabaseHelper(getActivity());
        dbHelper.deleteBanda(banda);
        Toast.makeText(getActivity(), nomeBanda + " excluído da base de dados", Toast.LENGTH_LONG).show();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_bandas, new ListarFragment()).commit();
    }
}