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
	2. 版主仲裁中使用[yudao-dependencies]，将[yudao-dependencies]的依赖传递给所有的子模块，进而统一管理所有模块的依赖

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





