package dev.yonel.wireguardbot.message_manager.messages;

public class ErrorMessage {

    public static String getMessage(){
        return """
            ⚠️ *¡Ups! Algo salió mal*
            
            Lo sentimos, hemos encontrado un error inesperado. Por favor:
            
            1. Intenta nuevamente
            2. Si persiste, contacta a @yoneljorge
            
            ¡Gracias por tu paciencia! 🙏
            """;
    }
}
