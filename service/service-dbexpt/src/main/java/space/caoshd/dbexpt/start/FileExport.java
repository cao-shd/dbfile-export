package space.caoshd.dbexpt.start;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import space.caoshd.dbexpt.service.advance.IFileExportManageService;

@Slf4j
@Component
public class FileExport implements CommandLineRunner {

    @Value("${file-export.enabled:true}")
    private boolean enabled;

    @Value("${file-export.cron:*/10 * * * * ?}")
    private String cron;

    @Autowired
    private IFileExportManageService fileExportManageService;

    public void setFileExportManageService(IFileExportManageService fileExportManageService) {
        this.fileExportManageService = fileExportManageService;
    }

    /**
     * 文件导出开关
     */
    private boolean start;

    /**
     * 重跑实例ID
     */
    private Long instanceId;

    /**
     * 重跑处理节点
     */
    private String processNode;

    /**
     * 启动入口
     */
    @Override
    public void run(String... args) {
        // 解析参数
        parseArgs(args);

        if (args.length < 2) {
            // 清理实例
            fileExportManageService.clean();
        }

        if (args.length >= 2) {
            // 重跑实例
            fileExportManageService.rerun(instanceId, processNode);
        }
    }

    /**
     * 解析启动参数
     *
     * @param args 启动参数
     */
    private void parseArgs(String[] args) {
        if (args.length == 0) {
            // 设置文件导出开关
            start = true;
        }

        if (args.length > 0) {
            // 设置文件导出开关
            start = "1".equals(args[0]);
        }

        if (args.length > 1) {
            // 设置重跑实例ID
            instanceId = Long.valueOf(args[1]);
        }

        if (args.length > 2) {
            // 设置重跑实例节点
            processNode = args[2];
        }

        if (enabled && start) {
            fileExportManageService.info();
        }
    }

    @Scheduled(cron = "*/10 * * * * ?")
    public void start() {
        if (enabled && start) {
            fileExportManageService.start();
        }
    }

}
