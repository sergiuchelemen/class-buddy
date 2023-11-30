import React, { useState, ChangeEvent, FormEvent } from "react";
import "../styles/register.scss";

interface FormData {
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
  dateOfBirth: string;
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
  });

  const [emptyFields, setEmptyFields] = useState<string[]>([]);
  const [passwordsMatch, setPasswordsMatch] = useState(true);
  const [isEmailValid, setIsEmailValid] = useState(true); // Состояние для проверки email

  const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    //verify if any fields are empty
    const emptyFieldNames = Object.keys(formData).filter(
      (key) => formData[key as keyof FormData] === ""
    );
    setEmptyFields(emptyFieldNames);

    // Проверка email по регулярному выражению
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(formData.email)) {
      setIsEmailValid(false);
      return;
    } else {
      setIsEmailValid(true);
    }

    //verify if passwords match
    if (formData.password !== formData.confirmPassword) {
      setPasswordsMatch(false);
      return;
    } else {
      setPasswordsMatch(true);
    }

    if (emptyFieldNames.length === 0) {
      console.log(formData);
      setEmptyFields([]);
    }
  };

  const isFieldEmpty = (fieldName: string) => {
    return emptyFields.includes(fieldName);
  };

  return (
    <div className="register">
      <h1 className="register-title">Register</h1>
      <div className="register-container">
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
          </div>

          <input type="submit" value="Register" className="submit-btn" />
        </form>
      </div>
    </div>
  );
};

export default RegistrationForm;
