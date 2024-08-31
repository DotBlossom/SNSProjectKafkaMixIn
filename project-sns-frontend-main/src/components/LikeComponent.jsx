import React, { useState, useEffect } from "react";
import "../css/LikeComponent.css";
import heartIcon from "../assets/img/Heart.svg";
import filledHeartIcon from "../assets/img/filled-Heart.svg";
import { postLikeApi } from "../api/controller/postLikeApi";
import { postApi } from "../api/controller/postApi";

const LikeComponent = ({ postId }) => {
  const [hasLiked, setHasLiked] = useState(false);
  const [likesCount, setLikesCount] = useState(0);
  const [likeId, setLikeId] = useState(null); // postLikeId 상태 추가
  const userId = localStorage.getItem("userId");
  const token = localStorage.getItem("authToken");

  useEffect(() => {
    const fetchLikes = async () => {
      try {
        const response = await postLikeApi.getLikesByPostId(postId, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        setLikesCount(response.data.length);

        response.data.forEach((like) => {
          if (like.user.userId === parseInt(userId, 10)) {
            setHasLiked(true);
            setLikeId(like.userPostLikeId); // postLikeId 저장
          }
        });
      } catch (error) {
        console.error("Error fetching likes:", error);
      }
    };

    fetchLikes();
  }, [postId, userId, token]);

  const handleLike = async () => {
    if (hasLiked) {
      try {
        await postLikeApi.unlikePost(likeId, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        setHasLiked(false);
        setLikesCount((prevCount) => prevCount - 1);
        setLikeId(null); // 좋아요 취소 후 likeId 초기화
      } catch (error) {
        console.error("Error unliking post:", error);
      }
    } else {
      try {
        const response = await postLikeApi.likePost(postId, userId, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        setHasLiked(true);
        setLikesCount((prevCount) => prevCount + 1);
        setLikeId(response.data.postLikeId); // 새로 생성된 likeId 저장
      } catch (error) {
        console.error("Error liking post:", error);
      }
    }
  };

  return (
    <div className="heart-compo">
      <img
        src={hasLiked ? filledHeartIcon : heartIcon}
        alt="Like"
        className={`heart-icon ${hasLiked ? "liked" : ""}`}
        onClick={handleLike}
        style={{ cursor: "pointer" }}
      />
      <p className="like-value">{likesCount} Likes</p>
    </div>
  );
};

export default LikeComponent;
