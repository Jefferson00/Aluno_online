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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class CadastrarPerfilActivity extends AppCompatActivity {
    Button btnInserir;
    EditText txNome;
    EditText txCurso;
    EditText txDtNasc;
    EditText txCampus;
    EditText txMatricula;
    EditText txInteresse;

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_perfil);

        btnInserir = findViewById(R.id.btnInserir);
        txInteresse = findViewById(R.id.txInteresses);

        txInteresse.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Toast.makeText(CadastrarPerfilActivity.this, "Separe com virgula",Toast.LENGTH_LONG).show();
            }
        });

        btnInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inserir();
            }
        });
    }

    protected void onStart() {
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
        txCampus = findViewById(R.id.txCampus);
        txCurso = findViewById(R.id.txCurso);
        txMatricula = findViewById(R.id.txMatricula);
        txDtNasc = findViewById(R.id.txDtNasc);
        txInteresse = findViewById(R.id.txInteresses);
        String interesses = txInteresse.getText().toString();

        Aluno aluno = new Aluno();
        aluno.nome = txNome.getText().toString();
        int mat = Integer.parseInt(txMatricula.getText().toString());
        aluno.matricula = mat;
        int dtNasc = Integer.parseInt(txDtNasc.getText().toString());
        aluno.dt_nasc = dtNasc;
        aluno.curso = txCurso.getText().toString();
        aluno.campus = txCampus.getText().toString();



        for(String s : interesses.split(",")){
            Interesses it = new Interesses();
            it.id = UUID.randomUUID().toString();
            it.tag = s ;
            aluno.interesses.add(it);
        }


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Aluno = database.getReference("iesb/alunos/" + UUID.randomUUID().toString());
        Aluno.setValue(aluno);
        Toast.makeText(CadastrarPerfilActivity.this, "Inserido Perfil com sucesso!",Toast.LENGTH_LONG).show();
    }
}
