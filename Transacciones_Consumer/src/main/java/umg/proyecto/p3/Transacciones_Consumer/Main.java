package umg.proyecto.p3.Transacciones_Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.universidad.proyecto.model.Transaccion;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {

    private static final String POST_URL = "https://7e0d9ogwzd.execute-api.us-east-1.amazonaws.com/default/guardarTransacciones";
    private static final String[] BANCOS = {"BAC", "BANRURAL", "BI", "GYT"};
    private static final String COLA_DUPLICADOS = "cola_duplicados";
    private static final String COLA_ERRORES = "cola_errores";

    private static final Set<String> idsProcesados = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            ObjectMapper mapper = new ObjectMapper();

            HttpClient httpClient = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

           
            channel.queueDeclare(COLA_DUPLICADOS, true, false, false, null);
            channel.queueDeclare(COLA_ERRORES, true, false, false, null);

            
            Map<String, Object> argumentosPrioridad = new HashMap<>();
            argumentosPrioridad.put("x-max-priority", 10);

            System.out.println(" [*] Esperando transacciones. Prioridad Máxima: 10. CTRL+C para salir.");

            for (String banco : BANCOS) {
                
                channel.queueDeclare(banco, true, false, false, argumentosPrioridad);

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String jsonMensaje = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    
                    
                    Integer prioridad = delivery.getProperties().getPriority();
                    if (prioridad == null) prioridad = 0;

                    try {
                        Transaccion tx = mapper.readValue(jsonMensaje, Transaccion.class);
                        String idActual = tx.getIdTransaccion();

                        if (idsProcesados.contains(idActual)) {
                            
                            imprimirLog(idActual, "DUPLICADA", COLA_DUPLICADOS, banco, prioridad);
                            channel.basicPublish("", COLA_DUPLICADOS, null, delivery.getBody());
                            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                        } else {
                           
                            idsProcesados.add(idActual);
                            imprimirLog(idActual, "PROCESADA", "API_AWS_POST", banco, prioridad);

                            boolean exitoso = enviarACloud(httpClient, jsonMensaje);

                            if (exitoso) {
                                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                                System.out.println(" [OK] Confirmado en AWS y Rabbit.");
                            } else {
                                
                                System.err.println(" [❌] Fallo en POST. Enviando a " + COLA_ERRORES);
                                channel.basicPublish("", COLA_ERRORES, null, delivery.getBody());
                                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                            }
                        }

                    } catch (Exception e) {
                        System.err.println(" [!] Error de formato: " + e.getMessage());
                        channel.basicPublish("", COLA_ERRORES, null, delivery.getBody());
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    }
                };

                channel.basicConsume(banco, false, deliverCallback, consumerTag -> {});
            }

        } catch (Exception e) {
            System.err.println("Error en el Consumer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void imprimirLog(String id, String estado, String destino, String banco, int prioridad) {
        System.out.println("\n-----------------------------------------");
        System.out.println("ID Transacción: " + id);
        System.out.println("Prioridad:      " + prioridad + (prioridad >= 8 ? " [ALTA]" : " [NORMAL]"));
        System.out.println("Estado:         " + estado);
        System.out.println("Cola Destino:   " + destino);
        System.out.println("Atendiendo cola: " + banco);
        System.out.println("-----------------------------------------");
    }

    private static boolean enviarACloud(HttpClient client, String jsonBody) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(POST_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200 || response.statusCode() == 201;
        } catch (Exception e) {
            return false;
        }
    }
}
