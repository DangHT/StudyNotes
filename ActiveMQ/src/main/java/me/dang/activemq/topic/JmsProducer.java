package me.dang.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author dht
 * @date 13/08/2019
 */
public class JmsProducer {

    public static final String ACTIVEMQ_URL = "tcp://master01:61616";
    public static final String TOPIC_NAME = "topic-danght";

    public static void main(String[] args) throws JMSException {
        //1.创建连接工厂，按照给定的url地址，采用默认的username/password
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2.通过连接工厂，获得connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //3.创建session，两个参数（事务，签收）
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4.创建目的地（具体是queue还是topic）
        Topic topic = session.createTopic(TOPIC_NAME);

        //5.创建消息的生产者
        MessageProducer messageProducer = session.createProducer(topic);
        //6.通过使用messageProducer生产3条消息发送到MQ队列中
        for (int i = 1; i <= 3; i++) {
            //7.创建消息
            TextMessage textMessage = session.createTextMessage(TOPIC_NAME + "---" + i);
            //8.通过messageProducer发送给MQ
            messageProducer.send(textMessage);
        }

        //9.关闭资源
        messageProducer.close();
        session.close();
        connection.close();

        System.out.println("---" + TOPIC_NAME + "主题发布完成---");
    }

}
