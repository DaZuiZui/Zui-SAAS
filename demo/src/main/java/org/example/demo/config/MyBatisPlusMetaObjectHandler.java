package org.example.demo.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * MyBatis Plus 字段自动填充配置
 */
@Component
public class MyBatisPlusMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 创建时间（时间戳）
        this.strictInsertFill(metaObject, "createAt", Long.class, System.currentTimeMillis());
        // 创建人 - 这里暂时设置为system，后续可以从SecurityContext获取当前用户
        this.strictInsertFill(metaObject, "createBy", String.class, "system");
        // 状态默认为0（正常）
        this.strictInsertFill(metaObject, "status", Integer.class, 0);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 修改时间（时间戳）
        this.strictUpdateFill(metaObject, "updateAt", Long.class, System.currentTimeMillis());
        // 修改人 - 这里暂时设置为system，后续可以从SecurityContext获取当前用户
        this.strictUpdateFill(metaObject, "updateBy", String.class, "system");
        // 版本号自增
        this.strictUpdateFill(metaObject, "version", Integer.class, 0);
    }
}
