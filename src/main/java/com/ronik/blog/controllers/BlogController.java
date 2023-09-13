package com.ronik.blog.controllers;

import com.ronik.blog.models.Post;
import com.ronik.blog.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;


@Controller
public class BlogController {
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/blog")
    public String blogMainPage(Model model) {
        model.addAttribute("title", "Блог сайта");
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "main-blog";
    }

    @GetMapping("/blog/add")
    public String addPost(Model model) {
        model.addAttribute("title", "Добавление статьи");
        return "post-add";
    }

    @PostMapping("/blog/add")
    public String postAddToDatabase(@RequestParam String title, @RequestParam String anons, @RequestParam String all_text, Model model) {
        Post post = new Post(title, anons, all_text);
        postRepository.save(post);
        return "redirect:/blog";
    }

    // view post all_text
    @GetMapping("/blog/{id}")
    public String postTextView(@PathVariable(value = "id") Long postId, Model model) {
        Optional<Post> post = postRepository.findById(postId);
        ArrayList<Post> postResult = new ArrayList<>();
        post.ifPresent(postResult::add);
        model.addAttribute("post", postResult);
        return "post-details";
    }

    @GetMapping("/blog/{id}/edit")
    public String postEdit(@PathVariable(value = "id") Long postId, Model model) {
        model.addAttribute("title", "Редактирование поста");
        if (!postRepository.existsById(postId)) {
            return "redirect:/blog";
        }
        Optional<Post> post = postRepository.findById(postId);
        ArrayList<Post> postResult = new ArrayList<>();
        post.ifPresent(postResult::add);
        model.addAttribute("post", postResult);
        return "post-edit";
    }

    @PostMapping("/blog/{id}/edit")
    public String updatePostById(@PathVariable(value = "id") Long postId, @RequestParam String title, @RequestParam String anons, @RequestParam String all_text, Model model) {
        Post post = postRepository.findById(postId).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setText(all_text);
        postRepository.save(post);
        return "redirect:/blog";
    }

    @PostMapping("/blog/{id}/remove")
    public String postDelete(@PathVariable(value = "id") Long postId, Model model) {
        Post post = postRepository.findById(postId).orElseThrow();
        postRepository.delete(post);
        return "redirect:/blog";
    }
}
