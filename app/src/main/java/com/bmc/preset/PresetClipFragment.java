package com.bmc.preset;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.baidubce.services.media.model.Clip;
import com.bmc.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class PresetClipFragment extends Fragment {

    private Clip clip;

    private OnPresetFragmentInteractionListener mListener;

    public PresetClipFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preset_clip, container, false);

        if (clip == null) {
            return view;
        }

        EditText startTime = (EditText) view.findViewById(R.id.start_time);
        EditText duration = (EditText) view.findViewById(R.id.duration);

        if (clip.getStartTimeInSecond() != null) {
            startTime.setText(clip.getStartTimeInSecond().toString());
        }

        if (clip.getDurationInSecond() != null) {
            duration.setText(clip.getDurationInSecond().toString());
        }
        enableEditText(mListener.editable(), startTime, duration);
        return view;
    }

    private void enableEditText(Boolean enable, EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.setFocusable(enable);
            editText.setClickable(enable);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPresetFragmentInteractionListener) activity;
            this.clip = mListener.getPreset().getClip();
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

}
