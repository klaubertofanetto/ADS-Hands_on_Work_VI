package com.example.how_vi.discos;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.how_vi.R;
import com.example.how_vi.bandas.Banda;
import com.example.how_vi.database.DatabaseHelper;

import java.util.ArrayList;

public class EditarFragment extends Fragment {

    private EditText etNome;
    private EditText etAno;
    Spinner spBanda;
    ArrayList<Integer> listBandaId;
    ArrayList<String> listBandaNome;
    DatabaseHelper dbHelper;
    Disco disco;

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
        View v = inflater.inflate(R.layout.disco_fragment_editar, container, false);

        Bundle b = getArguments();
        int id_disco = b.getInt("id");
        dbHelper = new DatabaseHelper(getActivity());
        disco = dbHelper.getDiscoById(id_disco);

        spBanda = v.findViewById(R.id.spinner_banda_disco);
        etNome = v.findViewById(R.id.edit_text_nome_disco);
        etAno = v.findViewById(R.id.edit_text_ano_disco);

        listBandaId = new ArrayList<>();
        listBandaNome = new ArrayList<>();
        dbHelper.getAllNameBanda(listBandaId, listBandaNome);

        ArrayAdapter<String> spBandaArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listBandaNome);
        spBanda.setAdapter(spBandaArrayAdapter);

        etNome.setText(disco.getNome());
        if (disco.getAnoLancamento() > 0){
            etAno.setText(Integer.toString(disco.getAnoLancamento()));
        }

        spBanda.setSelection(listBandaId.indexOf(disco.getId_banda()));

        Button btnEditar = v.findViewById(R.id.button_editar_disco);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar(id_disco);
            }
        });

        Button btnExcluir = v.findViewById(R.id.button_excluir_disco);
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Tem certeza que deseja excluir "+ disco.getNome());
                builder.setMessage("Isto também irá excluir o disco de sua coleção.");
                builder.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        excluir(disco);
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
        String nomeDisco = etNome.getText().toString();
        if (spBanda.getSelectedItem() == null) {
            Toast.makeText(getActivity(), "Por favor, selecione a banda", Toast.LENGTH_LONG).show();
        } else if (nomeDisco.equals("")){
            Toast.makeText(getActivity(), "Por favor, informe o nome do Disco", Toast.LENGTH_LONG).show();
        } else if (etAno.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o ano de lançamento do disco", Toast.LENGTH_LONG).show();
        } else {
            dbHelper = new DatabaseHelper(getActivity());
            Disco disco = new Disco();
            disco.setId(id);
            disco.setNome(nomeDisco);
            String nomeBanda = spBanda.getSelectedItem().toString();
            Integer idBanda = listBandaId.get(listBandaNome.indexOf(nomeBanda));
            Banda banda = new Banda(idBanda, nomeBanda);
            disco.setId_banda(banda);
            disco.setBanda(banda);
            disco.setAnoLancamento(Integer.parseInt(etAno.getText().toString()));

            dbHelper.updateDisco(disco);
            Toast.makeText(getActivity(), disco.getNome() + " atualizado", Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_discos, new ListarFragment()).commit();
        }
    }

    public void excluir (Disco disco){
        String nomeDisco = disco.getNome();
        dbHelper = new DatabaseHelper(getActivity());
        dbHelper.deleteDisco(disco);
        Toast.makeText(getActivity(), nomeDisco + " excluído da base de dados", Toast.LENGTH_LONG).show();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_discos, new ListarFragment()).commit();
    }
}