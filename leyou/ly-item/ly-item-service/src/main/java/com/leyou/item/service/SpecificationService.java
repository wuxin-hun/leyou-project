package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Description 所有的规格参数相关的
 * @Author TT Hun
 * @Data 2019/12/7 21:27
 */
@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper groupMapper;

    @Autowired
    private SpecParamMapper paramMapper;

    public List<SpecGroup> queryGroupByCid(Long cid) {
        SpecGroup group = new SpecGroup();
        group.setCid(cid);
//        根据实体类中的非空字段来查询，设置了cid的值就会根据cid来查询
        List<SpecGroup> list = groupMapper.select(group);
//        如果没查询到
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }
        return list;
    }


    /**
     * 通用Mapper在生成查询的时候会根据实体类里的字段来select。
     * @param gid
     * @param cid
     * @param searching
     * @return
     */
    public List<SpecParam> queryParamList(Long gid, Long cid, Boolean searching) {
        SpecParam param = new SpecParam();
        param.setGroupId(gid);
        param.setCid(cid);
        param.setSearching(searching);

        //上面三个字段都可能有2个为Null，那么下面这个就会根据其中的非空字段去查询
        List<SpecParam> list = paramMapper.select(param);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }
        return list;

    }

    public List<SpecParam> queryParams(Long id,Long cid,Boolean generic,Boolean searching){
        SpecParam record = new SpecParam();
        record.setGroupId(id);
        record.setCid(cid);
        record.setGeneric(generic);
        record.setSearching(searching);
        return this.paramMapper.select(record);

    }



    public List<SpecGroup> queryGroupsWithParam(Long cid) {
//        首先查询groupid，然后根据ｇｒｏｕｐｉｄ查询组内的信息
        List<SpecGroup> groups = this.queryGroupByCid(cid);
        groups.forEach(group->{
            List<SpecParam> params = this.queryParams(group.getId(),null,null,null);
            group.setParams(params);
        });
        return groups;
    }
}
