import { Route, Routes } from "react-router-dom";
import "./App.css";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import Home from "./pages/Home";
import Layout from "./pages/Layout";
import Message from "./pages/Message";
import Navigate from "./pages/Navigate";
import Notification from "./pages/Notification";
import Profile from "./pages/Profile";
import Search from "./pages/Search";
import PostDetail from "./pages/PostDetail";

function App() {

    return (
        <Routes>
            <Route path="/" element={<Layout />}>
                <Route index element={<Home />} />
                <Route path="postDetail/:id" element={<PostDetail />} />
                <Route path="message" element={<Message/>}/>
                <Route path="notification" element={<Notification />} />
                <Route path="navigate" element={<Navigate/>}/>
                <Route path="profile/:id" element={<Profile />} />
                <Route path="search" element={<Search />} />
            </Route>
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<Signup />} />
        </Routes>
    );
}

export default App;
