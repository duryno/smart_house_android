package orszag.smarthouse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

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

import orszag.smarthouse.Helpers.DevicesAdapter;
import orszag.smarthouse.objectClasses.Device;
import orszag.smarthouse.objectClasses.Room;

public class RoomActivity extends AppCompatActivity {

    public static final String SHOW_DEVICES = "show_devices";
    public static final String ROOM_ID = "room_id";
    public static final String ROOM_NAME = "room_name";

    private int roomID;

    private ListView lvDevices;
    private TextView error;
    private TextView temperature;
    private FloatingActionButton addDevice;
    private Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        error = (TextView) findViewById(R.id.deviceName);
        temperature = (TextView) findViewById(R.id.temperature);
        lvDevices = (ListView) findViewById(R.id.devices);
        lvDevices.addFooterView(new View(RoomActivity.this), null, true);

        if(getIntent() != null) {
            if(getIntent().getAction() != null && getIntent().getAction().equals(SHOW_DEVICES)) {
                roomID = getIntent().getIntExtra(ROOM_ID, 0);
                getSupportActionBar().setTitle(getIntent().getStringExtra(ROOM_NAME));
                showDevices();
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(getIntent().getAction() != null && getIntent().getAction().equals(SHOW_DEVICES)) {
            roomID = getIntent().getIntExtra(ROOM_ID, 0);
            showDevices();
        }
    }

    private void showDevices() {
        new GetRoom().execute(roomID);
    }

    public class GetRoom extends AsyncTask<Integer, Void, Room> {

        ProgressDialog progress = new ProgressDialog(RoomActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.show();
        }

        @Override
        protected Room doInBackground(Integer... roomId) {
            StringBuilder out;

            try {
                Gson gson = new Gson();
                String path = "http://thesmarthouse.azurewebsites.net/restAPI/Room/" + roomId[0];
                URL url = new URL(path);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                final InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                out = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    out.append(line);
                }

                JSONObject obj = new JSONObject(out.toString());
                JSONObject roomJson = obj.getJSONObject("Room");
                room = gson.fromJson(roomJson.toString(), Room.class);

            } catch (final ClientProtocolException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        error.setText("error " + e.toString());
                        error.setVisibility(View.VISIBLE);
                    }
                });
            } catch (final IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        error.setText("error " + e.toString());
                        error.setVisibility(View.VISIBLE);
                    }
                });
            } catch (final JSONException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        error.setText("error " + e.toString());
                        error.setVisibility(View.VISIBLE);
                    }
                });
            }
            return room;
        }

        @Override
        protected void onPostExecute(Room room) {
            if(room != null) {
                ArrayList<Device> devices = (ArrayList) room.getDevices();
                DevicesAdapter adapter = new DevicesAdapter(RoomActivity.this, devices);
                lvDevices.setAdapter(adapter);

                temperature.setText("Temperature is " + String.valueOf(room.getTemperature()) + "°C");

                addDevice = (FloatingActionButton) findViewById(R.id.fabAddDevice);

                addDevice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(RoomActivity.this, AddDeviceActivity.class);
                        intent.putExtra(ROOM_ID, roomID);
                        startActivity(intent);
                    }
                });
                error.setText("");
            } else {
                showDevices();
                error.setText("Error occurred while retrieving room, connecting again");
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
            showDevices();
        return super.onOptionsItemSelected(item);
    }

}
