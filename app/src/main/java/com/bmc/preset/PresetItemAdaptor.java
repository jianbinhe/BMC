package com.bmc.preset;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.baidubce.services.media.model.GetPresetResponse;
import com.bmc.BMCMainActivity;
import com.bmc.R;

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
        final GetPresetResponse preset = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView name = (TextView) view.findViewById(R.id.preset_name);
        TextView description = (TextView) view.findViewById(R.id.preset_description);

        name.setText(preset.getPresetName());
        description.setText(preset.getDescription());

        Button copy = (Button) view.findViewById(R.id.copy);
        copy.setFocusable(false);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), PresetDetailActivity.class);
                intent.putExtra("editable", true);
                intent.putExtra("presetName", preset.getPresetName());
                ((BMCMainActivity) getContext()).getFragment(BMCMainActivity.PRESET).startActivityForResult(intent, R.integer.create_new_preset);
            }
        });
        if (preset.getTransmux() != null && preset.getTransmux()) {
            copy.setEnabled(false);
        }

        return view;
    }
}
