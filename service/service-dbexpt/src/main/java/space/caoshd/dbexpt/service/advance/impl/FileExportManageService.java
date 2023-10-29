package space.caoshd.dbexpt.service.advance.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import space.caoshd.dbexpt.bo.FileExportInstanceBO;
import space.caoshd.dbexpt.bo.FileExportWorkspaceBO;
import space.caoshd.dbexpt.po.FileExportInstancePO;
import space.caoshd.dbexpt.po.FileExportWorkspacePO;
import space.caoshd.dbexpt.po.enums.TASK_STATUS;
import space.caoshd.dbexpt.service.advance.IFileExportExecuteService;
import space.caoshd.dbexpt.service.advance.IFileExportManageService;
import space.caoshd.dbexpt.service.basic.IFileExportInstanceService;
import space.caoshd.dbexpt.service.basic.IFileExportWorkspaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FileExportManageService implements IFileExportManageService {

    @Value("${file-export.node-id:127.0.0.1}")
    private String processNode;

    public void setProcessNode(String processNode) {
        this.processNode = processNode;
    }

    @Value("${file-export.fetch-enabled:true}")
    private boolean fetchEnabled;

    public void setFetchEnabled(boolean fetchEnabled) {
        this.fetchEnabled = fetchEnabled;
    }

    @Value("${file-export.thread-pool.max-pool-size:4}")
    private int maxPoolSize;

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    @Value("${file-export.thread-pool.queue-capacity:0}")
    private int queueCapacity;

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    @Autowired
    private IFileExportInstanceService<FileExportInstancePO, FileExportInstanceBO> fileExportInstanceService;

    public void setFileExportInstanceService(
        IFileExportInstanceService<FileExportInstancePO, FileExportInstanceBO> fileExportInstanceService
    ) {
        this.fileExportInstanceService = fileExportInstanceService;
    }

    @Autowired
    protected IFileExportWorkspaceService<FileExportWorkspacePO, FileExportWorkspaceBO> fileExportWorkspaceService;

    public void setFileExportWorkspaceService(
        IFileExportWorkspaceService<FileExportWorkspacePO, FileExportWorkspaceBO> fileExportWorkspaceService
    ) {
        this.fileExportWorkspaceService = fileExportWorkspaceService;
    }

    @Autowired
    private IFileExportExecuteService fileExportService;

    public void setFileExportService(IFileExportExecuteService fileExportService) {
        this.fileExportService = fileExportService;
    }

    @Override
    public void clean() {
        // 查询正在运行中实例
        List<FileExportInstanceBO> instanceBOS = runningInstanceBOS();
        for (FileExportInstanceBO instanceBO : instanceBOS) {
            // 更新实例状态
            updateInstanceTaskStatus(instanceBO, TASK_STATUS.RUNNING, TASK_STATUS.CANCEL);
            cleanWorkSpace(instanceBO.getId());
        }
    }

    @Override
    public void info() {
        log.info("处理节点　: {}", processNode);
        log.info("获取实例　: {}", fetchEnabled ? "开" : "关");
        log.info("可用线程数: {}", maxPoolSize + queueCapacity);
    }

    @Override
    public void start() {
        // 获取可用线程数
        int fetchCnt = checkFetchCnt();
        if (fetchCnt > 0) {
            // 获取并分配实例
            fetchAndAssign(fetchCnt);
            // 加载提交并执行实例
            loadSubmitAndExport(fetchCnt);
        }
    }

    @Override
    public void rerun(Long id, String processNode) {
        // 清理工作区
        cleanWorkSpace(id);
        // 重新运行实例
        rerunInstance(id, processNode);
    }

    private void rerunInstance(Long id, String processNode) {
        FileExportInstanceBO instanceBO = fileExportInstanceService.findById(id);
        if (processNode == null) {
            // 继续使用原处理节点重跑
            instanceBO.setTaskStatus(1);
        } else {
            // 重新指定重跑节点
            instanceBO.setTaskStatus(0);
            instanceBO.setProcessNode(processNode);
        }
        instanceBO.setDoneFileCnt(0);

        FileExportInstancePO query = new FileExportInstancePO();
        query.setId(id);
        query.setVersion(instanceBO.getVersion());
        int updateCnt = fileExportInstanceService.update(instanceBO, query);
        if (updateCnt > 0) {
            log.info("实例: {} 重置成功.", instanceBO.getId());
        } else {
            log.error("实例: {} 重置失败.", instanceBO.getId());
        }
    }

    private void cleanWorkSpace(Long instanceId) {
        FileExportWorkspacePO query = new FileExportWorkspacePO();
        query.setInstanceId(instanceId);
        List<FileExportWorkspaceBO> fileExportWorkspaceBOS = fileExportWorkspaceService.list(query);
        fileExportWorkspaceService.cleanWorkSpace(fileExportWorkspaceBOS);
    }

    private List<FileExportInstanceBO> runningInstanceBOS() {
        FileExportInstancePO query = new FileExportInstancePO();
        query.setTaskStatus(TASK_STATUS.RUNNING.code());
        query.setProcessNode(processNode);
        return fileExportInstanceService.list(query);
    }

    private boolean updateInstanceTaskStatus(
        FileExportInstanceBO instanceBO, TASK_STATUS oldStatus, TASK_STATUS newStatus
    ) {
        FileExportInstancePO query = new FileExportInstancePO();
        query.setId(instanceBO.getId());
        query.setTaskStatus(oldStatus.code());
        query.setProcessNode(processNode);

        instanceBO.setTaskStatus(newStatus.code());
        return 1 == fileExportInstanceService.update(instanceBO, query);
    }

    private int checkFetchCnt() {
        List<FileExportInstanceBO> runningInstanceBOS = runningInstanceBOS();
        int runningCnt = runningInstanceBOS.size();
        int avaliableCnt = maxPoolSize + queueCapacity - runningCnt;
        if (avaliableCnt < 0) {
            log.warn("无闲置线程可用, 运行线程数量: {}, 可用线程数量: {}", runningCnt, avaliableCnt);
        } else {
            log.info("运行线程数量: {}, 可用线程数量: {}", runningCnt, avaliableCnt);
        }
        return avaliableCnt;
    }

    private void fetchAndAssign(int fetchCnt) {
        if (fetchEnabled) {
            // 获取实例开关开启  循环获取实例
            for (FileExportInstanceBO instanceBO : fetch(fetchCnt)) {
                if (checkReady(instanceBO)) {
                    // 已准备完成
                    // 分配实例
                    assign(instanceBO);
                }
            }
        }
    }

    private void loadSubmitAndExport(int fetchCnt) {
        // 循环装载实例
        for (FileExportInstanceBO instanceBO : load(fetchCnt)) {
            if (checkReady(instanceBO)) {
                // 已准备完成
                // 提交实例
                submit(instanceBO);
                // 执行实例
                export(instanceBO);
            }
        }
    }

    private boolean checkReady(FileExportInstanceBO instanceBO) {
        if (instanceBO.getParentId() == null) {
            // 不依赖其他实例 可直接分配实例
            return true;
        }

        // 获取父实例的文件总数
        Integer parentFileCnt = parentInstanceFileCnt(instanceBO.getParentId());
        if (parentFileCnt == null) {
            // 父实例未定义, 暂不处理实例
            log.info("实例: {} 父实例未定义, 暂不处理实例.", instanceBO.getParentId());
            return false;
        }

        // 获取子实例个数
        Long subFileCnt = subInstanceFileCnt(instanceBO.getParentId());
        if (parentFileCnt != subFileCnt.longValue()) {
            // 子实例定义总数与父实例定义个数不一致, 暂不处理实例
            log.info("实例: {} 子实例定义总数与父实例定义个数不一致, 暂不处理实例.", instanceBO.getParentId());
            return false;
        }

        // 满足条件 可分配实例
        return true;
    }

    private List<FileExportInstanceBO> fetch(int fetchCnt) {
        // where
        QueryWrapper<FileExportInstancePO> wrapper = new QueryWrapper<>();
        wrapper.eq("task_status", TASK_STATUS.UNASSIGNED.code());
        wrapper.and(x -> x.eq("file_cnt", 1).or(y -> {
            y.gt("file_cnt", 1);
            y.apply("file_cnt = done_file_cnt");
        }));
        wrapper.last("limit " + fetchCnt);
        // execute
        return fileExportInstanceService.list(wrapper);
    }

    private void assign(FileExportInstanceBO instanceBO) {
        if (instanceBO.getParentId() == null) {
            // 更新单个实例
            instanceBO.setProcessNode(processNode);
            instanceBO.setTaskStatus(TASK_STATUS.ASSIGNED.code());

            FileExportInstancePO query = new FileExportInstancePO();
            query.setId(instanceBO.getId());
            query.setTaskStatus(TASK_STATUS.UNASSIGNED.code());
            int update = fileExportInstanceService.update(instanceBO, query);
            if (update == 1) {
                // 更新成功
                log.info("实例: {} 已分配节点: {}.", instanceBO.getId(), instanceBO.getProcessNode());
            }
        } else {
            // 更新实例及其关联实例
            FileExportInstancePO query = new FileExportInstancePO();
            query.setVersion(instanceBO.getVersion());
            query.setProcessNode(processNode);
            query.setParentId(instanceBO.getParentId());
            int update = fileExportInstanceService.updateAssociatedTaskStatus(query);
            if (update > 1) {
                // 更新成功
                log.info("实例: {} 和与它关联的{}个实例已分配.", instanceBO.getId(), update - 1);
            }
        }
    }


    private Long subInstanceFileCnt(Long id) {
        FileExportInstancePO query = new FileExportInstancePO();
        query.setParentId(id);
        return fileExportInstanceService.count(query);
    }

    private Integer parentInstanceFileCnt(Long id) {
        FileExportInstanceBO parentInstanceBO = fileExportInstanceService.findById(id);
        if (parentInstanceBO == null) {
            return null;
        }
        return parentInstanceBO.getFileCnt();
    }

    private List<FileExportInstanceBO> load(int fetchCnt) {
        QueryWrapper<FileExportInstancePO> query = new QueryWrapper<>();
        query.eq("task_status", TASK_STATUS.ASSIGNED.code());
        query.eq("process_node", processNode);
        query.last("limit " + fetchCnt);
        return fileExportInstanceService.list(query);
    }

    private void submit(FileExportInstanceBO instanceBO) {
        boolean updateFlag = updateInstanceTaskStatus(instanceBO, TASK_STATUS.ASSIGNED, TASK_STATUS.RUNNING);
        if (updateFlag) {
            instanceBO.setVersion(instanceBO.getVersion() + 1);
            log.info("实例: {} 已提交.", instanceBO.getId());
        }
    }

    private void export(FileExportInstanceBO instanceBO) {
        fileExportService.export(instanceBO);
    }

}
