package dev.yonel.wireguardbot.message_manager.messages;

public class SalidaMessage {

    public static String mensajeDespedidaParaElGrupo(String nombre) {
        return "🚶‍♂️ ¡Alguien se escapa!\n" +
                "😄 " + nombre + " ha decidido abandonar nuestra compañía\n" +
                "📆 Nos vemos en la próxima, ¡no tardes mucho!";
    }
}
