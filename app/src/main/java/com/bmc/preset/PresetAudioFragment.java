package com.bmc.preset;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.baidubce.services.media.model.Audio;
import com.bmc.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnPresetFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PresetAudioFragment extends Fragment {

    private Audio audio;

    private OnPresetFragmentInteractionListener mListener;

    private EditText bitRate;
    private EditText sampleRate;
    private EditText channels;

    public PresetAudioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_preset_audio, container, false);

        if (audio == null) {
            return view;
        }

        bitRate = (EditText) view.findViewById(R.id.bitRateInBps);
        sampleRate = (EditText) view.findViewById(R.id.sampleRateInHz);
        channels = (EditText) view.findViewById(R.id.channels);

        bitRate.setText(audio.getBitRateInBps().toString());
        sampleRate.setText(audio.getSampleRateInHz().toString());
        channels.setText(audio.getChannels().toString());

        enableEditText(mListener.editable(), bitRate, sampleRate, channels);

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
            this.audio = mListener.getPreset().getAudio();
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

    public Audio getAudio() {
        Audio audio = new Audio();
        if (!bitRate.getText().toString().isEmpty()) {
            audio.setBitRateInBps(Integer.valueOf(bitRate.getText().toString()));
        }
        if (!sampleRate.getText().toString().isEmpty()) {
            audio.setSampleRateInHz(Integer.valueOf(sampleRate.getText().toString()));
        }
        if (!channels.getText().toString().isEmpty()) {
            audio.setChannels(Integer.valueOf(channels.getText().toString()));
        }
        return audio;
    }
}
