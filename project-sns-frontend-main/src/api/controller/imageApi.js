import instance from '../axiosInstance';

export const imageApi = {
    createImage: (postId, formData) => instance.post(`/api/images`, formData, {
        params: { postId },
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    }),

    getImagesByPostId: (postId) => instance.get(`/api/images/post/${postId}`),

    getImageById: (imageId) => instance.get(`/api/images/${imageId}`),

    deleteImage: (imageId) => instance.delete(`/api/images/${imageId}`),
};