package dev.yonel.wireguardbot.common.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.simmetrics.StringMetric;
import org.simmetrics.metrics.StringMetrics;

/**
 * Clase de utilidad para encontrar la mejor coincidencia entre una cadena de
 * entrada
 * y una lista de opciones con múltiples alias.
 *
 * Utiliza la métrica de Levenshtein (a través de SimMetrics) para calcular la
 * similitud
 * entre cadenas de texto. Ideal para corregir errores tipográficos o
 * identificar variantes.
 */
public class Match {

    /**
     * Métrica de cadena de texto utilizada para calcular la similitud entre
     * cadenas.
     * En este caso, se utiliza la métrica de Levenshtein, que mide la distancia
     * entre dos secuencias de caracteres.
     */
    private static StringMetric metric = StringMetrics.levenshtein();

    /**
     * Encuentra la mejor coincidencia para una entrada dada dentro de una lista de
     * mapas.
     * Cada mapa contiene una única entrada donde la clave representa una opción
     * y el valor es un array de alias válidos para esa opción.
     *
     * @param input    Entrada proporcionada por el usuario.
     * @param opciones Lista de mapas con claves y sus respectivos alias.
     * @return La clave de la {@code opción} que contiene el alias más similar (si
     *         supera el umbral),
     *         o {@code null} si ninguna coincidencia alcanza el umbral mínimo.
     */
    public static String findBestMatch(String input, List<Map<String, String[]>> opciones) {

        return opciones.stream()
                // Convierte la lista de mapas en un stream de entradas (clave-valor)
                // individuales.
                .flatMap(map -> map.entrySet().stream())
                // Mapea cada entrada a un objeto MatchResult que contiene la clave (opción)
                // y la máxima similitud entre la entrada y sus alias.
                .map(entry -> new MatchResult((entry.getKey()), getMaxSimilarity(input, entry.getValue())))
                // Encuentra el MatchResult con la similitud más alta.
                .max(Comparator.comparingDouble(MatchResult::similarity))
                // Filtra el resultado máximo si su similitud es mayor que el umbral definido
                // (0.65).
                .filter(match -> match.similarity() > 0.65)
                // Si se encuentra una coincidencia que pasa el filtro, extrae la clave (la
                // opción).
                .map(MatchResult::key)
                // Si no se encuentra ninguna coincidencia que cumpla con el umbral, devuelve
                // null.
                .orElse(null);
    }

    /**
     * Encuentra la mejor coincidencia para una entrada dentro de un mapa
     * donde cada clave está asociada a múltiples alias.
     *
     * @param input Entrada a evaluar.
     * @param map   Mapa con claves y sus alias.
     * @return Clave con mayor similitud si supera el umbral, o cadena vacía si no
     *         hay coincidencia válida.
     */
    public static String findBestMatch(String input, Map<String, String[]> map) {
        String bestMatch = "";
        double maxSimilarity = 0.0;

        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            String key = entry.getKey();
            String[] aliases = entry.getValue();

            for (String aliase : aliases) {
                double similarity = getSimilarity(input.toLowerCase(), aliase.toLowerCase());
                if (similarity > maxSimilarity && similarity > 0.65) {
                    maxSimilarity = similarity;
                    bestMatch = key;
                }
            }
        }

        return bestMatch;
    }

    /**
     * Encuentra la cadena más parecida dentro de una lista.
     *
     * @param input Entrada del usuario.
     * @param lista Arreglo de posibles valores a comparar.
     * @return Cadena con mayor similitud si supera el umbral, o cadena vacía.
     */
    public static String findBestMatch(String input, String[] lista) {
        String bestMatch = "";
        double maxSimilarity = 0.0;

        for (String entry : lista) {
            double similarity = getSimilarity(input, entry);
            if (similarity > maxSimilarity && similarity > 0.65) {
                maxSimilarity = similarity;
                bestMatch = entry;
            }
        }

        return bestMatch;
    }

    /**
     * Calcula la similitud máxima entre una entrada y un arreglo de cadenas.
     *
     * @param input Entrada del usuario.
     * @param lista Lista de posibles coincidencias.
     * @return Valor de similitud más alto que supere el umbral, o 0.0 si ninguna
     *         coincidencia lo alcanza.
     */
    public static double findMaxSimilarity(String input, String[] lista) {
        double maxSimilarity = 0.0;

        for (String entry : lista) {
            double similarity = getSimilarity(input, entry);
            if (similarity > maxSimilarity && similarity > 0.65) {
                maxSimilarity = similarity;
            }
        }

        return maxSimilarity;
    }

    /**
     * Calcula la máxima similitud entre la entrada y un conjunto de alias.
     *
     * @param input   Entrada a comparar.
     * @param aliases Array de alias.
     * @return Similitud máxima encontrada.
     */
    private static double getMaxSimilarity(String input, String[] aliases) {
        double max = 0.0;
        // Itera sobre cada alias en el array.
        for (String alias : aliases) {
            // Calcula la similitud entre la entrada (en minúsculas) y el alias (en
            // minúsculas).
            double score = getSimilarity(input, alias);
            // Si la puntuación actual es mayor que la máxima encontrada hasta ahora,
            // actualiza el valor máximo.
            if (score > max) {
                max = score;
            }
        }
        // Devuelve la puntuación de similitud máxima encontrada.
        return max;
    }

    /**
     * Calcula la similitud entre dos cadenas ignorando mayúsculas y minúsculas.
     *
     * @param input Entrada del usuario.
     * @param alias Posible coincidencia.
     * @return Valor de similitud (entre 0.0 y 1.0).
     */
    private static double getSimilarity(String input, String alias) {
        return metric.compare(input.toLowerCase(), alias.toLowerCase());
    }

    /**
     * Estructura auxiliar para almacenar el resultado de una comparación:
     * una clave y su valor de similitud.
     *
     * @param key        Clave de la opción.
     * @param similarity Valor de similitud.
     */
    private record MatchResult(String key, double similarity) {
    }
}