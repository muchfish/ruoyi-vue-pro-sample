package cn.iocoder.yudao.module.system.controller.admin.test;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.system.controller.admin.test.vo.TestReqVO;
import cn.iocoder.yudao.module.system.controller.admin.test.vo.TestRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.AGE_IS_NOT_ENOUGH;

/**
 * 测试
 *
 * @author 芋道源码
 */
@Tag(name = "管理后台 - 测试")
@RestController("systemTestController")
@RequestMapping("/test")
@Slf4j
public class TestController {

    @GetMapping({"/get"})
    @Operation(summary = "测试接口")
    public CommonResult<TestRespVO> get(@Valid TestReqVO reqVO) {
        log.info("请求参数={}",reqVO);
        if (reqVO.getAge() <18) {
            throw exception(AGE_IS_NOT_ENOUGH,reqVO.getAge());
        }
        return success(new TestRespVO().setName("test").setAge(10));
    }

}
