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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AnimalDetailsActivity extends AppCompatActivity {

    private View mainView;
    private SharedPreferences sharedPref;
    private JSONObject animal;
    private JSONArray comments;
    private JSONObject loggedUser;
    private JSONArray users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animal_details);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        View customActionBarView = getLayoutInflater().inflate(R.layout.app_bar, null);
        getSupportActionBar().setCustomView(customActionBarView);

        mainView = findViewById(R.id.animal_details);

        Button buttonInsertComment = findViewById(R.id.button_animal_details_add_comment);
        buttonInsertComment.setOnClickListener((view) -> insertComment());

        initData();
        initPage();
    }

    private void startEditComment(View view) {
        String viewTag = view.getTag().toString();
        String[] viewTagSplit = viewTag.split("_");
        int commentId = Integer.parseInt(viewTagSplit[viewTagSplit.length - 1]);

        Button commentEditHeaderButton = mainView.findViewWithTag("button_edit_comment_" + commentId);
        Button commentDeleteHeaderButton = mainView.findViewWithTag("button_delete_comment_" + commentId);
        Button commentConfirmEditHeaderButton = mainView.findViewWithTag("button_confirm_edit_comment_" + commentId);
        Button commentCancelEditHeaderButton = mainView.findViewWithTag("button_cancel_edit_comment_" + commentId);
        commentEditHeaderButton.setVisibility(View.GONE);
        commentDeleteHeaderButton.setVisibility(View.GONE);
        commentConfirmEditHeaderButton.setVisibility(View.VISIBLE);
        commentCancelEditHeaderButton.setVisibility(View.VISIBLE);

        TextView commentTextView = mainView.findViewWithTag("text_comment_text_" + commentId);
        EditText commentEditText = mainView.findViewWithTag("edit_text_comment_" + commentId);
        commentTextView.setVisibility(View.GONE);
        commentEditText.setVisibility(View.VISIBLE);
    }

    private void startDeleteComment(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Da li ste sigurni da zelite da obrisete komentar?")
                .setPositiveButton("Da", (dialog, which) -> confirmDeleteComment(view))
                .setNegativeButton("Ne", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void confirmDeleteComment(View view) {
        String viewTag = view.getTag().toString();
        String[] viewTagSplit = viewTag.split("_");
        int commentId = Integer.parseInt(viewTagSplit[viewTagSplit.length - 1]);

        try {
            for (int i = 0; i < comments.length(); ++i) {
                JSONObject currComment = comments.getJSONObject(i);
                if (currComment.getInt("id") == commentId) {
                    comments.remove(i);

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("comments", comments.toString());
                    editor.apply();

                    finish();
                    startActivity(getIntent());
                    return;
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void confirmEditComment(View view) {
        String viewTag = view.getTag().toString();
        String[] viewTagSplit = viewTag.split("_");
        int commentId = Integer.parseInt(viewTagSplit[viewTagSplit.length - 1]);

        EditText commentEditText = mainView.findViewWithTag("edit_text_comment_" + commentId);
        String newComment = commentEditText.getText().toString();

        if (newComment.trim().equals("")) {
            Toast.makeText(AnimalDetailsActivity.this, "Komentar ne sme biti prazan. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            for (int i = 0; i < comments.length(); ++i) {
                JSONObject currComment = comments.getJSONObject(i);
                if (currComment.getInt("id") == commentId) {
                    currComment.put("commentText", newComment);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("comments", comments.toString());
                    editor.apply();

                    finish();
                    startActivity(getIntent());
                    return;
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void cancelEditComment(View view) {
        String viewTag = view.getTag().toString();
        String[] viewTagSplit = viewTag.split("_");
        int commentId = Integer.parseInt(viewTagSplit[viewTagSplit.length - 1]);

        Button commentEditHeaderButton = mainView.findViewWithTag("button_edit_comment_" + commentId);
        Button commentDeleteHeaderButton = mainView.findViewWithTag("button_delete_comment_" + commentId);
        Button commentConfirmEditHeaderButton = mainView.findViewWithTag("button_confirm_edit_comment_" + commentId);
        Button commentCancelEditHeaderButton = mainView.findViewWithTag("button_cancel_edit_comment_" + commentId);
        commentEditHeaderButton.setVisibility(View.VISIBLE);
        commentDeleteHeaderButton.setVisibility(View.VISIBLE);
        commentConfirmEditHeaderButton.setVisibility(View.GONE);
        commentCancelEditHeaderButton.setVisibility(View.GONE);

        TextView commentTextView = mainView.findViewWithTag("text_comment_text_" + commentId);
        EditText commentEditText = mainView.findViewWithTag("edit_text_comment_" + commentId);
        commentTextView.setVisibility(View.VISIBLE);
        commentEditText.setVisibility(View.GONE);
        commentEditText.setText(commentTextView.getText());
    }

    private void insertComment() {
        try {
            EditText editTextCommentText = findViewById(R.id.edit_text_animal_details_add_comment);
            String commentText = editTextCommentText.getText().toString();

            if (commentText.trim().equals("")) {
                Toast.makeText(AnimalDetailsActivity.this, "Komentar ne sme biti prazan. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
                return;
            }

            int maxCommentId = -1;
            for (int i = 0; i < comments.length(); ++i) {
                JSONObject currComment = comments.getJSONObject(i);

                int currCommentId = currComment.getInt("id");
                if (currCommentId > maxCommentId) {
                    maxCommentId = currCommentId;
                }
            }

            JSONObject newComment = new JSONObject();
            newComment.put("id", maxCommentId + 1);
            newComment.put("userId", loggedUser.getInt("id"));
            newComment.put("animalId", animal.getInt("id"));
            newComment.put("commentText", commentText);

            comments.put(newComment);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("comments", comments.toString());
            editor.apply();

            Toast.makeText(AnimalDetailsActivity.this, "Komentar je uspesno dodat.", Toast.LENGTH_LONG).show();

            finish();
            startActivity(getIntent());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void initPage() {
        try {
            float dpToPxMultiplier = getResources().getDisplayMetrics().density;

            TextView header = findViewById(R.id.text_animal_details_header);
            header.setText(animal.getString("name"));

            ImageView photo = findViewById(R.id.image_animal_details_photo);
            photo.setImageResource(getResources().getIdentifier(animal.getString("photo"), "drawable", getPackageName()));

            TextView description = findViewById(R.id.text_animal_details_description);
            description.setText(animal.getString("description"));

            TextView animalDetailsHeader = findViewById(R.id.text_animal_details_header);
            animalDetailsHeader.setText(animal.getString("name"));

            for (int i = comments.length() - 1; i >= 0; --i) {
                JSONObject currComment = comments.getJSONObject(i);
                if (currComment.getInt("animalId") == animal.getInt("id")) {
                    TextView textNoComments = findViewById(R.id.text_animal_details_no_comments);
                    textNoComments.setVisibility(View.GONE);

                    JSONObject currUser = null;
                    for (int ii = 0; ii < users.length(); ++ii) {
                        currUser = users.getJSONObject(ii);
                        if (currUser.getInt("id") == currComment.getInt("userId")) {
                            break;
                        }
                    }

                    LinearLayout commentLayout = new LinearLayout(this);
                    commentLayout.setOrientation(LinearLayout.VERTICAL);
                    LayoutParams commentLayoutParams = new LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    commentLayoutParams.setMargins(0, 0, 0, (int) (16 * dpToPxMultiplier));
                    commentLayout.setLayoutParams(commentLayoutParams);

                    LinearLayout commentHeaderLayout = new LinearLayout(this);
                    commentHeaderLayout.setOrientation(LinearLayout.HORIZONTAL);
                    commentHeaderLayout.setGravity(Gravity.CENTER_VERTICAL);
                    LayoutParams commentHeaderLayoutParams = new LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT
                    );
                    commentHeaderLayoutParams.setMargins(0, 0, 0, (int) (16 * dpToPxMultiplier));
                    commentLayout.setLayoutParams(commentHeaderLayoutParams);

                    TextView commentHeaderTextView = new TextView(this);
                    commentHeaderTextView.setText(currUser.getString("firstname") + " " + currUser.getString("lastname"));
                    commentHeaderTextView.setTextSize(23);
                    commentHeaderTextView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));

                    int visibility = currComment.getInt("userId") != loggedUser.getInt("id") ? View.INVISIBLE : View.VISIBLE;
                    Button commentEditHeaderButton = createButton("button_edit_comment_" + currComment.getInt("id"), "Izmeni", 70, 40, visibility);
                    Button commentDeleteHeaderButton = createButton("button_delete_comment_" + currComment.getInt("id"), "Obrisi", 70, 40, visibility);
                    Button commentConfirmEditHeaderButton = createButton("button_confirm_edit_comment_" + currComment.getInt("id"), "Potvrdi", 80, 40, View.GONE);
                    Button commentCancelEditHeaderButton = createButton("button_cancel_edit_comment_" + currComment.getInt("id"), "Odustani", 90, 40, View.GONE);
                    if (visibility == View.VISIBLE) {
                        commentEditHeaderButton.setOnClickListener((view) -> startEditComment(view));
                        commentDeleteHeaderButton.setOnClickListener((view) -> startDeleteComment(view));
                        commentConfirmEditHeaderButton.setOnClickListener((view) -> confirmEditComment(view));
                        commentCancelEditHeaderButton.setOnClickListener((view) -> cancelEditComment(view));
                    }

                    commentHeaderLayout.addView(commentHeaderTextView);
                    commentHeaderLayout.addView(commentEditHeaderButton);
                    commentHeaderLayout.addView(commentDeleteHeaderButton);
                    commentHeaderLayout.addView(commentConfirmEditHeaderButton);
                    commentHeaderLayout.addView(commentCancelEditHeaderButton);

                    TextView commentTextView = new TextView(this);
                    String commentTextViewIdString = "text_comment_text_" + currComment.getInt("id");
                    commentTextView.setId(View.generateViewId());
                    commentTextView.setTag(commentTextViewIdString);
                    commentTextView.setText(currComment.getString("commentText"));
                    commentTextView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));

                    EditText commentEditText = new EditText(this);
                    String commentEditTextIdString = "edit_text_comment_" + currComment.getInt("id");
                    commentEditText.setId(View.generateViewId());
                    commentEditText.setTag(commentEditTextIdString);
                    commentEditText.setText(currComment.getString("commentText"));
                    commentEditText.setBackgroundColor(Color.WHITE);
                    commentEditText.setEms(15);
                    LayoutParams commentEditTextLayoutParams = new LinearLayout.LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT
                    );
                    commentEditTextLayoutParams.setMargins(0, (int) (5 * dpToPxMultiplier), 0, 0);
                    commentEditText.setLayoutParams(commentEditTextLayoutParams);
                    commentEditText.setVisibility(View.GONE);

                    commentLayout.addView(commentHeaderLayout);
                    commentLayout.addView(commentTextView);
                    commentLayout.addView(commentEditText);

                    LinearLayout commentsLayout = findViewById(R.id.animal_details_comments);
                    commentsLayout.addView(commentLayout);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void initData() {
        try {
            sharedPref = getApplicationContext().getSharedPreferences(getApplicationContext().getString(R.string.local_storage), Context.MODE_PRIVATE);

            String animalJsonString = sharedPref.getString("currentAnimalDetails", null);
            String commentsJsonString = sharedPref.getString("comments", null);
            String loggedUserJsonString = sharedPref.getString("loggedUser", null);
            String usersJsonString = sharedPref.getString("users", null);

            if (animalJsonString == null || commentsJsonString == null || loggedUserJsonString == null | usersJsonString == null) {
                Toast.makeText(AnimalDetailsActivity.this, "Doslo je do greske prilikom inicijalizacije stranice. Pokusajte ponovo kasnije.", Toast.LENGTH_LONG).show();
                return;
            }

            animal = new JSONObject(animalJsonString);
            comments = new JSONArray(commentsJsonString);
            loggedUser = new JSONObject(loggedUserJsonString);
            users = new JSONArray(usersJsonString);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private Button createButton(String buttonIdString, String text, int width, int height, int visibility) {
        float dpToPxMultiplier = getResources().getDisplayMetrics().density;

        Button button = new Button(this);
        button.setId(View.generateViewId());
        button.setTag(buttonIdString);
        button.setText(text);
        button.setTextSize(10);
        button.setTextColor(Color.BLACK);
        LayoutParams buttonLayoutParams = new LayoutParams(
                (int) (width * dpToPxMultiplier),
                (int) (height * dpToPxMultiplier)
        );
        buttonLayoutParams.setMargins((int) (10 * dpToPxMultiplier), 0, 0, 0);
        button.setLayoutParams(buttonLayoutParams);
        button.setVisibility(visibility);
        button.setBackgroundResource(R.drawable.rounded_button);

        return button;
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
