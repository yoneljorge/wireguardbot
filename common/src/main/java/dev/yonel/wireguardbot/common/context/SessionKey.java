package dev.yonel.wireguardbot.common.context;

public enum SessionKey {

    COMMAND_START_ESPERAR_USERNAME("command_start_esperar_username"),

    CONFIGURACION_COMMAND_OPCION("configuracion_command_opcion"),

    ELIMINAR_CONFIGURACION_COMMAND_ESPERAR("esperarBotonTerminar?"),
    ELIMINAR_CONFIGURACION_COMMAND_MAP_PEERS_PUBLIC_KEY("eliminar configuraicon command map peers public key");

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
