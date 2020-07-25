package me.danght.activiti.interceptor;

import org.activiti.engine.impl.agenda.AbstractOperation;
import org.activiti.engine.impl.interceptor.DebugCommandInvoker;
import org.activiti.engine.logging.LogMDC;

/**
 * @author DangHT
 * @date 2020/07/23
 */
public class MDCCommandInvoker extends DebugCommandInvoker {

    @Override
    public void executeOperation(Runnable runnable) {
        boolean mdcEnabled = LogMDC.isMDCEnabled();
        LogMDC.setMDCEnabled(true);
        if (runnable instanceof AbstractOperation) {
            AbstractOperation operation = (AbstractOperation) runnable;

            if (operation.getExecution() != null) {
                LogMDC.putMDCExecution(operation.getExecution());
            }
        }
        super.executeOperation(runnable);
        LogMDC.clear();
        //若LogMDC之前是不生效的，退出时还原不生效状态
        if (!mdcEnabled) {
            LogMDC.setMDCEnabled(false);
        }
    }

}
