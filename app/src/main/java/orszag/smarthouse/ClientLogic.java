package orszag.smarthouse;

import android.os.AsyncTask;

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

import orszag.smarthouse.objectClasses.Device;
import orszag.smarthouse.objectClasses.House;
import orszag.smarthouse.objectClasses.Room;

/**
 * Created by Juraj on 16.11.2015.
 */
public abstract class ClientLogic {

    private static boolean done = false;

    private static House house;
    private static Room room;
    private static Device device;

    public static House getHouse(int houseId) {
        new GetHouse().execute();
        return house;
    }

    public static Room getRoom(int roomId) {
        new GetRoom().execute();
        return room;
    }

    public static Device getDevice(int deviceId) {
        new GetDevice().execute();
        return device;
    }

    public static boolean updateDevice(int deviceId) {
        new UpdateDevice().execute();
        return true;//check the status
    }

    public static class GetHouse extends AsyncTask<Void, Void, House> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
//            ArrayList<Room> rooms = (ArrayList) house.getRooms();
//            tvHouseId.setText(rooms.get(0).getName());
        }

    }

    public static class GetRoom extends AsyncTask<Integer, Void, Room> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Room doInBackground(Integer... roomId) {
            StringBuilder out;

            try {
                Gson gson = new Gson();
                URL url = new URL("http://thesmarthouse.azurewebsites.net/restAPI/thesmarthouse.azurewebsites.net/restAPI/Room/" + roomId + "/1");
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
            } catch (final IOException e) {
                e.printStackTrace();
            } catch (final JSONException e) {
                e.printStackTrace();
            }
            return room;
        }

        @Override
        protected void onPostExecute(Room room) {
            done = true;
//            ArrayList<Room> rooms = (ArrayList) house.getRooms();
//            tvHouseId.setText(rooms.get(0).getName());
        }

    }

    public static class GetDevice extends AsyncTask<Integer, Void, Device> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Device doInBackground(Integer... deviceId) {
            StringBuilder out;

            try {
                Gson gson = new Gson();
                URL url = new URL("http://thesmarthouse.azurewebsites.net/restAPI/Device/" + deviceId);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                final InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                out = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    out.append(line);
                }

                JSONObject obj = new JSONObject(out.toString());
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
//            ArrayList<Room> rooms = (ArrayList) house.getRooms();
//            tvHouseId.setText(rooms.get(0).getName());
        }

    }

    public static class UpdateDevice extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            StringBuilder out;

            try {
                Gson gson = new Gson();
                URL url = new URL("http://thesmarthouse.azurewebsites.net/restAPI/Device/" + params[0] + "/" + params[1]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("PUT");

                final InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                out = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    out.append(line);
                }

                JSONObject obj = new JSONObject(out.toString());
                JSONObject deviceJson = obj.getJSONObject("Device");
                device = gson.fromJson(deviceJson.toString(), Device.class);

            } catch (final ClientProtocolException e) {
                e.printStackTrace();
            } catch (final IOException e) {
                e.printStackTrace();
            } catch (final JSONException e) {
                e.printStackTrace();
            }
            return true; // check status
        }

        @Override
        protected void onPostExecute(Boolean success) {
//            ArrayList<Room> rooms = (ArrayList) house.getRooms();
//            tvHouseId.setText(rooms.get(0).getName());
        }

    }

}
