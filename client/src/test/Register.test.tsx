// import { render, screen } from "@testing-library/react";
// import "@testing-library/jest-dom"
// import Register from "../pages/Register";

// test("form submission with invalid data", () => {
//   render(<Register />);
//   const textElement = screen.getByText("Register");
//   expect(textElement).toBeInTheDocument();
// });

// test("demo", () => {
//   expect(true).toBe(true);
// });
import { render } from "@testing-library/react";
import "@testing-library/jest-dom";
import App from "../App";

test("demo", () => {
  expect(true).toBe(true);
});

test("Renders the main page", () => {
  render(<App />);
  expect(true).toBeTruthy();
});
