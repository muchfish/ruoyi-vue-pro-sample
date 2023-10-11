package cn.iocoder.yudao.module.system.controller.admin.dict;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.dict.vo.type.*;
import cn.iocoder.yudao.module.system.convert.dict.DictTypeConvert;
import cn.iocoder.yudao.module.system.dal.dataobject.dict.DictTypeDO;
import cn.iocoder.yudao.module.system.service.dict.DictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 字典类型")
@RestController
@RequestMapping("/system/dict-type")
@Validated
public class DictTypeController {

    @Resource
    private DictTypeService dictTypeService;

    @PostMapping("/create")
    @Operation(summary = "创建字典类型")
    public CommonResult<Long> createDictType(@Valid @RequestBody DictTypeCreateReqVO reqVO) {
        Long dictTypeId = dictTypeService.createDictType(reqVO);
        return success(dictTypeId);
    }

    @PutMapping("/update")
    @Operation(summary = "修改字典类型")
    public CommonResult<Boolean> updateDictType(@Valid @RequestBody DictTypeUpdateReqVO reqVO) {
        dictTypeService.updateDictType(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除字典类型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Boolean> deleteDictType(Long id) {
        dictTypeService.deleteDictType(id);
        return success(true);
    }

    @Operation(summary = "/获得字典类型的分页列表")
    @GetMapping("/page")
    public CommonResult<PageResult<DictTypeRespVO>> pageDictTypes(@Valid DictTypePageReqVO reqVO) {
        return success(DictTypeConvert.INSTANCE.convertPage(dictTypeService.getDictTypePage(reqVO)));
    }

    @Operation(summary = "/查询字典类型详细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @GetMapping(value = "/get")
    public CommonResult<DictTypeRespVO> getDictType(@RequestParam("id") Long id) {
        return success(DictTypeConvert.INSTANCE.convert(dictTypeService.getDictType(id)));
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "获得全部字典类型列表", description = "包括开启 + 禁用的字典类型，主要用于前端的下拉选项")
    // 无需添加权限认证，因为前端全局都需要
    public CommonResult<List<DictTypeSimpleRespVO>> getSimpleDictTypeList() {
        List<DictTypeDO> list = dictTypeService.getDictTypeList();
        return success(DictTypeConvert.INSTANCE.convertList(list));
    }


}
