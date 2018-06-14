package br.iesb.alunoonline;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by jefferson on 04/05/2018.
 */

public interface EscolaService {

    @GET("rest/escolas")
    Call<List<Escola>> listarEscolas();

    @GET("rest/escolas/{codEscola}")
    Call<Escola> buscaEscola(@Path("codEscola") String codEscola);
}
