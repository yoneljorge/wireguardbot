package dev.yonel.wireguardbot.common.enums;

public enum ParseMode {
    MARKDOWN("Markdown"),
    MARKDOWNV2("MarkdownV2"),
    HTML("HTML");

    private final String value;

    ParseMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
