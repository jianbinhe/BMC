package com.bmc;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.baidubce.services.media.model.GetPresetResponse;
import com.bmc.pipeline.PipelineDetailActivity;
import com.bmc.pipeline.PipelinesFragment;
import com.bmc.preset.PresetDetailActivity;
import com.bmc.preset.PresetsFragment;
import com.bmc.setting.ConfItemFragment;
import com.bmc.setting.CurrentConf;


public class BMCMainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        PresetsFragment.OnFragmentInteractionListener,
        PipelinesFragment.OnFragmentInteractionListener,
        ConfItemFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private int currentFragmentIndex = -1;

    private final int PRESET = 0;
    private final int PIPELINE = 1;
    private final int BUCKET = 2;
    private final int SETTING = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmcmain);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);


        if (CurrentConf.getMediaClient() == null) {
            transaction.show(getFragment(SETTING))
                    .hide(getFragment(PRESET))
                    .hide(getFragment(BUCKET))
                    .hide(getFragment(PIPELINE));
            currentFragmentIndex = SETTING;
        } else {
            onNavigationDrawerItemSelected(PIPELINE);
            transaction.hide(getFragment(SETTING))
                    .hide(getFragment(PRESET))
                    .hide(getFragment(BUCKET))
                    .show(getFragment(PIPELINE));
            currentFragmentIndex = PIPELINE;
        }

        transaction.commit();

        getFragment(currentFragmentIndex).onHiddenChanged(false);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        mNavigationDrawerFragment.selectItem(currentFragmentIndex);
    }

    @Override
    protected void onStart() {
        Log.d(getClass().toString(), "on start");
        super.onStart();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (position != currentFragmentIndex) {
            android.app.Fragment replaceBy = getFragment(position);
            FragmentTransaction transaction = getFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            if (currentFragmentIndex != -1) {
                transaction.hide(getFragment(currentFragmentIndex));
            }
            transaction.show(replaceBy)
                    .commit();
            currentFragmentIndex = position;
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_preset);
                break;
            case 2:
                mTitle = getString(R.string.title_pipeline);
                break;
            case 3:
                mTitle = getString(R.string.title_bucket);
                break;
            case 4:
                mTitle = getString(R.string.title_setting);
        }
    }

    private android.app.Fragment getFragment(int index) {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        switch (index) {
            case PRESET:
                return fragmentManager.findFragmentById(R.id.preset_fragment);
            case PIPELINE:
                return fragmentManager.findFragmentById(R.id.pipeline_fragment);
            case BUCKET:
                return fragmentManager.findFragmentById(R.id.bucket_fragment);
            case SETTING:
                return fragmentManager.findFragmentById(R.id.setting_fragment);

        }
        throw new RuntimeException("cannot find fragment for index:" + index);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.bmcmain, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_create) {
            switch (currentFragmentIndex) {
                case SETTING:
                    android.app.Fragment fragment = getFragment(SETTING);
                    ((ConfItemFragment) fragment).onCreateNewItem();
                    break;
                case PRESET:
                    break;
                case PIPELINE:
                    break;
                case BUCKET:
                    break;
            }
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(this.getClass().toString(), "hahahaha");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFragmentInteraction(GetPresetResponse preset) {
        Intent intent = new Intent(this, PresetDetailActivity.class);
        intent.putExtra("presetName", preset.getPresetName());
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(String pipelineName) {
        Intent intent = new Intent(this, PipelineDetailActivity.class);
        intent.putExtra("pipelineName", pipelineName);
        startActivity(intent);
    }

}
