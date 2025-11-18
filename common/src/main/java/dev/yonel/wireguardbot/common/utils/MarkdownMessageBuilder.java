package dev.yonel.wireguardbot.common.utils;

public class MarkdownMessageBuilder {

    private MarkdownMessageBuilder(){}

    public static String escapeMarkdown(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        // Caracteres especiales que necesitan ser escapados en MarkdownV2
        // Los siguientes caracteres deben ser escapados: _ * [ ] ( ) ~ ` > # + - = | { } . !
        // En este caso, solo escapamos los que pueden aparecer en un mensaje normal
        return text
                .replace("_", "\\_")
                .replace("*", "\\*")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("~", "\\~")
                .replace("`", "\\`")
                .replace(">", "\\>")
                .replace("#", "\\#")
                .replace("+", "\\+")
                .replace("-", "\\-")
                .replace("=", "\\=")
                .replace("|", "\\|")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace(".", "\\.")
                .replace("!", "\\!");
    }

    public static String bold(String text) {
        return "*" + text + "*";
    }

    public static String italic(String text) {
        return "_" + text + "_";
    }

    public static String code(String text) {
        return "`" + text + "`";
    }

    public static String pre(String text) {
        return "```\n" + text + "\n```";
    }

    public static String link(String text, String url) {
        return "[" + text + "](" + url + ")";
    }

    public static String strikeThrough(String text) {
        return "~" + text + "~";
    }
}
