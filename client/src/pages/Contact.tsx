import React, { useState, ChangeEvent, FormEvent } from "react";
import "../styles/contact.scss";
import { Link } from "react-router-dom";
import { FaRegArrowAltCircleLeft } from "react-icons/fa";

interface FormData {
  username: string;
  email: string;
  message: string;
}

const Contact: React.FC = () => {
  const [formData, setFormData] = useState<FormData>({
    username: "",
    email: "",
    message: "",
  });

  const [emptyFields, setEmptyFields] = useState<string[]>([]);
  const [isEmailValid, setIsEmailValid] = useState(true);

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

    // verify email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(formData.email)) {
      setIsEmailValid(false);
      return;
    } else {
      setIsEmailValid(true);
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
    <div className="contact">
      <Link to={"/"}>
        <FaRegArrowAltCircleLeft className="back-home" />
      </Link>

      <div className="contact-container">
        <h1 className="contact-title">Contact</h1>
        <form onSubmit={handleSubmit} action="" className="contact-form">
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

          <div className="textarea-message">
            <textarea
              type="text"
              placeholder="Message"
              className={`form-message ${
                isFieldEmpty("message") ? "empty-textarea" : ""
              }`}
              name="message"
              value={formData.message}
              onChange={handleInputChange}
            ></textarea>
          </div>

          <input type="submit" value="Contact Us" className="contact-btn" />
        </form>
      </div>
    </div>
  );
};

export default Contact;
