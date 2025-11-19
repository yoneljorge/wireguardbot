package dev.yonel.wireguardbot.message_manager.messages;

public class BienvenidaMessage {
    public static String message(String nombre) {

        String mensaje = """
                ¡Bienvenido/a a <b>WireGuard VPN Bot</b>! 🚀<br>
                <br>
                Somos tu solución de VPN ultra-rápida y segura, totalmente autogestionable desde aquí.<br>
                <br>
                🛡️ <b>Beneficios Clave:</b><br>
                - ✅ <b>Máxima Velocidad:</b> Disfruta de una conexión <i>súper-rápida</i> (ping bajo) ideal para juegos y streaming.<br>
                - 🛡️ <b>Seguridad Total:</b> Bloqueamos anuncios molestos, rastreadores (tracking) y publicidad de redes sociales, protegiendo tu privacidad.<br>
                - ⚡ <b>Split Tunneling (Túnel Dividido):</b> Soporte ideal para juegos como League of Legends. Esto mejora la latencia y <i>disminuye tu consumo de megas</i> al solo enrutar el tráfico necesario por la VPN.<br>
                  - <i>Requisito:</i> Para usar esta función, descarga el cliente TunnlTo. El enlace lo encuentras en nuestra comunidad: <a href="https://t.me/wireguardbot_devYonel">https://t.me/wireguardbot_devYonel</a><br>
                <br>
                💰 <b>Precios, Prueba y Ganancias:</b><br>
                - 🎁 <b>PRUEBA GRATIS:</b> Tienes 3 días para probar nuestro servicio sin compromiso. ¡Solo tienes que crear tu configuración!<br>
                - 💵 <b>Suscripción Mensual:</b> Solo 100 CUP.<br>
                - 🤝 <b>Gana con Referidos:</b> Atrae nuevos clientes y <i>disminuye considerablemente</i> el precio de tu suscripción. ¡Si tus ganancias sobrepasan el costo mensual, puedes <i>retirar</i> el excedente!<br>
                <br>
                🔹 <b>¿Cómo empezar?</b><br>
                - Para <i>crear</i> o <i>gestionar</i> tu configuración: usa el comando /menu<br>
                - Si necesitas ayuda o tienes dudas: usa el comando /ayuda<br>
                <br>
                Estamos aquí para que tu experiencia sea rápida y segura. ¡A gestionar! 💪
                """;
        return "🎉 <b>¡Hola, " + nombre + " !</b> 🎉<br>" + mensaje;

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
