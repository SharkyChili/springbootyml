本文章的产生是对一个bug的总结，是一次自底向上的记录，并不打算有一个全局的认识，以后有机会可以系统分析。  
git地址：https://github.com/fw1036994377/springbootyml.git
# bug展示
application.yml内容
``` yml
num: 0010
```
代码
```java
    @Autowired
    Environment environment;
    @Login
    @RequestMapping("/method")
    public Entity method(@LoginUser Entity entity, @RequestBody Entity entity1){
        System.out.println(environment.getProperty("num"));
        return entity;
    }
```
结果
```java
8
```
能看到环境里的属性与yml配置文件里的属性不一致  
项目里配置文件并不是10，这里做了一些简化，可以很容易猜测到，应该是读取的时候，把0010当作八进制了
# bug trace
话不多说，直接上图，如果不够清晰，可以点击链接
https://www.processon.com/view/link/5dea2236e4b07003fdfaf72d

大致流程就是：  
1：容器启动
2：触发监听
3：响应监听读取配置
4：利用snakeyaml来解析yml
5：解析map键值对
6：解析map的value（此时出了问题）

出问题的代码如下
```java
public class ConstructYamlInt extends AbstractConstruct {
        @Override
        public Object construct(Node node) {
            String value = constructScalar((ScalarNode) node).toString().replaceAll("_", "");
            int sign = +1;
            char first = value.charAt(0);
            if (first == '-') {
                sign = -1;
                value = value.substring(1);
            } else if (first == '+') {
                value = value.substring(1);
            }
            int base = 10;
            if ("0".equals(value)) {
                return Integer.valueOf(0);
            } else if (value.startsWith("0b")) {
                value = value.substring(2);
                base = 2;
            } else if (value.startsWith("0x")) {
                value = value.substring(2);
                base = 16;
                //因为我们是0010，因此进入这里，base即进制
            } else if (value.startsWith("0")) {
                value = value.substring(1);
                base = 8;
            } else if (value.indexOf(':') != -1) {
                String[] digits = value.split(":");
                int bes = 1;
                int val = 0;
                for (int i = 0, j = digits.length; i < j; i++) {
                    val += Long.parseLong(digits[j - i - 1]) * bes;
                    bes *= 60;
                }
                return createNumber(sign, String.valueOf(val), 10);
            } else {
                return createNumber(sign, value, 10);
            }
            return createNumber(sign, value, base);
        }
    }
```

![](https://user-gold-cdn.xitu.io/2019/12/6/16edad7a69c22287?w=1467&h=780&f=png&s=159043)

# 解决
既然看到了出问题的地方，那么就只要让value不以0开头就好了，拿双引号括起来就好了。（或者不用yml？哈哈哈哈）

加双引号之后
```java
0010
```

改为properties之后
```java
0010
```


# 想法
以后可以补一下spring监听机制和加载文件（包括系统属性、properties、yml的区别之类的）
