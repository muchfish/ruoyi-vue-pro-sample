package cn.iocoder.yudao.module.system.controller.admin.notify;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 我的站内信")
@RestController
@RequestMapping("/system/notify-message")
@Validated
public class NotifyMessageController {


    @GetMapping("/get-unread-count")
    @Operation(summary = "获得当前用户的未读站内信数量")
    public CommonResult<Long> getUnreadNotifyMessageCount() {
        return success(0L);
    }

}
