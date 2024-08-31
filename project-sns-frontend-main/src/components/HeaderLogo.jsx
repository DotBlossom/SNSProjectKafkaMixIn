import "../css/HeaderLogo.css";
import logo from "../assets/img/delta-blu.svg";

export default function HeaderLogo() {
    return (
        <div className="HeaderLogoContainer">
            <img className="main-logo-img" src={logo} alt="델타 로고" />
            <span className="main-logo-text">Delta</span>
        </div>
    );
}
