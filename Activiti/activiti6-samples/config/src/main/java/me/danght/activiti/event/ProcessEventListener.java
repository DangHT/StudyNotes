package me.danght.activiti.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 流程 Event
 * @author DangHT
 * @date 2020/07/25
 */
public class ProcessEventListener implements ActivitiEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessEventListener.class);

    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEventType type = event.getType();
        if (ActivitiEventType.PROCESS_STARTED.equals(type)) {
            LOGGER.info("流程启动 {}\t{}", type, event.getProcessInstanceId());
        } else if (ActivitiEventType.PROCESS_COMPLETED.equals(type)) {
            LOGGER.info("流程完成 {}\t{}", type, event.getProcessInstanceId());
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
