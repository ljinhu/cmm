package com.yi.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yi.pojo.Dict;
import com.yi.dao.DictMapper;
import com.yi.service.DictService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Override
    public List<Dict> findByTypeCode(String typeCode) {
        Wrapper<Dict> wrapper = new EntityWrapper<Dict>();
        wrapper.eq("TYPE_VALUE", typeCode);
        return this.selectList(wrapper);
    }

    @Override
    public int countByName(String name, String typeValue, String id) {
        Wrapper<Dict> wrapper = new EntityWrapper<Dict>();
        if (StringUtils.isNotEmpty(name)) {
            wrapper.eq("name", name);
        }
        if (StringUtils.isNotEmpty(typeValue)) {
            wrapper.eq("typeValue", typeValue);
        }
        if (StringUtils.isNotEmpty(id)) {
            wrapper.addFilter("id != {0}", id);
        }
        return this.selectCount(wrapper);
    }

    @Override
    public int countByValue(String value, String typeValue, String id) {
        Wrapper<Dict> wrapper = new EntityWrapper<Dict>();
        if (StringUtils.isNotEmpty(value)) {
            wrapper.eq("value", value);
        }
        if (StringUtils.isNotEmpty(typeValue)) {
            wrapper.eq("typeValue", typeValue);
        }
        if (StringUtils.isNotEmpty(id)) {
            wrapper.addFilter("id != {0}", id);
        }
        return this.selectCount(wrapper);
    }

    @Override
    public List<Dict> selectList(String typeValue, String value) {
        Wrapper<Dict> wrapper = new EntityWrapper<Dict>();
        if(StringUtils.isNotEmpty(typeValue)){
            wrapper.eq("type_value",typeValue);
        }
        if(StringUtils.isNotEmpty(value)){
            wrapper.like("value",value);
        }
        return this.selectList(wrapper);
    }

    @Override
    public List<Map<String, String>> convertToOptionData(String typeValue) {
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        List<Dict> dicts = this.selectList(typeValue, null);
        if (!CollectionUtils.isEmpty(dicts)) {
            for (Dict dict : dicts) {
                Map<String, String> map = new HashMap<>();
                map.put("id", dict.getName());
                map.put("text", dict.getName());
                listMap.add(map);
            }
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("id", "");
            map.put("text", "--请选择--");
            listMap.add(map);
        }

        return listMap;
    }
}
