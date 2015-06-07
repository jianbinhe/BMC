package com.bmc.preset;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.baidubce.services.media.model.GetPresetResponse;
import com.bmc.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnPresetFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PresetDetailFragment extends Fragment {

    private GetPresetResponse preset;

    private OnPresetFragmentInteractionListener mListener;


    public PresetDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_preset_detail, container, false);

        EditText presetName = (EditText) view.findViewById(R.id.preset_name);
        presetName.setText(preset.getPresetName());

        EditText presetDescription = (EditText) view.findViewById(R.id.preset_description);
        presetDescription.setText(preset.getDescription());

        enableEditText(mListener.editable(), presetName, presetDescription);

        fragmentSetting(view, R.id.audio_check_box, R.id.fragment_preset_audio, preset.getAudio() != null);
        fragmentSetting(view, R.id.video_check_box, R.id.fragment_preset_video, preset.getVideo() != null);
        fragmentSetting(view, R.id.clip_check_box, R.id.fragment_preset_clip, preset.getClip() != null);

        return view;
    }

    private void fragmentSetting(View view, int checkBoxId, int fragmentId, boolean enable) {
        CheckBox checkBox = (CheckBox) view.findViewById(checkBoxId);
        if (enable) {
            checkBox.setChecked(true);
        } else {
            android.app.FragmentManager fragmentManager = getFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(fragmentId);
            fragmentManager.beginTransaction()
                    .hide(fragment)
                    .commit();
        }
        checkBox.setEnabled(mListener.editable());
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
            this.preset = mListener.getPreset();
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
