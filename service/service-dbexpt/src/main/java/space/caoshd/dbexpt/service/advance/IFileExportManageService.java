package space.caoshd.dbexpt.service.advance;

/**
 * 文件导出管理 服务类
 *
 * @author caoshd
 * @since 2023-04-29
 */
public interface IFileExportManageService {

    /**
     * 显示信息
     */
    void info();

    /**
     * 程序启动时 清理实例状态
     */
    void clean();

    /**
     * 启动程序
     */
    void start();

    /**
     * 重新执行实例 使用新的处理节点
     *
     * @param id          实例ID
     * @param processNode 处理节点
     */
    void rerun(Long id, String processNode);

}
