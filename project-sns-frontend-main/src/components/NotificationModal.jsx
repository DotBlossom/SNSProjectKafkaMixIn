import "../css/NotificationBox.css";
import { useEffect, useState } from "react";
import { EventSourcePolyfill } from "event-source-polyfill";
import user from "src/assets/img/User-1.svg";
export default function NotificationModal({ onClose }) {
  const [notifications, setNotications] = useState([]);

  const formatDatetime = (t) => {
    const date = new Date(t);
    const options = { hour: '2-digit', minute: '2-digit', hour12: false }; // 24시간 형식 사용
    return date.toLocaleTimeString('ko-KR', options); 
  };
  
  useEffect(() => {
    const savedLastEventId = localStorage.getItem("lastEventId");
    const loggedInUserId = localStorage.getItem("userId");
    const defaultProfile = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png";
    const eventSource = new EventSourcePolyfill(
      `http://localhost:9292/kafkaListener/subscribe/${loggedInUserId}`,
      {
        headers: {
          "Last-Event-ID": savedLastEventId, // 저장된 lastEventId 값 사용
        },
        heartbeatTimeout: 3600000,
        
      }
    );

    eventSource.onopen = async () => {
      await console.log("sse opened!");
    };

    eventSource.addEventListener("notification", (e) => {
      let res = JSON.parse(e.data);
      console.log(res);

      res.eventCreatedTime = formatDatetime(res.eventCreatedTime);
  

      //
      const resArray = Array.isArray(res) ? res : [res];

      setNotications((notifications) => [...notifications, ...resArray]);
      console.log(notifications);
      console.log(e.lastEventId);




      localStorage.setItem("lastEventId", e.lastEventId);
    });


    // if unmounted. -> 조건부렌더out, page아웃
    return () => {
      eventSource.close();

    };
  }, []);

//close when click arised outside
useEffect(() => {
  const handleMouseUpOutside = (e) => {
    const targetEventListener= document.querySelector(".notification-modal")
    if (targetEventListener && !targetEventListener.contains(e.target)) {
      onClose();
    }
  }

  // add certaineventMapper
  document.addEventListener('mouseup', handleMouseUpOutside);

},[]);




  return (
    <>
      <div className="notification-modal">
        <div className="notification-header">
            <div className="header-text">알림</div>
            <button onClick={onClose} className="close-modal">X</button>
        </div>
        <hr className="notification-line"/>
        {notifications.length > 0 ? (
          <ul>
            {notifications.map((notify) =>
              notify.eventType === "postLike" ? (
                <li key={notify.id}>
                  <img src="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png" alt="userprofile" />
                  {notify.senderName} 이 회원님의 POST를 좋아해요. 
                  <span style={{fontSize : 12 }}>{notify.eventCreatedTime}</span>
                </li>
              ) : (
                <li key={notify.id}>
                  <img src="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png" alt="userprofile" />
                  {notify.senderName} 이 회원님을 팔로우하기 시작했어요.
                  <span style={{fontSize : 12}}>{notify.eventCreatedTime}</span>
                </li>
              )
            )}
          </ul>
        ) : (
          <div className="modal-empty">no notifications!</div>
        )}

      </div>
    </>
  );
}
