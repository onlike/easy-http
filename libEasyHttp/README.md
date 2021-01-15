## 基于OKHTTP进行封装，调用思想为Builder思想，每一个Request都需要建造，而Requeest的地址、参数、头信息以及成功的回调通知都是通过`Easy.Builder`来构建，接下来简单介绍一下库的使用，30秒快速上手

### 设置全局静态头信息，可以放在Application内初始化
```java
EasyOptions.get().addGlobalHeader("type", "Android");
```

### 设置全局静态参数信息，可以放在Application内初始化
```java
EasyOptions.get().addGlobalParam("type", "Android");
```

### 设置全局动态的参数信息，可以放在Application内提前初始化
### 所谓的动态参数，指的是每次发送请求前，都会触发getGlobalContainer方法，来额外追加一些基础参数
```java
EasyOptions.get().setGlobalParamsCallBack(new EasyContainerCallBack() {
	@Override
	public Container getGlobalContainer(Container container) {
    	container.add("Nonce", UUID.randomUUID().toString());
        return container;
    }
});
```

### 设置全局动态的请求头信息，可以放在Application内提前初始化
```java
EasyOptions.get().setGlobalHeaderCallBack(new EasyContainerCallBack() {
	@Override
    public Container getGlobalContainer(Container container) {
		container.add("Device-Id", UserInfoHelper.getInstance().getDeviceId());
        return container;
   	}
});
```

### 发起一个普通POST请求，提交参数为 application/json
```java
new Easy.Builder(FinalActivitysHelper.getInstance().getTopActivity())
	.url("http://www.baidu.com")
    .addParam("limit", "10")
    .addNotNullParam("marker", marker)
    .response(response)
    .build()
    .postJson();
```

## Builder 介绍
| 方法名 | 功能介绍 |
| - | - |
| url | 目标地址 |
| addParam | 单个普通参数，key-value 形式，如果value为空依然会保留key字段 |
| addParams | 添加一组参数 |
| addNotNullParam | 普通参数，key-valuue 形式，如果value为空，不会保留key字段 |
| addHeader | 添加单个请求头 key-value 形式 |
| addHeaders | 添加一组请求头 key-value 形式 |
| addNotNullHeader | 添加非空请求头，如果value为空，不会保留key字段 |
| response | 回调监听 |

## EasyCallback 介绍
### EasyCallback是个普通的接口，EasyHttp内部提供了两个实现类供调用方使用。
### EasyJsonCallback 该实现类隐藏了failed回调，如有需求，自己重写即可，success返回来的是json数据，比较原始的数据
### EasyEntityCallback<T> 该实现类隐藏了failed回调，如有需求，自己重写即可，success会根据传入的泛型进行解析返回，支持自己继承扩展回调


# END 
