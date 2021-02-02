package com.atguigu.eduservice.controller;


import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-02-02
 */
@RestController
@RequestMapping("/eduservice/teacher")
public class EduTeacherController {
    //把list集合返回，形式是json数据
    //把service注入
    @Autowired
    private EduTeacherService teacherService;

    //1.查询讲师表所有数据  rest风格
    //get提交     //访问地址：http://localhost:8001/eduservice/edu-teacher/findAll
    @GetMapping("findAll")
    public List<EduTeacher> findAllTeacher() {
        //调用service的方法实现查询所有的操作
        List<EduTeacher> list = teacherService.list(null);
        return list;
    }

    //逻辑删除讲师的方法   通过路径把值传递过去
    // id值需要通过路径传递id值  @PathVariable
    //String id  参数是id  赋给形参，类型是String
    @DeleteMapping("{id}")
    public boolean removeTeacher(@PathVariable String id) {
        //删除直接调用service中的方法
        boolean flag = teacherService.removeById(id);
        return flag;
    }
}

