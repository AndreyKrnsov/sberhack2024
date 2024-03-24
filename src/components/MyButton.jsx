import React from "react";
import { Button } from 'antd';

const MyButton = ({ btnOnClick, btnType, children }) => {
  
  function sendJson() {
    console.log("aboba");
  }

  return (
    <div>
      <Button type={btnType} onClick={eval(btnOnClick)}>{children}</Button>
    </div>
  );
};

export default MyButton;
