import { useState, useEffect } from "react";
import "./App.css";
import Register from "./components/Register";

interface Data {
  message: string;
}

function App() {
  // const url = "http://localhost:8080/";

  // const [data, setData] = useState<Data | null>(null);

  // useEffect(() => {
  //   const fetchData = async () => {
  //     try {
  //       const response = await fetch(url);
  //       if (response.ok) {
  //         const result: Data = await response.json();
  //         setData(result);
  //       } else {
  //         throw new Error("Failed to fetch data");
  //       }
  //     } catch (error) {
  //       console.error("Error fetching data:", error);
  //     }
  //   };

  //   fetchData();
  // }, [url]);

  return (
    <div className="app">
      {/* <div>{data ? data.message : "Loading..."}</div> */}
      <Register />
    </div>
  );
}

export default App;
