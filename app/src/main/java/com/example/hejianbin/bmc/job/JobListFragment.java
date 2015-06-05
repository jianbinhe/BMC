package com.example.hejianbin.bmc.job;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.baidubce.services.media.model.Job;
import com.example.hejianbin.bmc.R;
import com.example.hejianbin.bmc.setting.CurrentConf;
import com.example.hejianbin.bmc.setting.SettingCreateActivity;

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
public class JobListFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static final String PIPELINE_NAME = "pipelineName";

    private String pipelineName;

    private OnFragmentInteractionListener mListener;

    private List<Job> jobs;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static JobListFragment newInstance(String pipelineName) {
        JobListFragment fragment = new JobListFragment();
        Bundle args = new Bundle();
        args.putString(PIPELINE_NAME, pipelineName);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public JobListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            pipelineName = getArguments().getString(PIPELINE_NAME);
        }

        jobs = CurrentConf.getMediaClient().listJobs(pipelineName).getJobs();
        mAdapter = new JobItemAdaptor(getActivity(), R.layout.job_list_item, jobs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            pipelineName = mListener.getPipelineName();
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
        Job job = jobs.get(position);
        if (null != mListener) {
//             Notify the active callbacks interface (the activity, if the
//             fragment is attached to one) that an item has been selected.
        }
        Intent intent = new Intent(getActivity(), JobDetailActivity.class);
        intent.putExtra("jobId", job.getJobId());
        intent.putExtra("presetName", job.getTarget().getPresetName());
        intent.putExtra("pipelineName", job.getPipelineName());
        startActivity(intent);

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
        void onFragmentInteraction(String id);

        String getPipelineName();
    }

}
