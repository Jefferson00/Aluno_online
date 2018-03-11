package br.iesb.alunoonline;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CadastroActivity extends AppCompatActivity {

    private FirebaseAuth mAuth ;
    private EditText txEmail;
    private EditText txsenha;
    private EditText txsenha2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        mAuth = FirebaseAuth.getInstance();
        txEmail = findViewById(R.id.txEmail);
        txsenha = findViewById(R.id.txSenha);
        txsenha2 = findViewById(R.id.txSenha2);

        Button btCad = findViewById(R.id.btnCadastrar);
        btCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastar();
            }
        });
    }

    private void cadastar() {
        if (txsenha.getText().toString().equals(txsenha2.getText().toString())){
            mAuth.createUserWithEmailAndPassword(txEmail.getText().toString(), txsenha.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent it = new Intent(CadastroActivity.this, LoginActivity.class);
                                startActivity(it);


                            } else {
                                Toast.makeText(CadastroActivity.this, "Não foi possivel cadastrar",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }else{
            Toast.makeText(CadastroActivity.this, "Senha diferente",Toast.LENGTH_LONG).show();
        }


    }
}
