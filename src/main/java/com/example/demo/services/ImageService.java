package com.example.demo.services;

import com.example.demo.entity.Image;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.exceptions.ImageNotFoundException;
import com.example.demo.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageService {

    public static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);

    private final ImageRepository imageRepository;
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public ImageService(ImageRepository imageRepository, UserService userService, PostService postService) {
        this.imageRepository = imageRepository;
        this.userService = userService;
        this.postService = postService;
    }

    public Optional<Image> findByPostId(UUID postId) {
        return this.imageRepository.findByPostId(postId);
    }

    public void delete(Image image) {
        this.imageRepository.delete(image);
    }

    public Image uploadImageToUser(MultipartFile multipartFile, Principal principal) throws IOException {
        User user = this.userService.getCurrentUser(principal);
        LOGGER.info("Uploading image profile to User {}", user.getName());

        Image imageUserProfile = this.imageRepository.findByUserId(user.getId()).orElse(null);

        if (!ObjectUtils.isEmpty(imageUserProfile)) {
            this.imageRepository.delete(imageUserProfile);
        }

        Image image = new Image();
        image.setUserId(user.getId());
        image.setImageBytes(this.compressBytes(multipartFile.getBytes()));
        image.setName(multipartFile.getOriginalFilename());
        return this.imageRepository.save(image);
    }

    public Image uploadImageToPost(MultipartFile multipartFile, Principal principal, UUID postId) throws IOException {
        User user = this.userService.getCurrentUser(principal);
        Post post = user.getPostList().stream().filter(p -> p.getId().equals(postId)).collect(this.toSinglePostCollector());

        Image image = new Image();
        image.setPostId(post.getId());
        image.setName(multipartFile.getOriginalFilename());
        image.setImageBytes(this.compressBytes(multipartFile.getBytes()));
        LOGGER.info("Uploading image to Post {}", post.getId());

        return this.imageRepository.save(image);
    }

    public Image getImageToUser(Principal principal) {
        User user = this.userService.getCurrentUser(principal);

        Image image = this.imageRepository.findByUserId(user.getId()).orElse(null);

        if (!ObjectUtils.isEmpty(image)) {
            image.setImageBytes(this.decompressBytes(image.getImageBytes()));
        }

        return image;
    }

    public Image getImageToPost(UUID postId) {
        Image image = this.imageRepository.findByPostId(postId)
                .orElseThrow(() -> new ImageNotFoundException(String.format("Cannot find image to Post: %s" , postId)));

        if (!ObjectUtils.isEmpty(image)) {
            image.setImageBytes(this.decompressBytes(image.getImageBytes()));
        }

        return image;
    }

    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        try {
            outputStream.close();
        } catch (IOException ex) {
            LOGGER.error("Cannot compress Bytes.");
        }

        LOGGER.info("Compressed Image Byte Size - {}", outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    private byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            LOGGER.error("Cannot decompress Bytes.");
        }

        return outputStream.toByteArray();
    }

    private <T> Collector<T, ?, T> toSinglePostCollector() {
        return Collectors.collectingAndThen(
                Collectors.toList(), list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }
}
