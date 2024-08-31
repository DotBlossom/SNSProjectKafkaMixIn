import React from "react";
import { Link } from "react-router-dom";
import "../css/HeaderContainer.css";
import HeaderLogo from "./HeaderLogo";
import HeaderMenu from "./HeaderMenu";

export default function HeaderContainer() {
    // 로그인된 사용자명 가져오기
    const username = localStorage.getItem('username');

    return (
        <div className="Header">
            <Link to="/" style={{ color: 'inherit', textDecoration: 'inherit'}}><HeaderLogo/></Link>
            {username && (
                <div className="Header-username">
                    <span>안녕하세요, {username}님!</span>
                </div>
            )}
            <HeaderMenu />
        </div>
    );
}
