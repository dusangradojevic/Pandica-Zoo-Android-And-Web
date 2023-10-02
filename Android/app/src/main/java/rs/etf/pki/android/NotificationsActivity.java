package rs.etf.pki.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NotificationsActivity extends AppCompatActivity {
    private JSONObject loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        View customActionBarView = getLayoutInflater().inflate(R.layout.app_bar, null);
        getSupportActionBar().setCustomView(customActionBarView);

        initData();
        initPage();
    }

    private void initPage() {
        try {
            float dpToPxMultiplier = getResources().getDisplayMetrics().density;

            JSONArray notifications = loggedUser.getJSONArray("notifications");
            for (int i = notifications.length() - 1; i >= 0; --i) {
                String notification = notifications.getString(i);

                TextView notificationTextView = new TextView(this);
                notificationTextView.setText(notification);
                notificationTextView.setGravity(Gravity.CENTER);
                LayoutParams notificationTextViewLayoutParams = new LayoutParams(
                        (int) (300 * dpToPxMultiplier),
                        LayoutParams.WRAP_CONTENT
                );
                notificationTextViewLayoutParams.gravity = Gravity.CENTER;
                notificationTextViewLayoutParams.setMargins(0, (int) (16 * dpToPxMultiplier), 0, 0);
                notificationTextView.setLayoutParams(notificationTextViewLayoutParams);

                View blackLineView = new View(this);
                blackLineView.setBackgroundColor(Color.BLACK);
                LayoutParams blackLineViewLayoutParams = new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        (int) (1 * dpToPxMultiplier)
                );
                int margin = (int) (16 * dpToPxMultiplier);
                blackLineViewLayoutParams.setMargins(margin, margin, margin, margin);
                blackLineView.setLayoutParams(blackLineViewLayoutParams);

                LinearLayout notificationsLayout = findViewById(R.id.linear_layout_notifications);
                notificationsLayout.addView(notificationTextView);
                notificationsLayout.addView(blackLineView);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void initData() {
        try {
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getApplicationContext().getString(R.string.local_storage), Context.MODE_PRIVATE);
            String loggedUserJsonString = sharedPref.getString("loggedUser", null);

            if (loggedUserJsonString == null) {
                Toast.makeText(NotificationsActivity.this, "Doslo je do greske prilikom inicijalizacije stranice. Pokusajte ponovo kasnije.", Toast.LENGTH_LONG).show();
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
