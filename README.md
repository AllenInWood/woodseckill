# woodseckill
[![MIT License](https://img.shields.io/github/license/mashape/apistatus.svg)](LICENSE)

## Features
* Users login with distributed session with twice MD5 and jsr303 attrs validation standard.
* Impl second kill function with support of [mysql](https://www.mysql.com/) and [redis](https://redis.io/) cache (page level cache, url level cache and object level cache).
* [Jmeter](https://jmeter.apache.org/) throughput test under 1000 * 5 threads.
* Impl static page with ajax interaction.

## Issues
During second kill process, it occurs following situations:
### Stock < 0
* ***Question*** 2 users' requests come at same time, both found seckill stock > 0. So they both call 'reduceStock' function in GoodsDao. Under this situation, stock of seckill goods would be lesss than zero.
* ***Solution*** Modify sql of 'reduceStock' in GoodsDao to "update seckill_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0".

### Duplicate kill
* ***Question*** 1 user sends 2 requests at same time, both found success kill doesn't exist for current user. So each request will kill once and finally lead to duplicate kills.
* ***Solution*** Create 'u_uid_gid' unique BTree index for seckill_order table. It will throw exception in 'createOrder' function of OrderService when occurring duplicate kill, and 'createOrder' will roll back due to 'Transactional' annotation.