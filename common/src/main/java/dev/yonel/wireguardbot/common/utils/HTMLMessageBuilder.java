package dev.yonel.wireguardbot.common.utils;

/**
 * Clase de utilidad `HTMLMessageBuilder` diseñada para construir mensajes
 * con formato HTML de manera programática y fluida.
 *
 * Permite agregar diferentes elementos de texto con varios estilos HTML
 * (títulos centrados simulados, texto estándar, negritas, cursivas, negritas
 * y cursivas, código en línea y bloques de código) a un mensaje que se está
 * construyendo.
 *
 * La clase utiliza un `StringBuilder` internamente para optimizar la
 * concatenación de cadenas y proporciona métodos encadenables para una
 * construcción de mensajes más legible y concisa.
 */
public class HTMLMessageBuilder {
    /**
     * StringBuilder interno para construir el mensaje HTML.
     */
    private StringBuilder message;

    /**
     * Constructor por defecto que inicializa un nuevo `HTMLMessageBuilder`
     * con un mensaje interno vacío.
     */
    public HTMLMessageBuilder() {
        this.message = new StringBuilder();
    }

    public HTMLMessageBuilder(String mensaje) {
        this.message = new StringBuilder();
        addLine(mensaje);
    }

    /**
     * Escapa caracteres especiales para HTML.
     * 
     * @param text Texto a escapar
     * @return Texto escapado
     */
    public static String escapeHTML(String text) {
        if (text == null)
            return "";

        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    /**
     * Agrega un título al mensaje, simulando un centrado mediante la adición
     * de saltos de línea antes y después del título.
     *
     * @param title El título que se desea agregar al mensaje.
     * @return La instancia actual de `HTMLMessageBuilder` para permitir el
     *         encadenamiento de métodos.
     */
    public HTMLMessageBuilder addCenteredTitle(String title) {
        message.append("<b>").append(escapeHTML(title)).append("</b>\n\n");
        return this;
    }

    /**
     * Agrega una cadena de texto simple al mensaje sin ningún formato HTML
     * adicional.
     *
     * @param text La cadena de texto que se desea agregar.
     * @return La instancia actual de `HTMLMessageBuilder` para permitir el
     *         encadenamiento de métodos.
     */
    public HTMLMessageBuilder add(String text) {
        message.append(escapeHTML(text));
        return this;
    }

    /**
     * Agrega una línea de texto al mensaje, seguida de un carácter de nueva línea.
     *
     * @param line La cadena de texto que se desea agregar como una nueva línea.
     * @return La instancia actual de `HTMLMessageBuilder` para permitir el
     *         encadenamiento de métodos.
     */
    public HTMLMessageBuilder addLine(String line) {
        message.append(escapeHTML(line)).append("\n");
        return this;
    }

    /**
     * Agrega una línea de texto al mensaje con formato en negritas (envuelta en
     * <b>).
     *
     * @param line La cadena de texto que se desea agregar en negritas.
     * @return La instancia actual de `HTMLMessageBuilder` para permitir el
     *         encadenamiento de métodos.
     */
    public HTMLMessageBuilder addBoldLine(String line) {
        message.append("<b>").append(escapeHTML(line)).append("</b>\n");
        return this;
    }

    public HTMLMessageBuilder addBold(String line) {
        message.append("<b>").append(escapeHTML(line)).append("</b>");
        return this;
    }

    /**
     * Agrega una línea de texto al mensaje con formato en cursiva (envuelta en
     * <i>).
     *
     * @param line La cadena de texto que se desea agregar en cursiva.
     * @return La instancia actual de `HTMLMessageBuilder` para permitir el
     *         encadenamiento de métodos.
     */
    public HTMLMessageBuilder addItalicLine(String line) {
        message.append("<i>").append(escapeHTML(line)).append("</i>\n");
        return this;
    }

    public HTMLMessageBuilder addItalic(String line) {
        message.append("<i>").append(escapeHTML(line)).append("</i>");
        return this;
    }

    /**
     * Agrega una línea de texto al mensaje con formato en negritas y cursiva
     * (envuelta en <b><i>).
     *
     * @param line La cadena de texto que se desea agregar en negritas y cursiva.
     * @return La instancia actual de `HTMLMessageBuilder` para permitir el
     *         encadenamiento de métodos.
     */
    public HTMLMessageBuilder addBoldItalicLine(String line) {
        message.append("<b><i>").append(escapeHTML(line)).append("</i></b>\n");
        return this;
    }

    public HTMLMessageBuilder addBoldItalic(String line) {
        message.append("<b><i>").append(escapeHTML(line)).append("</i></b>");
        return this;
    }

    /**
     * Agrega una línea de texto al mensaje con formato de código en línea
     * (envuelta en <code>).
     *
     * @param code La cadena de texto que se desea agregar como código en línea.
     * @return La instancia actual de `HTMLMessageBuilder` para permitir el
     *         encadenamiento de métodos.
     */
    public HTMLMessageBuilder addCodeLine(String code) {
        message.append("<code>").append(escapeHTML(code)).append("</code>\n");
        return this;
    }

    public HTMLMessageBuilder addCode(String code) {
        message.append("<code>").append(escapeHTML(code)).append("</code>");
        return this;
    }

    /**
     * Agrega un bloque de código al mensaje (envuelto en
     * 
     * <pre>
     * <code>).
     *
     * @param code El bloque de código que se desea agregar.
     * @return La instancia actual de `HTMLMessageBuilder` para permitir el
     *         encadenamiento de métodos.
     */
    public HTMLMessageBuilder addCodeBlock(String code) {
        message.append("<pre><code>").append(escapeHTML(code)).append("</code></pre>\n");
        return this;
    }

    /**
     * Construye y devuelve el mensaje HTML final como una cadena de texto.
     *
     * @return El mensaje HTML construido.
     */
    public String build() {
        return message.toString();
    }

    /**
     * Borra el contenido del mensaje interno, permitiendo la reutilización
     * de la instancia de `HTMLMessageBuilder` para construir un nuevo mensaje.
     */
    public void reset() {
        message.setLength(0);
    }
}
