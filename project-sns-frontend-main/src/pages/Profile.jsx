import React, { useEffect, useState } from 'react'
import '../css/Profile.css'
import { userApi } from '../api/controller/userApi'
import { Link, useParams } from 'react-router-dom';

export default function Profile() {
    const { id } = useParams();

    const [user, setUser] = useState(null);

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const response = await userApi.getUser(id);
                setUser(response.data);
                console.log(response.data);
            } catch (error) {
                console.error("Error fetching user data:", error);
            }
        };

        fetchUser();
    }, []);

    return (
        <div className='profile-container'>
            <div className='profile-box'>
                <div className='user-info'>
                    <img src='https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png' alt='Profile'/>
                    <div className='user-detail'>
                        <p className='user-name'>{user?.username || "username"}</p> {/* Use user data here */}
                        <br/>
                        <div className='posts-and-follows'>
                            <div>
                                <p>{(user?.posts)?.length || 0}</p>
                                <p>게시물</p>
                            </div>
                            <div>
                                <p>{(user?.followers)?.length || 0}</p>
                                <p>팔로워</p>
                            </div>
                            <div>
                                <p>{(user?.followings)?.length || 0}</p>
                                <p>팔로잉</p>
                            </div>
                        </div>
                    </div>
                </div>
                
                <hr className='separate-line'/>

                <div className='posts'>
                    {user?.posts?.map((post, index) => (
                        <Link to={`/postDetail/${post?.postId}`}>
                            <img key={index} src={(post?.images).length > 0 ? `http://localhost:8080/uploads/${post?.images[0]?.fileName}` : '/src/assets/img/NoImage.png'} alt={`Post ${index}`} />
                        </Link>
                    ))}
                </div>
            </div>
        </div>
    );
}
