import React from "react";
import "../styles/navbar.scss";
import logo from "../img/classBuddy-logo.png";
import { Link } from "react-router-dom";

export default function Navbar() {
  return (
    <nav className="navbar">
      <div className="navbar-content">
        <div className="navbar-left">
          <img src={logo} alt="" />
          <h1 className="logo">ClassBuddy</h1>
        </div>

        <div className="navbar-right">
          <ul className="navbar-links">
            <li className="navbar-link">
              <Link to={"/register"}>Register</Link>
            </li>
            <li className="navbar-link">
              <a href="">Login</a>
            </li>
            <li className="navbar-link">
              <Link to={"/contact"}>Contact</Link>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
}
