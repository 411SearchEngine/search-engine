//package com.search.engine.action;
//
//import com.search.engine.service.TestService;
//import com.search.engine.util.R;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * Created by xuh
// * DATE 2019/11/29 1:08.
// * version 1.0
// */
//@RestController
//public class TestAction {
//
//    @Autowired
//    private TestService testService;
//
//    @GetMapping("/test/{test}")
//    public R testFind(@PathVariable String test) {
//        return R.ok(this.testService.findTest(test));
//    }
//
//    @GetMapping("/add/{test}/{id}")
//    public R addTest(@PathVariable String test, @PathVariable long id) {
//        this.testService.addTest(test, id);
//        return R.ok();
//    }
//
//}
