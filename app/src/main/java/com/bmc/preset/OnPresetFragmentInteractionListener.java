package com.bmc.preset;

import com.baidubce.services.media.model.GetPresetResponse;

/**
 * Created by hejianbin on 5/31/15.
 */
public interface OnPresetFragmentInteractionListener {
    GetPresetResponse getPreset();

    Boolean editable();
}
