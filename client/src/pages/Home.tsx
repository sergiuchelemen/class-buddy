import "../styles/home.scss";
import Navbar from "../components/Navbar";
import buddy from "../assets/buddy.png";

export default function Home() {
    fetch('http://localhost:8080')
        .then(response => response.json())
        .then(data => {
            console.log(data);
        })
        .catch(error => {
            console.error('Error:', error);
        });

    return (
        <div className="home">
            <Navbar/>
            <div className="home__container">
                <section className="hero">
                    <div className="hero__content">
                        <div className="content-text">
                            <h1 className="text-title">
                                Find Your Buddy on{" "}
                                <span className="logo">
                  ClassBuddy <img className="icon" src={buddy} alt=""/>
                </span>{" "}
                            </h1>
                            <p className="text-description">
                                ClassBuddy is a web application that allows students to connect
                                with each other and form study groups.
                            </p>
                        </div>
                        <div className="content-image">
                            <img
                                src="https://media1.giphy.com/media/RemHbGtR3lNsqyERMS/giphy.gif?cid=790b7611hmin5fyd2e8igi1wzm2ee2fxl48v70esgdn9ndtn&ep=v1_gifs_search&rid=giphy.gif&ct=s"
                                alt=""
                            />
                        </div>
                    </div>
                </section>
            </div>
        </div>
    );
}
