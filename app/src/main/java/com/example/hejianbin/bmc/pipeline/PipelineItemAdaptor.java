package com.example.hejianbin.bmc.pipeline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.baidubce.services.media.model.PipelineStatus;
import com.example.hejianbin.bmc.R;

import java.util.List;

/**
 * Created by hejianbin on 5/28/15.
 */
public class PipelineItemAdaptor extends ArrayAdapter<PipelineStatus> {
    private int resourceId;

    public PipelineItemAdaptor(Context context, int resourceId, List<PipelineStatus> pipelineStatuses) {
        super(context, resourceId, pipelineStatuses);
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PipelineStatus pipelineStatus = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView sourceBucket = (TextView) view.findViewById(R.id.source_bucket);
        TextView targetBucket = (TextView) view.findViewById(R.id.target_bucket);
        TextView pipelineName = (TextView) view.findViewById(R.id.pipeline_name);
        TextView capacity = (TextView) view.findViewById(R.id.pipeline_capacity);

        sourceBucket.setText(pipelineStatus.getSourceBucket());
        targetBucket.setText(pipelineStatus.getTargetBucket());
        pipelineName.setText(pipelineStatus.getPipelineName());
        capacity.setText(pipelineStatus.getConfig().getCapacity().toString());

        return view;
    }
}
