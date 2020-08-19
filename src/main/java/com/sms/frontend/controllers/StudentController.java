package com.sms.frontend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.frontend.models.Student;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class StudentController {

    private final RestTemplate restTemplate;

    public StudentController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping()
    public String StudentsList(Model model) {
        Student[] students = restTemplate.getForObject("http://localhost:8080/sms/api/all", Student[].class);

        model.addAttribute("students", students);
        return "studentList";
    }

    public static void printer(String x){
        System.out.println(x);
    }

    @PostMapping(path = "/delete")
    public String deleteAction(@RequestParam() Integer id, Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        String response = restTemplate.exchange(
                "http://localhost:8080/sms/api/" + id,
                HttpMethod.DELETE,
                entity,
                String.class
        ).getBody();

        model.addAttribute("response", response);

        return "deleted";
    }

    @GetMapping(path = "/update")
    public String upadateAction(
            @RequestParam() Integer id,
            @RequestParam() String name,
            @RequestParam() Integer age,
            @RequestParam() String gender,
            @RequestParam() String course,
            Model model
    ) {
        Student student  = new Student();
        ObjectMapper objectMapper = new ObjectMapper();

        student.setId(id);
        student.setName(name);
        student.setAge(age);
        student.setGender(gender);
        student.setCourse(course);

        String response = "failed";

        try{
            String jsonBody = objectMapper.writeValueAsString(student);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            response = restTemplate.exchange(
                    "http://localhost:8080/sms/api/" + id,
                    HttpMethod.PUT,
                    entity,
                    String.class
            ).getBody();
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }

        model.addAttribute("response", response);

        return "deleted";
    }

    @GetMapping(path = "/add")
    public String addAction(
            @RequestParam() String name,
            @RequestParam() Integer age,
            @RequestParam() String gender,
            @RequestParam() String course,
            Model model
    ) {
        Student student  = new Student();
        ObjectMapper objectMapper = new ObjectMapper();

        student.setName(name);
        student.setAge(age);
        student.setGender(gender);
        student.setCourse(course);

        String response = "failed";

        try{
            String jsonBody = objectMapper.writeValueAsString(student);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            response = restTemplate.exchange(
                    "http://localhost:8080/sms/api/add",
                    HttpMethod.POST,
                    entity,
                    String.class
            ).getBody();
        } catch (Exception e){
            e.printStackTrace();
        }

        model.addAttribute("response", response);

        return "deleted";
    }

}