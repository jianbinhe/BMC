package com.bmc.preset;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.baidubce.services.media.model.CodecOptions;
import com.baidubce.services.media.model.Video;
import com.bmc.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnPresetFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PresetVideoFragment extends Fragment {
    private OnPresetFragmentInteractionListener mListener;

    private Video video;

    private EditText bitRateInBps;
    private EditText maxHeight;
    private EditText maxWidth;
    private Spinner profile;
    private Spinner sizingPolicy;
    private Spinner codec;
    private Spinner maxFrameRate;


    public PresetVideoFragment() {
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
        View view = inflater.inflate(R.layout.fragment_preset_video, container, false);
        if (video == null) {
            return view;
        }

        bitRateInBps = (EditText) view.findViewById(R.id.bit_rate_in_bps);
        bitRateInBps.setText(video.getBitRateInBps().toString());

        maxHeight = (EditText) view.findViewById(R.id.max_height);
        if (video.getMaxHeightInPixel() != null) {
            maxHeight.setText(video.getMaxHeightInPixel().toString());
        }

        maxWidth = (EditText) view.findViewById(R.id.max_width);
        if (video.getMaxWidthInPixel() != null) {
            maxWidth.setText(video.getMaxWidthInPixel().toString());
        }

        profile = (Spinner) view.findViewById(R.id.profile);
        profile.setSelection(getIndex(profile, video.getCodecOptions().getProfile()));

        sizingPolicy = (Spinner) view.findViewById(R.id.sizing_policy);
        sizingPolicy.setSelection(getIndex(sizingPolicy, video.getSizingPolicy()));

        codec = (Spinner) view.findViewById(R.id.codec);
        codec.setSelection(getIndex(codec, video.getCodec()));

        maxFrameRate = (Spinner) view.findViewById(R.id.max_frame_rate);
        maxFrameRate.setSelection(getIndex(maxFrameRate, video.getMaxFrameRate()));

        enableEditText(mListener.editable(), bitRateInBps, maxHeight, maxWidth);
        enableView(mListener.editable(), profile, sizingPolicy, codec, maxFrameRate);

        return view;
    }

    private void enableView(Boolean enable, View... views) {
        for (View view : views) {
            view.setEnabled(enable);
        }
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
            video = mListener.getPreset().getVideo();
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

    private int getIndex(Spinner spinner, Float item) {
        for (int i = 0; i < spinner.getCount(); ++i) {
            if (Float.valueOf(spinner.getItemAtPosition(i).toString()).equals(item)) {
                return i;
            }
        }
        throw new RuntimeException("unrecognized item value:" + item.toString());
    }

    private int getIndex(Spinner spinner, String item) {
        for (int i = 0; i < spinner.getCount(); ++i) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(item)) {
                return i;
            }
        }
        throw new RuntimeException("Recognized item value:" + item);
    }

    public Video getVideo() {
        Video video = new Video();
        if (!bitRateInBps.getText().toString().isEmpty()) {
            video.setBitRateInBps(Integer.valueOf(bitRateInBps.getText().toString()));
        }

        if (!maxWidth.getText().toString().isEmpty()) {
            video.setMaxWidthInPixel(Integer.valueOf(maxWidth.getText().toString()));
        }

        if (!maxHeight.getText().toString().isEmpty()) {
            video.setMaxHeightInPixel(Integer.valueOf(maxHeight.getText().toString()));
        }

        String[] maxFrameRates = getResources().getStringArray(R.array.video_max_frame_rate);
        video.setMaxFrameRate(Float.valueOf(maxFrameRates[maxFrameRate.getSelectedItemPosition()]));

        String[] codecs = getResources().getStringArray(R.array.video_codec);
        video.setCodec(codecs[codec.getSelectedItemPosition()]);

        CodecOptions codecOptions = new CodecOptions();
        String[] profiles = getResources().getStringArray(R.array.video_profile);
        codecOptions.setProfile(profiles[profile.getSelectedItemPosition()]);
        video.setCodecOptions(codecOptions);

        String[] sizingPolicies = getResources().getStringArray(R.array.video_sizing_policy);
        video.setSizingPolicy(sizingPolicies[sizingPolicy.getSelectedItemPosition()]);

        return video;
    }

}
