package com.example.hejianbin.bmc.setting;

import com.baidubce.BceClientConfiguration;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.media.MediaClient;

import java.util.Random;
import java.util.UUID;

/**
 * Created by hejianbin on 6/3/15.
 */
public class CurrentConf {
    private static MediaClient mediaClient;
    private static ConfItem confItem;
    private static boolean confChanged = false;
    private static String version = UUID.randomUUID().toString();

    public static MediaClient getMediaClient() {
        if (confItem == null) {
            return null;
        } else if (confChanged) {
            BceClientConfiguration config = new BceClientConfiguration()
                    .withCredentials(new DefaultBceCredentials(confItem.getAccessKey(),
                            confItem.getSecretKey()))
                    .withEndpoint(Endpoints.getEndpoint(confItem.getEnv(), Product.BMC));
            mediaClient = new MediaClient(config);
            confChanged = false;
            version = UUID.randomUUID().toString();
        }
        return mediaClient;
    }

    public static void setConfItem(ConfItem confItem) {
        CurrentConf.confItem = confItem;
        confChanged = true;
    }

    public static String version() {
        return version;
    }
}
