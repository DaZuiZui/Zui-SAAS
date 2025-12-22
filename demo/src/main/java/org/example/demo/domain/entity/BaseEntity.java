package org.example.demo.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 实体类基类
 * Base entity class
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     * Primary key ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建人
     * Creator
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间（时间戳）
     * Create time (timestamp)
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createAt;

    /**
     * 数据状态：0-正常 1-删除
     * Data status: 0-normal 1-deleted
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer status;

    /**
     * 修改时间（时间戳）
     * Update time (timestamp)
     */
    @TableField(fill = FieldFill.UPDATE)
    private Long updateAt;

    /**
     * 修改人
     * Updater
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    /**
     * 乐观锁版本号
     * Optimistic lock version
     */
    @Version
    @TableField(fill = FieldFill.UPDATE)
    private Integer version;
}
