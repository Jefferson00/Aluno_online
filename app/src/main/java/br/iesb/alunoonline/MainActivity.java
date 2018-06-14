package br.iesb.alunoonline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.UUID;


import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap nMap;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String codEscola;
    EscolaService escolaService;

    GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)){
            String codEscolaList = intent.getStringExtra(Intent.EXTRA_TEXT);
            codEscola = codEscolaList;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://mobile-aceite.tcu.gov.br:80/nossaEscolaRS/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        escolaService = retrofit.create(EscolaService.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        nMap = googleMap;

        Call<Escola> requisicao = escolaService.buscaEscola(codEscola);

        requisicao.enqueue(new Callback<Escola>(){

            @Override
            public void onResponse(Call<Escola> requisicao, Response<Escola> response) {
                if (response.isSuccessful()){
                    Escola e = response.body();
                    if (e != null){
                        LatLng escola = new LatLng(e.latitude, e.longitude);
                        nMap.addMarker(new MarkerOptions().position(escola).title(e.nome));
                        nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(escola, 15));
                    }
                }
            }

            @Override
            public void onFailure(Call<Escola> requisicao, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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
            Intent it = new Intent(MainActivity.this, PerfilActivity.class);
            startActivity(it);
            return true;
        }
        else if (id == R.id.item_cad_perfil){
            Intent it = new Intent(MainActivity.this, CadastrarPerfilActivity.class);
            startActivity(it);
            return true;
        }
        else if (id == R.id.item_sair){
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
