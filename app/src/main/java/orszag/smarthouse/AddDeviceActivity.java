package orszag.smarthouse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddDeviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
