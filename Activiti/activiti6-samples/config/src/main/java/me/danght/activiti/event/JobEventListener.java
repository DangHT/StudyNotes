package me.danght.activiti.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Job Event
 * @author DangHT
 * @date 2020/07/25
 */
public class JobEventListener implements ActivitiEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobEventListener.class);

    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEventType eventType = event.getType();
        String name = eventType.name();

        if (name.startsWith("TIMER") || name.startsWith("JOB")) {
            LOGGER.info("监听到Job事件 {}\t{}", eventType, event.getProcessInstanceId());
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
