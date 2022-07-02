package com.example.how_vi.discos;

import com.example.how_vi.bandas.Banda;

public class Disco {
    private int id;
    private String nome;
    private int anoLancamento = 0;
    private String banda;
    private int id_banda;

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

    public int getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(int anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public String getBanda() {
        return banda;
    }

    public void setBanda(Banda banda) {
        this.banda = banda.getNome();
    }


    public int getId_banda() {
        return id_banda;
    }

    public void setId_banda(Banda banda) {
        this.id_banda = banda.getId();
    }
    public void setId_banda(int id_banda) {
        this.id_banda = id_banda;
    }

}
