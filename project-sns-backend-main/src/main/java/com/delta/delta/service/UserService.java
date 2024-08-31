package com.delta.delta.service;

import com.delta.delta.entity.User;

public interface UserService {

    User getUser(Long userId);

    User getUserByUsername(String username);

    User createUser(User user);

    User updateUser(Long userId, User user);

    void deleteUser(Long userId);

    void addFollower(Long userId, Long followerId);

    void deleteFollower(Long userId, Long followerId);

    String addFollowing(Long userId, Long followingId);

    void deleteFollowing(Long userId, Long followingId);
}
