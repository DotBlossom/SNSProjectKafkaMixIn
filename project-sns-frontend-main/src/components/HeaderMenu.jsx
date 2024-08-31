import React, { useState, useEffect, useRef } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../css/HeaderMenu.css";
import "../css/Modal.css";
import message from "src/assets/img/Message circle.svg";
import bell from "src/assets/img/Bell.svg";
import compass from "src/assets/img/Compass.svg";
import user from "src/assets/img/User.svg";
import vertical from "src/assets/img/More vertical.svg";
import SearchIcon from "src/assets/img/HeaderSearch.svg"; // Search 대신 이름 변경
import link from "src/assets/img/Link.svg";
import option from "src/assets/img/Option.svg";
import logout from "src/assets/img/Logout.svg";

import NotificationModal from "./NotificationModal";

function HeaderMenu() {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isSearchOpen, setIsSearchOpen] = useState(false);
    const [searchQuery, setSearchQuery] = useState(""); // 검색어를 위한 상태 추가
    const modalRef = useRef(null);
    const buttonRef = useRef(null);
    const searchRef = useRef(null);
    const navigate = useNavigate(); // 페이지 이동을 위한 useNavigate 사용

    const toggleModal = () => {
        setIsModalOpen(!isModalOpen);
    };

    const closeModal = (e) => {
        if (
            modalRef.current && !modalRef.current.contains(e.target) &&
            buttonRef.current && !buttonRef.current.contains(e.target) &&
            searchRef.current && !searchRef.current.contains(e.target)
        ) {
            setIsModalOpen(false);
            setIsSearchOpen(false);
        }
    };

    const handleMenuClick = () => {
        setIsModalOpen(false);
    };

    const handleSearchClick = () => {
        if (isSearchOpen && searchQuery.trim()) {
            // 검색창이 열려있고, 검색어가 입력된 경우 검색 실행
            navigate(`/search?query=${searchQuery}`);
        } else if (isSearchOpen && !searchQuery.trim()) {
            // 검색창이 열려있지만 검색어가 입력되지 않은 경우 검색창 닫기
            setIsSearchOpen(false);
        } else {
            // 검색창 열기
            setIsSearchOpen(true);
        }
    };


    // notificationModalPart --Start
    
    const [isNotifyModalOpen, setIsNotifyModalOpen] = useState(false);
    
    const openNotifyModal = () => {
        setIsNotifyModalOpen(true);
    }
    const closeNotifyModal = () => {
        setIsNotifyModalOpen(false);
    }


    // notificationModalPart --End



    useEffect(() => {
        document.addEventListener("mousedown", closeModal);

        return () => {
            document.removeEventListener("mousedown", closeModal);
        };
    }, []);

    return (
        <>
            <div className="image-container">
                {isSearchOpen && (
                    <input
                        type="text"
                        className="search-input"
                        placeholder="사용자명 검색"
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                        ref={searchRef}
                    />
                )}
                <img src={SearchIcon} onClick={handleSearchClick} alt="검색 아이콘" />
                <Link to="/message"><img src={message} alt="메세지 아이콘" /></Link>
                <div>
                    <img 
                        src={bell} 
                        alt="알림 아이콘" 
                        onClick={openNotifyModal}
                    
                    
                    />{isNotifyModalOpen && <NotificationModal onClose={closeNotifyModal}/>}
                
                </div>
                <Link to="/navigate"><img src={compass} alt="탐색 아이콘" /></Link>
                <Link to={`/profile/${localStorage.getItem("userId")}`}><img src={user} alt="유저 아이콘" /></Link>
                <img
                    src={vertical}
                    alt="더보기 아이콘"
                    onClick={toggleModal}
                    ref={buttonRef}
                />
                {isModalOpen && (
                    <div className="modal" ref={modalRef}>
                        <ul>
                            <li onClick={handleMenuClick}><img src={link}/>링크 복사</li>
                            <li onClick={handleMenuClick}><img src={option}/>설정</li>
                            <li onClick={handleMenuClick}><img src={logout}/>로그아웃</li>
                        </ul>
                    </div>
                )}
            </div>
        </>
    );
}

export default HeaderMenu;
