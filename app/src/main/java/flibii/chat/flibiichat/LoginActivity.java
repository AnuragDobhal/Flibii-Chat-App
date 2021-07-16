package flibii.chat.flibiichat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity
{

    private Button LoginButton,PhoneLoginButton;
    private EditText UserEmail,Userpassword;
    private TextView NeedNewAccountLink,ForgetPasswordLink;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private DatabaseReference UserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        InitializeFields();

        NeedNewAccountLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SendUserToRegisterActivity();
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AllowUserToLogin();
            }
        });
        PhoneLoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent PhoneLoginIntent = new Intent(LoginActivity.this,PhoneLoginActivity.class);
                startActivity(PhoneLoginIntent);
            }
        });
    }

    private void AllowUserToLogin()
    {
        String email=UserEmail.getText().toString();
        String password=Userpassword.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please enter your email address...",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please enter your password...",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Sign In");
            loadingBar.setMessage("please wait...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        String currentUserId =mAuth.getCurrentUser().getUid();
                        String deviceToken = FirebaseInstanceId.getInstance().getToken();
                        UserRef.child(currentUserId).child("device_token").setValue(deviceToken)
                                .addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if(task.isSuccessful())
                                {
                                    SendUserToMainActivity();
                                    Toast.makeText(LoginActivity.this,"Logged in Successfully...",Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }
                            }
                        });


                    }
                    else
                    {
                        String message= task.getException().toString();
                        Toast.makeText(LoginActivity.this,"Error : "+ message,Toast.LENGTH_SHORT).show();
                       loadingBar.dismiss();
                    }
                }
            });
        }
    }
    private void InitializeFields()
    {
        LoginButton=(Button)findViewById(R.id.login_button);

        PhoneLoginButton=(Button)findViewById(R.id.phone_login_button);

        UserEmail=(EditText) findViewById(R.id.login_email);

        Userpassword=(EditText) findViewById(R.id.login_password);

        NeedNewAccountLink=(TextView) findViewById(R.id.need_new_account_link);

        ForgetPasswordLink=(TextView) findViewById(R.id.forgot_password_link);

        loadingBar = new ProgressDialog(this);
    }

    private void  SendUserToMainActivity()
    {
        Intent mainIntent=new Intent(LoginActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void SendUserToRegisterActivity()
    {
        Intent registerIntent=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(registerIntent);
    }
}
