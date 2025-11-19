package dev.yonel.wireguardbot.common.enums;

public enum TypeMessage {

    TEXT("text"),
    IMAGE("image"),
    VIDEO("video"),
    AUDIO("audio"),
    DOCUMENT("document"),
    STICKER("sticker"),
    LOCATION("location"),
    CONTACT("contact"),
    POLL("poll"),
    ANIMATION("animation"),
    VOICE("voice"),
    GAME("game"),
    PHOTO("photo");

    private final String type;

    TypeMessage(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
