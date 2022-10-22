package com.imooc.api.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value="controller的标题",tags = {"xx功能的controller"})
public interface HelloControllerApi {

    /**
     * api的作用
     * api 就相当于企业的领导，老板，部门经理
     * 其他的服务层都是实现，相当于员工，只做事情
     * 老板（开发人员）来看一下每个人（服务）成的进度，做什么事
     * 老板不会问员工，只会对接部门经理
     * 这里所有的api接口就是统一在这里进行管理和调度的，微服务也是如此
     */

    /**
     * 运作：
     * 所有的接口都在这，实现则在各自的微服务中
     * 这里只写接口，不写实现，实现在各自的微服务工程中，因为以业务来划分的微服务有很多
     * controller也会分散在各个微服务中，一旦多了就很难管理和查看
     *
     * 微服务i之间的调用都是接口
     * 若不这样，微服务之间的调用就需要互相依赖了
     * 耦合度也就高了，接口的目的为了能够提供解耦
     *
     * 本工程的接口就是一套规范。实现都是由各自的工程去做的处理
     * 目前是spring boot作为接口实现，若未来出现新的java web框架，
     * 那么我们不需要修改接口，只需要去修改对应的实现就可以了。这其实也是一个解耦的体现
     *
     * Swagger2，基于接口的自动文档生成
     *
     * 总结：如此的做法，可以提高多服务的项目可扩展性
     */
    @ApiOperation(value = "hello方法的接口",notes = "hello方法的接口",httpMethod = "GET")
    @GetMapping("/hello")
    public Object hello();

}
