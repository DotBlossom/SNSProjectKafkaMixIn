import instance from '../axiosInstance';

export const userApi = {
    getUser: (userId) => instance.get(`/api/user/${userId}`),

    getUsername: (userName) => instance.get(`/api/user/username/${userName}`),

    createUser: (user) => instance.post('/api/user', user),

    updateUser: (userId, user) => instance.patch(`/api/user/${userId}`, user),

    deleteUser: (userId) => instance.delete(`/api/user/${userId}`),

    addFollower: (userId, followerId) => instance.post(`/api/user/addFollower/${userId}/${followerId}`),

    deleteFollower: (userId, followerId) => instance.post(`/api/user/deleteFollower/${userId}/${followerId}`),

    addFollowing: (userId, followingId) => instance.post(`/api/user/addFollowing/${userId}/${followingId}`),

    deleteFollowing: (userId, followingId) => instance.post(`/api/user/deleteFollowing/${userId}/${followingId}`),
    
};