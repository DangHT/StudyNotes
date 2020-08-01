package me.danght.activiti.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author DangHT
 * @date 2020/07/30
 */
public class MyTakeJavaDelegate implements JavaDelegate, Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyTakeJavaDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOGGER.info("run my take java delegate");
    }

}
