package com.example.how_vi.bandas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.how_vi.R;
import com.example.how_vi.database.DatabaseHelper;

public class AdicionarFragment extends Fragment {

    private EditText etNome;

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
        View v = inflater.inflate(R.layout.banda_fragment_adicionar, container, false);
        etNome = v.findViewById(R.id.edit_text_nome_banda);
        Button btnSalvar = v.findViewById(R.id.button_salvar_banda);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionar();
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    private void adicionar(){
        String banda = etNome.getText().toString();
        if (banda.equals("")){
            Toast.makeText(getActivity(), "Por favor, informe o nome da banda", Toast.LENGTH_LONG).show();
            return;
        }
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        if (dbHelper.isBandaRepetida(banda)){
            Toast.makeText(getActivity(), "Já existe uma banda cadastrada com o nome "+banda, Toast.LENGTH_LONG).show();
            return;
        }
        dbHelper.createBanda(banda);
        Toast.makeText(getActivity(), banda+ " salva na base de bandas", Toast.LENGTH_LONG).show();

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_bandas, new ListarFragment()).commit();
    }
}