package orszag.smarthouse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import orszag.smarthouse.objectClasses.Device;

public class DeviceActivity extends AppCompatActivity {

    public static String SHOW_STATUS = "show_status";
    public static String DEVICE_ID = "device_id";
    public static String DEVICE_NAME = "device_name";

    private int deviceID;

    TextView tvStatus;
    Switch aSwitch;
    ProgressDialog progress;
    private Device device;

    String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        tvStatus = (TextView) findViewById(R.id.status);
        progress = new ProgressDialog(DeviceActivity.this);

        if(getIntent() != null) {
            if(getIntent().getAction() != null && getIntent().getAction().equals(SHOW_STATUS)) {
                deviceID = getIntent().getIntExtra(DEVICE_ID, 0);
                getSupportActionBar().setTitle(getIntent().getStringExtra(DEVICE_NAME));
                showStatus();
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(getIntent().getAction() != null && getIntent().getAction().equals(SHOW_STATUS)) {
            deviceID = getIntent().getIntExtra(DEVICE_ID, 0);
            showStatus();
        }
    }

    private void showStatus() {
        new GetDevice().execute(deviceID);
    }

    private void updateDevice() {
        if(device.getStatus().equals("ON"))
            new UpdateDevice().execute(String.valueOf(deviceID), "OFF");
        else if(device.getStatus().equals("OFF"))
            new UpdateDevice().execute(String.valueOf(deviceID), "ON");
    }

    public class GetDevice extends AsyncTask<Integer, Void, Device> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setTitle("Changing status");
            progress.setMessage("Wait while executing...");
            progress.show();
        }

        @Override
        protected Device doInBackground(Integer... deviceId) {
            StringBuilder out;

            try {
                Gson gson = new Gson();
                String path = "http://thesmarthouse.azurewebsites.net/restAPI/Device/" + deviceId[0] + "/1/bded74425176f692690a66bc3fcaf1ac";
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
                json = obj.toString();
                JSONObject deviceJson = obj.getJSONObject("Device");
                device = gson.fromJson(deviceJson.toString(), Device.class);

            } catch (final ClientProtocolException e) {
                e.printStackTrace();
            } catch (final IOException e) {
                e.printStackTrace();
            } catch (final JSONException e) {
                e.printStackTrace();
            }
            return device;
        }

        @Override
        protected void onPostExecute(Device device) {
            Log.i("JSON", json.toString());
            if(device != null && device.getStatus() != null) {
                tvStatus.setText(device.getName() + " is " + device.getStatus());

                aSwitch = (Switch) findViewById(R.id.switch1);
                aSwitch.setChecked(device.getStatus() != null && device.getStatus().equals("ON") ? true : false);
                aSwitch.setVisibility(View.VISIBLE);

                aSwitch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateDevice();
                    }
                });

            } else {
                showStatus();
                tvStatus.setText("Error occurred while retrieving the device status, connecting again!");
            }
            progress.dismiss();
        }

    }

    public class UpdateDevice extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setTitle("Changing status");
            progress.setMessage("Wait while executing...");
            progress.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                String path = "http://thesmarthouse.azurewebsites.net/restAPI/Device/" + params[0] + "/" + params[1] + "/1/bded74425176f692690a66bc3fcaf1ac";
                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpPut httpPut = new HttpPut(path);
                HttpResponse response = httpclient.execute(httpPut);

            }
        catch (final IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvStatus.setText("Error " + e.toString());
                    }
                });
            }
            return true; // check status
        }

        @Override
        protected void onPostExecute(Boolean success) {
            showStatus();
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
            showStatus();
        return super.onOptionsItemSelected(item);
    }

}
