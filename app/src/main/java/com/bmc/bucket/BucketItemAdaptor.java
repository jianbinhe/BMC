package com.bmc.bucket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.baidubce.services.bos.model.BucketSummary;
import com.bmc.R;
import com.bmc.common.Constants;

import java.util.List;

/**
 * Created by hejianbin on 6/6/15.
 */
public class BucketItemAdaptor extends ArrayAdapter<BucketSummary> {
    private int resourceId;

    public BucketItemAdaptor(Context context, int resource, List<BucketSummary> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BucketSummary bucket = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView bucket_name = (TextView) view.findViewById(R.id.bucket_name);
        TextView create_name = (TextView) view.findViewById(R.id.create_time);

        bucket_name.setText(bucket.getName());
        create_name.setText(Constants.dateFormat.format(bucket.getCreationDate()));

        return view;
    }
}
