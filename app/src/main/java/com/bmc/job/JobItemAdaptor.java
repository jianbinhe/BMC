package com.bmc.job;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.baidubce.services.media.model.Job;
import com.bmc.R;
import com.bmc.common.Constants;

import java.util.List;

/**
 * Created by hejianbin on 5/29/15.
 */
public class JobItemAdaptor extends ArrayAdapter<Job> {
    private int resourceId;

    public JobItemAdaptor(Context context, int resourceId, List<Job> jobs) {
        super(context, resourceId, jobs);
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Job job = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView sourceKey = (TextView) view.findViewById(R.id.source_key);
        TextView jobId = (TextView) view.findViewById(R.id.jobId);
        TextView createTime = (TextView) view.findViewById(R.id.create_time);
        TextView endTime = (TextView) view.findViewById(R.id.end_time);
        TextView jobStatus = (TextView) view.findViewById(R.id.job_status);

        sourceKey.setText(job.getSource().getSourceKey());
        jobId.setText(job.getJobId());

        if (job.getStartTime() != null) {
            createTime.setText(Constants.dateFormat.format(job.getStartTime()));
        }

        if (job.getEndTime() != null) {
            endTime.setText(Constants.dateFormat.format(job.getEndTime()));
        }

        jobStatus.setText(job.getJobStatus());
        switch (job.getJobStatus()) {
            case "SUCCESS":
            case "RUNNING":
                jobStatus.setTextColor(Color.GREEN);
                break;
            case "PENDING":
                jobStatus.setTextColor(Color.YELLOW);
                break;
            case "FAILED":
                jobStatus.setTextColor(Color.RED);
                break;
        }
        return view;
    }
}
