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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class CadastrarPerfilActivity extends AppCompatActivity {
    Button btnInserir;
    EditText txNome;
    EditText txEstado;
    EditText txDtNasc;
    EditText txCidade;
    FirebaseAuth mAuth;
    GoogleApiClient mGoogleApiClient;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_perfil);

        btnInserir = findViewById(R.id.btnInserir);


        btnInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inserir();
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

            if (id == R.id.item_sair){
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            // ...
                            Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);
                        }

                    });

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void inserir(){
        txNome = findViewById(R.id.txNome);
        txEstado = findViewById(R.id.txEstado);
        txCidade = findViewById(R.id.txCidade);
        txDtNasc = findViewById(R.id.txDtNasc);

        Usuario usuario = new Usuario();
        usuario.nome = txNome.getText().toString();
        usuario.estado = txEstado.getText().toString();
        usuario.cidade = txCidade.getText().toString();
        usuario.dtNasc = txDtNasc.getText().toString();
        if (currentUser != null) {
            usuario.email = currentUser.getEmail();
            usuario.id = currentUser.getUid();
        }


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Usuario = database.getReference("appEduca/usuarios/" + UUID.randomUUID().toString());
        Usuario.setValue(usuario);
        Toast.makeText(CadastrarPerfilActivity.this, "Inserido Perfil com sucesso!",Toast.LENGTH_LONG).show();
        Intent i = new Intent(getApplicationContext(), ListaActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
