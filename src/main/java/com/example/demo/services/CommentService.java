package com.example.demo.services;

import com.example.demo.dto.CommentDTO;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {

    public static final Logger LOGGER = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;

    private final PostService postService;

    private final UserService userService;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostService postService, UserService userService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.userService = userService;
    }

    public Comment saveComment(UUID postId, CommentDTO commentDTO, Principal principal) {
        User user = this.userService.getCurrentUser(principal);
        Post post = this.postService.getPostById(postId);
        Comment comment = new Comment();
        comment.setUserId(user.getId());
        comment.setPost(post);
        comment.setUsername(user.getName());
        comment.setMessage(commentDTO.getMessage());

        LOGGER.info("Saving comment for Post: {}", post.getId());
        return this.commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPost(UUID postId) {
        return this.commentRepository.findAllByPost(this.postService.getPostById(postId));
    }

    public void deleteComment(UUID commentId) {
        Optional<Comment> comment = this.commentRepository.findById(commentId);
        comment.ifPresent(this.commentRepository::delete);
    }
}
