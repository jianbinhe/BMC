package com.bmc.bucket;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.baidubce.services.bos.model.BucketSummary;
import com.bmc.R;
import com.bmc.common.WithCreateNewItem;
import com.bmc.setting.CurrentConf;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link }
 * interface.
 */
public class BucketsFragment extends Fragment implements
        AbsListView.OnItemClickListener, WithCreateNewItem {

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;
    private ProgressBar progressBar;


    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    private List<BucketSummary> buckets = new ArrayList<BucketSummary>();


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BucketsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new BucketItemAdaptor(getActivity(), R.layout.bucket_list_item, buckets);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bucket, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            new GetBucketsTask().execute();
        }
    }

    @Override
    public void onCreateNewItem() {

    }

    class GetBucketsTask extends AsyncTask<Void, Void, List<BucketSummary>> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<BucketSummary> doInBackground(Void... voids) {
            List<BucketSummary> bucketSummaries;
            try {
                bucketSummaries = CurrentConf.getBosClient().listBuckets().getBuckets();
            } catch (Exception ex) {
                return null;
            }
            return bucketSummaries;
        }

        @Override
        protected void onPostExecute(List<BucketSummary> bucketSummaries) {
            progressBar.setVisibility(View.INVISIBLE);
            if (bucketSummaries == null) {
                Toast.makeText(getActivity(), "数据更新失败", Toast.LENGTH_SHORT).show();
            } else {
                buckets.clear();
                buckets.addAll(bucketSummaries);
                ((ArrayAdapter) mAdapter).notifyDataSetChanged();
                Toast.makeText(getActivity(), "数据刷新成功", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
