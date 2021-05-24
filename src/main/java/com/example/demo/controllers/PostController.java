package com.example.demo.controllers;

import com.example.demo.dto.PostDTO;
import com.example.demo.entity.Post;
import com.example.demo.facade.PostFacade;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.services.PostService;
import com.example.demo.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/post")
@CrossOrigin
public class PostController {

    private final PostService postService;

    private final PostFacade postFacade;

    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public PostController(PostService postService, PostFacade postFacade, ResponseErrorValidation responseErrorValidation) {
        this.postService = postService;
        this.postFacade = postFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDTO postDTO, BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = this.responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        Post post = this.postService.createPost(postDTO, principal);
        PostDTO createdPost = this.postFacade.postToPostDTO(post);

        return new ResponseEntity<>(createdPost, HttpStatus.OK);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> postDTOList = this.postService.getAllPosts()
                .stream()
                .map(this.postFacade::postToPostDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    @GetMapping("/user/posts")
    public ResponseEntity<List<PostDTO>> getPostsByUser(Principal principal) {
        List<PostDTO> postDTOList = this.postService.getPostsForUser(principal)
                .stream()
                .map(this.postFacade::postToPostDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    @PostMapping("/{postId}/{username}/like")
    public ResponseEntity<PostDTO> likePost(@PathVariable("postId") String postId, @PathVariable("username") String username) {
        Post post = this.postService.likeOrDislikePost(UUID.fromString(postId), username);
        PostDTO postDTO = this.postFacade.postToPostDTO(post);

        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}/delete")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("postId") String postId, Principal principal) {
        this.postService.deletePost(UUID.fromString(postId), principal);

        return new ResponseEntity<>(new MessageResponse("Post was deleted."), HttpStatus.OK);
    }
}
