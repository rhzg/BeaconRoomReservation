
package de.iosb.fraunhofer.baeconroomreservation.activity;

 import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.security.crypto.bcrypt.BCrypt;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.iosb.fraunhofer.baeconroomreservation.Constants;
import de.iosb.fraunhofer.baeconroomreservation.R;
import de.iosb.fraunhofer.baeconroomreservation.entity.LoginResponse;
import de.iosb.fraunhofer.baeconroomreservation.rest.Communicator;

/**
 * This is a application which is used for logging in. I
 *
 * @author Viseslav Sako
 */
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

        BCrypt hashGenerator = new BCrypt();
        String hashedPass = hashGenerator.hashpw(password, BCrypt.gensalt());

        password = hashedPass;

        new MyTask().execute();
    }

    private class MyTask extends AsyncTask<String, Integer, LoginResponse>
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
        protected LoginResponse doInBackground(String... params)
        {
            success = Communicator.loginPost(email, password);
            return new LoginResponse(Communicator.token, Communicator.admin);
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(LoginResponse result) {
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

    private void onLoginSucces(LoginResponse result)
    {
        createAccount(email, password, result.getToken(), result.isAdmin());
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        setAccountAuthenticatorResult(intent.getExtras());
        startActivityForResult(intent, REQUEST_SIGNUP);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
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

    public void createAccount(String email, String password, String authToken, boolean admin)
    {
        Account account = new Account(email, Constants.ACCOUNT_TYPE);
        AccountManager am = AccountManager.get(this);
        am.addAccountExplicitly(account, password, null);
        if(admin)
        {
            am.setUserData(account, Constants.ADMIN, "true");
            am.setAuthToken(account, "full_access", authToken);
        }
        else {
            am.setUserData(account, Constants.ADMIN, "false");
            am.setAuthToken(account, "read_only", authToken);
        }
    }
}