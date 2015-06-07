package com.bmc.setting;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.bmc.R;
import com.bmc.common.WithCreateNewItem;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
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
public class ConfItemFragment extends Fragment implements
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

    private List<ConfItem> confItems;

    private Integer itemsHash = 0;

    // TODO: Rename and change types of parameters
    public static ConfItemFragment newInstance(String param1, String param2) {
        ConfItemFragment fragment = new ConfItemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ConfItemFragment() {
    }

    private List<ConfItem> readConfFromFile() {
        List<ConfItem> result;
        FileInputStream in = null;
        BufferedReader reader = null;
        try {
            in = getActivity().openFileInput("conf.data");
            reader = new BufferedReader(new InputStreamReader(in));
            Gson gson = new Gson();
            result = gson.fromJson(reader, new TypeToken<List<ConfItem>>() {
            }.getType());
            Log.d(getClass().toString(), "success read conf from file");
        } catch (IOException ex) {
            result = new ArrayList<>();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        confItems = readConfFromFile();
        for (ConfItem confItem : confItems) {
            if (confItem.getActive()) {
                CurrentConf.setConfItem(confItem);
                break;
            }
        }
        itemsHash = confItems.hashCode();
        mAdapter = new ConfItemAdaptor(getActivity(), R.layout.conf_list_item, confItems);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confitems, container, false);

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

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ConfItemFragment", "returned from activity");
        if (resultCode != Activity.RESULT_OK) {
            Log.d("ConfItemFragment", "user not add new conf item");
            return;
        }
        ConfItem confItem = new ConfItem();
        confItem.setAccessKey(data.getStringExtra("access_key"));
        confItem.setSecretKey(data.getStringExtra("secret_key"));
        confItem.setDescription(data.getStringExtra("description"));
        confItem.setEnv(data.getStringExtra("env"));
        confItem.setActive(confItems.isEmpty());
        if (confItems.isEmpty()) {
            // 如果之前没有在使用的conf，则将此conf设置为活动的conf
            CurrentConf.setConfItem(confItem);
        }

        confItems.add(confItem);
        ((ArrayAdapter) mAdapter).notifyDataSetChanged();
    }


    public void onCreateNewItem() {
        Intent intent = new Intent(getActivity(), SettingCreateActivity.class);
        startActivityForResult(intent, R.integer.create_new_conf);
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

    public interface OnFragmentInteractionListener {

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // the fragment become invisible
            Log.d(getClass().toString(), "conf items fragment hidden");
            if (itemsHash == confItems.hashCode()) {
                Log.d(getClass().toString(), "conf items not changed");
            } else {
                Log.d(getClass().toString(), "conf items changed, save it to file");
                Gson gson = new Gson();
                String json = gson.toJson(confItems);
                FileOutputStream out;
                BufferedWriter writer = null;
                try {
                    out = getActivity().openFileOutput("conf.data", Context.MODE_PRIVATE);
                    writer = new BufferedWriter(new OutputStreamWriter(out));
                    writer.write(json);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            // this fragment become visible
            itemsHash = confItems.hashCode();
            Log.d(getClass().toString(), "recalculate items hash");
            if (confItems.isEmpty()) {
                // 如果为空的话，直接进入增加配置的界面
                onCreateNewItem();
            }
        }
    }
}
