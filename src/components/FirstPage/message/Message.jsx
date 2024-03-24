import React from "react";
import cl from "./Message.module.css"
import { Input, Button, Space } from "antd";
import { CloseCircleOutlined, CloseOutlined } from "@ant-design/icons";
import { useState, useRef } from "react";
import { message } from "antd";


const Message = ({ text, sender, isDeletable, id, sendDataToServer }) => {
    

    const [inputValue, setInputValue] = useState('');

    const btnSend = () => {
        if (inputValue.trim() === '') {
            
            message.info('Поле ввода пустое. Введите текст.');
        } else {
            
            setInputValue('');            
            message.info('Сообщение отправлено.');
        }
    };

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
                <p className={cl.sndr}>{sender}</p>
                <p className={cl.pText}>{text}</p>

                <Space direction="horizontal">
                    <Input
                        className={cl.input}
                        value={inputValue}
                        onChange={(e) => setInputValue(e.target.value)}                    
                    />
                    <Button className={cl.btn} type="primary" onClick={btnSend}>Отправить</Button>
                </Space>
            </Space>
        </div>
    )
}

export default Message;