package me.dang.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * 消息接收者
 * @author dht
 * @date 12/08/2019
 */
public class JmsConsumer {

    public static final String ACTIVEMQ_URL = "tcp://master01:61616";
    public static final String QUEUE_NAME = "queue01";

    public static void main(String[] args) throws JMSException, IOException {
        //1.创建连接工厂，按照给定的url地址，采用默认的username/password
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2.通过连接工厂，获得connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //3.创建session，两个参数（事务，签收）
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4.创建目的地（具体是queue还是topic）
        Queue queue = session.createQueue(QUEUE_NAME);

        //5.创建消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);

        /*
        同步阻塞方式（receive()）
        订阅者或接收者调用MessageConsumer的receive()方法来接受消息，receive方法在能够收到消息之前（或超时前）将一直阻塞
        */
        /*
        while (true) {
            TextMessage textMessage = (TextMessage) messageConsumer.receive(4000L);
            if (null != textMessage) {
                System.out.println("----received: " + textMessage.getText());
            } else {
                break;
            }
        }
        messageConsumer.close();
        session.close();
        connection.close();
         */
        /*
        通过监听方式来接收消息，异步非阻塞方式
        订阅者或接收者通过MessageConsumer的setMeassgeListener注册一个消息监听器
        当消息到达后，系统自动调用监听器MessageListener的onMessage方法
         */
        messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (null != message && message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("----received: " + textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        System.in.read(); //暂停控制台
        messageConsumer.close();
        session.close();
        connection.close();
    }

}
