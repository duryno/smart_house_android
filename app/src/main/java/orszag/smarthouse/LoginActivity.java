package orszag.smarthouse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.client.ClientProtocolException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class LoginActivity extends AppCompatActivity {

    private boolean loginSuccessful;
    private EditText etName;
    private EditText etPassword;
    private TextView error;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etName = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        error = (TextView) findViewById(R.id.error);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        String username = etName.getText().toString();
        String password = etPassword.getText().toString();
        new LogIn().execute(username, password);
    }

    public class LogIn extends AsyncTask<String, Void, Boolean> {

        ProgressDialog progress = new ProgressDialog(LoginActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            StringBuilder out;

            try {
                String path = "http://thesmarthouse.azurewebsites.net/restAPI/User/" + params[0] + "/" + params[1] + "/1";
                URL url = new URL(path);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                final InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                out = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    out.append(line);
                }

                loginSuccessful = out.toString().equals("true") ? true : false;

            } catch (final ClientProtocolException e) {
                e.printStackTrace();
            } catch (final IOException e) {
                e.printStackTrace();
            }
            return loginSuccessful;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if(loginSuccessful) {
                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            } else
                Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_LONG).show();
            progress.dismiss();
        }

    }


}
