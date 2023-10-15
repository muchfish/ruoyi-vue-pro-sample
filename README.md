### 提交大纲
#### 创建一个架子，并调通接口
1. [yudao-server]

   1. 空壳项目，作为启动使用。其他业务模块会作为依赖引入，进行整合。
   2. 调通接口

2. [yudao-dependencies]

    1. 基础 bom 文件，管理整个项目的依赖版本
    2. 管理springboot版本为2.7.13

3. [最外层pom.xml]
	1. 作为其他所有模块的父pom，管理项目的版本号和依赖
	2. 版本仲裁(`<dependencyManagement>`)中import[yudao-dependencies]，将[yudao-dependencies]的依赖管理传递给所有的子模块，进而统一管理所有模块的依赖版本
    -  `<scope>import</scope>`只能在`<dependencyManagement>`中使用，只会将被引用依赖中`<dependencyManagement>`中的依赖关系导入下来
#### 创建组件架子
1. [yudao-framework]
	1. 创建一个[yudao-framework]作为所有组件的父项目，管理子项目的依赖关系和构建配置。
		- <packaging>pom</packaging>表示该模块仅作为依赖项，不作为jar包。一般作为父项目，管理子项目的依赖关系和构建配置。

	2. 该包是技术组件，每个子包，代表一个组件。
2. [yudao-spring-boot-starter-web]
	1. 创建一个web组件空架子。用于未来实现或封装Web 框架，全局异常、API 日志等
	2. 引入spring-boot-starter-web依赖，web相关依赖会全放在这。
		- org.springframework.boot.autoconfigure.AutoConfiguration.imports 用于导入其他自动配置类（不写则不导入）。
		- YudaoWebAutoConfiguration 针对 SpringMVC 的基础封的配置类，用于自动配置针对 SpringMVC 的基础封装的各种组件和功能的类。
3. [yudao-dependencies]
	1. 对yudao-spring-boot-starter-web进行版本管理
4. [yudao-server]
	1. 移除spring-boot-starter-web依赖，使用组件[yudao-spring-boot-starter-web]提供的web功能

#### 创建banner组件和通用类、工具类jar依赖
1. [yudao-common]
   1. jar包模块，定义基础的通用类，和框架无关，所有的组件都会引用它
   2. 引入lombok和hutool 
2. [yudao-spring-boot-starter-banner]
   1. 简单的用于打banner的组件
   2. banner.txt，自定义的banner
   3. BannerApplicationRunner，在项目启动后执行日志打印，补充banner信息
   		- ApplicationRunner，Spring Boot提供的接口，在项目启动后执行run方法
3. lombok.config
   1. lombok.config是lombok的配置文件，用于配置此文件所在目录以及子目录的lombok特性，子目录中配置文件可以覆盖父目录配置



#### 创建系统管理业务模块架子[yudao-module-system]

1. [yudao-module-system]
   1. 存放管理后台系统管理相关业务，用户、部门、权限、数据字典等等
   2. 作为system模块的父pom
2. [yudao-module-system-api]
   1. system 模块 API，暴露给其它模块调用，暴露的接口由[yudao-module-system-biz]实现
3. [yudao-module-system-biz]
   1. system 模块的具体实现



#### 给管理后台和app端接口路径增加访问前缀

1. [yudao-spring-boot-starter-web]

   1. 给接口路径增加访问前缀，避免接口路径暴露（不理解）
      1. 给`**.controller.admin.**`下的接口路径增加访问前缀/admin-api
      2. 给`**.controller.app.**`下的接口路径增加访问前缀/app-api

   - WebMvcConfigurer
     - WebMvcConfigurer是一个接口，里面提供了很多web应用常用的拦截方法。通过实现该接口，可以实现web应用 跨域设置、类型转化器、自定义拦截器、页面跳转等功能。
   - configurePathMatch：路径匹配规则
     - 设置前端请求url与后端接口url的匹配规则。



#### 基于 Swagger + Knife4j 实现 API 接口文档

1. [yudao-spring-boot-starter-web]

   1. 生成接口文档，支持接口调试。地址：http://localhost:48080/doc.html#/home
   2. 编写swagger包，集成 Swagger + Knife4j 实现 API 接口文档
   3. 配置OpenAPI页面的接口信息

   - @ConditionalOnProperty：用于根据配置文件中的属性值来决定是否加载某个Bean或配置类。
     - @ConditionalOnProperty注解有以下几个常用的属性：
       - name：指定配置文件中的属性名。
       - havingValue：指定配置文件中的属性值，与name属性一起使用。
       - matchIfMissing：当配置文件中没有指定属性时，是否加载被注解的Bean或配置类。
       - prefix：指定配置文件中的属性名的前缀。
       - value：name属性的别名。
   - spring-boot-configuration-processor：用于生成配置元数据的注解处理器，使用后在配置文件（yml等）中写配置会有提示（不知道为什么没生效）
   - maven中`<optional>true</optional>`：true是用来标记一个依赖项为可选的。当一个依赖项被标记为可选时，它不会被自动包含在项目的依赖树中，也不会传递给其他依赖项。
   - Springdoc：Springdoc是一个用于生成OpenAPI（前身为swagger）文档的Spring Boot库。它通过解析应用程序中的注解和配置，自动生成API文档，并提供了一些自定义选项和扩展功能。与Swagger UI集成，可以方便地查看和测试API文档。
   - Knife4j：基于Springdoc的一个增强工具
   - knife4j-openapi3-spring-boot-starter：集成和使用Knife4j，一个用于生成和展示OpenAPI文档的工具。集成了springdoc-openapi-ui、
   - springdoc-openapi-ui：用于生成和展示OpenAPI文档的开源库，自动将你的API端点映射为OpenAPI规范（[yudao-server]中的TestController）



#### 数据校验

1. [yudao-server]

   1. 对接口入参进行数据校验

   - [spring-boot-starter-validation]：提供了对数据校验的支持。它基于Java Bean Validation规范（JSR 380）实现，可以用于对请求参数、方法参数、实体对象等进行校验。



#### 设置通用接口结果返回和异常统一处理

1. [yudao-common]
   1. 设置通用结果类CommonResult
   2. 设置异常体系
      1. ErrorCode：异常码
      2. ServiceException：业务异常
      3. ServerException：服务异常
      4. GlobalErrorCodeConstants：全局错误码枚举
      5. ServiceErrorCodeRange：业务异常的错误码区间规定
      6. ServiceExceptionUtil：业务异常工具类，便于手动抛业务异常和格式化异常信息
2. [yudao-spring-boot-starter-web]
   1. GlobalExceptionHandler：全局异常处理
3. [yudao-module-system-biz]
   1. TestController测试上述功能

#### 接入验证码和redis

1. [yudao-spring-boot-starter-redis]

   1. 创建RedisTemplate<String, Object> Bean，使用 JSON 序列化方式，支持任何对象类型的序列化/反序列化

   - JavaTimeModule：支持Java 8中的日期和时间类型的序列化和反序列化。
   - redisson：Java的分布式对象存储和缓存框架，目前仅使用到它创建RedisConnectionFactory bean，连接redis的功能

2. [yudao-spring-boot-starter-captcha]

   1. 基于 aj-captcha 实现滑块验证码

   - RedisCaptchaServiceImpl：基于SPI实现验证码缓存的set和get（redis实现）

3. [yudao-module-system-biz]

   1. 实现业务接口，验证码的获取和校验

   - @CrossOrigin：解决跨域

#### 自定义banner、开启验证码限流、全局跨域配置

1. [yudao-spring-boot-starter-banner]

   1. 自定义banner文件：MyBanner.txt

2. [yudao-spring-boot-starter-captcha]

   1. 开启一分钟内接口请求次数限制配置：req-frequency-limit-enable=true

   - CaptchaVO#browserInfo：客户端ip+userAgent，用于生成clientUid（限流开启时使用）
   - FrequencyLimitHandler：频率限制处理程序，按clientUid进行限流。（实现时使用clientUid作为缓存key，进行加锁和计数）

3. [yudao-spring-boot-starter-web]

   1. 创建CorsFilter Bean，解决跨域

   - FilterRegistrationBean：用于注册和管理过滤器（Filter）的Bean。管理生命周期或者设置过滤器的名称、URL模式、调用顺序等
   - CorsFilter：跨域filter
   - UrlBasedCorsConfigurationSource：为不同的URL路径配置不同的CORS策略（CorsConfiguration）。
   - CorsConfiguration：CORS配置

#### 集成mybatis和多数据源

1. [yudao-spring-boot-starter-mybatis]

   1. 整合mybatisplus
      1. 依赖mybatis-plus-boot-starter
      2. 开启下划线（数据库字段）转驼峰（DO字段）映射：map-underscore-to-camel-case: true
      3. 配置全局主键生成策略：id-type: AUTO（自增）
      4. 配置逻辑删除/未删除的值：logic-delete-value/logic-not-delete-value
      5. 数据库敏感数据加密密码配置：mybatis-plus.encryptor.password（应该没用到）
   2. MybatisPlus的注解
      1. @TableName：DO对应的表名，不写则取DO类名
         1. 属性autoResultMap：autoResultMap配置为true时，MyBatis会自动根据查询结果的列名和Java对象的属性名进行映射，创建结果映射。无需手动编写结果映射。
      2. @TableLogic：标注为逻辑删除字段.
   3. MybatisPlus的API
      1. BaseMapper类：Mapper 继承该接口后，无需编写 mapper.xml 文件，即可获得CRUD功能
      2. LambdaQueryWrapper：Lambda 语法使用 Wrapper（Wrapper为查询条件封装，eq、like等等）
   4. 开启多数据源支持
      1. 依赖dynamic-datasource-spring-boot-starter
   5. 配置druid数据源
      1. 依赖druid-spring-boot-starter
   6. 开启事务支持
      1. @EnableTransactionManagement
   7. 扫描mapper接口，注册映射器
   8. 配置mybatisPlus拦截器，开启分页

   - 扫描cn.iocoder.yudao包下的并且由@Mapper注解标记的接口,注册为MyBatis的映射器。lazyInitialization=true则开启延迟加载Mapper接口的实现类

     ```
     @MapperScan(value = "cn.iocoder.yudao", annotationClass = Mapper.class,
             lazyInitialization = "${mybatis.lazy-initialization:false}")
     ```

2. [yudao-module-system-biz]
   1. 编写业务接口：**/system/tenant/get-id-by-name**(使用租户名，获得租户编号)
   2. 创建TenantController、TenantService、TenantMapper



#### 使用账号密码登录

1. [yudao-module-system-biz]

   1. 实现/login接口
      1. 校验验证码
         - 服务端验证码二次校验：（猜测）验证码check接口和login接口非同一接口。为防止用户直接跳过验证码的check接口，因此会在login接口进行二次验证码校验，校验check接口下发的验证成功的票据
      2. 认证账号密码
         - 密码校验未做
      3. 下发token

2. [yudao-common]

   1. 创建ValidationUtils，提供方法内的参数校验

   - Validator：基于JSR-303标准的参数校验类
     - validate方法：执行校验操作，返回验证失败的约束
   - ConstraintViolation：验证失败的约束

#### 集成mapstruct（对象转换工具）

1. [yudao-common]

   1. 集成mapstruct的依赖，便于所有模块使用
      - mapstruct
        - Java注解处理器
        - 基于JSR 269实现，在编译期处理注解，并且读取、修改和添加抽象语法树中的内容（https://blog.csdn.net/Mango_Bin/article/details/125168370）
        - 在编译期自动生成映射转换代码，因此安全性高，速度快。

2. [yudao-module-system-biz]

   1. 创建/list-all-simple，获得全部字典数据列表接口

   - cn.iocoder.yudao.module.system.convert：存放mapstruct的对象转换类



#### RBAC权限-获取登录用户的权限信息（待完善）

1. [yudao-module-system-biz]
   1. 创建接口/get-permission-info：获取登录用户的权限信息
      - RBAC模式，通过登录用户获取用户角色信息，通过角色获取角色菜单信息
   2. 获取权限信息后，可以进入到后台页面了

#### 整合spring-security，完善登录认证

1. [yudao-spring-boot-starter-security]

   1. 自定义Filter（` TokenAuthenticationFilter`）完成token解析和用户认证

      1. 解析自定义header(头Authorization)获取token
      2. 解析token，构建用户信息
      3. 构建认证信息，完成认证（`cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils#buildAuthentication`）

   2. 配置`SecurityFilterChain`

      1. CSRF禁用
      2. 开放无需认证即可访问的接口，其他接口配置为认证才可访问
      3. 配置`TokenAuthenticationFilter`在`UsernamePasswordAuthenticationFilter`之前

      - @EnableWebSecurity：启用spring security，加载一堆相关的bean
      - SecurityFilterChain：过滤器链，spring security是基于各种过滤器实现的权限控制,`filterChain`方法就是构建各种过滤器
      - csrf：跨站请求伪造，spring security默认开启了csrf，启用后需要前端配合编码，因此关闭
      - authorizeRequests：允许根据使用RequestMatcher实现（即通过 URL 模式）限制HttpServletRequest访问。
      - antMatchers：ant模式的接口路径匹配。
      - .permitAll()：允许任何人访问（无论是否为登录用户）
      - httpSecurity.addFilterBefore：在某过滤器之前添加一个过滤器
      - UsernamePasswordAuthenticationFilter：基于用户名和密码的身份验证过滤器，是spring security默认的认证过滤器

2. [yudao-spring-boot-starter-redis]

   1. 创建RedisUtil，提供redis读写服务

3. [yudao-module-system-biz]

   1. /login接口中，在登录成功后将用户的token存入缓存中，用于` TokenAuthenticationFilter`中进行token的认证和解析，完善登录认证流程

#### 自定义免登录和免鉴权注解

1. [yudao-spring-boot-starter-security]

   1. 自定义免登录免鉴权注解

      1. @LoginFree：免登录
      2. @Authenticated：免鉴权

   2. 解析注解配置相应的权限规则（`YudaoWebSecurityConfigurerAdapter`方法`filterChain`中）

      - @PostConstruct：标记一个方法，在对象创建之后，依赖注入完成之后，自动执行该方法。

      - Multimap：多映射Map，相同的key可以有多个值。guava包提供，形如：

        > A → 1
        > A → 2
        > b → 3

      - `.permitAll()`：允许任何人访问（无论是否为登录用户）

      - `.authenticated()`：指定任何经过身份验证的用户都允许使用 URL

2. [yudao-module-system-biz]

   1. 登录之前的接口方法上使用@LoginFree进行标注，实现免登录

#### 封装数据库公共字段DO

1. [yudao-spring-boot-starter-mybatis]
   1. 抽取数据库公共字段封装成对象`BaseDO`

#### 实现部门管理功能和数据库通用字段自动填充
1. [yudao-spring-boot-starter-mybatis]

   1. `BaseDO`字段上面添加字段自动填充策略
      - fill：自动填充策略
        - DEFAULT：默认不处理
        - INSERT：插入时填充字段
        - UPDATE：更新时填充字段
        - INSERT_UPDATE：插入和更新时填充字段
      - jdbcType：字段对应的数据库类型
   2. 创建数据库通用字段自动填充控制器（见`DefaultDBFieldHandler`）
   3. 配置`MetaObjectHandler`bean

   - MetaObjectHandler：元对象字段填充控制器抽象类，实现公共字段自动写入

   - insertFill：插入时对字段的填充
   - updateFill：更新时对字段的填充
   - MetaObject：操作Java对象的工具类。它提供了一系列方法，可以方便地对Java对象进行属性的读取、写入和判断等操作。(Mybatis提供)

2. [yudao-module-system-biz]

   1. `/get-unread-count`接口暂时写一个空实现，不让页面报错
   
   2. 用户模块提供`/system/user/list-all-simple`接口，获取用户精简信息列表。供部门管理使用
   
   3. 部门管理
      1. 数据库模型
         ![ruoyi-vue-pro-部门管理.png](.image/ruoyi-vue-pro-部门管理.png)
      
      2. CRUD
         - `validateForCreateOrUpdate()`方法：
           - 校验自己是否存在
           - 校验父部门的有效性
             - 不能设置自己为父部门
             - 父岗位是否存在校验
             - 父部门不能是原来的子部门（需要递归校验所有子部门）



#### Mybatis组件增强

1. [yudao-spring-boot-starter-mybatis]

   1. `BaseMapperX`：在 MyBatis Plus 的`BaseMapper`的基础上拓展，提供更多的能力，简化dal层代码书写
      1. 增强`selectOne()`

      2. 增强`selectList()`

      3. 提供`insertBatch()`

      4. 提供`updateBatch()`

      5. 提供`saveOrUpdateBatch()`
   2. `LambdaQueryWrapperX`：拓展 MyBatis Plus `QueryWrapper`类
      1. 拼接条件的方法，增加 xxxIfPresent 方法，用于判断值不存在的时候，不要拼接到条件中。
      2. 重写父类方法，方便链式调用

      

#### 岗位管理

1.  [yudao-common]

   1. 分页查询支持

2. [yudao-spring-boot-starter-mybatis]

   1. 分页查询支持

3. [yudao-module-system-biz]

   1. 岗位管理数据库模型

      ![](.image/ruoyi-vue-pro-岗位管理.png)

   2. CRUD

#### Spring Security 密码编码器

1. [yudao-spring-boot-starter-security]
   1. 配置密码编码器`PasswordEncoder`
2. [yudao-module-system-biz]
   1. 登录接口使用密码编码器验证密码

#### 用户管理

1. [yudao-module-system-biz]
   
	1. 用户管理数据库模型
	    ![](.image/ruoyi-vue-pro-用户管理.png)
	
	2. CRUD
	
	    1. 检验用户正确性
	
	       1. 校验用户存在
	       2. 校验用户名唯一
	       3. 校验手机号唯一
	       4. 校验邮箱唯一
	       5. 校验部门处于开启状态
	       6. 校验岗位处于开启状态（所选的多个岗位）
	
	    2. 更新用户岗位
	
	       1. 计算应新增岗位：计算集合的单差集，即只返回【前端页面传递的岗位编号集合】中有，但是【该用户数据库记录中所拥有岗位编号集合】中没有的元素
	       2. 计算应删除岗位：计算集合的单差集，即只返回【该用户数据库记录中所拥有岗位编号集合】中有，但是【前端页面传递的岗位编号集合】中没有的元素
	       3. 执行新增和删除
	
	       - `CollUtil.subtract()`：求单差集
	       - `CollectionUtils.singleton(T o)`：返回仅包含指定对象的不可变集。
	         - o不可变
	         - 同时不可移除该元素。`removeIf`方法会报错。
	         - 这样设计应该是为了防止用户误操作
	
	    3. 用户分页查询-按部门查询时
	
	       1. 递归查询获取到当前部门的所有子部门(子子孙孙部门)
	       2. 按当前部门+所有子部门作为部门条件查询用户列表
	
	    4. 创建用户是密码加密后落库
	
	       1. 使用spring security的`PasswordEncoder`对密码加密后落库
	
	- @DateTimeFormat：用于请求参数（例如控制器方法的参数）上，以指定日期时间格式化规则，以便在请求处理期间将日期时间字符串转换为 Java 对象（）。
	
	  

#### 自定义Bean Validation 和自定义Mybatis Plus的类型处理器（`TypeHandler`）

1. [yudao-common]

   1. 自定义Bean Validation（`@Mobile`同理）

      1. 自定义验证注解`@InEnum`：用于校验参数是否在枚举值范围内

         - `@Constraint`：
           - `@Constraint` 是 Bean Validation 中的注解，用于创建自定义验证注解。
           - `validatedBy` 属性：`validatedBy` 属性是 `@Constraint` 注解的一部分，它接受一个验证器类或验证器类的数组。
           -  Bean Validation 会根据参数字段的类型选择合适的验证器进行验证。

      2. 自定义验证器`InEnumValidator`和`InEnumCollectionValidator`

         1. `InEnumValidator`：验证参数类型为`Integer`的字段的值
         2. `InEnumCollectionValidator`：验证参数类型为`Collection<Integer>>`的字段的值

         - `ConstraintValidator`：
           - 这是 Bean Validation 框架提供的一个接口，用于创建自定义验证器。
           - `initialize`: 在验证器初始化时调用，你可以在这里执行一些初始化操作。
           - `isValid`: 用于实际验证逻辑的方法，它接收被注解的字段或参数的值，并返回一个布尔值，表示验证是否通过。

      3. 辅助验证的接口`IntArrayValuable`

         1. 辅助@InEnum的验证器获取用于校验的枚举的值

      - `@Target` 注解：`@Target` 是一个元注解，用于指定注解的使用范围。
        - `ElementType.METHOD`：可以用于方法。
        - `ElementType.FIELD`：可以用于字段。
        - `ElementType.ANNOTATION_TYPE`：可以用于其他注解。
        - `ElementType.CONSTRUCTOR`：可以用于构造函数。
        - `ElementType.PARAMETER`：可以用于方法参数。
        - `ElementType.TYPE_USE`：可以用于类型使用处，例如在泛型中。
      - `@Retention` 注解：`@Retention` 是另一个元注解，用于指定注解的保留策略。在这里，`@InEnum` 注解的保留策略是 `RetentionPolicy.RUNTIME`，表示这个注解将在运行时保留，以便在运行时可以通过反射获取注解信息。

2. [yudao-spring-boot-starter-mybatis]

   1. 自定义`TypeHandler`

      1. 自定义Mybatis Plus类型处理器`JsonLongSetTypeHandler`：实现数据库字符串字段和 Set 并且泛型为 Long的java对象的自动转换 

      - `AbstractJsonTypeHandler`：是 MyBatis Plus 框架中的一个抽象类，用于处理数据库字段与 Java 中的 JSON 类型之间的转换。
      - `parse()`：json字符串转jaa对象
      - `toJson`：java对象转json字符串

   2. 使用`TypeHandler`

      ```java
          /**
           * 岗位编号数组
           */
          @TableField(typeHandler = JsonLongSetTypeHandler.class)
          private Set<Long> postIds;
      ```




#### 登录过期时自动转到登录页面重新登录

1. 自定义`AuthenticationEntryPoint`类[AuthenticationEntryPointImpl.java](yudao-framework%2Fyudao-spring-boot-starter-security%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Fsecurity%2Fcore%2Fhandler%2FAuthenticationEntryPointImpl.java)

   1. 用户登录过期后返回错误码401，前端接收响应后自动跳转到登录页

   - `AuthenticationEntryPoint`：即使用户已经登录，但由于某些原因（例如会话过期或凭证失效），仍然无法访问受保护的资源。在这种情况下，`AuthenticationEntryPoint` 也会被触发。

   

#### 角色管理

1. [yudao-module-system-biz]
   1. 角色管理数据库模型
      ![](.image/ruoyi-vue-pro-角色管理.png)
   2. CRUD
      1. 超级管理员(用code区分)，不允许创建
      2. 内置角色不允许操作

#### 菜单管理

1. [yudao-module-system-biz]
   1. 菜单数据库模型
      ![](.image/ruoyi-vue-pro-菜单管理.png)
      
      - 菜单id自关联
      - `permission`：
        - 权限标识：一般格式为：${系统}:${模块}:${操作} 例如说：system:admin:add，即 system 服务的添加管理员。
        - 仅按钮和菜单上需填写。目录不用
        - 用法:
          1. 对后端接口的访问权限控制:
             1. 用户的角色拥有该菜单或按钮就拥有此`permission`对应的 `@PreAuthorize`("@ss.hasPermission('`permission`')")接口访问权限
          2. 对页面按钮的展示控制：
             1. 前端按钮上会标记对应`permission`的值，与用户的权限进行匹配，匹配不上就不展示按钮
                1. 原因：用户的角色未分配该按钮，权限信息中就不会有该`permission`值，按钮就不会展示
      - type：目录、菜单、按钮（目录应该是任何人都能有权限访问，不控制权限）
      - path：路由地址。如果 path 为 http(s) 时，则它是外链
      - 组件地址：用途不明，可能是前端的vue文件，缓存时使用
      - visible：是否可见。只有菜单、目录使用 当设置为 true 时，该菜单不会展示在侧边栏，但是路由还是存在。例如说，一些独立的编辑页面 /edit/1024 等等
      - keepAlive：是否缓存 。只有菜单、目录使用，否使用 Vue 路由的 keep-alive 特性 注意：如果开启缓存，则必须填写 componentName 属性，否则无法缓存
      - alwaysShow：是否总是显示。如果为 false 时，当该菜单只有一个子菜单时，不展示自己，直接展示子菜单
      
   2. CRUD
      1. 校验父菜单存在
      2. 校验菜单（自己）
      3. 父菜单必须是目录或者菜单类型
      4. 存在子菜单，无法删除
      
      - 存在bug：可设置自己的子菜单为父菜单



#### RBAC权限-获取登录用户的权限信息（完善）

1. [yudao-module-system-biz]

   1. RBAC数据库模型
       ![](.image/ruoyi-vue-pro-RBAC.png)
      - 基于角色的权限控制。给用户分配角色即分配权限（菜单、按钮、接口等）
      - 特殊角色：角色`code`值为`super_admin`的为超级管理员，拥有所有权限。
   2. 编写登出接口
      - 登出时删除token的redis缓存

#### 租户管理与jackson序列化

1. [yudao-spring-boot-starter-web]
   1. jackson序列化：Java对象和JSON互相转换的过程
      1. [LocalDateTimeDeserializer.java](yudao-framework%2Fyudao-spring-boot-starter-web%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Fjackson%2Fcore%2Fdatabind%2FLocalDateTimeDeserializer.java)：LocalDateTime反序列化规则，会将毫秒级时间戳反序列化为LocalDateTime
         - `JsonDeserializer<LocalDateTime>`：表示它是一个用于反序列化LocalDateTime对象的自定义反序列化器。
         - `public static final LocalDateTimeDeserializer INSTANCE = new LocalDateTimeDeserializer();`：这是一个静态的常量，表示一个单例的LocalDateTimeDeserializer实例。通常，为了节省资源，自定义反序列化器会以单例模式创建，因此通常会有一个常量来表示该实例。
         - `deserialize()`：覆盖了JsonDeserializer类中的deserialize方法，以提供自定义的反序列化逻辑。
         - `return LocalDateTime.ofInstant(Instant.ofEpochMilli(p.getValueAsLong()), ZoneId.systemDefault());`：这是实际的反序列化逻辑。它执行以下操作：
           - `p.getValueAsLong()`：从JsonParser对象中获取时间戳的值，这个时间戳通常表示为毫秒数。 
           - `Instant.ofEpochMilli(...)`：将毫秒时间戳转换为Instant对象，Instant是Java 8中表示时间戳的类。 
           - `ZoneId.systemDefault()`：获取系统默认的时区。 
           - `LocalDateTime.ofInstant(...)`：将Instant对象转换为LocalDateTime对象，考虑了时区信息，从而将时间戳转换为本地日期和时间。
      2. [LocalDateTimeSerializer.java](yudao-framework%2Fyudao-spring-boot-starter-web%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Fjackson%2Fcore%2Fdatabind%2FLocalDateTimeSerializer.java)：LocalDateTime序列化规则，会将LocalDateTime序列化为毫秒级时间戳
         - `JsonSerializer<LocalDateTime>`：表示它是一个用于序列化LocalDateTime对象的自定义序列化器。
         - `serialize()`：覆盖了JsonSerializer类中的serialize方法，以提供自定义的序列化逻辑。
         - `public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException`：这是serialize方法的定义，用于实际执行序列化操作。它接受三个参数：
           - `value`：要序列化的LocalDateTime对象。
           - `gen`：一个JsonGenerator对象，用于生成JSON数据。
           - `serializers`：一个SerializerProvider对象，提供了序列化过程中的上下文信息。
         - `gen.writeNumber(value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());`：这是实际的序列化逻辑。它执行以下操作：
           - `value.atZone(ZoneId.systemDefault())`：将LocalDateTime对象与系统默认的时区（ZoneId.systemDefault()）关联，以便将日期时间信息转换为特定时区下的ZonedDateTime对象。
           - `toInstant()`：将ZonedDateTime对象转换为Instant对象，以便获取时间戳信息。
           - `toEpochMilli()`：将Instant对象的时间戳转换为毫秒数。
           - `gen.writeNumber(...)`：使用JsonGenerator对象将毫秒时间戳写入JSON中作为数字。
      3. [NumberSerializer.java](yudao-framework%2Fyudao-spring-boot-starter-web%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Fjackson%2Fcore%2Fdatabind%2FNumberSerializer.java)：Long 序列化规则 会将超长 long 值转换为 string，解决前端 JavaScript 最大安全整数是 2^53-1 的问题
         - `@JacksonStdImpl`：这是一个Jackson的注解，用于指示该类是Jackson标准实现之一。它可以提高Jackson序列化框架的性能，因为Jackson会根据这个注解的存在来优化一些操作。
         - NumberSerializer 类继承了com.fasterxml.jackson.databind.ser.std.NumberSerializer类，这是Jackson提供的默认数字序列化器的基类。它提供了一些默认的数字序列化行为。
         - MAX_SAFE_INTEGER 和 MIN_SAFE_INTEGER 常量：这些常量定义了安全整数的范围。安全整数是在JavaScript中可以精确表示的整数范围，通常是从 -9007199254740991 到 9007199254740991。这些常量将用于检查数字是否在安全整数范围内。
         - `NumberSerializer 构造函数`：这个构造函数接受一个Class参数，该参数表示要序列化的数字的具体类型，例如Integer.class、Long.class等。在构造函数中，它调用了父类的构造函数，将这个具体的类型传递给父类。
         - `serialize 方法`：这是一个覆盖了父类方法的自定义序列化逻辑。在序列化数字之前，它首先检查数字是否在安全整数范围内（即在MIN_SAFE_INTEGER和MAX_SAFE_INTEGER之间）。如果在范围内，则使用父类的默认数字序列化方式进行序列化。如果超出了安全整数范围，则将数字值转换为字符串，并使用gen.writeString(value.toString())将其序列化为字符串形式。
      4. [配置objectMappers](yudao-framework%2Fyudao-spring-boot-starter-web%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Fjackson%2Fconfig%2FYudaoJacksonAutoConfiguration.java)：
         - `ObjectMapper`：ObjectMapper是Jackson库中的一个类，用于序列化和反序列化Java对象和JSON。
         - `SimpleModule`：SimpleModule它是Jackson库中用于定制序列化和反序列化规则的容器。
         - `.addSerializer()`和`.addDeserializer()`：添加序列化和反序列化规则
         - `注册SimpleModule到objectMapper`：使用objectMappers参数中的每个ObjectMapper对象，通过循环调用objectMapper.registerModule(simpleModule)方法，将上述定义的SimpleModule注册到每个ObjectMapper中。这样，所有的ObjectMapper都将具有相同的自定义序列化和反序列化规则。
   - 自定义jackson序列化(反序列化)的思路：
     1. 创建自定义序列化器(`JsonSerializer`和`JsonDeserializer`)：在序列化（反序列化）器中，你可以定义如何将特定类型的Java对象转换为JSON或（JSON转Java对象）。
     2. 注册自定义序列化器：需要将（自定义序列化器）注册到Jackson的ObjectMapper中。这可以通过调用objectMapper.registerModule或objectMapper.setSerializerFactory等方法来完成。
     3. 标注Java对象（应该是和上一个步骤二选一）：如果你希望自定义序列化应用于特定类型的Java对象，你可以使用Jackson的注解，如@JsonSerialize(using = YourCustomSerializer.class)，将自定义序列化器与对象类关联起来。
2. [yudao-module-system-biz]
   1. 租户管理数据库模型
      ![](.image/ruoyi-vue-pro-租户管理.png)
      - 租户套餐表：主要用于控制租户可使用的菜单权限
      - 租户表：
        - 用于划分不同租户出来，隔离数据
        - 控制租户的过期时间
        - 控制租户下最大账号数量等
   2. crud
      - 租户创建：
        1. 创建租户，配置租户套餐等
        2. 创建默认租户管理员角色
        3. 给默认租户管理员配置选中的租户套餐所拥有的所有菜单权限
        4. 创建默认租户管用户，并赋予租户管理员角色
        5. 配置该默认用户为当前租户联系人
      - 租户更新：
        - 若租户套餐发生变化时，需要修改该租户下所有角色的菜单权限
      - 租户套餐创建：
        - 配置租户可拥有的菜单权限
      - 租户套餐修改：
        - 若租户套餐菜单权限发生变化，需要同步更新使用了该租户套餐的所有租户的旗下所有角色的菜单权限
      - 租户套餐删除：
        - 校验是否还有使用该租户套餐的租户，无租户使用后方能删除
      - 内置租户：
        - 本项目默认配置了租户套餐id为0的内置租户套餐。
        - 租户套餐id为0的内置租户不可删除



#### 租户业务组件接入

1. [yudao-spring-boot-starter-biz-tenant](yudao-framework%2Fyudao-spring-boot-starter-biz-tenant)
   1. 多租户Web的封装
      1. [TenantContextWebFilter.java](yudao-framework%2Fyudao-spring-boot-starter-biz-tenant%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Ftenant%2Fcore%2Fweb%2FTenantContextWebFilter.java)：多租户 Context Web 过滤器
         - 将请求 Header 中的 tenant-id 解析出来，添加到 `TenantContextHolder` 中，这样后续的 DB 等操作，可以获得到租户编号。
         - `TenantContextHolder`：多租户上下文 Holder
         - `OncePerRequestFilter`：一次请求过滤器，保证只执行一次
      2. [TenantContextHolder.java](yudao-framework%2Fyudao-spring-boot-starter-biz-tenant%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Ftenant%2Fcore%2Fcontext%2FTenantContextHolder.java)：多租户上下文 Holder
         - 存取租户id
         - 全局忽略租户开关
         - `TransmittableThreadLocal`：能够在多线程环境中传递变量的值
      3. 配置多租户 Context Web 过滤器
         ```java
           @Bean
           public FilterRegistrationBean<TenantContextWebFilter> tenantContextWebFilter() {
             FilterRegistrationBean<TenantContextWebFilter> registrationBean = new FilterRegistrationBean<>();
             registrationBean.setFilter(new TenantContextWebFilter());
             registrationBean.setOrder(WebFilterOrderEnum.TENANT_CONTEXT_FILTER);
             return registrationBean;
           }
         ```
         - `registrationBean.setOrder(WebFilterOrderEnum.TENANT_CONTEXT_FILTER)`：保证过滤器在Spring Security Filter的过滤器之前。便于在Spring Security中使用到`TenantContextHolder`中存放的租户的值
   2. 多租户DB的封装
      1. [TenantDatabaseInterceptor.java](yudao-framework%2Fyudao-spring-boot-starter-biz-tenant%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Ftenant%2Fcore%2Fdb%2FTenantDatabaseInterceptor.java)：MP的租户处理器
         - `TenantLineHandler`：租户处理器，主要用于在 SQL 查询中动态注入租户标识条件，以确保不同租户之间的数据隔离。
         - `getTenantId()`：获取租户 ID 值表达式，SQL中动态注入租户标识条件时使用
         - `ignoreTable(String tableName)`：根据表名判断是否忽略拼接多租户条件
         - `getTenantIdColumn`：获取租户字段名
      2. 配置租户拦截器：
         ```java
           @Bean
           public TenantLineInnerInterceptor tenantLineInnerInterceptor(TenantProperties properties,
                MybatisPlusInterceptor interceptor) {
                TenantLineInnerInterceptor inner = new TenantLineInnerInterceptor(new TenantDatabaseInterceptor(properties));
                // 添加到 interceptor 中
                // 需要加在首个，主要是为了在分页插件前面。这个是 MyBatis Plus 的规定
                MyBatisUtils.addInterceptor(interceptor, inner, 0);
                return inner;
           }
         ```
         - `TenantLineInnerInterceptor`：MP的租户拦截器，它在 MyBatis 执行 SQL 语句之前拦截并修改 SQL 查询，以注入租户标识条件。
         - 将租户拦截器置于拦截器首位
         - `TenantLineInnerInterceptor`和`TenantLineHandler`：TenantLineInnerInterceptor 依赖于 TenantLineHandler 的实现。当你使用 TenantLineInnerInterceptor 时，你需要为其配置一个具体的 TenantLineHandler 的实现，这个实现会处理租户标识的获取和注入逻辑。
      3. [TenantBaseDO.java](yudao-framework%2Fyudao-spring-boot-starter-biz-tenant%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Ftenant%2Fcore%2Fdb%2FTenantBaseDO.java)：拓展多租户的 BaseDO 基类
         - `tenantId`：定义租户字段名（"tenant_id"）
      4. [TenantProperties.java](yudao-framework%2Fyudao-spring-boot-starter-biz-tenant%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Ftenant%2Fconfig%2FTenantProperties.java)：多租户配置
         - `enable`：租户开关（默认开启，其实没多大用处）
         - `ignoreUrls`：需要忽略多租户的请求。默认情况下，每个请求需要带上 tenant-id 的请求头。但是，部分请求是无需带上的，例如说获取验证码接口
         - `ignoreTables`：需要忽略多租户的表。即默认所有表都开启多租户的功能，需忽略的在此配置
   3. 多租户Security的封装
      1. [TenantSecurityWebFilter.java](yudao-framework%2Fyudao-spring-boot-starter-biz-tenant%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Ftenant%2Fcore%2Fsecurity%2FTenantSecurityWebFilter.java)：多租户 Security Web 过滤器
         1. 如果是登陆的用户，校验是否有权限访问该租户，避免越权问题。
         2. 如果请求未带租户的编号，检查是否是忽略的 URL，否则也不允许访问。
         3. 校验租户是否合法，例如说被禁用、到期
         - `ApiRequestFilter`：在`shouldNotFilter()`方法中设置只过滤 API 请求的地址
      2. 配置多租户 Security Web 过滤器
         ```java
           @Bean
           public FilterRegistrationBean<TenantSecurityWebFilter> tenantSecurityWebFilter(TenantProperties tenantProperties,
              WebProperties webProperties,
              GlobalExceptionHandler globalExceptionHandler,
              TenantFrameworkService tenantFrameworkService) {
              FilterRegistrationBean<TenantSecurityWebFilter> registrationBean = new FilterRegistrationBean<>();
              registrationBean.setFilter(new TenantSecurityWebFilter(tenantProperties, webProperties,
              globalExceptionHandler, tenantFrameworkService));
              registrationBean.setOrder(WebFilterOrderEnum.TENANT_SECURITY_FILTER);
              return registrationBean;
           }
         ```
         - `registrationBean.setOrder(WebFilterOrderEnum.TENANT_SECURITY_FILTER);`：保证在 Spring Security 过滤器后面

   4. [TenantUtils.java](yudao-framework%2Fyudao-spring-boot-starter-biz-tenant%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Ftenant%2Fcore%2Futil%2FTenantUtils.java) ：多租户 Util
      - `execute(Long tenantId, Runnable runnable)`：使用指定租户，执行对应的逻辑。让业务代码和租户获取、切换的逻辑解耦

2. [yudao-common]
   1. Cache 工具类

      ```java
          public static <K, V> LoadingCache<K, V> buildAsyncReloadingCache(Duration duration, CacheLoader<K, V> loader) {
              return CacheBuilder.newBuilder()
                      // 只阻塞当前数据加载线程，其他线程返回旧值
                      .refreshAfterWrite(duration)
                      // 通过 asyncReloading 实现全异步加载，包括 refreshAfterWrite 被阻塞的加载线程
                      .build(CacheLoader.asyncReloading(loader, Executors.newCachedThreadPool())); 
          }
      ```

      > 这代码片段是一个Java类`CacheUtils`，其中定义了一个名为`buildAsyncReloadingCache`的静态方法，用于构建异步刷新（Asynchronous Reloading）的缓存。它使用了Google Guava缓存库（CacheBuilder）和Guava CacheLoader来实现缓存的创建。
      >
      > 让我解释这段代码的关键部分：
      >
      > 1. `LoadingCache<K, V>`：`LoadingCache` 是Google Guava库中的接口，用于表示具有加载功能的缓存。它支持自动加载缓存项，如果缓存中没有请求的数据，它将使用`CacheLoader`接口的实现来加载数据。
      >
      > 2. `buildAsyncReloadingCache` 方法：这是一个公共静态方法，接受两个参数，一个是`Duration`类型的时间段（表示缓存项的刷新时间间隔），另一个是`CacheLoader<K, V>`类型的加载器。
      >
      > 3. `CacheBuilder.newBuilder()`：这是Google Guava库中的`CacheBuilder`类的静态方法，用于创建缓存构建器。它允许你配置和定制缓存的行为。
      >
      > 4. `.refreshAfterWrite(duration)`：这行代码配置了缓存的刷新策略。它告诉缓存，如果某个缓存项在指定的`duration`时间内没有被访问，那么缓存将异步刷新这个缓存项，以确保数据的新鲜性。`duration`是`buildAsyncReloadingCache`方法的第一个参数。
      >
      > 5. `.build(CacheLoader.asyncReloading(loader, Executors.newCachedThreadPool()))`：这行代码创建并返回`LoadingCache`实例。它使用了`CacheLoader.asyncReloading`方法，该方法接受一个`loader`和一个`Executor`。
      >
      >    - `loader`是`CacheLoader`的实现，它定义了如何加载缓存项的逻辑。
      >    - `Executors.newCachedThreadPool()`创建了一个可缓存线程池，用于执行异步刷新任务。
      >
      > 所以，`buildAsyncReloadingCache`方法的目的是创建一个支持异步刷新的缓存。当某个缓存项过期后，它不会阻塞请求线程来刷新数据，而是使用线程池中的线程异步地执行刷新操作，以提高性能和响应性。
      >
      > 这个方法可以在需要使用缓存的地方调用，以创建一个具有异步刷新功能的缓存对象，这在某些情况下可以提高应用程序的性能和数据的新鲜度。
   
3. [yudao-module-system-biz]

   1. 登录时用户信息存放租户id
   2. 编写校验租户是否合法的逻辑



#### 字典管理

1. 字典管理数据库模型
   ![](.image/ruoyi-vue-pro-字典管理.png)



#### 接入数据字典组件和excel组件

1. [yudao-spring-boot-starter-biz-dict](yudao-framework%2Fyudao-spring-boot-starter-biz-dict)：数据字典组件。主要作用为通过将字典缓存在内存中，保证性能
   1. [DictFrameworkUtils.java](yudao-framework%2Fyudao-spring-boot-starter-biz-dict%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Fdict%2Fcore%2Futil%2FDictFrameworkUtils.java) ：数据字典工具类
      1. 缓存数据。K：字典类型-字典值，V：字典标签
      2. 缓存数据K。：字典类型-字典标签，V：字典值
      3. ` String getDictDataLabel(String dictType, String value)`：通过`字典类型-字典值`获取`字典标签`
      4. `String parseDictDataValue(String dictType, String label)`：解析`字典类型-字典标签`获取到`字典值`
      5. `DictDataApi`：`yudao-module-system-biz`模块提供的从数据中通过`字典类型-字典值`获取`字典标签`和解析`字典类型-字典标签`获取到`字典值`的Api
      6. `@SneakyThrows`：在方法上使用`@SneakyThrows`，以指示方法可能会抛出受检异常，而无需显式地捕获或声明这些异常。Lombok 在编译时会生成必要的异常处理代码，从而减少了代码的复杂性。
2. [yudao-spring-boot-starter-excel](yudao-framework%2Fyudao-spring-boot-starter-excel)：excel组件。基于 EasyExcel 实现 Excel 相关的操作
   1.  [ExcelUtils.java](yudao-framework%2Fyudao-spring-boot-starter-excel%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Fexcel%2Fcore%2Futil%2FExcelUtils.java) ：Excel 工具类，提供excel的读写功能
       - 提供excel文件读写功能
       - `autoCloseStream(false)`：禁用EasyExcel默认的自动流关闭功能，交给 Servlet 自己处理，Servlet容器通常会在一个请求结束后自动关闭响应流。
       - `Class<T> head`：会配合head类字段上的`@ExcelProperty`，自动生成excel的列头
   2.  [DictConvert.java](yudao-framework%2Fyudao-spring-boot-starter-excel%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Fexcel%2Fcore%2Fconvert%2FDictConvert.java) ：Excel 数据字典转换器。用于java和excel之间字典数据的互相转换
       - `Converter`： EasyExcel中用于执行Excel数据和Java对象之间的相互转换的接口
       - `Converter<Object>`：作用于Object类型的字段的转换
       - `convertToJavaData(...)`：将 Excel 对象转换为 Java 对象
       - `convertToExcelData(...)`：将 Java 对象转换为 Excel 对象
       - 使用`DictFrameworkUtils`类完成，`字典值`与`字典标签`的转换
       - [DictFormat.java](yudao-framework%2Fyudao-spring-boot-starter-excel%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Fexcel%2Fcore%2Fannotations%2FDictFormat.java) ：用于标注字段的字典类型，辅助`DictConvert`完成`字典值`与`字典标签`的转换
   3.   [JsonConvert.java](yudao-framework%2Fyudao-spring-boot-starter-excel%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Fexcel%2Fcore%2Fconvert%2FJsonConvert.java) 与 [MoneyConvert.java](yudao-framework%2Fyudao-spring-boot-starter-excel%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Fexcel%2Fcore%2Fconvert%2FMoneyConvert.java) ：同`DictConvert`
3. [yudao-module-system-biz]中的excel导入导出
   1. 【租户管理】excel导出
   2. 【用户管理】导出用户、导入用户模板下载、导入用户
      - 导入用户时没有校验最大租户配额
   3. 【角色管理】excel导出
   4. 【岗位管理】excel导出
   5. 【字典管理】字典类型导出、字典数据导出

#### 使用Spring Security的`@PreAuthorize`实现接口权限控制

1. [yudao-spring-boot-starter-security]：启用`@PreAuthorize`接口权限控制的支持

   1. `@EnableGlobalMethodSecurity(prePostEnabled = true)`：开启Spring Security的`@PreAuthorize`支持

      - `@EnableGlobalMethodSecurity`：启用方法级别的安全性控制
      - `prePostEnabled = true`：用于启用方法级别的安全控制中的 `@PreAuthorize` 和 `@PostAuthorize` 注解
      -  `@PreAuthorize`：用于在方法执行前进行权限验证。它允许你定义方法级别的安全控制规则，只有当规则中指定的条件满足时(EL表达式结果为true时)，方法才能被执行。

   2. [SecurityFrameworkService.java](yudao-framework%2Fyudao-spring-boot-starter-security%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Fsecurity%2Fcore%2Fservice%2FSecurityFrameworkService.java)：定义权限相关的校验操作。用于`@PreAuthorize`的EL表达式中使用

   3. 配置`SecurityFrameworkService`的bean，取别名`ss`简化引用

      ```java
          @Bean("ss") // 使用 Spring Security 的缩写，方便使用
          public SecurityFrameworkService securityFrameworkService(PermissionApi permissionApi) {
              return new SecurityFrameworkServiceImpl(permissionApi);
          }
      ```

2. [yudao-module-system-biz]：应用`@PreAuthorize`完成接口权限控制

   - ```java
     @PreAuthorize("@ss.hasPermission('system:dept:query')")
     ```



#### 通知公告（不知在哪使用）
1. 通知公告数据库模型
   ![](.image/ruoyi-vue-pro-通知公告.png)

   

#### 审计日志-登录日志

1. [AdminAuthServiceImpl.java](yudao-module-system%2Fyudao-module-system-biz%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fmodule%2Fsystem%2Fservice%2Fauth%2FAdminAuthServiceImpl.java)：登录登出时记录日志

2. [LoginLogController.java](yudao-module-system%2Fyudao-module-system-biz%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fmodule%2Fsystem%2Fcontroller%2Fadmin%2Flogger%2FLoginLogController.java)：登录日志查询

3. 登录日志数据库模型
   ![](.image/ruoyi-vue-pro-审计日志-登录日志.png)
   - 用户类型
   - 登录日志类型
   - 登录结果

#### 审计日志-操作日志

1. [yudao-spring-boot-starter-biz-operatelog](yudao-framework%2Fyudao-spring-boot-starter-biz-operatelog)：操作日志组件。基于AOP实现

   1. [OperateLogAspect.java](yudao-framework%2Fyudao-spring-boot-starter-biz-operatelog%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Foperatelog%2Fcore%2Faop%2FOperateLogAspect.java) ：日志切面

      1. 日志记录条件(满足如下任一条件，则会进行记录)

         1. 使用 @Operation + 非 @GetMapping的接口

         2. 使用 @OperateLog 注解

            > 但是，如果声明 @OperateLog 注解时，将 enable 属性设置为 false 时，强制不记录。

      2. 异步记入日志

      3. 拓展：拓展明细和拓展字段
   
         1. [OperateLogUtils.java](yudao-framework%2Fyudao-spring-boot-starter-biz-operatelog%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Foperatelog%2Fcore%2Futil%2FOperateLogUtils.java)：记录操作明细和拓展字段的工具类

   2. [OperateLog.java](yudao-framework%2Fyudao-spring-boot-starter-biz-operatelog%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Foperatelog%2Fcore%2Fannotations%2FOperateLog.java) 

      1. `module` 属性：操作模块，例如说：用户、岗位、部门等等。为空时，默认会读取类上的 Swagger `@Tag` 注解的 `name` 属性。
      2. `name` 属性：操作名，例如说：新增用户、修改用户等等。为空时，默认会读取方法的 Swagger `@Operation` 注解的 `summary` 属性。
      3. `type` 属性：操作类型，在 [OperateTypeEnum.java](yudao-framework%2Fyudao-spring-boot-starter-biz-operatelog%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Foperatelog%2Fcore%2Fenums%2FOperateTypeEnum.java) 枚举。目前有 `GET` 查询、`CREATE` 新增、`UPDATE` 修改、`DELETE` 删除、`EXPORT` 导出、`IMPORT` 导入、`OTHER` 其它，可进行自定义
   
   3. 操作日志记录内容
   
      ![](.image/ruoyi-vue-pro-审计日志-操作日志.png)


#### 站内信管理
1. 站内信模板
   1. 模板内容：使用 {var} 作为占位符，例如说 {name}、{code} 等
   2. 解析模板内容占位符名称
      - `ReUtil.findAllGroup1(Pattern pattern, CharSequence content)`：返回与正则表达式模式匹配的文本内容,只返回正则表达式中的第一个分组且只包含占位符部分。（hutool提供）
      - `ReUtil.findAllGroup0(Pattern pattern, CharSequence content)`：返回所有与正则表达式模式匹配的文本内容。（hutool提供）
      ```java
      String content = "Date: 2023-10-14, Time: 12:30 PM";
      Pattern pattern = Pattern.compile("Date: (\\d{4}-\\d{2}-\\d{2}), Time: (\\d{2}:\\d{2} [APM]{2})");
      
      List<String> allMatches0 = ReUtil.findAllGroup0(pattern, content);
      List<String> allMatches1 = ReUtil.findAllGroup1(pattern, content);
      
      // allMatches0 包含完整的匹配项，包括所有分组内容
      // ["Date: 2023-10-14, Time: 12:30 PM", "2023-10-14", "12:30 PM"]
      
      // allMatches1 只包含第一个分组的匹配内容
      // ["2023-10-14"]
      
      ```
   3. 填充模板内容
      - `String StrUtil.format(CharSequence template, Map<?, ?> map)`：（hutool提供，使用这个）
        - 将`这是模板内容{name}、{code}`转换为`这是模板内容张三、编码`。其中map的key为name和code，对应值会填充在模板内容当中
2. 站内信
   - 是否已读
3. 站内信管理数据库模型

   ![](.image/ruoyi-vue-pro-站内信管理.png)


#### 错误码管理
1. 错误码管理数据库模型

   ![](.image/ruoyi-vue-pro-错误码管理.png)
2. 错误码管理CRUD（数据库操作）：[ErrorCodeController.java](yudao-module-system%2Fyudao-module-system-biz%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fmodule%2Fsystem%2Fcontroller%2Fadmin%2Ferrorcode%2FErrorCodeController.java)
   - 此处的C和U操作后，错误码类型会转为2-手动编辑
   - 错误码类型：1-自动生成（从本地错误码常量接口中解析生成） 2-手动编辑
3. 错误码自动更新和本地错误码自动写入： [yudao-spring-boot-starter-biz-error-code](yudao-framework%2Fyudao-spring-boot-starter-biz-error-code)
   1. 自动更新：[ErrorCodeLoaderImpl.java](yudao-framework%2Fyudao-spring-boot-starter-biz-error-code%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Ferrorcode%2Fcore%2Floader%2FErrorCodeLoaderImpl.java)
      1. 将数据库中的错误码（主要针对`本地错误码自动写入`和`错误码管理CU操作`），更新到应用服务器缓存中(`ServiceExceptionUtil#MESSAGES`)，实现异常提示信息自动更新
      2. 初始全量更新，后续增量更新
      - `maxUpdateTime`：缓存错误码的最大更新时间，用于后续的增量轮询，判断是否有更新
      - `@EventListener(ApplicationReadyEvent.class)`：应用程序启动完成后执行自动更新
      - `@Scheduled(fixedDelay = REFRESH_ERROR_CODE_PERIOD, initialDelay = REFRESH_ERROR_CODE_PERIOD)`
         - `initialDelay`：首次执行任务的延迟时间，单位毫秒
         - `fixedDelay`：任务执行的固定延迟时间，单位毫秒
   2. 本地错误码自动写入：[ErrorCodeAutoGeneratorImpl.java](yudao-framework%2Fyudao-spring-boot-starter-biz-error-code%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fframework%2Ferrorcode%2Fcore%2Fgenerator%2FErrorCodeAutoGeneratorImpl.java)
      1. 解析`yudao.error-code.constants-class-list`指定的本地错误码常量接口
         - `ClassUtil.loadClass(String className)`：加载类并初始化（hutool提供）
      2. insertOrUpdate进数据库
         > 更新有三个前置条件
         > 1. 只更新自动生成的错误码，即 Type 为 ErrorCodeTypeEnum.AUTO_GENERATION
         > 2. 分组 applicationName 必须匹配，避免存在错误码冲突的情况
         > 3. 错误提示语存在差异


> 业务异常为什么不直接return CommonResult
>   因为 Spring @Transactional 声明式事务，是基于异常进行回滚的，如果使用 CommonResult 返回，则事务回滚会非常麻烦
 
 
#### 敏感词管理
1. 敏感词数据库模型
   
   ![](.image/ruoyi-vue-pro-敏感词管理.png)
   - tags：标签数组。标签即场景，可按不同场景校验敏感词
2. 敏感词实现原理-前缀树算法：[SimpleTrie.java](yudao-module-system%2Fyudao-module-system-biz%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fmodule%2Fsystem%2Futil%2Fcollection%2FSimpleTrie.java)
   ![前缀树](.image/Trie_example.png)
   - `敏感词的前缀树`：节点只存单个字符（即前缀），从根节点—>子子孙孙节点组成完整字符串
   - `根节点`：为空字符串（可无）
   - `children`：整棵前缀树。本例为`HashMap<String,V>`结构。`V`为子节点，无根节点。`V`的类型仍然为`HashMap<String,V>`。无限累加下去构成敏感词树.
   - 成员变量`Character CHARACTER_END = '\0';`：标记敏感词的终止（放在一个子节点链路的末尾）。
     > 用成员变量`CHARACTER_END`来标记敏感词的终止的目的：
       从HashMap中get key结果为null，不能判断该key不存在，还是该key存在但值为null。因此使用`CHARACTER_END`来代替敏感词的终止
       即从`children`中get字符的结果为null，代表该字符非敏感词，为`CHARACTER_END`，则代表该字符为敏感词，且是敏感词的终止标记。
   - `public SimpleTrie(Collection<String> strs)`：构建敏感词树
     - `短敏感词在前`：构建敏感词树时，将原敏感词列表按字符串长度升序排，使短字符串敏感词在前，后再构建树。这样包含相同前缀敏感词的较长敏感词无需存入树种。
   - `boolean isValid(String text) `：验证文本是否合法，即不包含敏感词。true-合法 false-不合法
   - `recursion(...)`：验证文本从指定位置开始，是否不包含某个敏感词
3. 敏感词CRUD和校验、缓存：[SensitiveWordServiceImpl.java](yudao-module-system%2Fyudao-module-system-biz%2Fsrc%2Fmain%2Fjava%2Fcn%2Fiocoder%2Fyudao%2Fmodule%2Fsystem%2Fservice%2Fsensitiveword%2FSensitiveWordServiceImpl.java)
   - `volatile SimpleTrie defaultSensitiveWordTrie`：默认的敏感词的字典树缓存，包含所有敏感词。校验敏感词忽略标签时使用。`volatile`保证多线程可见（没有体现出价值）。
   - `Map<String, SimpleTrie> tagSensitiveWordTries`：按标签分组的敏感词树。用于按标签校验敏感词的场景
   - `initLocalCache()`：敏感词缓存初始化。cud时需调用
   - `refreshLocalCache()`：敏感词缓存定时更新。增量更新。







