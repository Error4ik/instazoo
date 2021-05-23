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
    public ResponseEntity<MessageResponse> uploadImageToUser(
            @RequestParam("file") MultipartFile file, Principal principal) throws IOException {

        this.imageService.uploadImageToUser(file, principal);

        return new ResponseEntity<>(new MessageResponse("Image Uploaded Successfully."), HttpStatus.OK);
    }

    @PostMapping("/{postId}/upload")
    public ResponseEntity<MessageResponse> uploadImageToPost(
            @PathVariable("postId") String postId, @RequestParam("file") MultipartFile file, Principal principal) throws IOException {

        this.imageService.uploadImageToPost(file, principal, UUID.fromString(postId));

        return new ResponseEntity<>(new MessageResponse("Image Uploaded Successfully."), HttpStatus.OK);
    }

    @GetMapping("/profile-image")
    public ResponseEntity<Image> getImageToUser(Principal principal) {
        Image image = this.imageService.getImageToUser(principal);

        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    @GetMapping("/{postId}/post-image")
    public ResponseEntity<Image> getImageToPost(@PathVariable("postId") String postId) {
        Image image = this.imageService.getImageToPost(UUID.fromString(postId));

        return new ResponseEntity<>(image, HttpStatus.OK);
    }
}
