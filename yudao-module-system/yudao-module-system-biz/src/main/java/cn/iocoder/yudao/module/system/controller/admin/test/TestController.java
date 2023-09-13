package cn.iocoder.yudao.module.system.controller.admin.test;

import cn.iocoder.yudao.module.system.controller.admin.test.vo.TestReqVO;
import cn.iocoder.yudao.module.system.controller.admin.test.vo.TestRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 *
 * @author 芋道源码
 */
@RestController("systemTestController")
@RequestMapping("/test")
@Slf4j
public class TestController {

    @GetMapping({"/get"})
    public Object get(TestReqVO reqVO) {
        log.info("请求参数={}",reqVO);
        return new TestRespVO().setName("test").setAge(10);
    }

}
