package com.ww.springmvcFrameworkDemo.mvcFramework.servlet;

import com.ww.springmvcFrameworkDemo.mvcFramework.annotation.MVCAutowired;
import com.ww.springmvcFrameworkDemo.mvcFramework.annotation.MVCController;
import com.ww.springmvcFrameworkDemo.mvcFramework.annotation.MVCService;

import javax.print.attribute.standard.PrinterURI;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

public class DispatchServlet extends HttpServlet {
    private Properties contextConfig;
    private List<String> classNames = new ArrayList<String>();
    private Map<String, Object> ioc = new HashMap<String, Object>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    //初始化
    @Override
    public void init(ServletConfig config) throws ServletException {
        //1.加载配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));

        //2.通过解析配置文件中的内容，扫描出所有相关类
        doScanner(contextConfig.getProperty("scanPackage"));

        //3.把所有扫描出来的类实例化
        doInstance();

        //4.将实例化好的Bean进行依赖注入
        doAutowired();

        //5.初始化HandlerMapping
        initHandlerMapping();
    }

    /**
     * 这里才是真正MVC的内容
     */
    private void initHandlerMapping() {
    }

    /**
     * 依赖注入
     */
    private void doAutowired() {
        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            //因为进行依赖注入，实际上就是对属性赋值
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(MVCAutowired.class)) {
                    continue;
                }
                MVCAutowired autowired = field.getAnnotation(MVCAutowired.class);
                String beanName = autowired.value().trim();
                if ("".equals(beanName)) {
                    beanName = field.getType().getName();
                }
                //忽略private访问范围修饰符,强行访问
                field.setAccessible(true);
                //依赖注入就是赋值
                //反射机制中set方法,第一个参数是实参，第二个是实例
                try {
                    field.set(entry.getValue(),ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 实例化
     */
    private void doInstance() {
        if (classNames.isEmpty()) {
            return;
        }
        for (String className : classNames) {
            try {
                Class<?> clazz = Class.forName(className);
                //通过反射实例化类
                if (clazz.isAnnotationPresent(MVCController.class)) {
                    //BeanId通常是类名首字母小写
                    this.ioc.put(toLowerFirstCase(clazz.getSimpleName()), clazz.newInstance());
                } else if (clazz.isAnnotationPresent(MVCService.class)) {
                    //默认首字母小写
                    //如果自己定义beanName的话，要优先使用自己定义的
                    //如果注入的对象是接口，要把实现类赋值给它
                    MVCService service = clazz.getAnnotation(MVCService.class);
                    String beanName = service.value();
                    if ("".equals(beanName.trim())) {
                        beanName = toLowerFirstCase(clazz.getSimpleName());
                    }
                    Object instance = clazz.newInstance();
                    this.ioc.put(beanName, instance);
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> cla : interfaces) {
                        ioc.put(cla.getSimpleName(), instance);
                    }
                } else {
                    continue;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private void doScanner(String scanPackage) {

        URL url = this.getClass().getClassLoader().getResource(File.separator + scanPackage.replaceAll("\\.", File.separator));
        File classDir = new File(url.getFile());
        for (File file :
                classDir.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                String className = scanPackage + "." + file.getName().replace(".class", "");
                classNames.add(className);
            }
        }
    }

    private void doLoadConfig(String location) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(location);
        try {
            contextConfig.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String toLowerFirstCase(String simpleName) {

        char[] chars = simpleName.toCharArray();
        //利用ASCII大小写差32
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
