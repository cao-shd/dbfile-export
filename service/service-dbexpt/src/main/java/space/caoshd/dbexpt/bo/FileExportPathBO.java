package space.caoshd.dbexpt.bo;

import space.caoshd.common.service.bo.BaseBO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 导出路径配置BO
 *
 * @author caoshd
 * @since 2023-04-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FileExportPathBO extends BaseBO {

    private static final long serialVersionUID = 1L;

    /**
     * 路径名称
     */
    private String pathName;

    /**
     * 路径地址
     */
    private String location;
}
