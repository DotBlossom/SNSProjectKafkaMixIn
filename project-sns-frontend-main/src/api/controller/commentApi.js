import instance from '../axiosInstance';

export const commentApi = {
    createComment: (postId, userId, content) => instance.post('/api/comments', null, { params: { postId, userId, content } }),

    getCommentsByPostId: (postId) => instance.get(`/api/comments/post/${postId}`),

    getCommentsByUserId: (userId) => instance.get(`/api/comments/user/${userId}`),

    getCommentById: (commentId) => instance.get(`/api/comments/${commentId}`),

    updateComment: (commentId, content) => instance.put(`/api/comments/${commentId}`, null, { params: { content } }),

    deleteComment: (commentId) => instance.delete(`/api/comments/${commentId}`),
};