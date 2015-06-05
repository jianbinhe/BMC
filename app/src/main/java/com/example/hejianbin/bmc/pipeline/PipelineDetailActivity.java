package com.example.hejianbin.bmc.pipeline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.baidubce.services.media.model.PipelineStatus;
import com.example.hejianbin.bmc.R;
import com.example.hejianbin.bmc.job.JobListFragment;
import com.example.hejianbin.bmc.setting.CurrentConf;

import java.util.List;


public class PipelineDetailActivity extends ActionBarActivity implements JobListFragment.OnFragmentInteractionListener {
    private PipelineStatus pipeline;

    public static void start(Context context, String pipelineName) {
        Intent intent = new Intent(context, PipelineDetailActivity.class);
        intent.putExtra("pipelineName", pipelineName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        pipeline = getPipeline(intent.getStringExtra("pipelineName"));

        setContentView(R.layout.activity_pipeline_detail);
        initPipelineHeader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pipeline_detail, menu);
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

    private PipelineStatus getPipeline(String pipelineName) {
        List<PipelineStatus> pipelines = CurrentConf.getMediaClient()
                .listPipelines().getPipelines();
        for (PipelineStatus pipelineStatus : pipelines) {
            if (pipelineStatus.getPipelineName().equals(pipelineName)) {
                pipeline = pipelineStatus;
                return pipeline;
            }
        }

        throw new RuntimeException("cannot find specific pipeline: " + pipelineName);
    }


    private void initPipelineHeader() {

        TextView name = (TextView) findViewById(R.id.name);
        TextView description = (TextView) findViewById(R.id.description);
        TextView runningJob = (TextView) findViewById(R.id.runningJob);
        TextView pendingJob = (TextView) findViewById(R.id.pendingJob);
        TextView failedJob = (TextView) findViewById(R.id.failedJob);
        TextView successJob = (TextView) findViewById(R.id.successJob);

        name.setText(pipeline.getPipelineName());
        description.setText(pipeline.getDescription());
        runningJob.setText(pipeline.getJobStatus().getRunning().toString());
        pendingJob.setText(pipeline.getJobStatus().getPending().toString());
        failedJob.setText(pipeline.getJobStatus().getFailed().toString());
        successJob.setText(pipeline.getJobStatus().getSuccess().toString());
    }

    @Override
    public void onFragmentInteraction(String id) {

    }

    @Override
    public String getPipelineName() {
        return pipeline.getPipelineName();
    }
}
