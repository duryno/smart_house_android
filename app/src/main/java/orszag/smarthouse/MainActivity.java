package orszag.smarthouse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import orszag.smarthouse.Helpers.RoomsAdapter;
import orszag.smarthouse.objectClasses.House;
import orszag.smarthouse.objectClasses.Room;

public class MainActivity extends AppCompatActivity {

    public static final String HOUSE_ID = "house_id";

    TextView tvHouseId;
    ListView lvRooms;
    FloatingActionButton addRoom;
    StringBuilder out;
    House house;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        tvHouseId = (TextView) findViewById(R.id.houseID);
        lvRooms = (ListView) findViewById(R.id.rooms);
        lvRooms.addFooterView(new View(MainActivity.this), null, true);

        showRooms();

        getSupportActionBar().setTitle("Rooms");
    }

    private void showRooms() {
        new GetHouse().execute();
    }

    public class GetHouse extends AsyncTask<Void, Void, House> {

        ProgressDialog progress = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.show();
        }

        @Override
        protected House doInBackground(Void... arg0) {
            StringBuilder out;

            try {
                Gson gson = new Gson();
                URL url = new URL("http://thesmarthouse.azurewebsites.net/restAPI/House/getHouse/1");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                final InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                out = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    out.append(line);
                }

                JSONObject obj = new JSONObject(out.toString());
                JSONObject houseJson = obj.getJSONObject("House");
                house = gson.fromJson(houseJson.toString(), House.class);

            } catch (final ClientProtocolException e) {
                e.printStackTrace();
            } catch (final IOException e) {
                e.printStackTrace();
            } catch (final JSONException e) {
                e.printStackTrace();
            }
            return house;
        }

        @Override
        protected void onPostExecute(House house) {
            if(house != null) {
                ArrayList<Room> rooms = (ArrayList) house.getRooms();
                RoomsAdapter adapter = new RoomsAdapter(MainActivity.this, rooms);
                lvRooms.setAdapter(adapter);

                addRoom = (FloatingActionButton) findViewById(R.id.fabAddDevice);

                addRoom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, AddRoomActivity.class);
                        intent.putExtra(HOUSE_ID, 1);
                        startActivity(intent);
                    }
                });
            } else {
                showRooms();
                Snackbar.make(lvRooms, "Error occurred, trying again!", Snackbar.LENGTH_LONG).show();
            }
            progress.dismiss();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.refresh)
            showRooms();

        return super.onOptionsItemSelected(item);
    }

}
