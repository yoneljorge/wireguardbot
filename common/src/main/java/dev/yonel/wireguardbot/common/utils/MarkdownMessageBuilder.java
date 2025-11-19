package dev.yonel.wireguardbot.common.utils;
/**
 * Clase de utilidad `MarkdownMessageBuilder` diseñada para construir mensajes
 * con formato Markdown de manera programática y fluida.
 *
 * Permite agregar diferentes elementos de texto con varios estilos de Markdown
 * (títulos centrados simulados, texto estándar, negritas, cursivas, negritas
 * y cursivas, código en línea y bloques de código) a un mensaje que se está
 * construyendo.
 *
 * La clase utiliza un `StringBuilder` internamente para optimizar la
 * concatenación
 * de cadenas y proporciona métodos encadenables para una construcción de
 * mensajes
 * más legible y concisa.
 */
public class MarkdownMessageBuilder {
	/**
	 * StringBuilder interno para construir el mensaje Markdown.
	 */
	private StringBuilder message;

	/**
	 * Constructor por defecto que inicializa un nuevo `MarkdownMessageBuilder`
	 * con un mensaje interno vacío.
	 */
	public MarkdownMessageBuilder() {
		this.message = new StringBuilder();
	}

	public MarkdownMessageBuilder(String mensaje) {
		this.message = new StringBuilder();
		addLine(mensaje);
	}

	public static String escapeMarkdown(String text) {
		StringBuilder sb = new StringBuilder();
		for (char c : text.toCharArray()) {
			switch (c) {
				//case '*':
				//case '_':
				case '[':
				case ']':
				case '(':
				case ')':
				case '~':
				// case '`':
				case '>':
				case '#':
				case '+':
				case '-':
				case '!':
				case '.':
                case ':':
                case '¿':
                case '?':
					sb.append("\\").append(c);
					break;
				default:
					sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * Agrega un título al mensaje, simulando un centrado mediante la adición
	 * de saltos de línea antes y después del título.
	 *
	 * @param title
	 *           El título que se desea agregar al mensaje.
	 * @return La instancia actual de `MarkdownMessageBuilder` para permitir el
	 *         encadenamiento de métodos.
	 */
	public MarkdownMessageBuilder addCenteredTitle(String title) {
		message.append(title).append("\n\n");
		return this;
	}

	/**
	 * Agrega una cadena de texto simple al mensaje sin ningún formato Markdown
	 * adicional.
	 *
	 * @param text
	 *           La cadena de texto que se desea agregar.
	 * @return La instancia actual de `MarkdownMessageBuilder` para permitir el
	 *         encadenamiento de métodos.
	 */
	public MarkdownMessageBuilder add(String text) {
		message.append(text);
		return this;
	}

	/**
	 * Agrega una línea de texto al mensaje, seguida de un carácter de nueva línea.
	 *
	 * @param line
	 *           La cadena de texto que se desea agregar como una nueva línea.
	 * @return La instancia actual de `MarkdownMessageBuilder` para permitir el
	 *         encadenamiento de métodos.
	 */
	public MarkdownMessageBuilder addLine(String line) {
		message.append(line).append("\n");
		return this;
	}

	/**
	 * Agrega una línea de texto al mensaje con formato en negritas (envuelta en
	 * asteriscos `*`).
	 *
	 * @param line
	 *           La cadena de texto que se desea agregar en negritas.
	 * @return La instancia actual de `MarkdownMessageBuilder` para permitir el
	 *         encadenamiento de métodos.
	 */
	public MarkdownMessageBuilder addBoldLine(String line) {
		message.append("*").append(line).append("*").append("\n");
		return this;
	}

	public MarkdownMessageBuilder addBold(String line) {
		message.append("*").append(line).append("*");
		return this;
	}

	/**
	 * Agrega una línea de texto al mensaje con formato en cursiva (envuelta en
	 * guiones bajos `_`).
	 *
	 * @param line
	 *           La cadena de texto que se desea agregar en cursiva.
	 * @return La instancia actual de `MarkdownMessageBuilder` para permitir el
	 *         encadenamiento de métodos.
	 */
	public MarkdownMessageBuilder addItalicLine(String line) {
		message.append("_").append(line).append("_").append("\n");
		return this;
	}

	public MarkdownMessageBuilder addItalic(String line) {
		message.append("_").append(line).append("_");
		return this;
	}

	/**
	 * Agrega una línea de texto al mensaje con formato en negritas y cursiva
	 * (envuelta en tres asteriscos `***`).
	 *
	 * @param line
	 *           La cadena de texto que se desea agregar en negritas y cursiva.
	 * @return La instancia actual de `MarkdownMessageBuilder` para permitir el
	 *         encadenamiento de métodos.
	 */
	public MarkdownMessageBuilder addBoldItalicLine(String line) {
		message.append("***").append(line).append("***").append("\n");
		return this;
	}

	public MarkdownMessageBuilder addBoldItalic(String line) {
		message.append("***").append(line).append("***");
		return this;
	}

	/**
	 * Agrega una línea de texto al mensaje con formato de código en línea
	 * (envuelta en backticks `` ` ``).
	 *
	 * @param code
	 *           La cadena de texto que se desea agregar como código en línea.
	 * @return La instancia actual de `MarkdownMessageBuilder` para permitir el
	 *         encadenamiento de métodos.
	 */
	public MarkdownMessageBuilder addCodeLine(String code) {
		message.append("`").append(code).append("`").append("\n");
		return this;
	}

	public MarkdownMessageBuilder addCode(String code) {
		message.append("`").append(code).append("`");
		return this;
	}

	/**
	 * Agrega un bloque de código al mensaje (envuelto en tres backticks `` ``` ``
	 * al principio y al final, con saltos de línea).
	 *
	 * @param code
	 *           El bloque de código que se desea agregar.
	 * @return La instancia actual de `MarkdownMessageBuilder` para permitir el
	 *         encadenamiento de métodos.
	 */
	public MarkdownMessageBuilder addCodeBlock(String code) {
		message.append("```\n").append(code).append("\n```\n");
		return this;
	}

	/**
	 * Construye y devuelve el mensaje Markdown final como una cadena de texto.
	 *
	 * @return El mensaje Markdown construido.
	 */
	public String build() {
		return message.toString();
	}

	/**
	 * Borra el contenido del mensaje interno, permitiendo la reutilización
	 * de la instancia de `MarkdownMessageBuilder` para construir un nuevo mensaje.
	 */
	public void reset() {
		message.setLength(0);
	}
}