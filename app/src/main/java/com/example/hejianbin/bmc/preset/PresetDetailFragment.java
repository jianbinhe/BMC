package com.example.hejianbin.bmc.preset;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.baidubce.services.media.model.GetPresetResponse;
import com.example.hejianbin.bmc.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnPresetFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PresetDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PresetDetailFragment extends Fragment {

    private GetPresetResponse preset;

    private OnPresetFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PresetDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PresetDetailFragment newInstance(String param1, String param2) {
        PresetDetailFragment fragment = new PresetDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

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

        /*----------------audio setting -----------------------------*/
        CheckBox audio = (CheckBox) view.findViewById(R.id.audio_check_box);
        audio.setChecked(preset.getAudio() != null);
        audio.setEnabled(mListener.editable());

        /*----------------video setting -----------------------------*/
        CheckBox video = (CheckBox) view.findViewById(R.id.video_check_box);
        video.setChecked(preset.getVideo() != null);
        video.setEnabled(mListener.editable());

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
