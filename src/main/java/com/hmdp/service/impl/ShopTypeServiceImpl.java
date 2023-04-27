package com.hmdp.service.impl;

import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    private int num=0;
    @Override
    public Result queryAll() {
        String key="cache:shop:";


        if(num>0){
            List<ShopType> res=new ArrayList<>();
            //Shop shop = JSONUtil.toBean(shopJson,Shop.class);
            for(int i=0;i<num;i++){
                Integer k=i;
                String shopJson = stringRedisTemplate.opsForValue().get(key+k.toString());
                res.add(JSONUtil.toBean(shopJson,ShopType.class));

            }
            return Result.ok(res);
        }
        List<ShopType> typeList = query().orderByAsc("sort").list();
        
        num=typeList.size();
        //System.out.println(typeList.toString());
        if(typeList==null){
            return Result.fail("种类不存在");
        }
        Integer cur=0;

        for(ShopType shopType:typeList){
            //System.out.println(cur);
            stringRedisTemplate.opsForValue().set(key+cur.toString(),JSONUtil.toJsonStr(shopType));
            cur++;
        }

        return Result.ok(typeList);
    }
}
