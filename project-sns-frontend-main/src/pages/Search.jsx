import React, { useState, useEffect } from 'react';
import { userApi } from '../api/controller/userApi';
import { Link, useLocation } from 'react-router-dom'; 
import searchIcon from '../assets/img/Search.svg'; // 검색 아이콘 이미지 import
import '../css/Search.css'; // CSS 파일 이름도 고유하게 변경
import FollowButton from '../components/FollowButton'; // FollowButton 컴포넌트 import

export default function SearchComponent() {
    const [user, setUser] = useState(null);
    const [error, setError] = useState("");
    const location = useLocation();
    const [searchUserName, setSearchUserName] = useState("");
    const [currentUserId, setCurrentUserId] = useState(null); // 현재 로그인된 사용자 ID

    useEffect(() => {
        const params = new URLSearchParams(location.search);
        const query = params.get("query"); // URL에서 'query' 파라미터 가져오기

        if (query) {
            setSearchUserName(query);
            handleSearch(query);
        }

        // 현재 로그인된 사용자 ID 가져오기 (로컬 스토리지에서 가져올 수도 있음)
        setCurrentUserId(localStorage.getItem('userId'));
    }, [location]);

    // 사용자 이름으로 검색
    const handleSearch = async (userName) => {
        try {
            const response = await userApi.getUsername(userName);
            setUser(response.data);
            setError(""); // 오류 메시지 초기화
        } catch (err) {
            console.error("사용자 검색 중 오류 발생:", err);
            setError("사용자를 찾을 수 없습니다.");
            setUser(null);
        }
    };

    return (
        <div className="search-component-container"> {/* 전체 스타일을 적용 */}
            <div className="search-component-search-container">
                <input
                    className="search-component-user-input"
                    type="text"
                    placeholder="사용자명 검색"
                    value={searchUserName}
                    onChange={(e) => setSearchUserName(e.target.value)}
                />
                <button onClick={() => handleSearch(searchUserName)} className="search-component-search-btn">
                    <img src={searchIcon} alt="Search Icon" className="search-component-search-icon" />
                </button>
            </div>

            <div className="search-component-message-container"> {/* 사용자 정보 표시 영역 */}
                {error && <p style={{ color: 'red' }}>{error}</p>}

                {user && (
                    <div className="search-component-message-content">
                        <Link to={`/profile/${user.userId}`} style={{ textDecoration: 'none' }}>
                            <div className="search-component-profile-row">
                                <img
                                    src="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png"
                                    alt={user.username}
                                    className="search-component-user-image search-component-active-border"
                                />
                            </div>
                        </Link>
                        <div className="search-component-username-row">
                            <p className="search-component-username">{user.username}</p>
                        </div>
                        {currentUserId && (
                            <FollowButton 
                                currentUserId={currentUserId} 
                                selectedUserId={user.userId} 
                            />
                        )}
                    </div>
                )}
            </div>
        </div>
    );
}
