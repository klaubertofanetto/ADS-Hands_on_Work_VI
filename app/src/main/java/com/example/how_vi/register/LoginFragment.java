package com.example.how_vi.register;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.how_vi.MainActivity;
import com.example.how_vi.R;
import com.example.how_vi.RegisterActivity;
import com.example.how_vi.Usuario.Usuario;
import com.example.how_vi.bandas.AdicionarFragment;
import com.example.how_vi.bandas.EditarFragment;
import com.example.how_vi.bandas.ListarFragment;
import com.example.how_vi.database.DatabaseHelper;

public class LoginFragment extends Fragment {
    EditText etUsuario;
    EditText etSenha;
    Usuario usuario;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        etUsuario = v.findViewById(R.id.edit_text_usuario);
        etSenha = v.findViewById(R.id.edit_text_senha);

        Button btnEntrar = v.findViewById(R.id.btn_login);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entrar();
            }
        });

        TextView tv = v.findViewById(R.id.btn_cadastrar);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_login, new RegisterFragment()).commit();
            }
        });

        // Inflate the layout for this fragment
        return v;

    }

    public void entrar(){
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        usuario = dbHelper.buscaLogin(etUsuario.getText().toString(), etSenha.getText().toString());
        if (usuario.getId()>0){
            ((Usuario) getActivity().getApplication()).setId(usuario.getId());
            ((Usuario) getActivity().getApplication()).setNome(usuario.getNome());
            ((Usuario) getActivity().getApplication()).setEmail(usuario.getEmail());
            etUsuario.setText("");
            etSenha.setText("");
            openMainActivity();
        } else {
            Toast.makeText(getContext(), "Usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
        }
    }

    public void openMainActivity(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
}