import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios"; // Axios import
import "../css/Login.css";
import logo from "../assets/img/delta-blu.svg";

function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            const params = new URLSearchParams();
            params.append('username', username);
            params.append('password', password);

            const response = await axios.post("http://localhost:8080/api/auth/login", params, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });

            if (response.status === 200) {
                const { token } = response.data;
                localStorage.setItem('authToken', token); // JWT 토큰을 localStorage에 저장
                localStorage.setItem('userId', response.data.userId); // 사용자 ID 저장
                localStorage.setItem('username', username); // 사용자 username 저장
                alert("로그인 성공!");
                navigate("/"); // 홈 페이지로 이동
            } else {
                alert("로그인에 실패했습니다. 다시 시도해주세요.");
            }
        } catch (error) {
            console.error("로그인 중 오류 발생:", error);
            if (error.response && error.response.status === 401) {
                alert("잘못된 사용자 이름 또는 비밀번호입니다.");
            } else {
                alert("로그인 중 오류가 발생했습니다.");
            }
        }
    };

    return (
        <div className="login-container">
            <form className="login-form" onSubmit={handleLogin}>
                <div className="login-logo">
                    <img src={logo} alt="Delta Logo" />
                    <a>Delta</a>
                </div>
                <input
                    type="text"
                    placeholder="사용자 이름"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                />
                <input
                    type="password"
                    placeholder="비밀번호"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                <button
                    className="login-btn"
                    type="submit"
                >
                    <a>LOGIN</a>
                </button>
                <div className="signup-link">
                    <a>계정이 없으신가요? </a>
                    <span onClick={() => navigate("/signup")}>회원가입</span>
                </div>
            </form>
        </div>
    );
}

export default Login;
