package cn.iocoder.yudao.module.system.controller.admin.test.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "管理后台 -测试接口 Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestReqVO {
    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudaoyuanma")
    private String name;
    @Schema(description = "年龄", requiredMode = Schema.RequiredMode.REQUIRED, example = "13")
    private Integer age;
}
