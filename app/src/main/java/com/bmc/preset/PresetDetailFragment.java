package com.bmc.preset;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.baidubce.BceServiceException;
import com.baidubce.services.media.model.CreatePresetRequest;
import com.baidubce.services.media.model.GetPresetResponse;
import com.bmc.R;
import com.bmc.setting.CurrentConf;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnPresetFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PresetDetailFragment extends Fragment {

    private GetPresetResponse preset;

    private OnPresetFragmentInteractionListener mListener;

    private PresetVideoFragment videoFragment;
    private PresetAudioFragment audioFragment;
    private PresetClipFragment clipFragment;


    public PresetDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_preset_detail, container, false);

        FragmentManager fragmentManager = getFragmentManager();
        videoFragment = (PresetVideoFragment) fragmentManager.findFragmentById(R.id.fragment_preset_video);
        audioFragment = (PresetAudioFragment) fragmentManager.findFragmentById(R.id.fragment_preset_audio);
        clipFragment = (PresetClipFragment) fragmentManager.findFragmentById(R.id.fragment_preset_clip);

        final EditText presetName = (EditText) view.findViewById(R.id.preset_name);
        presetName.setText(preset.getPresetName());

        final EditText presetDescription = (EditText) view.findViewById(R.id.preset_description);
        presetDescription.setText(preset.getDescription());

        enableEditText(mListener.editable(), presetName, presetDescription);

        final Spinner presetContainer = (Spinner) view.findViewById(R.id.container);
        presetContainer.setEnabled(mListener.editable());

        fragmentSetting(view, R.id.audio_check_box, R.id.fragment_preset_audio, preset.getAudio() != null);
        fragmentSetting(view, R.id.video_check_box, R.id.fragment_preset_video, preset.getVideo() != null);
        fragmentSetting(view, R.id.clip_check_box, R.id.fragment_preset_clip, preset.getClip() != null);

        final CheckBox audio = (CheckBox) view.findViewById(R.id.audio_check_box);
        audio.setOnClickListener(createOnClickListener(audio, R.id.fragment_preset_audio));

        final CheckBox video = (CheckBox) view.findViewById(R.id.video_check_box);
        video.setOnClickListener(createOnClickListener(video, R.id.fragment_preset_video));

        final CheckBox clip = (CheckBox) view.findViewById(R.id.clip_check_box);
        clip.setOnClickListener(createOnClickListener(clip, R.id.fragment_preset_clip));

        if (!mListener.editable()) {
            view.findViewById(R.id.buttons).setVisibility(View.GONE);
        }

        Button save = (Button) view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder sb = new StringBuilder();
                if (presetName.getText().toString().isEmpty()) {
                    sb.append("模板名称不能为空\n");
                }
                if (presetDescription.getText().toString().isEmpty()) {
                    sb.append("描述不能为空\n");
                }
                if (!audio.isChecked() && !video.isChecked()) {
                    sb.append("视频和音频不能同时为空\n");
                }
                if (!sb.toString().isEmpty()) {
                    Toast.makeText(getActivity(), sb.toString(), Toast.LENGTH_SHORT).show();
                    return;
                }

                String[] containers = getResources().getStringArray(R.array.preset_container);

                CreatePresetRequest request = new CreatePresetRequest();
                request.setPresetName(presetName.getText().toString());
                request.setDescription(presetDescription.getText().toString());
                request.setContainer(containers[presetContainer.getSelectedItemPosition()]);

                if (video.isChecked()) {
                    request.setVideo(videoFragment.getVideo());
                }

                if (audio.isChecked()) {
                    request.setAudio(audioFragment.getAudio());
                }

                if (clip.isChecked()) {
                    request.setClip(clipFragment.getClip());
                }

                boolean success = false;
                try {
                    CurrentConf.getMediaClient().createPreset(request);
                    success = true;
                } catch (BceServiceException ex) {
                    Toast.makeText(view.getContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                }

                if (success) {
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }
            }
        });
        Button cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        return view;
    }

    private void fragmentSetting(View view, int checkBoxId, int fragmentId, boolean enable) {
        CheckBox checkBox = (CheckBox) view.findViewById(checkBoxId);
        if (enable) {
            checkBox.setChecked(true);
        } else {
            hideFragment(fragmentId, true);
        }
        checkBox.setEnabled(mListener.editable());
    }

    private void hideFragment(int fragmentId, boolean hide) {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(fragmentId);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (hide) {
            transaction.hide(fragment);
        } else {
            transaction.show(fragment);
        }
        transaction.commit();
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

    private View.OnClickListener createOnClickListener(final CheckBox checkBox, final int fragmentId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    hideFragment(fragmentId, false);
                    Log.d(getClass().toString(), "hide fragment" + fragmentId);
                } else {
                    hideFragment(fragmentId, true);
                    Log.d(getClass().toString(), "show fragment" + fragmentId);
                }
            }
        };
    }

}
