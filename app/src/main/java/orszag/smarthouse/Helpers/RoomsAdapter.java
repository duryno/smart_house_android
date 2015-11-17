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

import orszag.smarthouse.R;
import orszag.smarthouse.RoomActivity;
import orszag.smarthouse.objectClasses.Room;

/**
 * Created by Juraj on 16.11.2015.
 */
public class RoomsAdapter extends ArrayAdapter<Room> {

    Context mContext;
    ArrayList<Room> mRooms;

    Typeface light;

    public RoomsAdapter(Context context, ArrayList<Room> rooms) {
        super(context, -1, rooms);
        mContext = context;
        mRooms = rooms;

        light = Typeface.createFromAsset(mContext.getAssets(), "fonts/light.ttf");
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.room_item, parent, false);
        TextView tvName = (TextView) rowView.findViewById(R.id.roomName);
        tvName.setText(mRooms.get(position).getName());
        tvName.setTypeface(light);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RoomActivity.class);
                intent.setAction(RoomActivity.SHOW_DEVICES);
                intent.putExtra(RoomActivity.ROOM_ID, mRooms.get(position).getId());
                intent.putExtra(RoomActivity.ROOM_NAME, mRooms.get(position).getName());
                mContext.startActivity(intent);
            }
        });

        return rowView;
    }

}
