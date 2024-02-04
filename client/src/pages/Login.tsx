import React, { useState, ChangeEvent, FormEvent } from "react";
import "../styles/login.scss";
import { Link } from "react-router-dom";
import { FaRegArrowAltCircleLeft } from "react-icons/fa";

interface FormData {
  email: string;
  password: string;
}

const Login: React.FC = () => {
  const [formData, setFormData] = useState<FormData>({
    email: "",
    password: "",
  });
  
  //fetching data
  let url:string = "http://localhost:8080/login"; 

  const fetchData = async(url:string) => {
    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        email: formData.email,
        password: formData.password,
      }),
    });
    const data = await response.json();
    console.log(data);
    return data
  };
  fetchData(url);

  const [emptyFields, setEmptyFields] = useState<string[]>([]);
  const [isEmailValid, setIsEmailValid] = useState(true);

  const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  //send data to backend
  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    //verify if any fields are empty
    const emptyFieldNames = Object.keys(formData).filter(
      (key) => formData[key as keyof FormData] === ""
    );
    setEmptyFields(emptyFieldNames);

    // verify email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(formData.email)) {
      setIsEmailValid(false);
      return;
    } else {
      setIsEmailValid(true);
    }

    if (emptyFieldNames.length === 0) {
      setEmptyFields([]);
    }
  };

  //check if field is empty
  const isFieldEmpty = (fieldName: string) => {
    return emptyFields.includes(fieldName);
  };

  return (
    <div className="login">
      <Link to={"/"}>
        <FaRegArrowAltCircleLeft className="back-home" />
      </Link>

      <div className="login-container">
        <h1 className="login-title">Login</h1>
        <form onSubmit={handleSubmit} action="" className="login-form">
          <div className="input-group">
            <input
              type="text"
              placeholder="Email"
              className={`form-input ${isFieldEmpty("email") ? "empty" : ""} ${
                !isEmailValid ? "empty" : ""
              }`}
              name="email"
              value={formData.email}
              onChange={handleInputChange}
            />
            <input
              type="password"
              placeholder="Password"
              className={`form-input ${
                isFieldEmpty("password") ? "empty" : ""
              }`}
              name="password"
              value={formData.password}
              onChange={handleInputChange}
            />
          </div>
          <p className="login-link">
            Don't have an account ? <Link to="/register">Register</Link>
          </p>
          <input type="submit" value="Login" className="login-btn" />
        </form>
      </div>
    </div>
  );
};

export default Login;
