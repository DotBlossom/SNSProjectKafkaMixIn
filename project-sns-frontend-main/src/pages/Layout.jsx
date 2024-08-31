import React from "react";
import { Outlet } from "react-router-dom";
import HeaderContainer from "../components/HeaderContainer";
import "../css/Layout.css";

export default function Layout() {
  return (
    <>
      <div className="home">
        <HeaderContainer />
      </div>
      <div className="contents">
        <Outlet />
      </div>
    </>
  );
}
