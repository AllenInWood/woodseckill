# woodseckill
[![MIT License](https://img.shields.io/github/license/mashape/apistatus.svg)](LICENSE)

## Features
* Users login with distributed session with twice MD5 and jsr303 attrs validation standard.
* Impl second kill function with support of [Mysql](https://www.mysql.com/) and [Redis](https://redis.io/) cache (page level cache, url level cache and object level cache).
* [Jmeter](https://jmeter.apache.org/) throughput test under 1000 * 5 threads.
* Impl static page with ajax interaction.

## Issues
During second kill process, it occurs following situations:
### Stock < 0
* ***Question*** 2 users' requests come at same time, both found seckill stock > 0 in [SeckillController](https://github.com/AllenInWood/woodseckill/blob/8c73ec86513f76fc60a1de41462dfb4f5354e110/src/main/java/com/seckill/woodseckill/controller/SeckillController.java#L51). So they both call' function in GoodsDao. Under this situation, stock of seckill goods would be lesss than zero.
* ***Solution*** Modify [sql](https://github.com/AllenInWood/woodseckill/blob/8c73ec86513f76fc60a1de41462dfb4f5354e110/src/main/java/com/seckill/woodseckill/dao/GoodsDao.java#L22) of 'reduceStock' in GoodsDao to "update seckill_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0".

### Duplicate kill
* ***Question*** 1 user sends 2 requests at same time, both found success kill doesn't exist for current user in [SeckillController](https://github.com/AllenInWood/woodseckill/blob/8c73ec86513f76fc60a1de41462dfb4f5354e110/src/main/java/com/seckill/woodseckill/controller/SeckillController.java#L56). So each request will kill once and finally lead to duplicate kills.
* ***Solution*** Create 'u_uid_gid' unique BTree index for seckill_order table. It will [throw exception](https://github.com/AllenInWood/woodseckill/blob/8c73ec86513f76fc60a1de41462dfb4f5354e110/src/main/java/com/seckill/woodseckill/service/OrderService.java#L48) in 'createOrder' function of OrderService when occurring duplicate kill, and 'createOrder' will roll back due to ['Transactional' annotation](https://github.com/AllenInWood/woodseckill/blob/8c73ec86513f76fc60a1de41462dfb4f5354e110/src/main/java/com/seckill/woodseckill/service/OrderService.java#L31).