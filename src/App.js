import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import FirstPage from "./pages/FirstPage";


function App() {

  return (
    <div className="App">
      {/* <BrowserRouter>
        <Routes>
          <Route path="*" element={<FirstPage />} />
          
        </Routes>
      </BrowserRouter> */}
      <FirstPage/>
    </div>
  );
}

export default App;
