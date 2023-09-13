package cn.iocoder.yudao.module.system.controller.admin.test.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 -测试接口 Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestReqVO {
    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudaoyuanma")
    @NotBlank(message = "名称不能为空")
    @Length(min = 4, max = 16, message = "账号长度为 4-16 位")
    private String name;
    @Schema(description = "年龄", requiredMode = Schema.RequiredMode.REQUIRED, example = "13")
    @NotNull(message = "年龄不能为空")
    @Min(value = 0,message = "最小年龄为0岁")
    @Max(value = 100,message = "最大年龄为100岁")
    private Integer age;
}
