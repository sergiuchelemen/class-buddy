// src/test/__ mocks __/fileMock.js
export default {
  preset: "ts-jest",
  testEnvironment: "jest-environment-jsdom",
  transform: {
    "^.+\\.tsx?$": "ts-jest",
    // process `*.tsx` files with `ts-jest`
  },
  rootDir: "src",
  moduleNameMapper: {
    "\\.(gif|ttf|eot|svg|png)$": "<rootDir>/src/test/__mocks __/fileMock.js",

    "\\.(css|scss)$": "<rootDir>/test/__mocks__/fileMock.js",
    "\\.module\\.(css|scss)$": "<rootDir>/src/test/__mocks__/fileMock.js",
  },
};
