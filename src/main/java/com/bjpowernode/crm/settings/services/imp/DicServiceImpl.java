package com.bjpowernode.crm.settings.services.imp;

import com.bjpowernode.crm.settings.dao.DicTypeDao;
import com.bjpowernode.crm.settings.dao.DicValueDao;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.services.DicService;
import com.bjpowernode.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {

    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);


    @Override
    public Map<String, List<DicValue>> getAll() {

        Map<String, List<DicValue>> map =new HashMap<>();

        //将字典类型列表取出
        List<DicType> dtList = dicTypeDao.getTypeList();

        //将字典类型列表遍历
        for (DicType dt: dtList ) {

            //取得每一种类型的字典类型编码
            String code = dt.getCode();

            //根据每一个字典类型取出字典值列表
            List<DicValue> dvList = dicValueDao.getListByCode(code);

            //按code类型将字典值列表打包到map中
            map.put(code+"List",dvList);

        }

        return map;
    }
}
