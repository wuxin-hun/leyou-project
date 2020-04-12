package com.leyou.item.web;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description 这个界面里面视频上实现了查询的功能，并没有实现增加删除修改的功能，视频上说自己去做作业
 * 规格参数-》种类-》点击右边的界面进去的界面：其中修改的请求：api.leyou.com/api/item/spec/param PUT请求 
 * 删除的请求：api.leyou.com/api/item/spec/param/8
 * @Author TT Hun
 * @Data 2019/12/7 21:28
 */
@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specService;

    /**
     * 根据分类id查询规格组，请求的api是api.leyou.com/api/item/spec/group/76
     * 商品管理-》规格参数-》选择左边的分类，对应右边显示id,组名和操作。
     * 要找到需要返回什么东西就需要在js的界面里面去看返回了什么这个定位到SpecGroup.vue这个界面
     * 根据前台界面知道返回一个规格组的集合。前台界面Items对应的是数组，对应的后台界面返给的就应该是一个集合
     *
     * 查询改分类下的组的所有信息没有分页由于请求的时候没有传递分页信息，所以不需要做分页
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupByCid(@PathVariable("cid")Long cid) {
        return ResponseEntity.ok(specService.queryGroupByCid(cid));
    }

    /**
     * 根据id查询规格参数
     * 查询参数的集合，其中一个是商品管理中新增商品界面 =》基本信息=》所属商品分类发起请求：api.leyou.com/api/item/spec/param?cid=77，返回
     * 还有一个是商品管理-》规格参数-》点击右边界面的组名进入到该组下的值的查询。发起请求api.leyou.com/api/item/spec/param?gid=1。返回值：当前组下的所有
     * 一旦写2个的话，2个请求路径就会变成完全一样的，只不过就是方法参数不一样，导致不知道进入到哪个方法里面了就会产生冲冲突，就是用下面你这种写法。
     * 第三个参数是根据是否要不要搜索去查询，他是准备的，网站上还没用到。
     * @param gid 组id
     * @param cid 分类id
     * @param searching 是否搜索
     * @return
     */
    //这个地方的request请求是api.leyou.com/api/item/spec/params?gid=1所以这个地方是用问号拼接的没有使用路径占位符
    // 不能像上面那种用@PathVariable取到参数

    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParamList(
            //三个字段可以用也可以不用，一个根据gid组查询，一个根据cid查询，根据是否要求所查询
            @RequestParam(value = "gid" ,required = false) Long gid,
            @RequestParam(value = "cid" ,required = false) Long cid,
            @RequestParam(value = "searching", required = false) Boolean searching){
        return ResponseEntity.ok(specService.queryParamList(gid,cid,searching));
    }

    /**
     * 根据id查询组内的group的信息
     * @param cid
     * @return
     */
    @GetMapping("group/param/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsWithParam(@PathVariable("cid")Long cid){
        List<SpecGroup> groups = specService.queryGroupsWithParam(cid);
        if(CollectionUtils.isEmpty(groups)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(groups);
    }

}
