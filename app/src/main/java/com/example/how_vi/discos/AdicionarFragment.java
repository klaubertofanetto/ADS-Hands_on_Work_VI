package com.example.how_vi.discos;

import android.os.Bundle;

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
import com.example.how_vi.discos.ListarFragment;
import com.example.how_vi.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class AdicionarFragment extends Fragment {

    private EditText etNome;
    private EditText etAno;
    Spinner spBanda;
    ArrayList<Integer> listBandaId;
    ArrayList<String> listBandaNome;
    DatabaseHelper dbhelper;


    public AdicionarFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.disco_fragment_adicionar, container, false);

        spBanda = v.findViewById(R.id.spinner_banda_disco);
        etNome = v.findViewById(R.id.edit_text_nome_disco);
        etAno = v.findViewById(R.id.edit_text_ano_disco);
        dbhelper = new DatabaseHelper(getActivity());
        listBandaId = new ArrayList<>();
        listBandaNome = new ArrayList<>();
        dbhelper.getAllNameBanda(listBandaId, listBandaNome);
        listBandaNome.add("Selecione a banda");

        ArrayAdapter<String> spBandaArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listBandaNome){
            @Override
            public int getCount() {
                return listBandaNome.size()-1;
            }
        };
        spBanda.setAdapter(spBandaArrayAdapter);
        spBanda.setSelection(listBandaNome.size()-1);

        Button btnSalvar = v.findViewById(R.id.button_salvar_disco);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionar();
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    private void adicionar() {
        String nomeDisco = etNome.getText().toString();
        if (spBanda.getSelectedItem() == null) {
            Toast.makeText(getActivity(), "Por favor, selecione a banda", Toast.LENGTH_LONG).show();
            return;
        } else if (nomeDisco.equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o nome do disco", Toast.LENGTH_LONG).show();
            return;
        } else if (etAno.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o ano de lançamento do disco", Toast.LENGTH_LONG).show();
            return;
        }

        Disco disco = new Disco();
        String nomeBanda = spBanda.getSelectedItem().toString();
        disco.setId_banda(listBandaId
                .get(listBandaNome.indexOf(nomeBanda)));
        disco.setNome(nomeDisco);
        disco.setAnoLancamento(Integer.parseInt(etAno.getText().toString()));
        if (dbhelper.isDiscoRepetido(disco.getId_banda(), disco.getNome())){
            Toast.makeText(getActivity(), nomeDisco+", do "+nomeBanda +", já existe na base de dados", Toast.LENGTH_LONG).show();
            return;
        }
        dbhelper.createDisco(disco);
        Toast.makeText(getActivity(), "Disco salvo", Toast.LENGTH_LONG);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_discos, new ListarFragment()).commit();

    }
}