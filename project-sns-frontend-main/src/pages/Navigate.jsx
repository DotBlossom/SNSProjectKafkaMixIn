import React, { useState, useEffect } from "react";
import "../css/Navigate.css";
import search from "../assets/img/Search.svg";
import loader from "../assets/img/Loader-gry.svg";
import delta from "../assets/img/delta-blk.svg";

export default function Navigate() {
  const [hashtag, setHashtag] = useState("");
  const [posts, setPosts] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);
  const [dotCount, setDotCount] = useState(1);

  useEffect(() => {
    if (loading) {
      const interval = setInterval(() => {
        setDotCount((prevCount) => (prevCount % 3) + 1); // 1, 2, 3 반복
      }, 500);

      return () => clearInterval(interval);
    }
  }, [loading]); // 로딩 상태가 변경될 때만 실행

  const handleSearch = () => {
    if (!hashtag.trim()) {
      setError("해시태그를 입력해주세요.");
      setPosts([]);
      return;
    }

    setLoading(true); // 데이터 요청 시작 시 로딩 상태로 전환
    setError(null); // 새로운 검색 시 이전 에러 메시지 초기화
    setPosts([]); // 검색 시 이전 결과 초기화

    fetch(
      `http://localhost:5000/api/search?hashtag=${encodeURIComponent(hashtag)}`
    )
      .then((response) => response.json())
      .then((data) => {
        setLoading(false); // 데이터 요청 완료 후 로딩 상태 해제
        if (data.error) {
          setError(data.error);
          setPosts([]);
        } else {
          setPosts(data);
          setError(null);
        }
      })
      .catch((err) => {
        setLoading(false); // 에러 발생 시 로딩 상태 해제
        console.error("Failed to fetch data:", err);
        setError("데이터 불러오기를 실패하였습니다.");
        setPosts([]);
      });
  };

  const handleKeyPress = (event) => {
    if (event.key === "Enter") {
      handleSearch();
    }
  };

  return (
    <div className="Navigate">
      <div className="search-container">
        {/* <img src={delta} alt="delta Icon" className="delta-icon" /> */}
        <input
          className="nav-input"
          type="text"
          value={hashtag}
          onChange={(e) => setHashtag(e.target.value)}
          onKeyDown={handleKeyPress}
          placeholder="해시태그를 입력"
        />
        <button onClick={handleSearch} className="search-btn">
          <img src={search} alt="Search Icon" className="search-icon" />
        </button>
      </div>
      <div className="posts-container">
        {/* 초기 상태 메시지 */}
        {!loading && !error && posts.length === 0 && (
          <div className="message-overlay">
            <img className="loader-img" src={loader} alt="loader" />
            <p>해시태그를 입력해주세요.</p>
          </div>
        )}

        {/* 로딩 중 메시지 */}
        {loading && (
          <div className="message-overlay">
            <img className="loader-img" src={loader} alt="loader" />
            <p>데이터를 불러오는 중입니다{".".repeat(dotCount)}</p>
          </div>
        )}

        {/* 에러 메시지 */}
        {!loading && error && (
          <div className="message-overlay">
            <img className="loader-img" src={loader} alt="loader" />
            <p>{error}</p>
          </div>
        )}

        {/* 데이터 그리드 */}
        {!loading && !error && posts.length > 0 && (
          <div className="posts-grid">
            {posts.map((post, index) => (
              <div key={index} className="post">
                <a
                  href={post.post_url}
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  <img
                    src={`http://localhost:5000/api/image?url=${encodeURIComponent(
                      post.img_url
                    )}`}
                    alt={`Post ${index + 1}`}
                  />
                </a>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
