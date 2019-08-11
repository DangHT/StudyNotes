package me.dang.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 消息生产者demo
 * 注意写对 ACTIVEMQ_URL 和 QUEUE_NAME
 * 运行ActiveMQ服务
 * 运行本代码
 * 在浏览器中输入MQ服务地址，端口号8161查看queue信息
 * @author dht
 * @date 11/08/2019
 */
public class JmsProducer {

    public static final String ACTIVEMQ_URL = "tcp://master01:61616";
    public static final String QUEUE_NAME = "queue01";

    public static void main(String[] args) throws JMSException {
        //1.创建连接工厂，按照给定的url地址，采用默认的username/password
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2.通过连接工厂，获得connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //3.创建session，两个参数（事务，签收）
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4.创建目的地（具体是queue还是topic）
        Queue queue = session.createQueue(QUEUE_NAME);

        //5.创建消息的生产者
        MessageProducer messageProducer = session.createProducer(queue);
        //6.通过使用messageProducer生产3条消息发送到MQ队列中
        for (int i = 1; i <= 3; i++) {
            //7.创建消息
            TextMessage textMessage = session.createTextMessage("msg---" + i);
            //8.通过messageProducer发送给MQ
            messageProducer.send(textMessage);
        }

        //9.关闭资源
        messageProducer.close();
        session.close();
        connection.close();

        System.out.println("---消息发布完成---");
    }

}
