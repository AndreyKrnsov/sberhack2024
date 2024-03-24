import React from "react";
import cl from "./Rate.module.css"
import { Space, Button } from "antd";
import { CloseOutlined } from "@ant-design/icons";

const Rate = ({ text, countOfButton, isDeletable, id, sendDataToServer }) => {    
    const buttons = Array.from({ length: countOfButton }, (_, index) => index + 1);
    
    
    
    const handleDeleteClick = () => {
        sendDataToServer({ id: id });
    }
    return (
        <div className={cl.container}>
            <Space direction="vertical">
                {isDeletable && (
                    <button onClick={handleDeleteClick} className={cl.clsbtn}>
                        <CloseOutlined className={cl.clsicon} />
                    </button>
                )}
                <p className={cl.textP}>{text}</p>
                <Space direction="horizontal" className={cl.btnContainer}>
                    <div>


                        {buttons.map((num) => (
                            <Button key={num} className={cl.btn} type="primary">{num}</Button>
                        ))}
                        
                    </div>
                </Space>
            </Space>
        </div>
    )
}

export default Rate