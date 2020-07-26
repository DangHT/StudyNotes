package me.danght.activiti.delegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author DangHT
 * @date 2020/07/26
 */
public class HelloBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloBean.class);

    public void sayHello() {
        LOGGER.info("sayHello...");
    }
}
