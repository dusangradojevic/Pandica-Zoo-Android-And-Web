package rs.etf.pki.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserProfileActivity extends AppCompatActivity {

    private JSONObject loggedUser;
    private TextView firstnameTextView;
    private TextView lastnameTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private TextView usernameTextView;
    private TextView passwordTextView;
    private TextView passwordConfirmTextView;
    private EditText firstnameEditText;
    private EditText lastnameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;
    private Button buttonStartChangeInfo;
    private Button buttonConfirmChangeInfo;
    private Button buttonCancelChangeInfo;
    private LinearLayout layoutPassword;
    private LinearLayout layoutPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        View customActionBarView = getLayoutInflater().inflate(R.layout.app_bar, null);
        getSupportActionBar().setCustomView(customActionBarView);

        initData();
        initPage();
    }

    private void startChangeInfo() {
        try {
            firstnameTextView.setVisibility(View.GONE);
            lastnameTextView.setVisibility(View.GONE);
            emailTextView.setVisibility(View.GONE);
            phoneTextView.setVisibility(View.GONE);
            usernameTextView.setVisibility(View.GONE);
            passwordTextView.setVisibility(View.VISIBLE);
            passwordConfirmTextView.setVisibility(View.VISIBLE);

            firstnameEditText.setVisibility(View.VISIBLE);
            lastnameEditText.setVisibility(View.VISIBLE);
            emailEditText.setVisibility(View.VISIBLE);
            phoneEditText.setVisibility(View.VISIBLE);
            usernameEditText.setVisibility(View.VISIBLE);
            passwordEditText.setVisibility(View.VISIBLE);
            passwordConfirmEditText.setVisibility(View.VISIBLE);

            firstnameEditText.setText(loggedUser.getString("firstname"));
            lastnameEditText.setText(loggedUser.getString("lastname"));
            emailEditText.setText(loggedUser.getString("email"));
            phoneEditText.setText(loggedUser.getString("phone"));
            usernameEditText.setText(loggedUser.getString("username"));

            buttonStartChangeInfo.setVisibility(View.GONE);
            buttonConfirmChangeInfo.setVisibility(View.VISIBLE);
            buttonCancelChangeInfo.setVisibility(View.VISIBLE);

            layoutPassword.setVisibility(View.VISIBLE);
            layoutPasswordConfirm.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void confirmChangeInfo() {
        String firstname = firstnameEditText.getText().toString();
        String lastname = lastnameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordConfirm = passwordConfirmEditText.getText().toString();

        if (!isDataValid(firstname, lastname, email, phone, username, password, passwordConfirm)) {
            return;
        }

        if (!updateUser(firstname, lastname, email, phone, username, password)) {
            return;
        }

        finish();
        startActivity(getIntent());
    }

    private void cancelChangeInfo() {
        firstnameTextView.setVisibility(View.VISIBLE);
        lastnameTextView.setVisibility(View.VISIBLE);
        emailTextView.setVisibility(View.VISIBLE);
        phoneTextView.setVisibility(View.VISIBLE);
        usernameTextView.setVisibility(View.VISIBLE);

        firstnameEditText.setVisibility(View.GONE);
        lastnameEditText.setVisibility(View.GONE);
        emailEditText.setVisibility(View.GONE);
        phoneEditText.setVisibility(View.GONE);
        usernameEditText.setVisibility(View.GONE);

        buttonStartChangeInfo.setVisibility(View.VISIBLE);
        buttonConfirmChangeInfo.setVisibility(View.GONE);
        buttonCancelChangeInfo.setVisibility(View.GONE);

        layoutPassword.setVisibility(View.GONE);
        layoutPasswordConfirm.setVisibility(View.GONE);
    }

    private boolean updateUser(String firstname, String lastname, String email, String phone, String username, String password) {
        try {
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getApplicationContext().getString(R.string.local_storage), Context.MODE_PRIVATE);

            String usersJsonString = sharedPref.getString("users", null);
            if (usersJsonString == null) {
                Toast.makeText(UserProfileActivity.this, "Doslo je do greske prilikom registracije. Pokusajte ponovo kasnije.", Toast.LENGTH_LONG).show();
                return false;
            }

            JSONArray users = new JSONArray(usersJsonString);
            int userIndex = 0;
            for (int i = 0; i < users.length(); ++i) {
                JSONObject currUser = users.getJSONObject(i);

                boolean isThisUser = loggedUser.getInt("id") == currUser.getInt("id");

                if (isThisUser) {
                    userIndex = i;
                }

                if (!isThisUser && currUser.getString("username").equals(username)) {
                    Toast.makeText(UserProfileActivity.this, "Korisnicko ime mora biti jedinstveno. Pokusajte ponovo kasnije.", Toast.LENGTH_LONG).show();
                    return false;
                }

                if (!isThisUser && currUser.getString("email").equals(email)) {
                    Toast.makeText(UserProfileActivity.this, "Mejl adresa mora biti jedinstvena. Pokusajte ponovo kasnije.", Toast.LENGTH_LONG).show();
                    return false;
                }
            }

            JSONArray notifications = loggedUser.getJSONArray("notifications");
            notifications.put("Uspesno ste promenili svoje podatke.");

            JSONObject thisUser = users.getJSONObject(userIndex);
            thisUser.put("username", username);
            thisUser.put("firstname", firstname);
            thisUser.put("lastname", lastname);
            thisUser.put("phone", phone);
            thisUser.put("email", email);
            thisUser.put("notifications", notifications);
            if (!password.equals("")) {
                thisUser.put("password", password);
            }

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("loggedUser", thisUser.toString());
            editor.putString("users", users.toString());
            editor.apply();

            Toast.makeText(UserProfileActivity.this, "Uspesno ste promenili svoje podatke.", Toast.LENGTH_LONG).show();

            return true;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isDataValid(String firstname, String lastname, String email, String phone, String username, String password, String passwordConfirm) {
        if (firstname.trim().equals("")) {
            Toast.makeText(UserProfileActivity.this, "Ime nije uneto. Pokusajte ponovo", Toast.LENGTH_LONG).show();
            return false;
        } else if (lastname.trim().equals("")) {
            Toast.makeText(UserProfileActivity.this, "Prezime nije uneto. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
            return false;
        } else if (email.trim().equals("")) {
            Toast.makeText(UserProfileActivity.this, "Mejl nije unet. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
            return false;
        } else if (phone.trim().equals("")) {
            Toast.makeText(UserProfileActivity.this, "Broj telefona nije unet. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
            return false;
        } else if (username.trim().equals("")) {
            Toast.makeText(UserProfileActivity.this, "Korisnicko ime nije uneto. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
            return false;
        } else if (password.trim().equals("") && !passwordConfirm.trim().equals("")) {
            Toast.makeText(UserProfileActivity.this, "Lozinka nije uneta. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
            return false;
        } else if (!password.trim().equals("") && passwordConfirm.trim().equals("")) {
            Toast.makeText(UserProfileActivity.this, "Lozinka nije potvrdjena. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
            return false;
        } else if (!password.equals(passwordConfirm)) {
            Toast.makeText(UserProfileActivity.this, "Lozinke se ne poklapaju. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void initPage() {
        try {
            firstnameTextView = findViewById(R.id.text_user_profile_firstname);
            lastnameTextView = findViewById(R.id.text_user_profile_lastname);
            emailTextView = findViewById(R.id.text_user_profile_email);
            phoneTextView = findViewById(R.id.text_user_profile_phone);
            usernameTextView = findViewById(R.id.text_user_profile_username);
            passwordTextView = findViewById(R.id.text_user_profile_password);
            passwordConfirmTextView = findViewById(R.id.text_user_profile_password_confirm);
            TextView typeTextView = findViewById(R.id.text_user_profile_type);

            firstnameTextView.setText(loggedUser.getString("firstname"));
            lastnameTextView.setText(loggedUser.getString("lastname"));
            emailTextView.setText(loggedUser.getString("email"));
            phoneTextView.setText(loggedUser.getString("phone"));
            usernameTextView.setText(loggedUser.getString("username"));
            typeTextView.setText(loggedUser.getString("type"));

            firstnameEditText = findViewById(R.id.edit_text_user_profile_firstname);
            lastnameEditText = findViewById(R.id.edit_text_user_profile_lastname);
            emailEditText = findViewById(R.id.edit_text_user_profile_email);
            phoneEditText = findViewById(R.id.edit_text_user_profile_phone);
            usernameEditText = findViewById(R.id.edit_text_user_profile_username);
            passwordEditText = findViewById(R.id.edit_text_user_profile_password);
            passwordConfirmEditText = findViewById(R.id.edit_text_user_profile_password_confirm);

            buttonStartChangeInfo = findViewById(R.id.button_user_profile_start_change_info);
            buttonStartChangeInfo.setOnClickListener((view -> startChangeInfo()));
            buttonConfirmChangeInfo = findViewById(R.id.button_user_profile_confirm_change_info);
            buttonConfirmChangeInfo.setOnClickListener((view -> confirmChangeInfo()));
            buttonCancelChangeInfo = findViewById(R.id.button_user_profile_cancel_change_info);
            buttonCancelChangeInfo.setOnClickListener((view -> cancelChangeInfo()));

            layoutPassword = findViewById(R.id.linear_layout_user_profile_password);
            layoutPasswordConfirm = findViewById(R.id.linear_layout_user_profile_password_confirm);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void initData() {
        try {
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getApplicationContext().getString(R.string.local_storage), Context.MODE_PRIVATE);
            String loggedUserJsonString = sharedPref.getString("loggedUser", null);
            String usersJsonString = sharedPref.getString("users", null);
            if (loggedUserJsonString == null || usersJsonString == null) {
                Toast.makeText(UserProfileActivity.this, "Doslo je do greske prilikom inicijalizacije stranice. Pokusajte ponovo kasnije.", Toast.LENGTH_LONG).show();
                return;
            }

            loggedUser = new JSONObject(loggedUserJsonString);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_my_profile:
                startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                return true;
            case R.id.menu_item_buy_tickets:
                startActivity(new Intent(getApplicationContext(), TicketsActivity.class));
                return true;
            case R.id.menu_item_events:
                startActivity(new Intent(getApplicationContext(), EventsActivity.class));
                return true;
            case R.id.menu_item_animals:
                startActivity(new Intent(getApplicationContext(), AnimalsActivity.class));
                return true;
            case R.id.menu_item_contact:
                startActivity(new Intent(getApplicationContext(), ContactActivity.class));
                return true;
            case R.id.menu_item_notifications:
                startActivity(new Intent(getApplicationContext(), NotificationsActivity.class));
                return true;
            case R.id.menu_item_logout:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}