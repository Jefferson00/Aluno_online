package br.iesb.alunoonline;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperaSenhaActivity extends AppCompatActivity {
    EditText email;
    Button btnEnviar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recupera_senha);

        email = findViewById(R.id.txRecuperaEmail);
        btnEnviar = findViewById(R.id.btnEnviarEmail);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperaSenha();
            }
        });
    }

    public void recuperaSenha(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = email.getText().toString();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Email de recuperação enviado com sucesso!" , Toast.LENGTH_LONG).show();
                            Intent it = new Intent(RecuperaSenhaActivity.this, LoginActivity.class);
                            startActivity(it);
                        }
                        else{
                            Exception e = task.getException();
                            String erro = e.getMessage();
                            Toast.makeText(getApplicationContext(), erro , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void recuperaSenha2(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String email1 = email.getText().toString().trim();
        mAuth.sendPasswordResetEmail(email1)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(getApplicationContext(), "Email de recuperação enviado com sucesso!" , Toast.LENGTH_LONG).show();
                        Intent it = new Intent(RecuperaSenhaActivity.this, LoginActivity.class);
                        startActivity(it);
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Erro, verifique se o e-mail está correto!" , Toast.LENGTH_LONG).show();
            }

    });
    }
}
