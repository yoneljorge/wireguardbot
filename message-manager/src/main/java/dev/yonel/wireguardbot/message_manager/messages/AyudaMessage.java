package dev.yonel.wireguardbot.message_manager.messages;

public class AyudaMessage {

    /**
     * Genera el mensaje de ayuda principal para el usuario.
     * @return El mensaje de ayuda formateado en HTML, cubriendo los comandos y temas principales.
     */
    public static String message() {

        String mensaje = """
                👋 <b>CENTRO DE AYUDA RÁPIDA</b>

                Aquí puedes encontrar información sobre cómo usar el bot y resolver dudas comunes:

                <pre>
                -----------------------------------
                </pre>
                
                <b>⚙️ COMANDOS PRINCIPALES</b>
                Si deseas ver el menú completo de comandos, usa <code>/menu</code>.

                1.  <b>Gestionar Configuración (<code>/gestion</code>)</b>
                    - ¿Necesitas un perfil nuevo? Usa el submenú de gestión para <i>Crear</i>, <i>Obtener</i> tu archivo <code>.conf</code> o <i>Eliminar</i> un perfil viejo.
                    - Recuerda que tienes <b>3 DÍAS DE PRUEBA GRATIS</b> al crear tu primera configuración.

                2.  <b>Suscripción y Pagos (<code>/pago</code>)</b>
                    - Para ver cuánto tiempo te queda: usa <code>/vencimiento</code>.
                    - Para pagar o extender tu servicio (100 CUP mensual): usa <code>/pago</code>.
                    - Si ya pagaste y quieres extender tu periodo: usa <code>/renovar</code>.

                3.  <b>Monitoreo de Datos (<code>/estadisticas</code>)</b>
                    - Revisa tu consumo de datos (subida/bajada) y el estado de tu conexión (último <i>handshake</i>). Esto te ayuda a diagnosticar problemas de velocidad.

                4.  <b>Sistema de Referidos (<code>/referidos</code>)</b>
                    - ¡Gana dinero o reduce tu factura! Usa este comando para obtener tu enlace y ver cuántas ganancias has acumulado.
                    - <i>Beneficio:</i> Puedes retirar las ganancias que excedan el costo de tu suscripción mensual.

                <pre>
                -----------------------------------
                </pre>
                
                <b>🛠️ SOPORTE TÉCNICO Y CONEXIÓN</b>
                
                * <b>Túnel Dividido (Split Tunneling):</b> Si estás usando el cliente TunnlTo para juegos (como LoL) y necesitas baja latencia y menor consumo de megas, asegúrate de que tu configuración lo soporte.
                    - <i>Enlace de Descarga de TunnlTo:</i> <a href="https://t.me/wireguardbot_devYonel">https://t.me/wireguardbot_devYonel</a>
                
                * <b>¿Mi velocidad es lenta?</b>
                    - Revisa <code>/estadisticas</code>. Si el <i>handshake</i> es antiguo, puede haber un problema de red local. Si persiste, contacta soporte.
                
                * <b>No recibí mi archivo .conf / QR:</b>
                    - Intenta usar <code>/obtener</code>. Si el problema continúa, contacta soporte.
                
                <pre>
                -----------------------------------
                </pre>
                
                <b>💬 CONTACTO</b>
                Si ninguno de los comandos resuelve tu problema, por favor contacta a un administrador en nuestro grupo de soporte:
                
                <a href="https://t.me/wireguardbot_devYonel">Ir a la Comunidad</a>
                """;
        return mensaje;

    }
}