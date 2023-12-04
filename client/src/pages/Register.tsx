import React, { useState, useEffect, ChangeEvent, FormEvent } from "react";
import "../styles/register.scss";
import axios from "axios";
import { Link } from "react-router-dom";
import { FaRegArrowAltCircleLeft } from "react-icons/fa";

interface FormData {
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
  dateOfBirth: string;
  // studentStatus: string;
}

const RegistrationForm: React.FC = () => {
  const [formData, setFormData] = useState<FormData>({
    firstName: "",
    lastName: "",
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
    dateOfBirth: "",
    // studentStatus: "",
  });

  const [emptyFields, setEmptyFields] = useState<string[]>([]);
  const [passwordsMatch, setPasswordsMatch] = useState(true);
  const [isEmailValid, setIsEmailValid] = useState(true);

  const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  //verify if passwords match
  useEffect(() => {
    if (formData.password !== formData.confirmPassword) {
      setPasswordsMatch(false);
    } else {
      setPasswordsMatch(true);
    }
  }, [formData.password, formData.confirmPassword]);

  //send data to backend
  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
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

    // if (emptyFieldNames.length === 0) {
    //   console.log(formData);
    //   setEmptyFields([]);
    // }

    if (emptyFieldNames.length === 0) {
      try {
        //post reques to back-end
        const response = await axios.post(
          "http://localhost:8080/register",
          formData
        );
        console.log(response.data);
        setEmptyFields([]);

        setFormData({
          firstName: "",
          lastName: "",
          username: "",
          email: "",
          password: "",
          confirmPassword: "",
          dateOfBirth: "",
        });
        const inputFields = document.querySelectorAll(".form-input");
        inputFields.forEach((input) => {
          (input as HTMLInputElement).value = "";
        });
      } catch (error) {
        console.error("Error:", error);
      }
    }
  
  };

  //check if field is empty
  const isFieldEmpty = (fieldName: string) => {
    return emptyFields.includes(fieldName);
  };

  console.log(formData);
  return (
    <div className="register">
      <Link to={"/"}>
        <FaRegArrowAltCircleLeft className="back-home" />
      </Link>

      <div className="register-container">
        <h1 className="register-title">Register</h1>
        <form onSubmit={handleSubmit} action="" className="register-form">
          <div className="input-group">
            <input
              type="text"
              placeholder="First Name"
              className={`form-input ${
                isFieldEmpty("firstName") ? "empty" : ""
              }`}
              name="firstName"
              value={formData.firstName}
              onChange={handleInputChange}
            />
            <input
              type="text"
              placeholder="Last Name"
              className={`form-input ${
                isFieldEmpty("lastName") ? "empty" : ""
              }`}
              name="lastName"
              value={formData.lastName}
              onChange={handleInputChange}
            />
          </div>

          <div className="input-group">
            <input
              type="text"
              placeholder="Username"
              className={`form-input ${
                isFieldEmpty("username") ? "empty" : ""
              }`}
              name="username"
              value={formData.username}
              onChange={handleInputChange}
            />
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
          </div>

          <div className="input-group">
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
            <input
              type="password"
              placeholder="Confirm Password"
              className={`form-input ${
                isFieldEmpty("confirmPassword") ? "empty" : ""
              } ${!passwordsMatch ? "empty" : ""}`}
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleInputChange}
            />
          </div>

          <div className="input-group">
            <input
              type="date"
              placeholder="Date of Birth"
              className={`form-input ${
                isFieldEmpty("dateOfBirth") ? "empty" : ""
              }`}
              name="dateOfBirth"
              value={formData.dateOfBirth}
              onChange={handleInputChange}
            />

            <select className="form-input">
              <option value="">Primary School Student</option>
              <option value="">Basic General Education Student</option>
              <option value=""> Secondary School Student</option>
              <option value="">
                {" "}
                Lower post-secondary vocational education
              </option>
              <option value="">Incomplete Higher Education</option>
              <option value="">Select Student Status</option>
              <option value="">Bachelor's Degree</option>
              <option value="">Master's Degree</option>
              <option value=""> Postgraduate Degree or PhD</option>
            </select>
          </div>

          <input type="submit" value="Register" className="submit-btn" />

          <p className="login-link">
            Already have an account ? <Link to="/login">Login</Link>
          </p>
        </form>
      </div>
    </div>
  );
};

export default RegistrationForm;
