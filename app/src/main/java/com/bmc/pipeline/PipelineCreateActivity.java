package com.bmc.pipeline;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidubce.BceServiceException;
import com.baidubce.services.bos.model.BucketSummary;
import com.bmc.R;
import com.bmc.setting.CurrentConf;

import java.util.ArrayList;
import java.util.List;

public class PipelineCreateActivity extends ActionBarActivity {

    private List<String> buckets = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pipeline_create);

        final TextView pipelineName = (TextView) findViewById(R.id.pipeline_name);
        final TextView description = (TextView) findViewById(R.id.description);
        final TextView capacity = (TextView) findViewById(R.id.pipeline_capacity);

        loadBuckets();
        final Spinner sourceBucket = (Spinner) findViewById(R.id.source_bucket);
        final Spinner targetBucket = (Spinner) findViewById(R.id.target_bucket);

        sourceBucket.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, buckets));
        targetBucket.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, buckets));

        final Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder sb = new StringBuilder();
                if (pipelineName.getText().toString().isEmpty()) {
                    sb.append("pipeline名称不能为空");
                }
                if (description.getText().toString().isEmpty()) {
                    sb.append("描述不能为空");
                }
                if (capacity.getText().toString().isEmpty()) {
                    sb.append("队列容量不能为空");
                }
                if (!sb.toString().isEmpty()) {
                    Toast.makeText(view.getContext(), sb.toString(), Toast.LENGTH_SHORT).show();
                }

                boolean success = false;
                try {
                    save.setText("提交中");
                    CurrentConf.getMediaClient().createPipeline(
                            pipelineName.getText().toString(),
                            description.getText().toString(),
                            buckets.get(sourceBucket.getSelectedItemPosition()),
                            buckets.get(targetBucket.getSelectedItemPosition()),
                            Integer.valueOf(capacity.getText().toString()));
                    success = true;
                } catch (BceServiceException ex) {
                    Toast.makeText(view.getContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                } finally {
                    save.setText("保存");
                }
                if (success) {
                    finish();
                }
            }
        });

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pipeline_create, menu);
        return true;
    }

    private void loadBuckets() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("加载数据");
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            List<BucketSummary> bucketSummaries = CurrentConf.getBosClient().listBuckets().getBuckets();
            for (BucketSummary summary : bucketSummaries) {
                buckets.add(summary.getName());
            }
        } catch (Exception ex) {
            Toast.makeText(this, "加载失败", Toast.LENGTH_SHORT).show();
        } finally {
            dialog.dismiss();
        }
    }

}
