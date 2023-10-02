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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EventsActivity extends AppCompatActivity {
    private View mainView;
    private SharedPreferences sharedPref;
    private JSONArray events;
    private JSONObject loggedUser;
    private JSONArray likes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        View customActionBarView = getLayoutInflater().inflate(R.layout.app_bar, null);
        getSupportActionBar().setCustomView(customActionBarView);

        mainView = findViewById(R.id.events);

        initData();
        initPage();
    }

    private void likeDislike(View view) {
        try {
            String viewTag = view.getTag().toString();
            String[] viewTagSplit = viewTag.split("_");
            int eventId = Integer.parseInt(viewTagSplit[viewTagSplit.length - 1]);
            boolean wasLiked = viewTagSplit[viewTagSplit.length - 2].equals("liked");

            ImageView likeImageView = (ImageView) view;
            likeImageView.setImageResource(wasLiked ? R.drawable.baseline_thumb_up_24_black : R.drawable.baseline_thumb_up_24_green);
            likeImageView.setTag((wasLiked ? "image_like_button_not-liked_" : "image_like_button_liked_") + eventId);

            TextView numberOfLikesTextView = mainView.findViewWithTag("text_likes_number_" + eventId);
            Integer updatedNumberOfLikes = Integer.parseInt(numberOfLikesTextView.getText().toString()) + (wasLiked ? -1 : 1);
            numberOfLikesTextView.setText(updatedNumberOfLikes.toString());

            if (!wasLiked) {
                JSONObject newLike = new JSONObject();
                int maxLikeId = 0;
                for (int i = 0; i < likes.length(); ++i) {
                    JSONObject currLike = likes.getJSONObject(i);

                    int currLikeId = currLike.getInt("id");
                    if (currLikeId > maxLikeId) {
                        maxLikeId = currLikeId;
                    }
                }
                newLike.put("id", maxLikeId);
                newLike.put("eventId", eventId);
                newLike.put("userId", loggedUser.getInt("id"));
                likes.put(newLike);
            } else {
                for (int i = 0; i < likes.length(); ++i) {
                    JSONObject currLike = likes.getJSONObject(i);
                    if (currLike.getInt("eventId") == eventId && currLike.getInt("userId") == loggedUser.getInt("id")) {
                        likes.remove(i);
                        break;
                    }
                }
            }

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("likes", likes.toString());
            editor.apply();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void initPage() {
        try {
            float dpToPxMultiplier = getResources().getDisplayMetrics().density;

            for (int i = 0; i < events.length(); ++i) {
                JSONObject event = events.getJSONObject(i);

                LinearLayout eventLayout = new LinearLayout(this);
                eventLayout.setGravity(Gravity.CENTER);
                eventLayout.setOrientation(LinearLayout.HORIZONTAL);
                LayoutParams linearLayoutParams = new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                );
                eventLayout.setLayoutParams(linearLayoutParams);

                ImageView eventImageView = new ImageView(this);
                eventImageView.setImageResource(getResources().getIdentifier(event.getString("photo"), "drawable", getPackageName()));
                LayoutParams eventImageViewLayoutParams = new LayoutParams(
                        (int) (90 * dpToPxMultiplier),
                        (int) (80 * dpToPxMultiplier)
                );
                eventImageViewLayoutParams.setMargins((int) (0 * dpToPxMultiplier), 0, (int) (5 * dpToPxMultiplier), 0);
                eventImageView.setLayoutParams(eventImageViewLayoutParams);

                LinearLayout eventNameDescriptionLayout = new LinearLayout(this);
                eventNameDescriptionLayout.setOrientation(LinearLayout.VERTICAL);
                eventNameDescriptionLayout.setLayoutParams(new LayoutParams(
                        (int) (200 * dpToPxMultiplier),
                        LayoutParams.WRAP_CONTENT
                ));

                TextView nameTextView = new TextView(this);
                nameTextView.setTextSize(15);
                nameTextView.setText(event.getString("name"));
                LayoutParams nameTextViewLayoutParams = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                );
                nameTextView.setLayoutParams(nameTextViewLayoutParams);

                TextView descriptionTextView = new TextView(this);
                descriptionTextView.setTextSize(10);
                descriptionTextView.setText(event.getString("description"));
                LayoutParams descriptionTextViewLayoutParams = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                );
                descriptionTextView.setLayoutParams(descriptionTextViewLayoutParams);

                eventNameDescriptionLayout.addView(nameTextView);
                eventNameDescriptionLayout.addView(descriptionTextView);

                Integer numberOfLikes = 0;
                boolean hasThisUserLiked = false;
                for (int ii = 0; ii < likes.length(); ++ii) {
                    JSONObject like = likes.getJSONObject(ii);
                    if (like.getInt("eventId") == event.getInt("id")) {
                        numberOfLikes++;
                        if (like.getInt("userId") == loggedUser.getInt("id")) {
                            hasThisUserLiked = true;
                        }
                    }
                }

                ImageView likeImageView = new ImageView(this);
                String likeImageIdString = "image_like_button_not-liked_" + event.getInt("id");
                likeImageView.setId(View.generateViewId());
                likeImageView.setTag(likeImageIdString);
                LayoutParams likeImageViewLayoutParams = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                );
                likeImageViewLayoutParams.setMargins((int) (5 * dpToPxMultiplier), 0, (int) (5 * dpToPxMultiplier), 0);
                likeImageView.setLayoutParams(likeImageViewLayoutParams);
                likeImageView.setImageResource(hasThisUserLiked ? R.drawable.baseline_thumb_up_24_green : R.drawable.baseline_thumb_up_24_black);
                likeImageView.setOnClickListener((view -> likeDislike(view)));

                TextView numberOfLikesTextView = new TextView(this);
                String numberOfLikesTextViewIdString = "text_likes_number_" + event.getInt("id");
                numberOfLikesTextView.setId(View.generateViewId());
                numberOfLikesTextView.setTag(numberOfLikesTextViewIdString);
                numberOfLikesTextView.setText(numberOfLikes.toString());
                numberOfLikesTextView.setTextColor(Color.BLACK);
                LayoutParams numberOfLikesTextViewLayoutParams = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                );
                descriptionTextView.setLayoutParams(numberOfLikesTextViewLayoutParams);

                eventLayout.addView(eventImageView);
                eventLayout.addView(eventNameDescriptionLayout);
                eventLayout.addView(likeImageView);
                eventLayout.addView(numberOfLikesTextView);

                LinearLayout eventsLayout = findViewById(R.id.linear_layout_events);
                eventsLayout.addView(eventLayout);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void initData() {
        try {
            sharedPref = getApplicationContext().getSharedPreferences(getApplicationContext().getString(R.string.local_storage), Context.MODE_PRIVATE);
            String eventsJsonString = sharedPref.getString("events", null);
            String loggedUserJsonString = sharedPref.getString("loggedUser", null);
            String likesJsonString = sharedPref.getString("likes", null);
            if (eventsJsonString == null || loggedUserJsonString == null || likesJsonString == null) {
                Toast.makeText(EventsActivity.this, "Doslo je do greske prilikom inicijalizacije stranice. Pokusajte ponovo kasnije.", Toast.LENGTH_LONG).show();
                return;
            }

            events = new JSONArray(eventsJsonString);
            loggedUser = new JSONObject(loggedUserJsonString);
            likes = new JSONArray(likesJsonString);
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
