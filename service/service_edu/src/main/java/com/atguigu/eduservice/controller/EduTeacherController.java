package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.servicebase.exceptionHandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-02-02
 */
@Api(tags = "讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin
public class EduTeacherController {
    //把list集合返回，形式是json数据
    //把service注入
    @Autowired
    private EduTeacherService teacherService;

    //1.查询讲师表所有数据  rest风格
    //get提交     //访问地址：http://localhost:8001/eduservice/edu-teacher/findAll
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public R findAllTeacher() {
        //调用service的方法实现查询所有的操作
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items", list);
    }

    //逻辑删除讲师的方法   通过路径把值传递过去
    // id值需要通过路径传递id值  @PathVariable
    //String id  参数是id  赋给形参，类型是String
    @ApiOperation(value = "逻辑删除讲师")
    @DeleteMapping("{id}")
    public R removeTeacher(@ApiParam(name = "id", value = "讲师ID", required = true) @PathVariable String id) {
        //删除直接调用service中的方法
        boolean flag = teacherService.removeById(id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //3.分页查询讲师的方法 current 当前页，limit每页记录数
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageListTeacher
    (@PathVariable long current, @PathVariable long limit) {


        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);
        //调用方法实现分页
        //调用方法的时候，底层封装，把分页所有数据封装到pageTeacher对象里面
        teacherService.page(pageTeacher, null);

        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> records = pageTeacher.getRecords();//数据list集合

        return R.ok().data("total", total).data("rows", records);

        /*Map map = new HashMap<>();
        map.put("total", total);
        map.put("rows", records);
        return R.ok().data(map);//直接传入map*/

    }


    //4.条件查询带分页的方法
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery) {

        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);

        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();

        //wrapper  多条件组合查询
        //mybatis动态sql  做判断条件值是否有
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();

        //判断条件值是否为空，如果不为空拼接条件

        if (!StringUtils.isEmpty(name)) {
            //构建条件
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(level)) {
            //构建条件
            wrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            //构建条件
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            //构建条件
            wrapper.le("gmt_create", end);
        }

        //排序
        wrapper.orderByDesc("gmt_create");

        //调用方法实现条件查询分页
        teacherService.page(pageTeacher, wrapper);
        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> records = pageTeacher.getRecords();//数据list集合

        return R.ok().data("total", total).data("rows", records);
    }


    //5.添加讲师接口的方法
    // 先保证添加的时间为了能通用，在common的service_base中采用了配置
    // 添加方法  用RequestBody传
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean save = teacherService.save(eduTeacher);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    //5.根据讲师id进行查询
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable String id) {
        EduTeacher eduTeacher = teacherService.getById(id);
        return R.ok().data("teacher", eduTeacher);
    }

    //6.讲师修改功能
    @PostMapping("updateTeacher") //RequestBody必须Post
    public R updateTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean flag = teacherService.updateById(eduTeacher);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }
}