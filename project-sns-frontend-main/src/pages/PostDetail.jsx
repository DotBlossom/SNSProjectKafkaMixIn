import React, { useState, useEffect, useRef } from 'react';
import '../css/PostDetail.css';
import moreHorizontal from 'src/assets/img/MoreHorizontal.svg';
import heart from 'src/assets/img/Heart.svg';
import edit from 'src/assets/img/Edit 3.svg';
import sendIcon from 'src/assets/img/Comment.svg';
import deleteIcon from 'src/assets/img/Delete.svg';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { postApi } from '../api/controller/postApi';
import { commentApi } from '../api/controller/commentApi';
import Comment from './Comment';

export default function PostDetail() {
    const { id } = useParams();
    const currentUserId = localStorage.getItem("userId");
    const currentUsername = localStorage.getItem("username");

    const [post, setPost] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isPostOptions, setIsPostOptions] = useState(false);
    const [isCommentBoxVisible, setIsCommentBoxVisible] = useState(false);
    const [modalPosition, setModalPosition] = useState({ top: 0, left: 0 });
    const [commentContent, setCommentContent] = useState('');
    const [commentList, setCommentList] = useState([]);
    const modalRef = useRef(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchPost = async () => {
            try {
                const response = await postApi.getPostById(id);
                setPost(response.data);
                console.log(response.data);
            } catch (error) {
                console.error("Error fetching post data:", error);
            }
        };

        fetchPost();
    }, [id]);

    useEffect(() => {
        fetchCommentList();
    }, []);

    const fetchCommentList = async () => {
        try {
            const response = await commentApi.getCommentsByPostId(id);
            setCommentList(response.data);
            console.log(response.data);
        } catch (error) {
            console.error("Error fetching comment list data", error);
        }
    };

    const updateCommentInList = (updatedComment, isDeleted = false) => {
        if (isDeleted) {
            // 댓글 삭제 후 없데이트
            setCommentList((prevComments) =>
                prevComments.filter(comment => comment.commentId !== updatedComment.commentId)
            );
        } else {
            // 댓글 수정 후 업데이트
            setCommentList((prevComments) =>
                prevComments.map((comment) =>
                    comment.commentId === updatedComment.commentId ? updatedComment : comment
                )
            );
        }
    };

    const toggleModal = (event, isPost, commentUsername) => {
        const buttonRect = event.target.getBoundingClientRect();
        setModalPosition({ top: buttonRect.bottom + window.scrollY, left: buttonRect.left + window.scrollX });
        setIsModalOpen(true);

        if (isPost) {
            setIsPostOptions(true);
        } else {
            setIsPostOptions(false);
        }
    };

    const closeModal = (e) => {
        if (modalRef.current && !modalRef.current.contains(e.target)) {
            setIsModalOpen(false);
        }
    };

    useEffect(() => {
        document.addEventListener("mousedown", closeModal);

        return () => {
            document.removeEventListener("mousedown", closeModal);
        };
    }, []);

    const handleWriteComment = () => {
        setIsModalOpen(false);
        setIsCommentBoxVisible(true);
    };

    const handleEditClick = () => {
        // setIsEditing(true);
        // setIsModalOpen(false);
        // setCommentContent(post.content);
    };

    const handleSaveComment = async () => {
        console.log('Comment sent:', commentContent);

        try {
            await commentApi.createComment(id, localStorage.getItem("userId"), commentContent);
        } catch (error) {
            console.error("Error sending comment:", error);
        }

        setIsCommentBoxVisible(false);
        setCommentContent('');
        fetchCommentList();
    };

    const handleCancelComment = () => {
        setIsCommentBoxVisible(false);
        setCommentContent('');
        setIsModalOpen(false);
        // 삭제 로직 작성
        console.log("Cancel comment");
    };

    const handleDeletePost = async () => {
        const decision = confirm("정말 해당 게시물을 삭제하시겠습니까?");

        if (decision) {
            try {
                await postApi.deletePost(post.postId);
                navigate("/profile");
            } catch (error) {
                console.error("Error deleting post:", error);
            }
        }
    }

    return (
        <div className='post-detail-container'>
            <div className='post-detail-box'>
                <div className='post-detail-user-basic-info'>
                    <Link to={`/profile/${post?.user?.userId}`} style={{ textDecoration: 'none', color: 'inherit' }}>
                        <div className='post-detail-user-info'>
                            <img src={post?.user?.profileImage || 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png'} alt="User" />
                            <p className='user-name'>{post?.user?.username}</p>
                        </div>
                    </Link>
                    <img
                        className='post-detail-option'
                        src={moreHorizontal}
                        alt="More options"
                        onClick={(e) => toggleModal(e, true)}
                    />
                </div>

                {post?.images?.length > 0 && (
                    <>
                        <img
                            src={`http://localhost:8080/uploads/${post.images[0].fileName}`}
                            alt="Post"
                        />
                        <p className='post-detail-content'>{post.content}</p>
                    </>
                )}

                <div className='post-detail-functions'>
                    <img src={heart} alt="Like" />
                    <img src={edit} alt="Edit" onClick={handleWriteComment} />
                </div>

                {/* Conditional Comment Box */}
                {isCommentBoxVisible && (
                    <div className='post-detail-comments-box'>
                        <div className='post-detail-comments'>
                            <img src='https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png' alt="Commenter" />
                            <div>
                                <p className='post-detail-commenter-user-name'>{localStorage.getItem("username")}</p>
                                <textarea
                                    className='post-detail-comments-new'
                                    value={commentContent}
                                    onChange={(e) => setCommentContent(e.target.value)}
                                    placeholder="Write a comment..."
                                />
                            </div>
                        </div>
                        <div className="comment-actions">
                            <img
                                src={sendIcon}
                                alt="Send"
                                onClick={handleSaveComment}
                                style={{ cursor: 'pointer', marginRight: '10px' }}
                            />
                            <img
                                src={deleteIcon}
                                alt="Delete"
                                onClick={handleCancelComment}
                                style={{ cursor: 'pointer' }}
                            />
                        </div>
                    </div>
                )}

                {/* Comment Section */}
                <div className='post-detail-comments-section'>
                    {commentList?.map((comment) => (
                        <Comment
                            key={comment.commentId}
                            comment={comment}
                            currentUsername={currentUsername}
                            onUpdateComment={updateCommentInList}
                        />
                    ))}
                </div>

                {/* Modal */}
                {isModalOpen && (
                    <div
                        className="modal"
                        ref={modalRef}
                        style={{ top: modalPosition.top, left: modalPosition.left }}
                    >
                        <ul>
                            {isPostOptions ? (
                                <>
                                    <li onClick={handleEditClick}><img src={edit} alt="edit" /> 게시물 수정</li>
                                    <li onClick={handleDeletePost}><img src={deleteIcon} alt="delete" /> 게시물 삭제</li>
                                </>
                            ) : (
                                <>
                                    <li onClick={() => setIsModalOpen(false)}><img src={sendIcon} alt="reply" /> 게시물 보내기</li>
                                </>
                            )}
                        </ul>
                    </div>
                )}
            </div>
        </div>
    );
}
