package dev.yonel.wireguardbot.agent.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dev.yonel.wireguardbot.common.dtos.wireguard.WireGuardPeer;

/**
 * Servicio para gestionar peers de WireGuard (agregar y eliminar)
 * Utiliza el comando 'wg' para gestionar peers dinámicamente
 */
@Service
public class WireGuardPeerService {

    private static final Logger logger = LoggerFactory.getLogger(WireGuardPeerService.class);

    /**
     * Agrega un peer a la interfaz WireGuard
     * 
     * @param peer Información del peer a agregar
     * @throws RuntimeException si hay un error al ejecutar el comando
     */
    public void addPeer(String wireguardInterface, WireGuardPeer peer) {
        if (peer.getPublicKey() == null || peer.getPublicKey().isEmpty()) {
            throw new IllegalArgumentException("La clave pública del peer es requerida");
        }
        if (peer.getAllowedIp() == null || peer.getAllowedIp().isEmpty()) {
            throw new IllegalArgumentException("La IP permitida del peer es requerida");
        }

        List<String> command = new ArrayList<>();
        command.add("sudo");
        command.add("wg");
        command.add("set");
        command.add(wireguardInterface);
        command.add("peer");
        command.add(peer.getPublicKey());
        command.add("allowed-ips");
        command.add(peer.getAllowedIp());

        executeCommand(command);
        logger.info("Peer agregado exitosamente. Clave pública: {}", peer.getPublicKey());
    }

    /**
     * Elimina un peer de la interfaz WireGuard
     * 
     * @param wireguardInterface Interfaz de WireGuard
     * @param peer        Información del peer a eliminar
     * @throws RuntimeException si hay un error al ejecutar el comando
     */
    public void removePeer(String wireguardInterface, WireGuardPeer peer) {
        if (peer.getPublicKey() == null || peer.getPublicKey().isEmpty()) {
            throw new IllegalArgumentException("La clave pública del peer es requerida");
        }

        List<String> command = new ArrayList<>();
        command.add("sudo");
        command.add("wg");
        command.add("set");
        command.add(wireguardInterface);
        command.add("peer");
        command.add(peer.getPublicKey());
        command.add("remove");

        executeCommand(command);
        logger.info("Peer eliminado exitosamente. Clave pública: {}", peer.getPublicKey());
    }

    /**
     * Verifica si un peer existe en la interfaz WireGuard
     * 
     * @param wireguardInterface Interfaz de WireGuard
     * @param peer          Información del peer
     * @return true si el peer existe, false en caso contrario
     */
    public boolean peerExists(String wireguardInterface, WireGuardPeer peer) {
        if (peer.getPublicKey() == null || peer.getPublicKey().isEmpty()) {
            return false;
        }

        try {
            List<String> command = new ArrayList<>();
            command.add("sudo");
            command.add("wg");
            command.add("show");
            command.add(wireguardInterface);
            command.add("peers");

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().equals(peer.getPublicKey())) {
                        process.waitFor();
                        return true;
                    }
                }
            }

            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            logger.error("Error al verificar si el peer existe", e);
            return false;
        }
    }

    /**
     * Ejecuta un comando del sistema y maneja errores
     * 
     * @param command Lista de argumentos del comando
     * @throws RuntimeException si el comando falla
     */
    private void executeCommand(List<String> command) {
        try {
            logger.debug("Ejecutando comando: {}", String.join(" ", command));

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();
            StringBuilder errorOutput = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
                    BufferedReader errorReader = new BufferedReader(
                            new InputStreamReader(process.getErrorStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                while ((line = errorReader.readLine()) != null) {
                    errorOutput.append(line).append("\n");
                }
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                String errorMsg = errorOutput.length() > 0
                        ? errorOutput.toString()
                        : output.toString();
                logger.error("Error al ejecutar comando. Código de salida: {}. Error: {}",
                        exitCode, errorMsg);
                throw new RuntimeException(
                        String.format("Error al ejecutar comando WireGuard. Código: %d. Error: %s",
                                exitCode, errorMsg));
            }

            if (output.length() > 0) {
                logger.debug("Salida del comando: {}", output.toString());
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Comando interrumpido", e);
        } catch (Exception e) {
            logger.error("Error inesperado al ejecutar comando WireGuard", e);
            throw new RuntimeException("Error al ejecutar comando WireGuard: " + e.getMessage(), e);
        }
    }
}
