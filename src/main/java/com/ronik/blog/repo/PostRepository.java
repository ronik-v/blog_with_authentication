package com.ronik.blog.repo;

import com.ronik.blog.models.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> { }
