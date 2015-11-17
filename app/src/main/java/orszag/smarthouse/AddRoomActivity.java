package orszag.smarthouse;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import orszag.smarthouse.Helpers.RoomsAdapter;
import orszag.smarthouse.objectClasses.Device;
import orszag.smarthouse.objectClasses.Environment;
import orszag.smarthouse.objectClasses.House;
import orszag.smarthouse.objectClasses.Room;

public class AddRoomActivity extends AppCompatActivity {

    private Button btnCreateRoom;
    private TextView error;
    private Room room;

    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        error = (TextView) findViewById(R.id.error);
        btnCreateRoom = (Button) findViewById(R.id.btnCreateRoom);

        btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRoom();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addRoom() {
        new CreateRoom().execute();
    }

    public class CreateRoom extends AsyncTask<Void, Void, Room> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Room doInBackground(Void... arg0) {
            StringBuilder out;

            try {
                Gson gson = new Gson();
                String path = "http://thesmarthouse.azurewebsites.net/restAPI/Room/createRoom/1";
//                URL url = new URL("http://thesmarthouse.azurewebsites.net/restAPI/House/getHouse/1");

                room = new Room();
                room.setId(0);
                room.setDevices(new ArrayList<Device>());
                room.setName("JurajsTestRoom");

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(path);
                HttpResponse response;
                JSONObject jsonObject = new JSONObject(gson.toJson(room));
                StringEntity json = new StringEntity(jsonObject.toString());
                json.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httpPost.setEntity(json);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

//                ResponseHandler responseHandler = new BasicResponseHandler();
//                response = (HttpResponse) httpClient.execute(httpPost, responseHandler);
                response = httpClient.execute(httpPost);

//                text = response.getEntity().getContent();

//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

//                final InputStream in = new BufferedInputStream(response.getStatusLine().getStatusCode());
//                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//                out = new StringBuilder();
//                String line = "";
//                while ((line = reader.readLine()) != null) {
//                    out.append(line);
//                }

                text = String.valueOf(response.getStatusLine().getStatusCode());

//                JSONObject obj = new JSONObject(out.toString());
//                JSONObject houseJson = obj.getJSONObject("House");
//                room = gson.fromJson(houseJson.toString(), Room.class);

            } catch (final IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        error.setText(e.toString());
                    }
                });
            } catch (final JSONException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        error.setText(e.toString());
                    }
                });
            }
            return room;
        }

        @Override
        protected void onPostExecute(Room room) {
//            startActivity(new Intent(AddRoomActivity.this, MainActivity.class));
            Toast.makeText(AddRoomActivity.this, text, Toast.LENGTH_LONG).show();
        }

    }

}
