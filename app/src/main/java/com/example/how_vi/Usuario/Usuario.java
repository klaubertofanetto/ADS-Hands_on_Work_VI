package com.example.how_vi.Usuario;

import android.app.Application;

public class Usuario extends Application {

    private int id;
    private String nome;
    private String email;
    private String senha;

    public Usuario() {};

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void clear(){
        this.id = 0;
        this.nome = "";
        this.email = "";
        this.senha = "";
    }
}
