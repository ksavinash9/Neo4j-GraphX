package messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import config.ConfigurationLoader;

public class Worker {
    private static final String TASK_QUEUE_NAME = "jobs";

    public static void sendMessage(String message)
            throws java.io.IOException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(ConfigurationLoader.getInstance().getRabbitmqNodename());
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

        channel.basicPublish( "", TASK_QUEUE_NAME,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}
