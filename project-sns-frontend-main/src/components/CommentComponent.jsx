import "../css/CommentComponent.css";
import commentIcon from "../assets/img/Edit.svg";

export default function LikeComponent() {
  return (
    <div>
      <img src={commentIcon} alt="Comment" className="comment-icon" />
      <p className="comment-value"></p>
    </div>
  );
}
