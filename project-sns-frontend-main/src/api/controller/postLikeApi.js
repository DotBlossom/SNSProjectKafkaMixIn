import instance from '../axiosInstance';

export const postLikeApi = {
    likePost: (postId, userId) => instance.post('/api/postlikes', null, { params: { postId, userId } }),

    unlikePost: (postLikeId) => instance.delete(`/api/postlikes/${postLikeId}`),

    getLikesByPostId: (postId) => instance.get(`/api/postlikes/post/${postId}`),

    getLikesByUserId: (userId) => instance.get(`/api/postlikes/user/${userId}`),

    getPostLikeById: (postLikeId) => instance.get(`/api/postlikes/${postLikeId}`),
};