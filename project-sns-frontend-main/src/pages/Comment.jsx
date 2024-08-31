import React, { useState, useRef, useEffect } from 'react';
import moreVertical from 'src/assets/img/More Vertical.svg';
import edit from 'src/assets/img/Edit 3.svg';
import sendIcon from 'src/assets/img/Comment.svg';
import deleteIcon from 'src/assets/img/Delete.svg';
import { commentApi } from '../api/controller/commentApi';

export default function Comment({ comment, currentUsername, onUpdateComment }) {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [modalPosition, setModalPosition] = useState({ top: 0, left: 0 });
    const [isEditing, setIsEditing] = useState(false);
    const [editedContent, setEditedContent] = useState(comment.content);
    const modalRef = useRef(null);

    const toggleModal = (event) => {
        const buttonRect = event.target.getBoundingClientRect();
        setModalPosition({ top: buttonRect.bottom + window.scrollY, left: buttonRect.left + window.scrollX });
        setIsModalOpen(!isModalOpen);
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

    const handleEditClick = () => {
        setIsEditing(true);
        setIsModalOpen(false);
    };

    const handleCancelClick = () => {
        setIsEditing(false);
    };

    const handleSaveClick = async () => {
        try {
            await commentApi.updateComment(comment?.commentId, editedContent);
            setIsEditing(false);

            const updatedComment = { ...comment, content: editedContent };
            onUpdateComment(updatedComment);
        } catch (error) {
            console.error('Error updating comment:', error);
        }
    };

    const handleReplyClick = () => {

    }

    const handleDeleteClick = async () => {
        try {
            await commentApi.deleteComment(comment?.commentId);
            
            onUpdateComment(comment, true);
        } catch (error) {
            console.error('Error deleting comment:', error);
        }
    };

    return (
        <div className='post-detail-comments-box'>
            <div className='post-detail-comments'>
                <img
                    src={comment.user.profileImage || 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png'}
                    alt="Commenter"
                />
                <div>
                    <p className='post-detail-commenter-user-name'>{comment.user.username}</p>
                    {isEditing ? (
                        <textarea
                            className='post-detail-comments-new'
                            value={editedContent}
                            onChange={(e) => setEditedContent(e.target.value)}
                        />
                    ) : (
                        <p className='post-detail-comments-content'>{comment.content}</p>
                    )}
                </div>
            </div>
            {isEditing ? (
                <div className="comment-actions">
                    <img
                        src={sendIcon}
                        alt="Send"
                        onClick={handleSaveClick}
                        style={{ cursor: 'pointer', marginRight: '10px' }}
                    />
                    <img
                        src={deleteIcon}
                        alt="Delete"
                        onClick={handleCancelClick}
                        style={{ cursor: 'pointer' }}
                    />
                </div>
            ) : (
                <img
                    className='comment-option'
                    src={moreVertical}
                    alt="Comment options"
                    onClick={toggleModal}
                />
            )}

            {isModalOpen && (
                <div
                    className="modal"
                    ref={modalRef}
                    style={{ top: modalPosition.top, left: modalPosition.left }}
                >
                    <ul>
                        {comment.user.username === currentUsername ? (
                            <>
                                <li onClick={handleEditClick}><img src={edit} alt="edit" /> 댓글 수정</li>
                                <li onClick={handleReplyClick}><img src={sendIcon} alt="reply" /> 답글 달기</li>
                                <li onClick={handleDeleteClick}><img src={deleteIcon} alt="delete" /> 댓글 삭제</li>
                            </>
                        ) : (
                            <>
                                <li onClick={handleReplyClick}><img src={sendIcon} alt="reply" /> 답글 달기</li>
                                <li onClick={handleDeleteClick}><img src={deleteIcon} alt="delete" /> 댓글 삭제</li>
                            </>
                        )}
                    </ul>
                </div>
            )}
        </div>
    );
}
