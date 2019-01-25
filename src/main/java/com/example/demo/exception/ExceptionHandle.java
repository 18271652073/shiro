package com.example.demo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Administrator
 * @date 2018/8/28.
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandle {


    @ExceptionHandler
    public ModelAndView handleDuplicateKeyException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException, ServletException {
        String exception = e.getClass().getSimpleName();
        ModelAndView modelAndView = new ModelAndView();
        switch (exception) {
            case "ConcurrentAccessException":
                modelAndView.setViewName("login");
//                modelAndView.setViewName("home");
                modelAndView.addObject("msg", e.getMessage());
                break;
            case "IncorrectCredentialsException":
                modelAndView.setViewName("login");
                modelAndView.addObject("msg", "用户名密码错误！");
                break;
            case "ExcessiveAttemptsException":
                modelAndView.setViewName("login");
                modelAndView.addObject("msg",  e.getMessage());
                break;
            case "MyException":
                modelAndView.setViewName("login");
                modelAndView.addObject("msg", e.getMessage());
                break;
            default:
                modelAndView.setViewName("login");
                modelAndView.addObject("msg", e.getMessage());
        }
        return modelAndView;
    }

//    @ExceptionHandler(ConcurrentAccessException.class)
//    public Map<String,Object> handleDuplicateKeyException(ConcurrentAccessException e) throws IOException {
//        Map<String,Object> map=new HashMap<>();
//        map.put("error:",e.getMessage());
//        return map;
//    }
//    @ExceptionHandler(AuthorizationException.class)
//    public Map<String,Object> handleDuplicateKeyException(AuthorizationException e) throws IOException {
//        Map<String,Object> map=new HashMap<>();
//        map.put("error:",e.getMessage());
//        return map;
//    }
//    @ExceptionHandler(IncorrectCredentialsException.class)
//    public Map<String,Object> handleDuplicateKeyException(IncorrectCredentialsException e) throws IOException {
//        Map<String,Object> map=new HashMap<>();
//        map.put("error:",e.getMessage());
//        return map;
//    }
//    @ExceptionHandler(ExcessiveAttemptsException.class)
//    public Map<String,Object> handleDuplicateKeyException(ExcessiveAttemptsException e) throws IOException {
//        Map<String,Object> map=new HashMap<>();
//        map.put("error:",e.getMessage());
//        return map;
//    }
//    @ExceptionHandler(MyException.class)
//    public Map<String,Object> handleDuplicateKeyException(MyException e) throws IOException {
//        Map<String,Object> map=new HashMap<>();
//        map.put("error:",e.getMessage());
//        return map;
//    }


//    @ExceptionHandler
//    public ResultEntity handler(HttpServletRequest req, HttpServletResponse res, Exception ex) throws IOException {
//        if (res.getStatus() == HttpStatus.BAD_REQUEST.value()) {
//            log.info("修改返回状态值为200");
//            res.setStatus(HttpStatus.OK.value());
//        }
//        String exceptionName = ex.getClass().getSimpleName();
//        switch (exceptionName) {
//            case "BusinessException":
//                break;
//            case "BusinessException1":
//                break;
//            case "BusinessException2":
//                break;
//            case "BonusInsertException":
//                break;
//            default:
//                res.sendRedirect("/404");
//                log.error("ControllerExceptionHandleAdvice:: URL=" + req.getRequestURL(), ex);
//                break;
//        }
//        return ResultEntity.error(ex.getMessage());
//    }

}
