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
         ![ruoyi-vue-pro-部门管理[部门管理].png](.image/ruoyi-vue-pro-部门管理[部门管理].png)
      
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

   ​     
