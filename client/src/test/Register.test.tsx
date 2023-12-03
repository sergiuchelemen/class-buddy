import React from "react";
import { render, screen } from "@testing-library/react";
import Register from "../pages/register";

const isFieldEmpty = (fieldName: string) => {
  return emptyFields.includes(fieldName);
};
const emptyFields = ["field1", "field2", "field3"];

describe("isFieldEmpty", () => {
  it("should return true if the field is empty", () => {
    expect(isFieldEmpty("field1")).toBe(true);
  });

  it("should return false if the field is not empty", () => {
    expect(isFieldEmpty("field4")).toBe(false);
  });

  it("should handle empty field name", () => {
    expect(isFieldEmpty("")).toBe(false);
  });
});
