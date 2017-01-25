#wilddog-rest-java

基于 [Wilddog REST API](https://z.wilddog.com/rest/quickstart).
### 示例

```java

// <appId> 替换成在野狗申请的appId
String wilddog_baseUrl = "https://<appId>.wilddogio.com/rest";

// 创建一个Wilddog对象
Wilddog wilddog = new Wilddog( wilddog_baseUrl );

// "PUT" (test-map into the wd4jDemo-root)
Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
dataMap.put( "PUT-root", "This was PUT into the wd4jDemo-root" );
WilddogResponse response = wilddog.put( dataMap );
System.out.println( "\n\nResult of PUT (for the test-PUT to wd4jDemo-root):\n" + response );


// "GET" (the wd4jDemo-root)
response = wilddog.get();
System.out.println( "\n\nResult of GET:\n" + response );
System.out.println("\n");


```
更多示例 参见 `com.wilddog.client.rest.demo.Demo`

### Token生成

Token用于标识用户身份，结合 [规则表达式](https://z.wilddog.com/rule/quickstart) 做数据读写权限的控制。可以使用超级密钥本身做为token，这样的终端将获得管理员权限，读写数据操作不受规则表达式的限制。也可以使用超级密钥进行签名，自行生成标准jwt格式的token。

token格式的文档参见：[https://z.wilddog.com/rule/guide/4](https://z.wilddog.com/rule/guide/4)。

java版token生成工具：[wilddog-token-generator-java](https://github.com/WildDogTeam/wilddog-token-generator-java)。


### API列表

```java
Wilddog(String baseUrl );                       // 使用baseUrl创建一个Wilddog Sync 实例
Wilddog(String baseUrl, String secureToken);    // 使用baseUrl和token创建一个实例
get();                                          // 读取Wilddog Sync中的数据 
get(String path );                              // 读取数据 
patch(Map<String, Object> data);                // 更新数据
patch(String path, Map<String, Object> data);   // 更新数据
patch(String jsonData);                         // 更新数据
patch(String path, String jsonData);            // 更新数据
put(Map<String, Object> data );                 // 覆盖式更新数据
put(String path, Map<String, Object> data );    // 存储数据
put(String jsonData );                          // 存储数据
put(String path, String jsonData );             // 存储数据
post(Map<String, Object> data );                // 自动生成key的存储数据
post(String path, Map<String, Object> data );   // 自动生成key的存储数据
post(String jsonData );                         // 自动生成key的存储数据
post(String path, String jsonData );            // 自动生成key的存储数据
delete();                                       // 删除数据
delete(String path );                           // 删除数据
```


### License
MIT
http://wilddog.mit-license.org/