package rs.etf.pki.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AnimalsActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private JSONArray animals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animals);

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

            for (int i = 0; i < animals.length(); ++i) {
                JSONObject animal = animals.getJSONObject(i);

                LinearLayout animalLayout = new LinearLayout(this);
                String animalIdString = "linear_layout_animal_" + animal.getInt("id");
                animalLayout.setId(View.generateViewId());
                animalLayout.setTag(animalIdString);
                animalLayout.setGravity(Gravity.CENTER_VERTICAL);
                animalLayout.setOrientation(LinearLayout.HORIZONTAL);
                LayoutParams linearLayoutParams = new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                );
                linearLayoutParams.gravity = Gravity.CENTER;
                linearLayoutParams.setMargins(0, 0, 0, (int) (16 * dpToPxMultiplier));
                animalLayout.setLayoutParams(linearLayoutParams);

                ImageView imageView = new ImageView(this);
                String imageViewIdString = "image_animal_" + animal.getInt("id");
                imageView.setId(View.generateViewId());
                imageView.setTag(imageViewIdString);
                imageView.setImageResource(getResources().getIdentifier(animal.getString("photo"), "drawable", getPackageName()));
                LayoutParams imageLayoutParams = new LayoutParams(
                        (int) (113 * dpToPxMultiplier),
                        (int) (100 * dpToPxMultiplier)
                );
                imageLayoutParams.setMargins((int) (70 * dpToPxMultiplier), 0, 0, 0);
                imageView.setLayoutParams(imageLayoutParams);
                imageView.setOnClickListener((view) -> goToAnimalDetails(view));

                TextView textView = new TextView(this);
                String textViewIdString = "text_animal_" + animal.getInt("id");
                textView.setId(View.generateViewId());
                textView.setTag(textViewIdString);
                textView.setText(animal.getString("name"));
                textView.setTextSize(30);
                LayoutParams textLayoutParams = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                );
                textLayoutParams.setMargins((int) (10 * dpToPxMultiplier), 0, 0, 0);
                textView.setLayoutParams(textLayoutParams);
                textView.setOnClickListener((view) -> goToAnimalDetails(view));

                animalLayout.addView(imageView);
                animalLayout.addView(textView);

                LinearLayout animalsLayout = findViewById(R.id.linear_layout_animals);
                animalsLayout.addView(animalLayout);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void goToAnimalDetails(View view) {
        try {
            String viewTag = view.getTag().toString();
            String[] viewTagSplit = viewTag.split("_");
            int animalId = Integer.parseInt(viewTagSplit[viewTagSplit.length - 1]);
            for (int i = 0; i < animals.length(); ++i) {
                JSONObject animal = animals.getJSONObject(i);
                if (animalId == animal.getInt("id")) {
                    sharedPref = getApplicationContext().getSharedPreferences(getApplicationContext().getString(R.string.local_storage), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("currentAnimalDetails", animal.toString());
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), AnimalDetailsActivity.class);
                    startActivity(intent);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void initData() {
        try {
            sharedPref = getApplicationContext().getSharedPreferences(getApplicationContext().getString(R.string.local_storage), Context.MODE_PRIVATE);
            String animalsJsonString = sharedPref.getString("animals", null);
            if (animalsJsonString == null) {
                Toast.makeText(AnimalsActivity.this, "Doslo je do greske prilikom inicijalizacije stranice. Pokusajte ponovo kasnije.", Toast.LENGTH_LONG).show();
                return;
            }

            animals = new JSONArray(animalsJsonString);
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