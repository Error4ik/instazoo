package com.example.demo.controllers;

import com.example.demo.entity.Image;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/image")
@CrossOrigin
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadImageForUser(
            @RequestParam("file") MultipartFile file, Principal principal) throws IOException {

        this.imageService.uploadImageForUser(file, principal);

        return new ResponseEntity<>(new MessageResponse("Image Uploaded Successfully."), HttpStatus.OK);
    }

    @PostMapping("/{postId}/upload")
    public ResponseEntity<MessageResponse> uploadImageForPost(
            @PathVariable("postId") String postId, @RequestParam("file") MultipartFile file, Principal principal) throws IOException {

        this.imageService.uploadImageForPost(file, principal, UUID.fromString(postId));

        return new ResponseEntity<>(new MessageResponse("Image Uploaded Successfully."), HttpStatus.OK);
    }

    @GetMapping("/profile-image")
    public ResponseEntity<Image> getImageByUser(Principal principal) {
        Image image = this.imageService.getImageByUser(principal);

        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    @GetMapping("/{postId}/post-image")
    public ResponseEntity<Image> getImageByPost(@PathVariable("postId") String postId) {
        Image image = this.imageService.getImageByPost(UUID.fromString(postId));

        return new ResponseEntity<>(image, HttpStatus.OK);
    }
}
