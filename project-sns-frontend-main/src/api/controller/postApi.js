import instance from '../axiosInstance';

export const postApi = {
    createPost: (userId, content) => instance.post('/api/posts', null, { params: { userId, content } }),

    getPostsByUserId: (userId) => instance.get(`/api/posts/user/${userId}`),

    getPostById: (postId) => instance.get(`/api/posts/${postId}`),

    updatePost: (postId, content) => instance.put(`/api/posts/${postId}`, null, { params: { content } }),

    deletePost: (postId) => instance.delete(`/api/posts/${postId}`),
};