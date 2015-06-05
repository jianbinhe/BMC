package com.example.hejianbin.bmc.preset;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.baidubce.services.media.model.GetPresetResponse;
import com.example.hejianbin.bmc.R;

import java.util.List;

/**
 * Created by hejianbin on 6/1/15.
 */
public class PresetItemAdaptor extends ArrayAdapter<GetPresetResponse> {
    private int resourceId;

    public PresetItemAdaptor(Context context, int resource, List<GetPresetResponse> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GetPresetResponse preset = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView name = (TextView) view.findViewById(R.id.preset_name);
        TextView description = (TextView) view.findViewById(R.id.preset_description);

        name.setText(preset.getPresetName());
        description.setText(preset.getDescription());

        return view;
    }
}
