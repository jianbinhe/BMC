package com.bmc.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;


import com.bmc.R;

import java.util.List;

/**
 * Created by hejianbin on 6/2/15.
 */
public class ConfItemAdaptor extends ArrayAdapter<ConfItem> {
    private int resourceId;

    public ConfItemAdaptor(Context context, int resource, List<ConfItem> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final ConfItem confItem = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView env = (TextView) view.findViewById(R.id.conf_env);
        TextView description = (TextView) view.findViewById(R.id.conf_description);
        TextView accessKey = (TextView) view.findViewById(R.id.access_key);
        final Switch active = (Switch) view.findViewById(R.id.conf_switcher);

        env.setText(confItem.getEnv());
        description.setText(confItem.getDescription());
        accessKey.setText(confItem.getAccessKey());
        active.setChecked(confItem.getActive());

        active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    for (int i = 0; i < getCount(); ++i) {
                        ConfItem item = getItem(i);
                        item.setActive(false);
                    }
                    confItem.setActive(checked);
                    CurrentConf.setConfItem(confItem);
                }
                notifyDataSetChanged();
            }
        });
        return view;
    }

}
