package rs.etf.pki.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        View customActionBarView = getLayoutInflater().inflate(R.layout.app_bar, null);
        getSupportActionBar().setCustomView(customActionBarView);

        Button buttonHeader = (Button) customActionBarView.findViewById(R.id.button_header);
        buttonHeader.setText("Prijava");
        buttonHeader.setVisibility(View.VISIBLE);
        buttonHeader.setOnClickListener((view) -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        Button buttonRegister = (Button) findViewById(R.id.button_register);
        buttonRegister.setOnClickListener((view) -> {
            register();
        });
    }

    private void register() {
        EditText firstnameEditText = findViewById(R.id.edit_text_register_firstname);
        EditText lastnameEditText = findViewById(R.id.edit_text_register_lastname);
        EditText emailEditText = findViewById(R.id.edit_text_register_email);
        EditText phoneEditText = findViewById(R.id.edit_text_register_phone);
        EditText usernameEditText = findViewById(R.id.edit_text_register_username);
        EditText passwordEditText = findViewById(R.id.edit_text_register_password);
        EditText passwordConfirmEditText = findViewById(R.id.edit_text_register_password_confirm);
        Spinner typeSpinner = findViewById(R.id.spinner_register_type);

        String firstname = firstnameEditText.getText().toString();
        String lastname = lastnameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordConfirm = passwordConfirmEditText.getText().toString();
        String type = typeSpinner.getSelectedItem().toString();

        if (!isDataValid(firstname, lastname, email, phone, username, password, passwordConfirm, type)) {
            return;
        }

        if (!insertNewUser(firstname, lastname, email, phone, username, password, type)) {
            return;
        }

        finish();
        startActivity(getIntent());
    }

    private boolean insertNewUser(String firstname, String lastname, String email, String phone, String username, String password, String type) {
        try {
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getApplicationContext().getString(R.string.local_storage), Context.MODE_PRIVATE);

            String usersJsonString = sharedPref.getString("users", null);
            if (usersJsonString == null) {
                Toast.makeText(RegisterActivity.this, "Doslo je do greske prilikom registracije. Pokusajte ponovo kasnije.", Toast.LENGTH_LONG).show();
                return false;
            }

            JSONArray users = new JSONArray(usersJsonString);
            int maxUserId = -1;
            for (int i = 0; i < users.length(); ++i) {
                JSONObject currUser = users.getJSONObject(i);

                if (currUser.getString("username").equals(username)) {
                    Toast.makeText(RegisterActivity.this, "Korisnicko ime mora biti jedinstveno. Pokusajte ponovo kasnije.", Toast.LENGTH_LONG).show();
                    return false;
                }

                if (currUser.getString("email").equals(email)) {
                    Toast.makeText(RegisterActivity.this, "Mejl adresa mora biti jedinstvena. Pokusajte ponovo kasnije.", Toast.LENGTH_LONG).show();
                    return false;
                }

                int currUserId = currUser.getInt("id");
                if (currUserId > maxUserId) {
                    maxUserId = currUserId;
                }
            }

            JSONObject user = new JSONObject();
            user.put("id", maxUserId + 1);
            user.put("username", username);
            user.put("password", password);
            user.put("firstname", firstname);
            user.put("lastname", lastname);
            user.put("phone", phone);
            user.put("email", email);
            user.put("notifications", new JSONArray());
            user.put("type", type);
            user.put("status", "pending");

            users.put(user);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("users", users.toString());
            editor.apply();

            Toast.makeText(RegisterActivity.this, "Zahtev za registraciju uspesno poslat.", Toast.LENGTH_LONG).show();

            return true;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isDataValid(String firstname, String lastname, String email, String phone, String username, String password, String passwordConfirm, String type) {
        if (firstname.trim().equals("")) {
            Toast.makeText(RegisterActivity.this, "Ime nije uneto. Pokusajte ponovo", Toast.LENGTH_LONG).show();
            return false;
        } else if (lastname.trim().equals("")) {
            Toast.makeText(RegisterActivity.this, "Prezime nije uneto. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
            return false;
        } else if (email.trim().equals("")) {
            Toast.makeText(RegisterActivity.this, "Mejl nije unet. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
            return false;
        } else if (phone.trim().equals("")) {
            Toast.makeText(RegisterActivity.this, "Broj telefona nije unet. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
            return false;
        } else if (username.trim().equals("")) {
            Toast.makeText(RegisterActivity.this, "Korisnicko ime nije uneto. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
            return false;
        } else if (password.trim().equals("")) {
            Toast.makeText(RegisterActivity.this, "Lozinka nije uneta. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
            return false;
        } else if (passwordConfirm.trim().equals("")) {
            Toast.makeText(RegisterActivity.this, "Lozinka nije potvrdjena. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
            return false;
        } else if (type.equals("")) {
            Toast.makeText(RegisterActivity.this, "Tip nije izabran. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
            return false;
        } else if (!password.equals(passwordConfirm)) {
            Toast.makeText(RegisterActivity.this, "Lozinke se ne poklapaju. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
