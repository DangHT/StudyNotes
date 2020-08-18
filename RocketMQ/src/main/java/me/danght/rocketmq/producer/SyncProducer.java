package me.danght.rocketmq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * <h1>Producer端发送同步消息</h1>
 * <p>这种可靠性同步地发送方式使用的比较广泛，比如：重要的消息通知，短信通知。</p>
 * @author DangHT
 * @date 2020/08/17
 */
public class SyncProducer {

    public static void main(String[] args) throws Exception {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
        // 设置NameServer的地址
        producer.setNamesrvAddr("192.168.0.106:9876");
        // 启动Producer实例
        producer.start();
        for (int i = 0; i < 100; i++) {
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message(
                    "TopicTest",
                    "TagA",
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)
            );
            // 发送一个消息到Broker
            SendResult result = producer.send(msg);
            // 通过SendResult返回消息是否成功送达
            System.out.printf("%s%n", result);
        }
        // 如果不再发送消息，关闭Producer实例
        producer.shutdown();
    }

}
