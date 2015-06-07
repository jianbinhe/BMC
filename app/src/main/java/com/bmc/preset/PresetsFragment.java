package com.bmc.preset;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidubce.services.media.model.GetPresetResponse;
import com.bmc.R;
import com.bmc.common.WithCreateNewItem;
import com.bmc.pipeline.PipelineCreateActivity;
import com.bmc.setting.CurrentConf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class PresetsFragment extends Fragment implements
        AbsListView.OnItemClickListener, WithCreateNewItem {


    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    private List<GetPresetResponse> presets = new ArrayList<>();

    private Date updateTime;

    private String confVersion = "";

    private ProgressBar progressBar;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PresetsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mAdapter = new PresetItemAdaptor(getActivity(), R.layout.preset_list_item, presets);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preset, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);


        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        Log.d(this.getClass().toString(), "onCreateView");

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            Log.d(getClass().toString(), "on attach");
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(presets.get(position));
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Override
    public void onCreateNewItem() {
        Intent intent = new Intent(getActivity(), PipelineCreateActivity.class);
        startActivityForResult(intent, R.integer.create_new_preset);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(GetPresetResponse preset);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            Date current = new Date();
            if (updateTime == null || (!confVersion.equals(CurrentConf.version()))
                    || (current.getTime() - updateTime.getTime()) > 1000 * 60) {
                new GetPresetsTask().execute();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            new GetPresetsTask().execute();
        }
    }

    class GetPresetsTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            List<GetPresetResponse> presetList = CurrentConf.getMediaClient().listPresets().getPresets();
            presets.clear();
            presets.addAll(presetList);
            confVersion = CurrentConf.version();
            updateTime = new Date();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            progressBar.setVisibility(View.INVISIBLE);
            if (!success) {
                Toast.makeText(getActivity(), "数据更新失败", Toast.LENGTH_SHORT).show();
            } else {
                ((ArrayAdapter) mAdapter).notifyDataSetChanged();
                Toast.makeText(getActivity(), "数据刷新成功", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
