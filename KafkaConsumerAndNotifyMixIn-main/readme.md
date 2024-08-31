components/notification?
==> export to GloballayoutComponents<&&isLogined, NavBar?>

max-notifiaction 개수?
login - jwt read.


import { useEffect } from 'react'
import { EventSourcePolyfill } from 'event-source-polyfill';
export default function Notification() {
    
    useEffect(() => {
        const userId = 2; 
        const savedLastEventId = localStorage.getItem('lastEventId');
        const eventSource = new EventSourcePolyfill(
            `http://localhost:9292/api/kafkaListener/subscribe/${userId}`,
            {
              headers: {
                'Last-Event-ID': savedLastEventId // 저장된 lastEventId 값 사용
              }
            })
        
            
        eventSource.onopen = async () => {
            await console.log("sse opened!")
        }
        eventSource.addEventListener("notification", (e) =>
            {
                const data = JSON.parse(e.data);
                console.log(data);
                console.log(e.lastEventId);
                
                localStorage.setItem('lastEventId', e.lastEventId);
                
            }
        )
        eventSource.onerror
        // if unmounted. -> 조건부렌더out, page아웃
        return () => {
            
            eventSource.close()
        }


    },[])
    
    
    return (
        <div>Notification</div>
    )
}
