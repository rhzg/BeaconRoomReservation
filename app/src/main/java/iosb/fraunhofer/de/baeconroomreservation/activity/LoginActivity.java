
package iosb.fraunhofer.de.baeconroomreservation.activity;

 import android.accounts.Account;
 import android.accounts.AccountAuthenticatorActivity;
 import android.accounts.AccountManager;
 import android.app.ProgressDialog;
 import android.os.AsyncTask;
 import android.os.Bundle;
 import android.util.Log;
 import android.content.Intent;
 import android.view.View;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.TextView;
 import android.widget.Toast;

 import butterknife.ButterKnife;
 import butterknife.BindView ;
 import iosb.fraunhofer.de.baeconroomreservation.R;
 import iosb.fraunhofer.de.baeconroomreservation.rest.Communicator;

public class LoginActivity extends AccountAuthenticatorActivity
{
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private String email;
    private String password;

    @BindView (R.id.input_email) EditText _emailText;
    @BindView (R.id.input_password) EditText _passwordText;
    @BindView (R.id.btn_login) Button _loginButton;
    @BindView (R.id.link_signup) TextView _signupLink;
    ProgressDialog progress;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                login();
            }
        });
        progress = new ProgressDialog(this);
        _signupLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login()
    {
        Log.d(TAG, "Login");

        if (!validate())
        {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);
        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();

        new MyTask().execute();


    }

    private class MyTask extends AsyncTask<String, Integer, String>
    {
        Boolean success;
        @Override
        protected void onPreExecute()
        {
            progress.setTitle("Logging in");
            progress.setMessage("Wait while logging in...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            success = Communicator.loginPost(email, password);
            return Communicator.token;
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progress.dismiss();
            if (success)
            {
                onLoginSucces(result);
            }else
            {
                onLoginFailed();
            }
        }
    }

    private void onLoginSucces(String token)
    {
        createAccount(email, password, token);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginFailed()
    {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate()
    {
        boolean valid = true;
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else
        {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10)
        {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else
        {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void createAccount(String email, String password, String authToken)
    {
        Account account = new Account(email, "iosb.fraunhofer.de.baeconroomreservation");

        AccountManager am = AccountManager.get(this);
        am.addAccountExplicitly(account, password, null);
        am.setAuthToken(account, "full_access", authToken);
    }
}