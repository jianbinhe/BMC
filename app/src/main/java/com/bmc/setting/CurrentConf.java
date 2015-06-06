package com.bmc.setting;

import com.baidubce.BceClientConfiguration;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.media.MediaClient;

import java.util.UUID;

/**
 * Created by hejianbin on 6/3/15.
 */
public class CurrentConf {
    private static MediaClient mediaClient;
    private static BosClient bosClient;

    private static ConfItem confItem;
    private static boolean confChanged = false;
    private static String version = UUID.randomUUID().toString();

    public static MediaClient getMediaClient() {
        if (confItem == null) {
            return null;
        } else if (confChanged) {
            renewClients();
        }
        return mediaClient;
    }

    public static BosClient getBosClient() {
        if (confItem == null) {
            return null;
        } else if (confChanged) {
            renewClients();
        }
        return bosClient;
    }

    private static void renewClients() {
        BceClientConfiguration mediaConf = new BceClientConfiguration()
                .withCredentials(new DefaultBceCredentials(confItem.getAccessKey(),
                        confItem.getSecretKey()))
                .withEndpoint(Endpoints.getEndpoint(confItem.getEnv(), Product.BMC));
        mediaClient = new MediaClient(mediaConf);

        BosClientConfiguration config = new BosClientConfiguration()
                .withCredentials(new DefaultBceCredentials(confItem.getAccessKey(),
                        confItem.getSecretKey()))
                .withEndpoint(Endpoints.getEndpoint(confItem.getEnv(), Product.BOS));
        bosClient = new BosClient(config);

        confChanged = false;
        version = UUID.randomUUID().toString();
    }

    public static void setConfItem(ConfItem confItem) {
        CurrentConf.confItem = confItem;
        confChanged = true;
    }

    public static String version() {
        return version;
    }
}
