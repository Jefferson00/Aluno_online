package br.iesb.alunoonline;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListaActivity extends AppCompatActivity implements EscolasAdapter.ListItemClickListener{
    private EscolasAdapter adapter;
    private List<Escola> listaEscolas = new ArrayList<>();
    EscolaService escolaService;
    private Toast mToast;
    private TextView cod;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://mobile-aceite.tcu.gov.br:80/nossaEscolaRS/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        escolaService = retrofit.create(EscolaService.class);



        adapter = new EscolasAdapter(this, listaEscolas, this);
        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listarEscolas();
    }

    private void listarEscola() {
        Escola e1 = new Escola();
        e1.codEscola = 1;
        e1.nome = "Escola 1";

        listaEscolas.add(e1);

        Escola e2 = new Escola();
        e2.codEscola = 2;
        e2.nome = "Escola 2";
        listaEscolas.add(e2);

        Escola e3 = new Escola();
        e3.codEscola = 3;
        e3.nome = "Escola 3";
        listaEscolas.add(e3);

        Escola e4 = new Escola();
        e4.codEscola = 4;
        e4.nome = "Escola 4";
        listaEscolas.add(e4);

        Escola e5 = new Escola();
        e5.codEscola = 5;
        e5.nome = "Escola 5";
        listaEscolas.add(e5);

        Escola e6 = new Escola();
        e6.codEscola = 6;
        e6.nome = "Escola 6";
        listaEscolas.add(e6);

    }

    private void listarEscolas() {

        Call<List<Escola>> requisicao = escolaService.listarEscolas();

        requisicao.enqueue(new Callback<List<Escola>>(){

            @Override
            public void onResponse(Call<List<Escola>> requisicao, Response<List<Escola>> response) {

                if (response.isSuccessful()){

                    List<Escola> lista = response.body();
                    if (lista != null && lista.size() > 0){
                        for(Escola e : lista){
                            listaEscolas.add(e);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }else{
                    Escola e2 = new Escola();
                    e2.codEscola = 2;
                    e2.nome = "Escola 2";
                    listaEscolas.add(e2);
                }
            }

            @Override
            public void onFailure(Call<List<Escola>> requisicao, Throwable t) {
                Toast.makeText(ListaActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onListItemClick(int clickedItemIndex, String cod) {
        String codEscolaList = cod;
        if (mToast != null) {
            mToast.cancel();
        }

        String toastMessage = "Item #" + clickedItemIndex + " clicked.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);

        Intent it = new Intent(ListaActivity.this, MainActivity.class);
        it.putExtra(Intent.EXTRA_TEXT,codEscolaList);
        startActivity(it);

        //mToast.show();
    }

    protected void onStart() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.options, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.item_perfil){
            Intent it = new Intent(ListaActivity.this, PerfilActivity.class);
            startActivity(it);
            return true;
        }
        else if (id == R.id.item_cad_perfil){
            Intent it = new Intent(ListaActivity.this, CadastrarPerfilActivity.class);
            startActivity(it);
            return true;
        }
        else if (id == R.id.item_sair){
            FirebaseAuth.getInstance().signOut();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            // ...
                            Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }

                    });
            mAuth.signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}