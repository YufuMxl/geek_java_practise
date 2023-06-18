docker run -d --name redis -p 6379:6379 \
  -v /Users/maxiaolong/mountdir/redis/redis.conf:/etc/redis/redis.conf \
  -v /Users/maxiaolong/mountdir/redis/data:/data \
  redis:6.0.9 \
  redis-server /etc/redis/redis.conf --appendonly yes

redis-benchmark -n 100000 -c 32 -t SET,GET,INCR,HSET,LPUSH,MSET -q
