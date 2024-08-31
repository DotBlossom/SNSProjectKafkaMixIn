import React, { useState, useEffect } from 'react';
import PostModal from '../components/PostModal';  // 모달 컴포넌트 import
import PostCard from '../components/PostCard';  // PostCard 컴포넌트 import
import { userApi } from '../api/controller/userApi';  // API 모듈 import
import { postApi } from '../api/controller/postApi';
import '../css/Home.css';  // 필요한 스타일링 import

export default function Home() {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [posts, setPosts] = useState([]);  // 게시물 상태 추가
    const [error, setError] = useState('');  // 오류 상태 추가
    const [loading, setLoading] = useState(true);  // 로딩 상태 추가

    // 모달을 여는 함수
    const openModal = () => {
        setIsModalOpen(true);
    };

    // 모달을 닫는 함수
    const closeModal = () => {
        setIsModalOpen(false);
    };

    useEffect(() => {
        fetchPosts();
    }, []);

    const fetchPosts = async () => {
        try {
            // 현재 로그인된 사용자 ID 가져오기
            const loggedInUserId = localStorage.getItem('userId');
            if (!loggedInUserId) {
                setError('로그인이 필요합니다.');
                setLoading(false);
                return;
            }

            // 현재 사용자의 팔로우 중인 사용자 ID 가져오기
            const response = await userApi.getUser(loggedInUserId);
            const followingUsers = response.data.followings; // 팔로우 중인 유저 ID 리스트

            // 팔로우 중인 모든 유저들의 게시물 가져오기
            const postPromises = followingUsers.map(userId =>
                postApi.getPostsByUserId(userId)
            );

            // 모든 게시물 가져오기 완료
            const postResults = await Promise.all(postPromises);

            // 게시물 배열 합치기 및 정렬
            const allPosts = postResults.flatMap(res => res.data);
            allPosts.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt)); // 최신순 정렬

            // 상태 업데이트
            setPosts(allPosts);
        } catch (err) {
            console.error('게시물 로드 중 오류 발생:', err);
            setError('게시물 로드 중 오류가 발생했습니다.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className={`home-container ${isModalOpen ? 'modal-open' : ''}`}>
            {/* 새 Post 작성 버튼 */}
            <button className="new-post-btn" onClick={openModal}>
                <img src="/src/assets/img/Edit.svg" alt="New Post" />
            </button>

            {/* Post 작성 모달 */}
            {isModalOpen && <PostModal onClose={closeModal} />}

            {/* 게시물 출력 */}
            {loading ? (
                <p>로딩 중...</p>
            ) : error ? (
                <p className="error">{error}</p>
            ) : posts.length > 0 ? (
                posts.map(post => (
                    <PostCard key={post.postId} post={post} />
                ))
            ) : (
                <p>팔로우 중인 사용자의 게시물이 없습니다.</p>
            )}
        </div>
    );
}
