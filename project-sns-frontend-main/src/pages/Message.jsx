import { useState } from "react";
import search from "../assets/img/Search.svg";
import send from "../assets/img/Send-gry.svg";
import "../css/Message.css";

export default function Message() {
  const [searchTerm, setSearchTerm] = useState("");

  const users = [
    {
      id: 1,
      name: "User 1",
      imgSrc:
        "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png",
    },
    {
      id: 2,
      name: "User 2",
      imgSrc:
        "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png",
    },
    {
      id: 3,
      name: "User 3",
      imgSrc:
        "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png",
    },
    {
      id: 4,
      name: "User 4",
      imgSrc:
        "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png",
    },
    {
      id: 5,
      name: "User 5",
      imgSrc:
        "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png",
    },
    {
      id: 6,
      name: "User 6",
      imgSrc:
        "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png",
    },
  ];

  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value);
  };

  return (
    <div className="Message">
      <div className="search-container">
        <input
          className="user-input"
          type="text"
          placeholder="사용자 입력"
          value={searchTerm}
          onChange={handleSearchChange}
        />
        <button onClick={""} className="search-btn">
          <img src={search} alt="Search Icon" className="search-icon" />
        </button>
      </div>

      <div className="message-container">
        <div className="profile-row">
          {users.map((user) => (
            <img
              key={user.id}
              src={user.imgSrc}
              alt={user.name}
              className={`user-image ${
                searchTerm.toLowerCase() === user.name.toLowerCase()
                  ? "active-border"
                  : ""
              }`}
            />
          ))}
        </div>
        <div className="username-row">
          {users.map((user) => (
            <p key={user.id} className="username">
              {user.name}
            </p>
          ))}
        </div>
        <hr className="message-hr" />
        <div className="message-content">
          <div></div>
          <div className="send-container">
            <input className="user-message-input" type="text" />
            <button onClick={""} className="send-btn">
              <img src={send} alt="Send Icon" className="send-icon" />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
