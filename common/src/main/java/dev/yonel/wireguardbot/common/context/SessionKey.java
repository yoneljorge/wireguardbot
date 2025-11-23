package dev.yonel.wireguardbot.common.context;

public enum SessionKey {

    COMMAND_START_ESPERAR_USERNAME("command_start_esperar_username"),
    GESTION_COMMAND_OPCION("gestion_command_opcion");

    private final String key;

    SessionKey(String key) {
        this.key = key;
    }

    public String value() {
        return key;
    }

    public static boolean isValid(String key) {
        for (SessionKey k : values()) {
            if (k.key.equals(key)) {
                return true;
            }
        }
        return false;
    }
}
