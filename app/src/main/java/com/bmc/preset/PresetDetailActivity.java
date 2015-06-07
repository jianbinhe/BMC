package com.bmc.preset;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.baidubce.services.media.model.GetPresetResponse;
import com.bmc.R;
import com.bmc.setting.CurrentConf;

public class PresetDetailActivity extends ActionBarActivity
        implements OnPresetFragmentInteractionListener {


    private GetPresetResponse preset;

    private boolean editable = false;

    public static void start(Context context, String presetName) {
        Intent intent = new Intent(context, PresetDetailActivity.class);
        intent.putExtra("presetName", presetName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String presetName = intent.getStringExtra("presetName");
        editable = intent.getBooleanExtra("editable", false);

        if (presetName == null) {
            preset = new GetPresetResponse();
        } else {
            preset = CurrentConf.getMediaClient().getPreset(presetName);
            if (editable && presetName.startsWith("bce.")) {
                // user copy system preset, remove system prefix
                preset.setPresetName(presetName.substring(4));
            }
        }

        setContentView(R.layout.activity_preset_detail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preset_detail, menu);
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

    @Override
    public GetPresetResponse getPreset() {
        return preset;
    }

    @Override
    public Boolean editable() {
        return editable;
    }
}
