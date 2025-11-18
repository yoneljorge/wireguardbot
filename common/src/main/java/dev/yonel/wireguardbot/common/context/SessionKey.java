package dev.yonel.wireguardbot.common.context;

public enum SessionKey {
    TARJETA_DTO("tarjeta_dto"),
    TARJETA_DTO_PARA_RECARGA("tarjeta_dto_para_recarga"),

    TARJETA_BANCO("tarjeta_banco"),
    TARJETA_NRO_TARJETA("tarjeta_nro_tarjeta"),
    TARJETA_MOVIL_ADMIN("tarjeta_movil_admin"),
    TARJETA_DUENO_MOVIL("tarjeta_dueno_movil"),
    TARJETA_DUENO_NOMBRE("tarjeta_dueno_nombre"),
    TARJETA_DUENO_APELLIDO("tarjeta_dueno_apellido"),

    COMMAND_START_ESPERAR_USERNAME("command_start_esperar_username"),

    COMMAND_JUGAR_DATA_LOTERIA("data_loteria"),
    COMMAND_JUGAR_DATA_APUESTA("data_apuesta"),

    COMMAND_RECARGAR_METODO_RECARGA("command_recargar_metodo_recarga"),
    COMMAND_RECARGAR_TYPE_BANCO("command_recargar_type_banco"),
    COMMAND_RECARGAR_MONTO("command_recargar_monto"),

    COMMAND_ADMIN_TARJETA_OPCION("opciones_tarjeta"),

    COMMAND_ADMIN_EDITAR_TARJETA_CAMPO("campos_editar_tarjeta"),
    COMMAND_ADMIN_EDITAR_TARJETA_OPCION("opciones_editar_tarjeta"),

    /**
     * Clave para guardar la información de la opción seleccionada en el comando
     * AdminUsuarioCommand
     */
    COMMAND_ADMIN_USUARIO_Opcion("command_admin_usuario_opcion"),
    /**
     * Clave para guardar la información del id del usuario seleccionado.
     */
    COMMAND_ADMIN_USUARIO_Id_Usuario_Seleccionado("command_admin_usuario_seleccionar_usuario_por_userid"),

    /**
     * Clave para guardar la información de la opción seleccionada en el command
     * BuscarUsuarioCommand.
     */
    COMMAND_ADMIN_BUSCAR_USUARIO_Opcion("command_admin_buscar_usuario_opcion"),
    /**
     * Clave para guardar el UsuarioDto seleccionado en AdminUsuarioCommand.
     */
    COMMAND_ADMIN_USUARIO_UsuarioDto_Seleccionado("command_admin_usuario_seleccionado_usuario_dto");

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
