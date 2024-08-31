import React, { useState, useEffect } from 'react';
import { postApi } from '../api/controller/postApi';
import { imageApi } from '../api/controller/imageApi';
import imageIcon from '../assets/img/imagesave.svg';
import Arrow from '../assets/img/Arrow right.svg'
import '../css/PostModal.css';

export default function PostModal({ onClose }) {
    const [content, setContent] = useState('');
    const [image, setImage] = useState(null);
    const [isVisible, setIsVisible] = useState(false);

    useEffect(() => {
        setIsVisible(true); // 모달을 화면에 표시
    }, []);

    const handleImageChange = (e) => {
        const file = e.target.files[0];
        setImage(file);
    };

    const handleContentChange = (e) => {
        setContent(e.target.value);
    };

    const handleClose = () => {
        setIsVisible(false); // 모달이 닫힐 때
        setTimeout(() => {
            onClose(); 
        }, 400); 
    };

    const handleSubmit = async () => {
        const userId = localStorage.getItem('userId');
    
        try {
            const response = await postApi.createPost(userId, content);
            const postId = response.data.postId;
    
            if (image) {
                const formData = new FormData();
                formData.append('fileName', image);
                await imageApi.createImage(postId, formData);
            }
    
            alert('게시물이 성공적으로 업로드되었습니다.');
            onClose();
        } catch (error) {
            console.error('게시물 업로드 중 오류 발생:', error);
            alert('게시물 업로드에 실패했습니다.');
        }
    };

    return (
        <div className={`modal-overlay ${isVisible ? 'show' : ''}`}>
            <div className="modal-content">
                <div className="modal-header">
                    <span>username</span>
                </div>
                <textarea 
                    value={content} 
                    onChange={handleContentChange} 
                    placeholder="오늘은 어떤 일들이 있으셨나요?"
                />
                <div className="image-upload-container">
                    {image && (
                        <img
                            src={URL.createObjectURL(image)}
                            alt="Preview"
                            className="thumbnail"
                        />
                    )}
                    <label>
                        <img src={imageIcon} alt="Upload" />
                        <input
                            type="file"
                            style={{ display: 'none' }}
                            onChange={handleImageChange}
                        />
                    </label>
                </div>
                <div className="button-container">
                <button onClick={handleClose} className="close-modal">
                        <img src={Arrow} alt="Close"/>
                    </button>
                    <button onClick={handleSubmit} className="submit-post">게시</button>
                </div>
            </div>
        </div>
    );
}
