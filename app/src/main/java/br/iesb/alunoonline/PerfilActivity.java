package br.iesb.alunoonline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class PerfilActivity extends AppCompatActivity {
    GoogleApiClient mGoogleApiClient;
    TextView txNome;
    TextView txEmail;
    TextView txEstado;
    TextView txCidade;
    //private DatabaseReference mDatabase;
    private ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    private  String userID;
    private FirebaseDatabase database;
    private DatabaseReference appEducaRef;
    FirebaseUser userLog = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        txNome = findViewById(R.id.txNome);
        txEmail = findViewById(R.id.txEmail);
        txEstado = findViewById(R.id.txEstado);
        txCidade = findViewById(R.id.txCidade);

       // mDatabase = FirebaseDatabase.getInstance().getReference();
        if (userLog!=null) {
            for (UserInfo userInfo : userLog.getProviderData()) {
                if (userID == null && userInfo.getUid() != null) {
                    userID = userLog.getUid();
                }
            }

        }
        getData();

    }

    public void getData(){
        database = FirebaseDatabase.getInstance();
        appEducaRef = database.getReference("appEduca");

        DatabaseReference Usuario = database.getReference("appEduca/usuarios/");
        Usuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                while((iterator.hasNext())){
                    Usuario value = iterator.next().getValue(Usuario.class);
                    if (value.getId().equals(userLog.getUid())){
                        txNome.setText(value.getNome());
                        txEmail.setText(value.getEmail());
                        txEstado.setText(value.getEstado());
                        txCidade.setText(value.getCidade());

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

        /*mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot usuarios : dataSnapshot.getChildren()) {
                        Usuario user = new Usuario();
                        user.setNome(usuarios.child("usuarios").child(userID).getValue(Usuario.class).getNome());
                        txNome.setText(user.getNome());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
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
        switch (id){
            case (R.id.item_listar) :
                Intent it = new Intent(PerfilActivity.this, ListaActivity.class);
                startActivity(it);
                return true;
            case (R.id.item_cad_perfil)  :
                Intent it1 = new Intent(PerfilActivity.this, CadastrarPerfilActivity.class);
                startActivity(it1);
                return true;
            case (R.id.item_chat)  :
                Intent it2 = new Intent(PerfilActivity.this, ChatActivity.class);
                startActivity(it2);
                return true;
            case (R.id.item_sair) :
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

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
