package com.example.demo.services;

import com.example.demo.dto.PostDTO;
import com.example.demo.entity.Image;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.exceptions.PostNotFoundException;
import com.example.demo.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {

    public static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;

    private final UserService userService;

    private final ImageService imageService;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService, ImageService imageService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.imageService = imageService;
    }

    public Post createPost(PostDTO postDTO, Principal principal) {
        User user = this.userService.getCurrentUser(principal);
        Post post = new Post();
        post.setUser(user);
        post.setCaption(postDTO.getCaption());
        post.setLocation(postDTO.getLocation());
        post.setTitle(postDTO.getTitle());
        post.setNumberOfLikes(0);

        LOGGER.info("Saving post for User: {}", user.getEmail());
        return this.postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return this.postRepository.findAllByOrderByCreatedDateDesc();
    }

    public Post getPostByIdAndUser(UUID postId, Principal principal) {
        User user = this.userService.getCurrentUser(principal);
        return this.postRepository
                .findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException(String.format("Post cannot be found for %s", user.getEmail())));
    }

    public Post getPostById(UUID postId) {
        return this.postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post cannot be found."));
    }

    public List<Post> getPostsForUser(Principal principal) {
        return this.postRepository.findAllByUserOrderByCreatedDateDesc(this.userService.getCurrentUser(principal));
    }

    public Post likeOrDislikePost(UUID postId, String name) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found."));

        Optional<String> userLiked = post.getLikedUsers()
                .stream()
                .filter(username -> username.equals(name))
                .findAny();

        if (userLiked.isPresent()) {
            post.setNumberOfLikes(post.getNumberOfLikes() - 1);
            post.getLikedUsers().remove(name);
        } else {
            post.setNumberOfLikes(post.getNumberOfLikes() + 1);
            post.getLikedUsers().add(name);
        }

        return this.postRepository.save(post);
    }

    public void deletePost(UUID postId, Principal principal) {
        Post post = getPostByIdAndUser(postId, principal);
        Optional<Image> image = this.imageService.findByPostId(post.getId());
        this.postRepository.delete(post);
        image.ifPresent(this.imageService::delete);
    }
}
