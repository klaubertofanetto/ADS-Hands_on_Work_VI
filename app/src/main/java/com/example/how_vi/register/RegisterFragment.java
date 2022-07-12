package com.example.how_vi.register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.how_vi.R;
import com.example.how_vi.Usuario.Usuario;
import com.example.how_vi.bandas.ListarFragment;
import com.example.how_vi.database.DatabaseHelper;

public class RegisterFragment extends Fragment {
    EditText etNome;
    EditText etEmail;
    EditText etSenha;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v  =   inflater.inflate(R.layout.fragment_register, container, false);
        etNome = v.findViewById(R.id.edit_text_cadastrar_usuario);
        etEmail = v.findViewById(R.id.edit_text_cadastrar_email);
        etSenha =  v.findViewById(R.id.edit_text_cadastrar_senha);

        Button btnCadastrar = v.findViewById(R.id.btn_registrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });

        TextView tv = v.findViewById(R.id.btn_entrar);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_login, new LoginFragment()).commit();
            }
        });

        return v;

    }


    private void registrar(){
        String nome = etNome.getText().toString();
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();

        if (nome.equals("") || email.equals("") || senha.equals("")){
            Toast.makeText(getActivity(), "Todos os campos são obrigatórios", Toast.LENGTH_LONG).show();
            return;
        }
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        if (jaExiste(dbHelper, nome, email)){
            return;
        }
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        dbHelper.createUsuario(usuario);
        Toast.makeText(getActivity(), "Usuário criado", Toast.LENGTH_LONG).show();

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_login, new LoginFragment()).commit();
    }

    private boolean jaExiste(DatabaseHelper dbHelper, String nome, String email){
        if (dbHelper.getUsuarioByNome(nome)) {
            Toast.makeText(getActivity(), "Já existe um usuário com este nome", Toast.LENGTH_LONG).show();
            return true;
        }
        if (dbHelper.getUsuarioByEmail(email)) {
            Toast.makeText(getActivity(), "Já existe um usuário com este e-mail", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

}