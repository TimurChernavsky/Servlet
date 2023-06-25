package ru.netology.repository;

import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
    private Map<Long, Post> list;
    private AtomicLong postID;

    public List<Post> all() {
        return new ArrayList<>(list.values());

    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(list.get(id));
    }

    public Post save(Post post) {
        Random random = new Random();
        postID.addAndGet(random.nextLong());
        list.put(postID.get(), post);
        return post;
    }

    public void removeById(long id) {
        list.remove(id);
    }
}
