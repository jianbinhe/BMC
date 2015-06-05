package com.example.hejianbin.bmc.job;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.baidubce.services.media.MediaClient;
import com.baidubce.services.media.model.Job;
import com.example.hejianbin.bmc.R;
import com.example.hejianbin.bmc.setting.CurrentConf;
import com.example.hejianbin.bmc.setting.SettingCreateActivity;

import java.util.List;


public class JobListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);
        String pipelineName = getIntent().getStringExtra("pipelineName");
        JobItemAdaptor adaptor = new JobItemAdaptor(JobListActivity.this,
                R.layout.job_list_item, getJobs(pipelineName));
        ListView listView = (ListView) findViewById(R.id.jobListView);
        listView.setAdapter(adaptor);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_job_list, menu);
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

    private List<Job> getJobs(String pipelineName) {
        return CurrentConf.getMediaClient()
                .listJobs(pipelineName).getJobs();
    }
}
