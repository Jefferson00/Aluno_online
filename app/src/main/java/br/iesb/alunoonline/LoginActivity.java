package br.iesb.alunoonline;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private FirebaseAuth mAuth;
    private EditText txtEmail;
    private EditText txtSenha;
    private static final int RC_SIGN_IN = 777;
    private GoogleApiClient googleApiClient;
    private CallbackManager mCallbackManager;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);

        /*METODO DE LOGIN POR EMAIL E SENHA*/

        Button btLogin = findViewById(R.id.btnLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaUser()) {
                    if(validarForm()){
                        login();
                    }

                }else{
                    goMainScreen();
                }
            }
        });

        /*INICIO METODO DE LOGIN COM O GOOGLE*/

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
                .requestProfile()
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        ImageView googleLoginBtn = findViewById(R.id.googleLoginBtn);

        googleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaUser()) {
                    Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                    startActivityForResult(intent, RC_SIGN_IN);
                }
            }
        });
        /*FIM METODO LOGIN COM O GOOGLE*/

        /*INICIO METODO DE LOGIN COM O FACEBOOK*/


        ImageView faceLoginBtn = findViewById(R.id.facebookLoginBtn);

        //faceLoginBtn.setReadPermissions("email");


        mCallbackManager = CallbackManager.Factory.create();
//        LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
//        loginButton.setReadPermissions("email", "public_profile");

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //handleFacebookAccessToken(loginResult.getAccessToken());
                if (verificaUser()) {
                    Intent it1 = new Intent(LoginActivity.this, ListaActivity.class);
                    it1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(it1);
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Cancelado", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Não foi posível efetuar login", Toast.LENGTH_LONG).show();
            }
        });

        faceLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
            }
        });

        /*FIM METODO DE LOGIN COM O FACEBOOK*/

        /*CHAMAR TELA DE CADASTRO DE USUARIO*/
        TextView txCad = findViewById(R.id.textViewCadastro);
        txCad.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent t1 = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(t1);
            }
        });

        /*CHAMAR TELA DE RECUPERAR SENHA*/
        TextView txRecupera = findViewById(R.id.textViewRecuperaSenha);
        txRecupera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t2 = new Intent(LoginActivity.this, RecuperaSenhaActivity.class);
                startActivity(t2);
            }
        });
    }

    private boolean validarForm() {
        boolean valid = true;

        String email = txtEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            txtEmail.setError("Required.");
            valid = false;
        } else {
            txtEmail.setError(null);
        }

        String senha = txtSenha.getText().toString();
        if (TextUtils.isEmpty(senha)) {
            txtSenha.setError("Required.");
            valid = false;
        } else {
            txtSenha.setError(null);
        }

        return valid;    }


    /*METODOS LOGIN FACEBOOK*/

    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent it = new Intent(LoginActivity.this, ListaActivity.class);
                            startActivity(it);
                        } else {
                            Toast.makeText(LoginActivity.this, "Não foi posível efetuar login", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void loginFacebook() {
        Toast.makeText(LoginActivity.this, "Login Facebook", Toast.LENGTH_LONG).show();
    }

    /*METODO LOGIN EMAIL E SENHA*/
    private void login() {

        mAuth.signInWithEmailAndPassword(txtEmail.getText().toString(), txtSenha.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Não foi posível efetuar login", Toast.LENGTH_LONG).show();
                            } else {
                                Intent it = new Intent(LoginActivity.this, ListaActivity.class);
                                startActivity(it);
                            }
                        }
                    });

    }

    /*METODOS LOGIN GOOGLE*/
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//

//    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            goMainScreen();
        } else {
            Toast.makeText(LoginActivity.this, "Falha no login", Toast.LENGTH_LONG).show();
        }
    }

    private void goMainScreen() {
        Intent intent = new Intent(this, ListaActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean verificaUser(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return false;
        } else {
            return true;
        }
    }
}
