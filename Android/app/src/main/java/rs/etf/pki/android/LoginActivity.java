package rs.etf.pki.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private JSONArray users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        View customActionBarView = getLayoutInflater().inflate(R.layout.app_bar, null);
        getSupportActionBar().setCustomView(customActionBarView);

        sharedPref = getApplicationContext().getSharedPreferences(getApplicationContext().getString(R.string.local_storage), Context.MODE_PRIVATE);

        Button buttonHeader = customActionBarView.findViewById(R.id.button_header);
        buttonHeader.setText("Registracija");
        buttonHeader.setVisibility(View.VISIBLE);
        buttonHeader.setOnClickListener((view) -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        });

        Button buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener((view) -> login());

        initData();
    }

    private void login() {
        try {
            EditText usernameEditText = findViewById(R.id.edit_text_login_username);
            EditText passwordEditText = findViewById(R.id.edit_text_login_password);
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (username.trim().equals("")) {
                Toast.makeText(LoginActivity.this, "Korisnicko ime nije uneto. Pokusajte ponovo", Toast.LENGTH_LONG).show();
                return;
            }

            if (password.trim().equals("")) {
                Toast.makeText(LoginActivity.this, "Lozinka nije uneta. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
                return;
            }

            for (int i = 0; i < users.length(); ++i) {
                JSONObject currUser = users.getJSONObject(i);

                if (username.equals(currUser.getString("username"))) {
                    if (password.equals(currUser.getString("password"))) {
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("loggedUser", currUser.toString());
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(), TicketsActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Uneli ste pogresnu lozinku. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
                    }

                    return;
                }
            }

            Toast.makeText(LoginActivity.this, "Korisnicko ime ne postoji. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void initData() {
        try {
            String usersJsonString = sharedPref.getString("users", null);
            if (usersJsonString == null) {
                Toast.makeText(LoginActivity.this, "Doslo je do greske prilikom inicijalizacije stranice. Pokusajte ponovo kasnije.", Toast.LENGTH_LONG).show();
                return;
            }

            users = new JSONArray(usersJsonString);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}