package com.bmc.pipeline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baidubce.services.media.model.PipelineStatus;
import com.bmc.R;

import java.util.List;


public class PipelineListActivity extends Activity {
    private List<PipelineStatus> pipelines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_pipeline_list);
        PipelineItemAdaptor adaptor = new PipelineItemAdaptor(PipelineListActivity.this,
                R.layout.pipeline_list_item, getPipelines());
        ListView listView = (ListView) findViewById(R.id.pipelineListView);
        listView.setAdapter(adaptor);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                PipelineStatus pipelineStatus = pipelines.get(position);
                Intent intent = new Intent(PipelineListActivity.this, PipelineDetailActivity.class);
                intent.putExtra("pipelineName", pipelineStatus.getPipelineName());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pipeline_list, menu);
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


    private List<PipelineStatus> getPipelines() {
//        MediaClient mediaClient = new MediaClient(SettingCreateActivity.getMediaConfig());
//        pipelines = mediaClient.listPipelines().getPipelines();
        return pipelines;
    }
}
