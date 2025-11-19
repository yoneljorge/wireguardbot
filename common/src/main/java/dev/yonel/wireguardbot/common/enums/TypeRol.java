package dev.yonel.wireguardbot.common.enums;

/**
 * Clase que va a contener los diferentes roles de los usuarios.
 * 
 */
public enum TypeRol {

    ADMIN(1, "ADMINISTRADOR"),
    COMERCIAL(2, "COMERCIAL"),
    USUARIO(3, "USUARIO");

    private final int number;
    private final String value;

    TypeRol(int number, String nombre) {
        this.number = number;
        this.value = nombre;
    }

    public int getNumber() {
        return number;
    }

    /**
     * Obtiene el valor del rol.
     * 
     * @return el valor del rol del usuario.
     */
    public String getValue() {
        return value;
    }

    /**
     * Devuelve el Typo de Rol en dependencia del value que se pase como argumento.
     * 
     * @param value el valor del rol que se quiere.
     * 
     * @return TypeRol encontrado.
     */
    public static TypeRol fromNombre(String value) {
        for (TypeRol rol : TypeRol.values()) {
            if (rol.getValue().equals(value)) {
                return rol;
            }
        }

        throw new IllegalArgumentException("Invalid rol value: " + value);
    }
}