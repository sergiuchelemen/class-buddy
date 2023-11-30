import React from "react";
import "../styles/home.scss";
import Navbar from "../components/Navbar";
import logo from "../img/classBuddy-alternative.png";

export default function Home() {
  return (
    <div className="home">
      <Navbar />
      <div className="home__container">
        <section className="hero">
          <div className="hero__content">
            <div className="content-text">
              <h1 className="text-title">ClassBuddy</h1>
              <p className="text-description">
                ClassBuddy is a web application that allows students to connect
                with each other and form study groups.
              </p>
            </div>
            <div className="content-image">
              <img src={logo} alt="" />
            </div>
          </div>
        </section>
      </div>
    </div>
  );
}
