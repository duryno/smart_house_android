package orszag.smarthouse.Helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import orszag.smarthouse.DeviceActivity;
import orszag.smarthouse.R;
import orszag.smarthouse.objectClasses.Device;

/**
 * Created by Juraj on 16.11.2015.
 */
public class DevicesAdapter extends ArrayAdapter<Device> {

    Context mContext;
    ArrayList<Device> mDevices;

    Typeface light;

    public DevicesAdapter(Context context, ArrayList<Device> devices) {
        super(context, -1, devices);
        mContext = context;
        mDevices = devices;

        light = Typeface.createFromAsset(mContext.getAssets(), "fonts/light.ttf");
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.device_item, parent, false);
        TextView tvName = (TextView) rowView.findViewById(R.id.deviceName);
        tvName.setText(mDevices.get(position).getName());
        tvName.setTypeface(light);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DeviceActivity.class);
                intent.setAction(DeviceActivity.SHOW_STATUS);
                intent.putExtra(DeviceActivity.DEVICE_ID, mDevices.get(position).getId());
                intent.putExtra(DeviceActivity.DEVICE_NAME, mDevices.get(position).getName());
                mContext.startActivity(intent);
            }
        });

        return rowView;
    }


}
