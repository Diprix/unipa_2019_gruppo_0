package it.eng.unipa.filesharing.model;

import java.util.UUID;

public class PushSelector {
    private String email;
    private UUID uuid;
    private String name;

    public PushSelector(String email, UUID uuid, String name) {
        this.email = email;
        this.uuid = uuid;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String userMail) {
        this.email = userMail;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PushSelector{" +
                "userMail='" + email + '\'' +
                ", uuid=" + uuid +
                ", name='" + name + '\'' +
                '}';
    }
}
