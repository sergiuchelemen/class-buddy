import React from "react";
import { render, fireEvent } from "@testing-library/react";
import RegistrationForm from "../pages/Register";

describe("RegistrationForm component", () => {
  test("form submission with valid data", () => {
    const { getByPlaceholderText, getByText } = render(<RegistrationForm />);

    // Fill in the form fields
    fireEvent.change(getByPlaceholderText("First Name"), {
      target: { value: "John" },
    });
    fireEvent.change(getByPlaceholderText("Last Name"), {
      target: { value: "Doe" },
    });
    fireEvent.change(getByPlaceholderText("Username"), {
      target: { value: "johndoe123" },
    });
    fireEvent.change(getByPlaceholderText("Email"), {
      target: { value: "john@example.com" },
    });
    fireEvent.change(getByPlaceholderText("Password"), {
      target: { value: "password123" },
    });
    fireEvent.change(getByPlaceholderText("Confirm Password"), {
      target: { value: "password123" },
    });
    fireEvent.change(getByPlaceholderText("Date of Birth"), {
      target: { value: "1990-01-01" },
    });
    fireEvent.change(getByPlaceholderText("Select Student Status"), {
      target: { value: "Bachelor's Degree" },
    });

    // Submit the form
    fireEvent.click(getByText("Register"));
    
    expect(getByText("Registration Successful")).toBeTruthy();
  });

});
