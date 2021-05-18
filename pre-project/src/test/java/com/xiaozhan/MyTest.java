package com.xiaozhan;

import com.alibaba.fastjson.JSONObject;
import com.xiaozhan.model.Student;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MyTest {
    //把对象转为json ：序列化
    @Test
    public void test01(){
        Student student=new Student();
        student.setId(1001);
        student.setName("肖战");
        student.setAge(29);
        String json = JSONObject.toJSONString(student);
        System.out.println("把Student转为json="+json);
    }
    @Test
    public void test02(){
        List<Student> studentList=new ArrayList<>();
        Student student=new Student();
        student.setId(1001);
        student.setName("肖战");
        student.setAge(29);
        studentList.add(student);
        student=new Student();
        student.setId(1002);
        student.setName("王一博");
        student.setAge(24);
        studentList.add(student);
        String json = JSONObject.toJSONString(studentList);
        System.out.println("List-Json"+json);

    }
    @Test
    public void test03() {
        String json="{\"age\":22,\"id\":1003,\"name\":\"波波\"}";
        Student student=JSONObject.parseObject(json,Student.class);
        System.out.println("student="+student.toString());
        JSONObject jsonObject=JSONObject.parseObject(json);
        Integer id=jsonObject.getInteger("id");
        int age=jsonObject.getIntValue("age");
        System.out.println("id="+id+" ,age="+age);
    }
    @Test
    public void test04(){
        String str="{\"age\":20,\"name\":\"李响\",\"phone\":\"1350000000\",\"school\":{\"address\":\"北京的海淀区\",\"name\":\"北京大学\"}}";
        JSONObject o1=JSONObject.parseObject(str);
        JSONObject o2=o1.getJSONObject("school");
        System.out.println("name:"+o2.getString("name"));
        JSONObject.parseObject(str).getJSONObject("school").getString("name");
    }
    @Test
    public void test05(){
        ThreadLocalRandom random=ThreadLocalRandom.current();
        /*for(int i=0;i<10;i++){
            System.out.println("nextInt="+random.nextInt());
            System.out.println("random.nextInt(100)"+random.nextInt(100));
        }*/
        for(int i=0;i<20;i++){
            System.out.println(random.nextInt(1000,10000));
        }
    }
}
