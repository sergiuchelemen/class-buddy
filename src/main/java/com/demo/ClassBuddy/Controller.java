package com.demo.ClassBuddy;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/")
@CrossOrigin(origins = "http://localhost:5173/")


public class Controller {

    @GetMapping
    @ResponseBody
    public MyResponse hello(){
        MyResponse response = new MyResponse();
        response.setMessage("Hello World!");
        return response;
    }

    public static class MyResponse {
        private String message;

        // Constructor, getter și setter pentru 'message'

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}

