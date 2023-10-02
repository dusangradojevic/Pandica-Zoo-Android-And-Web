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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketsActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private JSONObject loggedUser;
    private JSONArray users;
    private JSONArray promoCodes;
    private JSONArray tickets;
    private List<JSONObject> promoPackagesList = new ArrayList<>();
    private List<Integer> promoPackageNumbers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tickets);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        View customActionBarView = getLayoutInflater().inflate(R.layout.app_bar, null);
        getSupportActionBar().setCustomView(customActionBarView);

        initData();
        initPage();
    }

    private void buyTickets() {
        try {
            EditText packageNumberEditText = findViewById(R.id.edit_text_ticket_order_number);
            EditText quantityEditText = findViewById(R.id.edit_text_ticket_order_quantity);
            EditText discountCodeEditText = findViewById(R.id.edit_text_ticket_order_discount_code);

            String packageNumberString = packageNumberEditText.getText().toString();
            if (packageNumberString.equals("")) {
                Toast.makeText(TicketsActivity.this, "Niste uneli redni broj paketa. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
                return;
            }

            String quantityString = quantityEditText.getText().toString();
            if (quantityString.equals("")) {
                Toast.makeText(TicketsActivity.this, "Niste uneli kolicinu. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
                return;
            }

            int packageNumber = Integer.parseInt(packageNumberString);
            JSONObject promoPackage = null;
            for (int i = 0; i < promoPackageNumbers.size(); ++i) {
                if (promoPackageNumbers.get(i) == packageNumber) {
                    promoPackage = promoPackagesList.get(i);
                    break;
                }
            }
            if (promoPackage == null) {
                Toast.makeText(TicketsActivity.this, "Redni broj mora imati vrednost nekog od ponudjenih paketa. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
                return;
            }

            int quantity = Integer.parseInt(quantityEditText.getText().toString());

            int price = promoPackagesList.get(promoPackage.getInt("id")).getInt("price") * quantity;

            String promoCode = discountCodeEditText.getText().toString();
            int promoCodeId = -1;
            if (!promoCode.equals("")) {
                int i;
                for (i = 0; i < promoCodes.length(); ++i) {
                    JSONObject currPromoCode = promoCodes.getJSONObject(i);
                    if (currPromoCode.getString("code").equals(promoCode)) {
                        int currPromoCodeQuantity = currPromoCode.getInt("quantity");
                        if (currPromoCodeQuantity == 0) {
                            Toast.makeText(TicketsActivity.this, "Promo kod nije validan. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        promoCodeId = currPromoCode.getInt("id");
                        price = (price * (100 - currPromoCode.getInt("discount"))) / 100;
                        break;
                    }
                }
                if (i == promoCodes.length()) {
                    Toast.makeText(TicketsActivity.this, "Promo kod nije validan. Pokusajte ponovo.", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            int maxPromoCodeId = -1;
            for (int i = 0; i < promoCodes.length(); ++i) {
                JSONObject currPromoCode = promoCodes.getJSONObject(i);
                int currPromoCodeId = currPromoCode.getInt("id");
                if (currPromoCodeId > maxPromoCodeId) {
                    maxPromoCodeId = currPromoCodeId;
                }
            }

            JSONObject ticket = new JSONObject();
            ticket.put("id", maxPromoCodeId + 1);
            ticket.put("userId", loggedUser.getInt("id"));
            ticket.put("promoPackageId", promoPackage.getInt("id"));
            ticket.put("quantity", quantity);
            ticket.put("price", price);
            ticket.put("promoCodeId", promoCodeId);
            ticket.put("status", "pending");

            tickets.put(ticket);

            JSONArray notifications = loggedUser.getJSONArray("notifications");
            notifications.put("Uspesno ste poslali zahtev za kupovinu paketa sa rednim brojem " + packageNumber + " po ceni od " + price + " dinara.");
            loggedUser.put("notifications", notifications);

            for (int i = 0; i < users.length(); ++i) {
                JSONObject currUser = users.getJSONObject(i);
                if (currUser.getInt("id") == loggedUser.getInt("id")) {
                    currUser.put("notifications", notifications);
                }
            }

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("loggedUser", loggedUser.toString());
            editor.putString("users", users.toString());
            editor.putString("tickets", tickets.toString());
            editor.apply();

            Toast.makeText(TicketsActivity.this, "Uspesno ste poslali zahtev za kupovinu paketa.", Toast.LENGTH_LONG).show();

            finish();
            startActivity(getIntent());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void initPage() {
        try {
            Button buyTicketsButton = findViewById(R.id.button_buy_tickets);
            buyTicketsButton.setOnClickListener(view -> buyTickets());

            for (int i = 0; i < promoPackagesList.size(); ++i) {
                JSONObject promoPackage = promoPackagesList.get(i);

                TextView promoPackageTextView = new TextView(this);
                String promoPackageTextString = promoPackage.getString("name");
                if (promoPackageNumbers.get(i) != -1) {
                    promoPackageTextString = promoPackageNumbers.get(i) + ". " + promoPackageTextString + " - " + promoPackage.getInt("price") + " din";
                }
                promoPackageTextView.setText(promoPackageTextString);
                promoPackageTextView.setGravity(Gravity.CENTER);
                LayoutParams promoPackageTextViewLayoutParams = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                );
                promoPackageTextViewLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                promoPackageTextView.setLayoutParams(promoPackageTextViewLayoutParams);

                boolean isPackageTypeSingle = promoPackage.getString("type").equals("single");
                LinearLayout ticketsLayout = findViewById(isPackageTypeSingle ? R.id.linear_layout_tickets_single : R.id.linear_layout_tickets_group);
                ticketsLayout.addView(promoPackageTextView);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void initData() {
        try {
            sharedPref = getApplicationContext().getSharedPreferences(getApplicationContext().getString(R.string.local_storage), Context.MODE_PRIVATE);
            String loggedUserJsonString = sharedPref.getString("loggedUser", null);
            String usersJsonString = sharedPref.getString("users", null);
            String ticketsJsonString = sharedPref.getString("tickets", null);
            String promoCodesJsonString = sharedPref.getString("promoCodes", null);
            String promoPackagesJsonString = sharedPref.getString("promoPackages", null);

            if (loggedUserJsonString == null || usersJsonString == null || ticketsJsonString == null || promoCodesJsonString == null || promoPackagesJsonString == null) {
                Toast.makeText(TicketsActivity.this, "Doslo je do greske prilikom inicijalizacije stranice. Pokusajte ponovo kasnije.", Toast.LENGTH_LONG).show();
                return;
            }

            loggedUser = new JSONObject(loggedUserJsonString);
            users = new JSONArray(usersJsonString);
            tickets = new JSONArray(ticketsJsonString);
            promoCodes = new JSONArray(promoCodesJsonString);

            JSONArray promoPackagesJSONArray = new JSONArray(promoPackagesJsonString);
            for (int i = 0; i < promoPackagesJSONArray.length(); ++i) {
                promoPackagesList.add(promoPackagesJSONArray.getJSONObject(i));
            }

            Collections.sort(promoPackagesList, (promoPackage1, promoPackage2) -> {
                try {
                    String type1 = promoPackage1.getString("type");
                    String type2 = promoPackage2.getString("type");
                    int price1 = promoPackage1.getInt("price");
                    int price2 = promoPackage2.getInt("price");
                    if (type1.equals(type2) && (price1 == 0 || price2 == 0)) {
                        return price2 == 0 ? -1 : 1;
                    } else if (!type1.equals(type2)) {
                        return type2.equals("single") ? 1 : -1;
                    } else {
                        return 0;
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });

            int index = 1;
            for (JSONObject promoPackage : promoPackagesList) {
                try {
                    int price = promoPackage.getInt("price");
                    if (price != 0) {
                        promoPackageNumbers.add(index++);
                    } else {
                        promoPackageNumbers.add(-1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
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