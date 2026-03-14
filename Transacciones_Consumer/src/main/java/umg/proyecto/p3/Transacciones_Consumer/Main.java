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
import java.util.HashSet;
import java.util.Set;

public class Main {

    private static final String POST_URL = "https://7e0d9ogwzd.execute-api.us-east-1.amazonaws.com/default/guardarTransacciones";
    private static final String[] BANCOS = {"BAC", "BANRURAL", "BI", "GYT"};
    private static final String COLA_DUPLICADOS = "cola_duplicados";

  
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

            System.out.println(" [*] Esperando transacciones de los bancos. Para salir presiona CTRL+C");

            for (String banco : BANCOS) {
                channel.queueDeclare(banco, true, false, false, null);

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String jsonMensaje = new String(delivery.getBody(), StandardCharsets.UTF_8);

                    try {
                        Transaccion tx = mapper.readValue(jsonMensaje, Transaccion.class);
                        String idActual = tx.getIdTransaccion();

                      
                        if (idsProcesados.contains(idActual)) {
                        
                            System.out.println("\n-----------------------------------------");
                            System.out.println("ID Transacción: " + idActual);
                            System.out.println("Estado: DUPLICADA");
                            System.out.println("Cola Destino: " + COLA_DUPLICADOS);
                            System.out.println("Atendiendo cola: " + banco);
                            System.out.println("-----------------------------------------");

                            
                            channel.basicPublish("", COLA_DUPLICADOS, null, jsonMensaje.getBytes(StandardCharsets.UTF_8));
                            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                        } else {
                            idsProcesados.add(idActual);

                            System.out.println("\n-----------------------------------------");
                            System.out.println("ID Transacción: " + idActual);
                            System.out.println("Estado: PROCESADA");
                            System.out.println("Cola Destino: API_AWS_POST");
                            System.out.println("Atendiendo cola: " + banco);
                            System.out.println("-----------------------------------------");

                            boolean exitoso = enviarACloud(httpClient, jsonMensaje);

                            if (exitoso) {
                                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                                System.out.println(" Transacción guardada en la nube y confirmada.");
                            } else {
                                System.err.println(" [❌] Error al enviar a la nube. Reintentando...");
                                channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
                            }
                        }

                    } catch (Exception e) {
                        System.err.println(" [!] Error procesando el mensaje: " + e.getMessage());
                        channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
                    }
                };

                channel.basicConsume(banco, false, deliverCallback, consumerTag -> {});
            }

        } catch (Exception e) {
            System.err.println("Error en el Consumer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean enviarACloud(HttpClient client, String jsonBody) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(POST_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(" Respuesta de Amazon: " + response.statusCode());
            return response.statusCode() == 200 || response.statusCode() == 201;

        } catch (Exception e) {
            System.err.println(" Fallo de conexión con Amazon: " + e.getMessage());
            return false;
        }
    }
}
