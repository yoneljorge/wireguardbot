package dev.yonel.wireguardbot.message_manager.messages;

public class BienvenidaMessage {
    public static String message(String nombre) {

        String mensaje = """
                ¡Bienvenido/a a <b>WireGuard VPN Bot</b>! 🚀

                Somos tu solución de VPN ultra-rápida y segura, totalmente autogestionable desde aquí.

                🛡️ <b>Beneficios Clave:</b>
                - ✅ <b>Máxima Velocidad:</b> Disfruta de una conexión <i>súper-rápida</i> (ping bajo) ideal para juegos y streaming.
                - 🛡️ <b>Seguridad Total:</b> Bloqueamos anuncios molestos, rastreadores (tracking) y publicidad de redes sociales, protegiendo tu privacidad.
                - ⚡ <b>Split Tunneling (Túnel Dividido):</b> Soporte ideal para juegos como League of Legends. Esto mejora la latencia y <i>disminuye tu consumo de megas</i> al solo enrutar el tráfico necesario por la VPN.
                - <i>Requisito:</i> Para usar esta función, descarga el cliente TunnlTo. El enlace lo encuentras en nuestra comunidad: <a href="https://t.me/wireguardbot_devYonel">https://t.me/wireguardbot_devYonel</a>

                💰 <b>Precios, Prueba y Ganancias:</b>
                - 🎁 <b>PRUEBA GRATIS:</b> Tienes 7 días para probar nuestro servicio sin compromiso. ¡Solo tienes que crear tu configuración!
                - 💵 <b>Suscripción Mensual:</b> Solo 100 CUP.
                - 🤝 <b>Gana con Referidos:</b> Atrae nuevos clientes y <i>disminuye considerablemente</i> el precio de tu suscripción. ¡Si tus ganancias sobrepasan el costo mensual, puedes <i>retirar</i> el excedente!

                🔹 <b>¿Cómo empezar?</b>
                - Para <i>crear</i> o <i>gestionar</i> tu configuración: usa el comando /menu
                - Si necesitas ayuda o tienes dudas: usa el comando /ayuda

                Estamos aquí para que tu experiencia sea rápida y segura. ¡A gestionar! 💪
                """;
        return "🎉 <b>¡Hola, " + nombre + " !</b> 🎉\n" + mensaje; // Usamos \n en lugar de <br>

    }

    public static String mensajeParaContinuarActualiceUsername() {
        return """
                👋 *¡Un paso más para completar tu registro!*

                Por favor, dime cómo te gustaría que te llame (puedes usar tu nombre, apodo o como prefieras):

                Ejemplo: "Juan" o "Alex"
                """;
    }

    public static String mensajeBienvenidaParaElGrupo(String nombre) {
        return "🎉 ¡Tenemos un nuevo miembro en la casa!\n" +
                "👋 Demos una cálida bienvenida a " + nombre + " !";
    }
}
