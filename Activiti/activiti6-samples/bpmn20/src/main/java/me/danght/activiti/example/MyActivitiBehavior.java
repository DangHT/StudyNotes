package me.danght.activiti.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.delegate.ActivityBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 暂停流程，等待执行命令
 * @author DangHT
 * @date 2020/07/31
 */
public class MyActivitiBehavior implements ActivityBehavior {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyActivitiBehavior.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOGGER.info("run my activiti behavior");
    }
}
