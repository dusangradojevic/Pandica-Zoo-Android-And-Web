package rs.etf.pki.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSharedPreferences();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    private void initSharedPreferences() {
        try {
            JSONObject animal1 = new JSONObject();
            animal1.put("id", 0);
            animal1.put("name", "Africki slon");
            animal1.put("description", "Slon je impresivan, veliki kopneni sisar s karakterističnim dugačkim kljovama i velikim ušima, koji nas podseća na snagu i gracioznost divljine.");
            animal1.put("photo", "elephant");

            JSONObject animal2 = new JSONObject();
            animal2.put("id", 1);
            animal2.put("name", "Tigar");
            animal2.put("description", "Tigar je moćna divlja mačka s prepoznatljivim prugastim krznom, koji simbolizuje snagu i eleganciju u svetu životinja.");
            animal2.put("photo", "tiger");

            JSONObject animal3 = new JSONObject();
            animal3.put("id", 2);
            animal3.put("name", "Lav");
            animal3.put("description", "Lav je kralj afričke savane, predstavnik maestralne moći i hrabrosti u životinjskom carstvu, s impozantnom grivom i plemenitim držanjem.");
            animal3.put("photo", "lion");

            JSONObject animal4 = new JSONObject();
            animal4.put("id", 3);
            animal4.put("name", "Vuk");
            animal4.put("description", "Vuk je lukav i društven sisar sa snažnim instinktom za preživljavanje i karakterističnim krznom sivih nijansi, koji nas podseća na divlju i slobodnu prirodu.");
            animal4.put("photo", "wolf");

            JSONObject animal5 = new JSONObject();
            animal5.put("id", 4);
            animal5.put("name", "Beli medved");
            animal5.put("description", "Beli medved je fascinantan sisar koji se prilagodio hladnim arktičkim uslovima, sa svojom debelom belom dlakom i impozantnim izgledom koji ga čini simbolom snega i leda.");
            animal5.put("photo", "polar_bear_baby");

            JSONObject animal6 = new JSONObject();
            animal6.put("id", 5);
            animal6.put("name", "Orao");
            animal6.put("description", "Orao je veličanstvena ptica grabljivica, sa širokim rasponom krila i oštrim kandžama, koja simbolizuje slobodu i moć u svetu ptica.");
            animal6.put("photo", "eagle");

            JSONObject animal7 = new JSONObject();
            animal7.put("id", 6);
            animal7.put("name", "Nosorog");
            animal7.put("description", "Nosorog je ogroman kopneni sisar sa debelom kožom, velikim rogom na nosu i impozantnim izgledom koji ga čini jednim od najupečatljivijih članova divlje faune.");
            animal7.put("photo", "rhino");

            JSONObject animal8 = new JSONObject();
            animal8.put("id", 7);
            animal8.put("name", "Leopard");
            animal8.put("description", "Leopard je elegantna divlja mačka s prepoznatljivim šarama na krznu, koja odiše tajanstvenošću i snagom u svetu divljine.");
            animal8.put("photo", "leopard");

            JSONObject animal9 = new JSONObject();
            animal9.put("id", 8);
            animal9.put("name", "Grizli");
            animal9.put("description", "Grizli je veliki i moćan medved sa smeđim krznom i karakterističnom grivom oko vrata, koji predstavlja simbol divljine i snage u Severnoj Americi.");
            animal9.put("photo", "grizzly");

            JSONObject animal10 = new JSONObject();
            animal10.put("id", 9);
            animal10.put("name", "Foka");
            animal10.put("description", "Foka je morski sisar sa dugim telom, pljosnatim repom i velikim očima, koja se odlično prilagodila životu u hladnim morskim vodama i simbolizuje gracioznost u podvodnom svetu.");
            animal10.put("photo", "seal");

            JSONArray animals = new JSONArray();
            animals.put(animal1);
            animals.put(animal2);
            animals.put(animal3);
            animals.put(animal4);
            animals.put(animal5);
            animals.put(animal6);
            animals.put(animal7);
            animals.put(animal8);
            animals.put(animal9);
            animals.put(animal10);

            JSONObject event1 = new JSONObject();
            event1.put("id", 0);
            event1.put("name", "Igranje sa bebom slona");
            event1.put("description", "Igranje i maženje bebe slona!");
            event1.put("photo", "@drawable/elephant_baby");

            JSONObject event2 = new JSONObject();
            event2.put("id", 1);
            event2.put("name", "Zoo Challenge");
            event2.put("description", "Pokazite timski duh, poznavanje zivotinja i snalazenje u prostoru! Vas tim dobija pocetni trag koji vas vodi kroz razlicite avanture do konacnog resenja misterija Pandica Zoo Vrta!");
            event2.put("photo", "@drawable/grey_wolf");

            JSONObject event3 = new JSONObject();
            event3.put("id", 2);
            event3.put("name", "Scavanger hunt");
            event3.put("description", "Dva tima dobijaju spisak zivotinja koje treba da pronadju i fotografisu telefonom. Sa nekim zivotinjama moraju da naprave cak i selfi fotografiju!");
            event3.put("photo", "@drawable/cat_selfie");

            JSONArray events = new JSONArray();
            events.put(event1);
            events.put(event2);
            events.put(event3);

            JSONObject promoCode1 = new JSONObject();
            promoCode1.put("id", 0);
            promoCode1.put("code", "PROMO1");
            promoCode1.put("discount", 10);
            promoCode1.put("quantity", 6);

            JSONObject promoCode2 = new JSONObject();
            promoCode2.put("id", 1);
            promoCode2.put("code", "PROMO2");
            promoCode2.put("discount", 15);
            promoCode2.put("quantity", 5);

            JSONObject promoCode3 = new JSONObject();
            promoCode3.put("id", 2);
            promoCode3.put("code", "PROMO3");
            promoCode3.put("discount", 20);
            promoCode3.put("quantity", 100);

            JSONArray promoCodes = new JSONArray();
            promoCodes.put(promoCode1);
            promoCodes.put(promoCode2);
            promoCodes.put(promoCode3);

            JSONObject promoPackage1 = new JSONObject();
            promoPackage1.put("id", 0);
            promoPackage1.put("name", "Odrasli (vise od 15 godina)");
            promoPackage1.put("price", 700);
            promoPackage1.put("type", "single");

            JSONObject promoPackage2 = new JSONObject();
            promoPackage2.put("id", 1);
            promoPackage2.put("name", "Deca (od 5 do 15 godina)");
            promoPackage2.put("price", 400);
            promoPackage2.put("type", "single");

            JSONObject promoPackage3 = new JSONObject();
            promoPackage3.put("id", 2);
            promoPackage3.put("name", "Deca do 5 godina ne placaju ulaznicu");
            promoPackage3.put("price", 0);
            promoPackage3.put("type", "single");

            JSONObject promoPackage4 = new JSONObject();
            promoPackage4.put("id", 3);
            promoPackage4.put("name", "Grupe odraslih (vise od 15 osoba)");
            promoPackage4.put("price", 500);
            promoPackage4.put("type", "group");

            JSONObject promoPackage5 = new JSONObject();
            promoPackage5.put("id", 4);
            promoPackage5.put("name", "Grupe iz osnovnih i srednjih skola (vise od 15 dece)");
            promoPackage5.put("price", 300);
            promoPackage5.put("type", "group");

            JSONObject promoPackage6 = new JSONObject();
            promoPackage6.put("id", 5);
            promoPackage6.put("name", "Grupe iz predskolskih ustanova");
            promoPackage6.put("price", 200);
            promoPackage6.put("type", "group");

            JSONArray promoPackages = new JSONArray();
            promoPackages.put(promoPackage1);
            promoPackages.put(promoPackage2);
            promoPackages.put(promoPackage3);
            promoPackages.put(promoPackage4);
            promoPackages.put(promoPackage5);
            promoPackages.put(promoPackage6);

            JSONObject ticket1 = new JSONObject();
            ticket1.put("id", 0);
            ticket1.put("userId", 1);
            ticket1.put("promoPackageId", 1);
            ticket1.put("quantity", 1);
            ticket1.put("price", 700);
            ticket1.put("promoCodeId", -1);
            ticket1.put("status", "pending");

            JSONObject ticket2 = new JSONObject();
            ticket2.put("id", 1);
            ticket2.put("userId", 1);
            ticket2.put("promoPackageId", 0);
            ticket2.put("quantity", 1);
            ticket2.put("price", 1000);
            ticket2.put("promoCodeId", -1);
            ticket2.put("status", "pending");

            JSONObject ticket3 = new JSONObject();
            ticket3.put("id", 2);
            ticket3.put("userId", 1);
            ticket3.put("promoPackageId", 3);
            ticket3.put("quantity", 1);
            ticket3.put("price", 1300);
            ticket3.put("promoCodeId", -1);
            ticket3.put("status", "pending");

            JSONArray tickets = new JSONArray();
            tickets.put(ticket1);
            tickets.put(ticket2);
            tickets.put(ticket3);

            JSONArray notifications = new JSONArray();
            notifications.put("Dobrodosli na sajt zooloskog vrta \"Pandica\"!");

            JSONObject user1 = new JSONObject();
            user1.put("id", 0);
            user1.put("username", "panda");
            user1.put("password", "panda");
            user1.put("firstname", "Panda");
            user1.put("lastname", "Pandic");
            user1.put("phone", "065123482");
            user1.put("email", "panda@mail.com");
            user1.put("notifications", notifications);
            user1.put("type", "Posetilac");
            user1.put("status", "accepted");

            JSONObject user2 = new JSONObject();
            user2.put("id", 1);
            user2.put("username", "koala");
            user2.put("password", "koala");
            user2.put("firstname", "Koala");
            user2.put("lastname", "Koalovic");
            user2.put("phone", "060987612");
            user2.put("email", "koala@mail.com");
            user2.put("notifications", notifications);
            user2.put("type", "Posetilac");
            user2.put("status", "accepted");

            JSONArray users = new JSONArray();
            users.put(user1);
            users.put(user2);

            JSONArray comments = new JSONArray();

            sharedPref = getApplicationContext().getSharedPreferences(getApplicationContext().getString(R.string.local_storage), Context.MODE_PRIVATE);
            editor = sharedPref.edit();
            editor.putString("animals", animals.toString());
            editor.putString("events", events.toString());
            editor.putString("promoCodes", promoCodes.toString());
            editor.putString("promoPackages", promoPackages.toString());
            editor.putString("tickets", tickets.toString());
            editor.putString("users", users.toString());
            editor.putString("comments", comments.toString());
            editor.putString("loggedUser", "");
            editor.putString("likes", new JSONArray().toString());
            editor.apply();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}