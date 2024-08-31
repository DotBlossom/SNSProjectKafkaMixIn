import React, { useState, useEffect } from 'react';
import { userApi } from '../api/controller/userApi';
import "../css/FollowButton.css";

export default function FollowButton({ currentUserId, selectedUserId }) {
    const [isFollowing, setIsFollowing] = useState(false);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        // 현재 사용자가 선택된 사용자를 팔로우하고 있는지 확인
        // 본인의 경우 로그 메세지를 보내야되나..?

        const fetchFollowStatus = async () => {
            try {
                const response = await userApi.getUser(currentUserId);
                const followings = response.data.followings;
                setIsFollowing(followings.includes(selectedUserId));
            } catch (error) {
                console.error('팔로우 상태를 확인하는 중 오류 발생:', error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchFollowStatus();
    }, [currentUserId, selectedUserId]);

    const handleFollowToggle = async () => {
        setIsLoading(true);
        try {
            if (isFollowing) {
                await userApi.deleteFollowing(currentUserId, selectedUserId);
                setIsFollowing(false);
            } else {
                await userApi.addFollowing(currentUserId, selectedUserId);
                setIsFollowing(true);
            }
        } catch (error) {
            console.error('팔로우 상태를 변경하는 중 오류 발생:', error);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <button 
            onClick={handleFollowToggle} 
            disabled={isLoading} 
            className={`follow-button ${isFollowing ? 'following' : 'not-following'}`}
        >
            {isLoading ? '처리 중...' : (isFollowing ? '언팔로우' : '팔로우')}
        </button>
    );
}
