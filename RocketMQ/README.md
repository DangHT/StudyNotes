# RocketMQ样例测试
启动测试前先修改NamesrvAddr

注意：

1. 启动NameServer时显示地指定IP地址

   ```bash
   nohup sh bin/mqnamesrv -n 192.168.0.106:9876 &
   ```

2. 修改`conf/broker.conf` 添加 `brokerIP1=192.168.0.106`

3. 启动BrokerServer时指定broker配置

   ```bash
   nohup sh bin/mqbroker -n 192.168.0.106:9876 -c conf/broker.conf autoCreateTopicEnable=true &
   ```

   

