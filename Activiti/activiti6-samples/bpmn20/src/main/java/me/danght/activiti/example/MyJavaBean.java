package me.danght.activiti.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author DangHT
 * @date 2020/07/31
 */
public class MyJavaBean implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyJavaBean.class);

    private String name;

    public MyJavaBean() {}

    public MyJavaBean(String name) {
        this.name = name;
    }

    public String getName() {
        LOGGER.info("run getName name:{}", name);
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void sayHello() {
        LOGGER.info("say Hello");
    }
}
