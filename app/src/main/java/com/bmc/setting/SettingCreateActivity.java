package com.bmc.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bmc.R;

import java.util.Arrays;
import java.util.List;


public class SettingCreateActivity extends AppCompatActivity {
    private Spinner envSpinner;

    private static final List<String> envList = Arrays.asList("qa_sandbox", "online_bj");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_setting);
        envSpinner = (Spinner) findViewById(R.id.env_spinner);
        envSpinner.setAdapter(new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, envList));

        final EditText accessKey = (EditText) findViewById(R.id.access_key);
        final EditText secretKey = (EditText) findViewById(R.id.secret_key);
        final EditText description = (EditText) findViewById(R.id.description);

        Button save = (Button) findViewById(R.id.setting_save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder sb = new StringBuilder();
                if (accessKey.getText().toString().isEmpty()) {
                    sb.append("access key不能为空\n");
                }
                if (secretKey.getText().toString().isEmpty()) {
                    sb.append("secret key不能为空\n");
                }
                if (description.getText().toString().isEmpty()) {
                    sb.append("description不能为空");
                }
                if (!sb.toString().isEmpty()) {
                    Toast.makeText(view.getContext(), sb.toString(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("access_key", accessKey.getText().toString());
                intent.putExtra("secret_key", secretKey.getText().toString());
                intent.putExtra("description", description.getText().toString());
                intent.putExtra("env", envList.get(envSpinner.getSelectedItemPosition()));

                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
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
