package com.example.how_vi.Usuario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.how_vi.R;
import com.example.how_vi.colecao.ListarFragment;
import com.example.how_vi.database.DatabaseHelper;


public class EditarFragment extends Fragment {
    private EditText etNome;
    private EditText etMail;
    private EditText etSenha;
    private DatabaseHelper dbHelper;
    private Usuario usuario;


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
        View v = inflater.inflate(R.layout.usuario_fragment_editar, container, false);
        etNome = v.findViewById(R.id.edit_text_nome_usuario);
        etMail = v.findViewById(R.id.edit_text_email_usuario);
        etSenha = v.findViewById(R.id.edit_text_senha_usuario);
        usuario = ((Usuario) getActivity().getApplication());
        etNome.setText(usuario.getNome());
        etMail.setText(usuario.getEmail());

        Button btnEditar = v.findViewById(R.id.button_editar_usuario);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar();
            }
        });

        Button btnCancelar = v.findViewById(R.id.button_cancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_usuario, new ListarFragment()).commit();
            }
        });





        // Inflate the layout for this fragment
        return v;
    }

    private void editar(){
        String nome = etNome.getText().toString();
        String mail = etMail.getText().toString();
        String senha = etSenha.getText().toString();
        Usuario usuarioAlterado = new Usuario();
        usuarioAlterado.setNome(nome);
        usuarioAlterado.setEmail(mail);
        usuarioAlterado.setId(usuario.getId());

        if (nome.equals("") || mail.equals("")){
            Toast.makeText(getActivity(), "Nome e e-mail são de preenchimento obrigatório", Toast.LENGTH_LONG).show();
            return;
        }
        dbHelper = new DatabaseHelper(getActivity());
        if (dbHelper.isSenhaValida(usuario.getId(), senha)){
            dbHelper.updateUsuario(usuarioAlterado);
            Toast.makeText(getActivity(), "Usuario alterado", Toast.LENGTH_LONG).show();
            usuario.setNome(nome);
            usuario.setEmail(mail);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_usuario, new ListarFragment()).commit();
        }
        Toast.makeText(getActivity(), "Senha incorreta", Toast.LENGTH_LONG).show();
    }
}