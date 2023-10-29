package space.caoshd.dbexpt.service.advance.impl;

import space.caoshd.dbexpt.bo.FileExportInstanceBO;
import space.caoshd.dbexpt.po.FileExportInstancePO;
import space.caoshd.dbexpt.po.enums.FILE_TYPE;
import space.caoshd.dbexpt.po.enums.TASK_STATUS;
import space.caoshd.dbexpt.service.advance.IFileExportExecuteService;
import space.caoshd.dbexpt.service.advance.IFileExportExecutor;
import space.caoshd.dbexpt.service.basic.IFileExportInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FileExportExecuteServiceSync implements IFileExportExecuteService {

    @Value("${file-export.node-id:127.0.0.1}")
    private String processNode;

    public void setProcessNode(String processNode) {
        this.processNode = processNode;
    }

    @Autowired
    private IFileExportInstanceService<FileExportInstancePO, FileExportInstanceBO> fileExportInstanceService;

    public void setFileExportInstanceService(
        IFileExportInstanceService<FileExportInstancePO, FileExportInstanceBO> fileExportInstanceService
    ) {
        this.fileExportInstanceService = fileExportInstanceService;
    }

    @Autowired
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void export(FileExportInstanceBO instanceBO) {
        try {
            // 选择执行器
            IFileExportExecutor fileExportExecutor = choiceFileExportExecutor(instanceBO.getFileType());

            // 执行导出
            fileExportExecutor.execute(instanceBO);

            // 导出完成
            updateExportStatusDone(instanceBO);
        } catch (Exception e) {
            // 导出失败
            Throwable throwable = e.getCause() != null ? e.getCause() : e;
            log.error("实例: {} 导出文件发生预想外异常.", instanceBO.getId(), throwable);
            updateExportStatusFail(instanceBO);
        }
    }

    private IFileExportExecutor choiceFileExportExecutor(Integer fileType) {
        String beanName = FILE_TYPE.byCode(fileType).value();
        return applicationContext.getBean(beanName, IFileExportExecutor.class);
    }

    private void updateExportStatusDone(FileExportInstanceBO instanceBO) {
        instanceBO.setTaskStatus(TASK_STATUS.DONE.code());

        FileExportInstancePO query = new FileExportInstancePO();
        query.setId(instanceBO.getId());
        query.setStatus(TASK_STATUS.RUNNING.code());
        query.setProcessNode(processNode);
        int update = fileExportInstanceService.update(instanceBO, query);

        if (update == 1) {
            log.info("实例: {} 已完成.", instanceBO.getId());
        } else {
            log.info("实例: {} 更新完成状态失败.", instanceBO.getId());
        }
    }

    private void updateExportStatusFail(FileExportInstanceBO instanceBO) {
        instanceBO.setTaskStatus(TASK_STATUS.ERROR.code());

        FileExportInstancePO query = new FileExportInstancePO();
        query.setId(instanceBO.getId());
        query.setTaskStatus(TASK_STATUS.RUNNING.code());
        query.setProcessNode(processNode);
        int update = fileExportInstanceService.update(instanceBO, query);

        if (update == 1) {
            log.info("实例: {} 已报错.", instanceBO.getId());
        } else {
            log.info("实例: {} 更新错误状态失败.", instanceBO.getId());
        }
    }

}
