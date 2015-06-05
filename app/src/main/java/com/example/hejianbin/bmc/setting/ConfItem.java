package com.example.hejianbin.bmc.setting;

/**
 * Created by hejianbin on 5/28/15.
 */
public class ConfItem {
    private String accessKey;
    private String secretKey;
    private String env;
    private Boolean active = false;
    private String description = "description";

    @Override
    public String toString() {
        return "ConfItem{" +
                "accessKey='" + accessKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", env='" + env + '\'' +
                ", active=" + active +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConfItem confItem = (ConfItem) o;

        if (!accessKey.equals(confItem.accessKey)) return false;
        if (!secretKey.equals(confItem.secretKey)) return false;
        if (!env.equals(confItem.env)) return false;
        if (!active.equals(confItem.active)) return false;
        return description.equals(confItem.description);

    }

    @Override
    public int hashCode() {
        int result = accessKey.hashCode();
        result = 31 * result + secretKey.hashCode();
        result = 31 * result + env.hashCode();
        result = 31 * result + active.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }
}
