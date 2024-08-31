import instance from '../axiosInstance';

export const commentLikeApi = {
    getCommentLike: (userId, commentId) => instance.get(`/api/commentlike/user/${userId}/comment/${commentId}`),

    countCommentLikes: (commentId) => instance.get(`/api/commentlike/comment/${commentId}/count`),

    getCommentLikesByUser: (userId) => instance.get(`/api/commentlike/user/${userId}`),

    likeComment: (commentLike) => instance.post('/api/commentlike', commentLike),

    unlikeComment: (id) => instance.delete(`/api/commentlike/${id}`),
};