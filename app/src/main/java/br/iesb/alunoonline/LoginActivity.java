package br.iesb.alunoonline;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.auth.GoogleAuthProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private FirebaseAuth mAuth;
    private EditText txtEmail;
    private EditText txtSenha;
    private static final int RC_SIGN_IN = 777;
    private GoogleApiClient googleApiClient;
    private CallbackManager mCallbackManager;

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
                login();
            }
        });

        /*INICIO METODO DE LOGIN COM O GOOGLE*/

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
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
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });
        /*FIM METODO LOGIN COM O GOOGLE*/

        /*INICIO METODO DE LOGIN COM O FACEBOOK*/
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        ImageView faceLoginBtn = findViewById(R.id.facebookLoginBtn);

        //faceLoginBtn.setReadPermissions("email");
        faceLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFacebook();
            }
        });

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent it = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(it);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

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


    /*METODOS LOGIN FACEBOOK*/

    protected void onActivityResultFacebook(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent it = new Intent(LoginActivity.this, MainActivity.class);
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
        if (txtEmail.getText().toString().equals(" ") || txtSenha.getText().toString().equals(" ")) {
            Toast.makeText(LoginActivity.this, "Digite email e senha", Toast.LENGTH_LONG).show();
        } else {
            mAuth.signInWithEmailAndPassword(txtEmail.getText().toString(), txtSenha.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Não foi posível efetuar login", Toast.LENGTH_LONG).show();
                            } else {
                                Intent it = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(it);
                            }
                        }
                    });
        }
    }

    /*METODOS LOGIN GOOGLE*/
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            goMainScreen();
        } else {
            Toast.makeText(LoginActivity.this, "Falha no login", Toast.LENGTH_LONG).show();
        }
    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
