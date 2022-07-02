package com.example.how_vi.bandas;

public class Banda {
    private int id;
    private String nome;

    public Banda() {}

    public Banda(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Banda(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
