package space.caoshd.dbexpt.service.advance.impl;

import space.caoshd.dbexpt.bo.FileExportInstanceBO;
import space.caoshd.dbexpt.service.advance.IFileExportExecuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Primary
@Service
public class FileExportExecuteServiceAsync implements IFileExportExecuteService {

    @Autowired
    private IFileExportExecuteService fileExportService;

    public void setFileExportService(IFileExportExecuteService fileExportService) {
        this.fileExportService = fileExportService;
    }

    @Override
    @Async("fileExportThreadPoolTaskExecutor")
    public void export(FileExportInstanceBO instanceBO) {
        fileExportService.export(instanceBO);
    }

}
