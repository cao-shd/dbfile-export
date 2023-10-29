package space.caoshd.common.service.bo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class BaseBO implements Serializable {
    /**
     * 主键ID
     */
    private Long id;
    private Integer version;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String updateBy;
}
