package me.dang.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @author dht
 * @date 13/08/2019
 */
public class JmsConsumer {

    public static final String ACTIVEMQ_URL = "tcp://master01:61616";
    public static final String TOPIC_NAME = "topic-danght";

    public static void main(String[] args) throws JMSException, IOException {
        //1.创建连接工厂，按照给定的url地址，采用默认的username/password
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2.通过连接工厂，获得connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //3.创建session，两个参数（事务，签收）
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4.创建目的地（具体是queue还是topic）
        Topic topic = session.createTopic(TOPIC_NAME);

        //5.创建消费者
        MessageConsumer messageConsumer = session.createConsumer(topic);
        System.out.println("Consumer#02");

        //lambda表达式
        messageConsumer.setMessageListener((message) -> {
            if (null != message && message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("----Topic received: " + textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        System.in.read();
        messageConsumer.close();
        session.close();
        connection.close();
    }

}
