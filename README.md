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