package com.example.news_service.controllers;

import com.example.news_service.models.News;
import com.example.news_service.services.NewsService;
import com.google.common.io.Resources;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService newsService;


    //from
    /*@GetMapping("/test")
    public News getNews() throws IOException {
        System.out.println("getNews");
        News news = newsService.getNews();

        File file = new File("");
        byte[] fileBytes = Files.readAllBytes(file.toPath());

        String encodedString = Base64.getEncoder().encodeToString(fileBytes);
        news.setImg(encodedString);

        //byte[] decodedBytes = Base64.decode(encodedString, Base64.DEFAULT);
        //Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

        return news;
    }*/

    @GetMapping("/getNews")
    public List<News> getNews(/*Principal principal*/
            @RequestParam("faculty") String faculty) {
        //Узнаем факультет, чтобы отправить новости только текущего факультета
        System.out.println("getNews");

        //
        return newsService.getNews(faculty);
    }

    @GetMapping("/getOneNews")
    public News getOneNews() {
        return new News();
    }

    @PostMapping("/createNews")
    public void createNews(@RequestBody @Valid News news,
                           BindingResult bindingResult) {
        System.out.println("createNews");

        if (bindingResult.hasErrors())
            return;

        newsService.createNews(news);
    }
}