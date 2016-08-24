# check in broker

### 功能
* 自动大象打卡
* 手动大象打卡
* 操作日志查看


### 原理
用户手动``注册``, 提交大象的登录cookie, 模拟用户行为, 调用打卡api完成打卡

### 机制

* 每天(工作日)9, 16, 20, 22开始定时运行
* 运行开始, 随机睡眠 5~25分钟, 来保证每天打卡时间不一致
* 如果某用户打卡失败, 会尝试最多3次

### HOW TO USE

说明

```
${foo} 表示这里用foo的值来替代
eg: 
www.${example}.com  当example代表百度时
替换为www.baidu.com
```

注册用户

```
${host}/cmd/addAnonymousUser?session=${session}

其中session为大象app抓包的cookie, 
使用任何代理, 抓取大象的http包, 截取其中所有cookie
其cookie类似下列形式:

"JSESSIONID=xxxxxxjkxjkxjkA7E0FF75C6B3;
_ele_uid=3372720aa2-4755-be7b-5e4d9aa6a130;
ai=1;
appId=1;
ck=682178d3-c8b5-4b32-9d15-10409a4ce466;
date=1472040343936;
deviceType=1;
dt=1;
token=6a4d6b22ccc5b26925ceb153c8cf2fea;
u=132224;
uid=132224;
_ga=GA1.2.1063283796.1468594765"

如果成功
返回体如下:
{
code: "200",
msg: "success",
data: 21
}
其中21是userId, 需要保存好
```

主动打卡

```
${host}/checkin/user/${userId}

其中userId是注册用户后得到的id

如果成功
返回体如下:

{
code: "200",
msg: "success",
data: true
}

data表示此次打卡是否成功
```



