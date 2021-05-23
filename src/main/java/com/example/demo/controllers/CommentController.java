package com.example.demo.controllers;

import com.example.demo.dto.CommentDTO;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.facade.CommentFacade;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.services.CommentService;
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
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    private final CommentFacade commentFacade;

    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public CommentController(CommentService commentService, CommentFacade commentFacade, ResponseErrorValidation responseErrorValidation) {
        this.commentService = commentService;
        this.commentFacade = commentFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/{postId}/create")
    public ResponseEntity<Object> createComment(@Valid
                                                @RequestBody CommentDTO commentDTO,
                                                @PathVariable("postId") String postId,
                                                BindingResult bindingResult,
                                                Principal principal) {
        ResponseEntity<Object> errors = this.responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        Comment comment = this.commentService.saveComment(UUID.fromString(postId), commentDTO, principal);
        CommentDTO createdComment = this.commentFacade.commentToCommentDTO(comment);

        return new ResponseEntity<>(createdComment, HttpStatus.OK);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsForPost(@PathVariable("postId") String postId) {
        List<CommentDTO> comments = this.commentService.getCommentsByPost(UUID.fromString(postId))
                .stream()
                .map(this.commentFacade::commentToCommentDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}/delete")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") String commentId) {
        this.commentService.deleteComment(UUID.fromString(commentId));

        return new ResponseEntity<>(new MessageResponse("Comment was deleted"), HttpStatus.OK);
    }
}
