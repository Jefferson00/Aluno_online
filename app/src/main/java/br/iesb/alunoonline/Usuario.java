package br.iesb.alunoonline;

/**
 * Created by jefferson on 10/06/2018.
 */

public class Usuario {
    public String id;
    public String nome;
    public String email;
    public String dtNasc;
    public String estado;
    public String cidade;

    public Usuario(){

    }

    public Usuario(String id, String nome, String email, String dtNasc, String estado, String cidade){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.dtNasc = dtNasc;
        this.estado = estado;
        this.cidade = cidade;
    }
}
