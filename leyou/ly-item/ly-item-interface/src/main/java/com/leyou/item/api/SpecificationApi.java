package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description 规格参数API
 * @Author TT Hun
 * @Data 2020/3/23 22:53
 */
public interface SpecificationApi {

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
//
    @GetMapping("spec/params")
    List<SpecParam> queryParamList(
            //三个字段可以用也可以不用，一个根据gid组查询，一个根据cid查询，根据是否要求所查询
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "searching", required = false) Boolean searching);


    /**
     * 根据id查询组内的group的信息
     * @param cid
     * @return
     */
    @GetMapping("group/param/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsWithParam(@PathVariable("cid")Long cid);

}