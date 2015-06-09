package com.bmc.pipeline;

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

import com.baidubce.services.media.model.PipelineStatus;
import com.bmc.R;
import com.bmc.common.WithCreateNewItem;
import com.bmc.setting.CurrentConf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
public class PipelinesFragment extends Fragment implements
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

    private List<PipelineStatus> pipelines = new ArrayList<PipelineStatus>();

    private Date updateTime;

    private String confVersion = "";

    private ProgressBar progressBar;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PipelinesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mAdapter = new PipelineItemAdaptor(getActivity(), R.layout.pipeline_list_item, pipelines);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pipelines, container, false);

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
            mListener.onFragmentInteraction(pipelines.get(position).getPipelineName());
        }
    }

    public void onCreateNewItem() {
        Intent intent = new Intent(getActivity(), PipelineCreateActivity.class);
        startActivityForResult(intent, R.integer.create_new_pipeline);
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
        void onFragmentInteraction(String pipelineName);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            Date current = new Date();
            if (updateTime == null || (!confVersion.equals(CurrentConf.version()))
                    || (current.getTime() - updateTime.getTime()) > 1000 * 60) {
                new GetPipelinesTask().execute();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            new GetPipelinesTask().execute();
        }
    }

    class GetPipelinesTask extends AsyncTask<Void, Void, List<PipelineStatus>> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<PipelineStatus> doInBackground(Void... voids) {
            List<PipelineStatus> pipelineStatuses;
            try {
                pipelineStatuses =
                        CurrentConf.getMediaClient().listPipelines().getPipelines();
            } catch (Exception ex) {
                return null;
            }
            Collections.sort(pipelineStatuses, new Comparator<PipelineStatus>() {
                @Override
                public int compare(PipelineStatus p0, PipelineStatus p1) {
                    return p1.getreateTime().compareTo(p0.getreateTime());
                }
            });

            return pipelineStatuses;
        }

        @Override
        protected void onPostExecute(List<PipelineStatus> pipelineStatuses) {
            progressBar.setVisibility(View.INVISIBLE);
            if (pipelineStatuses == null) {
                Toast.makeText(getActivity(), "数据更新失败", Toast.LENGTH_SHORT).show();
            } else {
                pipelines.clear();
                pipelines.addAll(pipelineStatuses);
                confVersion = CurrentConf.version();
                updateTime = new Date();
                ((ArrayAdapter) mAdapter).notifyDataSetChanged();
                Toast.makeText(getActivity(), "数据刷新成功", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
