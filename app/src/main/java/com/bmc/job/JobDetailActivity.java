package com.bmc.job;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.baidubce.services.media.model.GetJobResponse;
import com.bmc.R;
import com.bmc.common.Constants;
import com.bmc.preset.PresetDetailActivity;
import com.bmc.setting.CurrentConf;

public class JobDetailActivity extends Activity {

    private GetJobResponse job;

    public static void start(Context context, String jobId) {
        Intent intent = new Intent(context, JobDetailActivity.class);
        intent.putExtra("jobId", jobId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        Intent intent = getIntent();
        String id = intent.getStringExtra("jobId");
        job = CurrentConf.getMediaClient().getJob(id);

        TextView jobId = (TextView) findViewById(R.id.job_id);
        TextView pipelineName = (TextView) findViewById(R.id.pipeline_name);
        final TextView presetName = (TextView) findViewById(R.id.preset_name);
        TextView jobStatus = (TextView) findViewById(R.id.job_status);
        TextView message = (TextView) findViewById(R.id.message);
        TextView targetKey = (TextView) findViewById(R.id.target_key);
//        TextView createTime = (TextView) findViewById(R.id.create_time);
        TextView startTime = (TextView) findViewById(R.id.start_time);
        TextView endTime = (TextView) findViewById(R.id.end_time);
        View view = findViewById(R.id.error);

        jobId.setText(job.getJobId());
        pipelineName.setText(job.getPipelineName());

        presetName.setText(job.getTarget().getPresetName());
        presetName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PresetDetailActivity.start(view.getContext(), job.getTarget().getPresetName());
            }
        });
        jobStatus.setText(job.getJobStatus());
        targetKey.setText(job.getTarget().getTargetKey());
        // TODO set job createTime
        //createTime.setText();
//        createTime.setVisibility(View.GONE);

        if (job.getStartTime() == null) {
            startTime.setText("未开始");
        } else {
            startTime.setText(Constants.dateFormat.format(job.getStartTime()));
        }

        endTime.setVisibility(View.GONE);
        if (job.getEndTime() == null) {
            endTime.setText("未结束");
        } else {
            endTime.setText(Constants.dateFormat.format(job.getEndTime()));
        }

        if (job.getJobStatus().equals("FAILED")) {
            // TODO show failed message
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.GONE);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_job_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
