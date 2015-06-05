package com.example.hejianbin.bmc.preset;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.baidubce.services.media.model.Video;
import com.example.hejianbin.bmc.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnPresetFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PresetVideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PresetVideoFragment extends Fragment {
    private OnPresetFragmentInteractionListener mListener;

    private Video video;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PresetVideoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PresetVideoFragment newInstance(String param1, String param2) {
        PresetVideoFragment fragment = new PresetVideoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

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

        EditText bitRateInBps = (EditText) view.findViewById(R.id.bit_rate_in_bps);
        bitRateInBps.setText(video.getBitRateInBps().toString());

        EditText maxHeight = (EditText) view.findViewById(R.id.max_height);
        if (video.getMaxHeightInPixel() != null) {
            maxHeight.setText(video.getMaxHeightInPixel().toString());
        }

        EditText maxWidth = (EditText) view.findViewById(R.id.max_width);
        if (video.getMaxWidthInPixel() != null) {
            maxWidth.setText(video.getMaxWidthInPixel().toString());
        }

        Spinner profile = (Spinner) view.findViewById(R.id.profile);
        profile.setSelection(getIndex(profile, video.getCodecOptions().getProfile()));

        Spinner sizingPolicy = (Spinner) view.findViewById(R.id.sizing_policy);
        sizingPolicy.setSelection(getIndex(sizingPolicy, video.getSizingPolicy()));

        Spinner codec = (Spinner) view.findViewById(R.id.codec);
        codec.setSelection(getIndex(codec, video.getCodec()));

        Spinner maxFrameRate = (Spinner) view.findViewById(R.id.max_frame_rate);
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
        throw new RuntimeException("Recognized item value:" + item.toString());
    }

    private int getIndex(Spinner spinner, String item) {
        for (int i = 0; i < spinner.getCount(); ++i) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(item)) {
                return i;
            }
        }
        throw new RuntimeException("Recognized item value:" + item);
    }

}
